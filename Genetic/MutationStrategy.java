public interface MutationStrategy {
    /**
     * Randomly flips bits in an individual's chromosome.
     * @param individual The individual to mutate.
     * @param mutationRate The probability (0.0 to 1.0) of each bit flipping.
     * @param fixedCells A boolean array where 'true' means the cell cannot be changed.
     */
    void mutate(Individual individual, double mutationRate, boolean[] fixedCells);
}