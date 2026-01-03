import java.util.ArrayList;

public interface SelectionStrategy {
    /**
     * Chooses parents for reproduction.
     * @param population The current pool of individuals.
     * @return An array of two Individuals selected to be parents.
     */
    Individual[] select(Population population);
}   