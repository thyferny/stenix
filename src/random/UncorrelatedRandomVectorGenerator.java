package random;

import java.util.Arrays;

import exception.DimensionMismatchException;

public class UncorrelatedRandomVectorGenerator
  implements RandomVectorGenerator {

    /** Underlying scalar generator. */
    private final NormalizedRandomGenerator generator;

    /** Mean vector. */
    private final double[] mean;

    /** Standard deviation vector. */
    private final double[] standardDeviation;

  /** Simple constructor.
   * <p>Build an uncorrelated random vector generator from
   * its mean and standard deviation vectors.</p>
   * @param mean expected mean values for each component
   * @param standardDeviation standard deviation for each component
   * @param generator underlying generator for uncorrelated normalized
   * components
   */
  public UncorrelatedRandomVectorGenerator(double[] mean,
                                           double[] standardDeviation,
                                           NormalizedRandomGenerator generator) {
    if (mean.length != standardDeviation.length) {
        throw new DimensionMismatchException(mean.length, standardDeviation.length);
    }
    this.mean              = mean.clone();
    this.standardDeviation = standardDeviation.clone();
    this.generator = generator;
  }

  /** Simple constructor.
   * <p>Build a null mean random and unit standard deviation
   * uncorrelated vector generator</p>
   * @param dimension dimension of the vectors to generate
   * @param generator underlying generator for uncorrelated normalized
   * components
   */
  public UncorrelatedRandomVectorGenerator(int dimension,
                                           NormalizedRandomGenerator generator) {
    mean              = new double[dimension];
    standardDeviation = new double[dimension];
    Arrays.fill(standardDeviation, 1.0);
    this.generator = generator;
  }

  /** Generate an uncorrelated random vector.
   * @return a random vector as a newly built array of double
   */
  public double[] nextVector() {

    double[] random = new double[mean.length];
    for (int i = 0; i < random.length; ++i) {
      random[i] = mean[i] + standardDeviation[i] * generator.nextNormalizedDouble();
    }

    return random;

  }

}
