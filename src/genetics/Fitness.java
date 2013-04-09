package genetics;

public interface Fitness {

    /**
     * Compute the fitness. This is usually very time-consuming, so the value should be cached.
     * @return fitness
     */
    double fitness();

}
