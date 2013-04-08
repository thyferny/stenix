package ode.nonstiff;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import linear.Array2DRowFieldMatrix;
import linear.Array2DRowRealMatrix;
import linear.ArrayFieldVector;
import linear.FieldDecompositionSolver;
import linear.FieldLUDecomposition;
import linear.FieldMatrix;
import linear.MatrixUtils;
import linear.QRDecomposition;
import linear.RealMatrix;
import fraction.BigFraction;


public class AdamsNordsieckTransformer {

    /** Cache for already computed coefficients. */
    private static final Map<Integer, AdamsNordsieckTransformer> CACHE =
        new HashMap<Integer, AdamsNordsieckTransformer>();

    /** Update matrix for the higher order derivatives h<sup>2</sup>/2y'', h<sup>3</sup>/6 y''' ... */
    private final Array2DRowRealMatrix update;

    /** Update coefficients of the higher order derivatives wrt y'. */
    private final double[] c1;

    /** Simple constructor.
     * @param nSteps number of steps of the multistep method
     * (excluding the one being computed)
     */
    private AdamsNordsieckTransformer(final int nSteps) {

        // compute exact coefficients
        FieldMatrix<BigFraction> bigP = buildP(nSteps);
        FieldDecompositionSolver<BigFraction> pSolver =
            new FieldLUDecomposition<BigFraction>(bigP).getSolver();

        BigFraction[] u = new BigFraction[nSteps];
        Arrays.fill(u, BigFraction.ONE);
        BigFraction[] bigC1 = pSolver
            .solve(new ArrayFieldVector<BigFraction>(u, false)).toArray();

        // update coefficients are computed by combining transform from
        // Nordsieck to multistep, then shifting rows to represent step advance
        // then applying inverse transform
        BigFraction[][] shiftedP = bigP.getData();
        for (int i = shiftedP.length - 1; i > 0; --i) {
            // shift rows
            shiftedP[i] = shiftedP[i - 1];
        }
        shiftedP[0] = new BigFraction[nSteps];
        Arrays.fill(shiftedP[0], BigFraction.ZERO);
        FieldMatrix<BigFraction> bigMSupdate =
            pSolver.solve(new Array2DRowFieldMatrix<BigFraction>(shiftedP, false));

        // convert coefficients to double
        update         = MatrixUtils.bigFractionMatrixToRealMatrix(bigMSupdate);
        c1             = new double[nSteps];
        for (int i = 0; i < nSteps; ++i) {
            c1[i] = bigC1[i].doubleValue();
        }

    }

    /** Get the Nordsieck transformer for a given number of steps.
     * @param nSteps number of steps of the multistep method
     * (excluding the one being computed)
     * @return Nordsieck transformer for the specified number of steps
     */
    public static AdamsNordsieckTransformer getInstance(final int nSteps) {
        synchronized(CACHE) {
            AdamsNordsieckTransformer t = CACHE.get(nSteps);
            if (t == null) {
                t = new AdamsNordsieckTransformer(nSteps);
                CACHE.put(nSteps, t);
            }
            return t;
        }
    }

    /** Get the number of steps of the method
     * (excluding the one being computed).
     * @return number of steps of the method
     * (excluding the one being computed)
     */
    public int getNSteps() {
        return c1.length;
    }

    /** Build the P matrix.
     * <p>The P matrix general terms are shifted j (-i)<sup>j-1</sup> terms:
     * <pre>
     *        [  -2   3   -4    5  ... ]
     *        [  -4  12  -32   80  ... ]
     *   P =  [  -6  27 -108  405  ... ]
     *        [  -8  48 -256 1280  ... ]
     *        [          ...           ]
     * </pre></p>
     * @param nSteps number of steps of the multistep method
     * (excluding the one being computed)
     * @return P matrix
     */
    private FieldMatrix<BigFraction> buildP(final int nSteps) {

        final BigFraction[][] pData = new BigFraction[nSteps][nSteps];

        for (int i = 0; i < pData.length; ++i) {
            // build the P matrix elements from Taylor series formulas
            final BigFraction[] pI = pData[i];
            final int factor = -(i + 1);
            int aj = factor;
            for (int j = 0; j < pI.length; ++j) {
                pI[j] = new BigFraction(aj * (j + 2));
                aj *= factor;
            }
        }

        return new Array2DRowFieldMatrix<BigFraction>(pData, false);

    }

    /** Initialize the high order scaled derivatives at step start.
     * @param h step size to use for scaling
     * @param t first steps times
     * @param y first steps states
     * @param yDot first steps derivatives
     * @return Nordieck vector at first step (h<sup>2</sup>/2 y''<sub>n</sub>,
     * h<sup>3</sup>/6 y'''<sub>n</sub> ... h<sup>k</sup>/k! y<sup>(k)</sup><sub>n</sub>)
     */
    public Array2DRowRealMatrix initializeHighOrderDerivatives(final double h, final double[] t,
                                                               final double[][] y,
                                                               final double[][] yDot) {

        // using Taylor series with di = ti - t0, we get:
        //  y(ti)  - y(t0)  - di y'(t0) =   di^2 / h^2 s2 + ... +   di^k     / h^k sk + O(h^(k+1))
        //  y'(ti) - y'(t0)             = 2 di   / h^2 s2 + ... + k di^(k-1) / h^k sk + O(h^k)
        // we write these relations for i = 1 to i= n-1 as a set of 2(n-1) linear
        // equations depending on the Nordsieck vector [s2 ... sk]
        final double[][] a     = new double[2 * (y.length - 1)][c1.length];
        final double[][] b     = new double[2 * (y.length - 1)][y[0].length];
        final double[]   y0    = y[0];
        final double[]   yDot0 = yDot[0];
        for (int i = 1; i < y.length; ++i) {

            final double di    = t[i] - t[0];
            final double ratio = di / h;
            double dikM1Ohk    =  1 / h;

            // linear coefficients of equations
            // y(ti) - y(t0) - di y'(t0) and y'(ti) - y'(t0)
            final double[] aI    = a[2 * i - 2];
            final double[] aDotI = a[2 * i - 1];
            for (int j = 0; j < aI.length; ++j) {
                dikM1Ohk *= ratio;
                aI[j]     = di      * dikM1Ohk;
                aDotI[j]  = (j + 2) * dikM1Ohk;
            }

            // expected value of the previous equations
            final double[] yI    = y[i];
            final double[] yDotI = yDot[i];
            final double[] bI    = b[2 * i - 2];
            final double[] bDotI = b[2 * i - 1];
            for (int j = 0; j < yI.length; ++j) {
                bI[j]    = yI[j] - y0[j] - di * yDot0[j];
                bDotI[j] = yDotI[j] - yDot0[j];
            }

        }

        // solve the rectangular system in the least square sense
        // to get the best estimate of the Nordsieck vector [s2 ... sk]
        QRDecomposition decomposition;
        decomposition = new QRDecomposition(new Array2DRowRealMatrix(a, false));
        RealMatrix x = decomposition.getSolver().solve(new Array2DRowRealMatrix(b, false));
        return new Array2DRowRealMatrix(x.getData(), false);
    }

    /** Update the high order scaled derivatives for Adams integrators (phase 1).
     * <p>The complete update of high order derivatives has a form similar to:
     * <pre>
     * r<sub>n+1</sub> = (s<sub>1</sub>(n) - s<sub>1</sub>(n+1)) P<sup>-1</sup> u + P<sup>-1</sup> A P r<sub>n</sub>
     * </pre>
     * this method computes the P<sup>-1</sup> A P r<sub>n</sub> part.</p>
     * @param highOrder high order scaled derivatives
     * (h<sup>2</sup>/2 y'', ... h<sup>k</sup>/k! y(k))
     * @return updated high order derivatives
     * @see #updateHighOrderDerivativesPhase2(double[], double[], Array2DRowRealMatrix)
     */
    public Array2DRowRealMatrix updateHighOrderDerivativesPhase1(final Array2DRowRealMatrix highOrder) {
        return update.multiply(highOrder);
    }

    /** Update the high order scaled derivatives Adams integrators (phase 2).
     * <p>The complete update of high order derivatives has a form similar to:
     * <pre>
     * r<sub>n+1</sub> = (s<sub>1</sub>(n) - s<sub>1</sub>(n+1)) P<sup>-1</sup> u + P<sup>-1</sup> A P r<sub>n</sub>
     * </pre>
     * this method computes the (s<sub>1</sub>(n) - s<sub>1</sub>(n+1)) P<sup>-1</sup> u part.</p>
     * <p>Phase 1 of the update must already have been performed.</p>
     * @param start first order scaled derivatives at step start
     * @param end first order scaled derivatives at step end
     * @param highOrder high order scaled derivatives, will be modified
     * (h<sup>2</sup>/2 y'', ... h<sup>k</sup>/k! y(k))
     * @see #updateHighOrderDerivativesPhase1(Array2DRowRealMatrix)
     */
    public void updateHighOrderDerivativesPhase2(final double[] start,
                                                 final double[] end,
                                                 final Array2DRowRealMatrix highOrder) {
        final double[][] data = highOrder.getDataRef();
        for (int i = 0; i < data.length; ++i) {
            final double[] dataI = data[i];
            final double c1I = c1[i];
            for (int j = 0; j < dataI.length; ++j) {
                dataI[j] += c1I * (start[j] - end[j]);
            }
        }
    }

}
