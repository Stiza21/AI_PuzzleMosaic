public interface CrossoverStrategy {
    /**
     * Combines genes from two parents to create a new offspring.
     * @param parent1 The first parent.
     * @param parent2 The second parent.
     * @return A new Individual representing the offspring.
     */
    Individual crossover(Individual parent1, Individual parent2);
}