package random;

public interface NormalizedRandomGenerator {

  /** Generate a random scalar with null mean and unit standard deviation.
   * <p>This method does <strong>not</strong> specify the shape of the
   * distribution, it is the implementing class that provides it. The
   * only contract here is to generate numbers with null mean and unit
   * standard deviation.</p>
   * @return a random scalar with null mean and unit standard deviation
   */
  double nextNormalizedDouble();

}
