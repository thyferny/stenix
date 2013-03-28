package linear;

import math.util.IterationManager;
import exception.DimensionMismatchException;
import exception.MaxCountExceededException;
import exception.NullArgumentException;
import exception.util.ExceptionContext;


public class ConjugateGradient
    extends PreconditionedIterativeLinearSolver {

    /** Key for the <a href="#context">exception context</a>. */
    public static final String OPERATOR = "operator";

    /** Key for the <a href="#context">exception context</a>. */
    public static final String VECTOR = "vector";

    /**
     * {@code true} if positive-definiteness of matrix and preconditioner should
     * be checked.
     */
    private boolean check;

    /** The value of &delta;, for the default stopping criterion. */
    private final double delta;

    /**
     * Creates a new instance of this class, with <a href="#stopcrit">default
     * stopping criterion</a>.
     *
     * @param maxIterations the maximum number of iterations
     * @param delta the &delta; parameter for the default stopping criterion
     * @param check {@code true} if positive definiteness of both matrix and
     * preconditioner should be checked
     */
    public ConjugateGradient(final int maxIterations, final double delta,
                             final boolean check) {
        super(maxIterations);
        this.delta = delta;
        this.check = check;
    }

    /**
     * Creates a new instance of this class, with <a href="#stopcrit">default
     * stopping criterion</a> and custom iteration manager.
     *
     * @param manager the custom iteration manager
     * @param delta the &delta; parameter for the default stopping criterion
     * @param check {@code true} if positive definiteness of both matrix and
     * preconditioner should be checked
     * @throws NullArgumentException if {@code manager} is {@code null}
     */
    public ConjugateGradient(final IterationManager manager,
                             final double delta, final boolean check)
        throws NullArgumentException {
        super(manager);
        this.delta = delta;
        this.check = check;
    }

    /**
     * Returns {@code true} if positive-definiteness should be checked for both
     * matrix and preconditioner.
     *
     * @return {@code true} if the tests are to be performed
     */
    public final boolean getCheck() {
        return check;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NonPositiveDefiniteOperatorException if {@code a} or {@code m} is
     * not positive definite
     */
    @Override
    public RealVector solveInPlace(final RealLinearOperator a,
                                   final RealLinearOperator m,
                                   final RealVector b,
                                   final RealVector x0)
        throws NullArgumentException, NonPositiveDefiniteOperatorException,
        NonSquareOperatorException, DimensionMismatchException,
        MaxCountExceededException, NonPositiveDefiniteOperatorException {
        checkParameters(a, m, b, x0);
        final IterationManager manager = getIterationManager();
        // Initialization of default stopping criterion
        manager.resetIterationCount();
        final double rmax = delta * b.getNorm();
        final RealVector bro = RealVector.unmodifiableRealVector(b);

        // Initialization phase counts as one iteration.
        manager.incrementIterationCount();
        // p and x are constructed as copies of x0, since presumably, the type
        // of x is optimized for the calculation of the matrix-vector product
        // A.x.
        final RealVector x = x0;
        final RealVector xro = RealVector.unmodifiableRealVector(x);
        final RealVector p = x.copy();
        RealVector q = a.operate(p);

        final RealVector r = b.combine(1, -1, q);
        final RealVector rro = RealVector.unmodifiableRealVector(r);
        double rnorm = r.getNorm();
        RealVector z;
        if (m == null) {
            z = r;
        } else {
            z = null;
        }
        IterativeLinearSolverEvent evt;
        evt = new DefaultIterativeLinearSolverEvent(this,
            manager.getIterations(), xro, bro, rro, rnorm);
        manager.fireInitializationEvent(evt);
        if (rnorm <= rmax) {
            manager.fireTerminationEvent(evt);
            return x;
        }
        double rhoPrev = 0.;
        while (true) {
            manager.incrementIterationCount();
            evt = new DefaultIterativeLinearSolverEvent(this,
                manager.getIterations(), xro, bro, rro, rnorm);
            manager.fireIterationStartedEvent(evt);
            if (m != null) {
                z = m.operate(r);
            }
            final double rhoNext = r.dotProduct(z);
            if (check && (rhoNext <= 0.)) {
                final NonPositiveDefiniteOperatorException e;
                e = new NonPositiveDefiniteOperatorException();
                final ExceptionContext context = e.getContext();
                context.setValue(OPERATOR, m);
                context.setValue(VECTOR, r);
                throw e;
            }
            if (manager.getIterations() == 2) {
                p.setSubVector(0, z);
            } else {
                p.combineToSelf(rhoNext / rhoPrev, 1., z);
            }
            q = a.operate(p);
            final double pq = p.dotProduct(q);
            if (check && (pq <= 0.)) {
                final NonPositiveDefiniteOperatorException e;
                e = new NonPositiveDefiniteOperatorException();
                final ExceptionContext context = e.getContext();
                context.setValue(OPERATOR, a);
                context.setValue(VECTOR, p);
                throw e;
            }
            final double alpha = rhoNext / pq;
            x.combineToSelf(1., alpha, p);
            r.combineToSelf(1., -alpha, q);
            rhoPrev = rhoNext;
            rnorm = r.getNorm();
            evt = new DefaultIterativeLinearSolverEvent(this,
                manager.getIterations(), xro, bro, rro, rnorm);
            manager.fireIterationPerformedEvent(evt);
            if (rnorm <= rmax) {
                manager.fireTerminationEvent(evt);
                return x;
            }
        }
    }
}
