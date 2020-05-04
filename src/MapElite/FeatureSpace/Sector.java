package MapElite.FeatureSpace;

import MapElite.ISolution;

import java.util.ArrayList;
import java.util.Random;

/**
 * A sector maintains a list of paretoFront which defines the paretoFront belonging to a sector span when mapped.
 */
public class Sector implements ISector {
    private ArrayList<ISolution> paretoFront;
    private boolean storeDuplicates;
    private Random random;

    /**
     * Constructs a empty Sector which consists of no paretoFront.
     *
     * @param storeDuplicates used for setting if the sector should store duplicates.
     */
    Sector(boolean storeDuplicates) {
        this.storeDuplicates = storeDuplicates;
        paretoFront = new ArrayList<>();
        random = new Random(); // An instance of Random is stored in the object as the random method is used a lot when program runs.
    }

    @Override
    public void insertIntoSector(ISolution solution) {
        this.paretoFront.add(solution);
        removeSmallerSolutions();
    }

    /**
     * Check all paretoFront and remove those who are smaller than the rest.
     * Maintains the para-to front of the sector.
     */
    private void removeSmallerSolutions() {
        for (int i = 0; i < paretoFront.size(); i++) {
            boolean isLargerSolution = true;
            for (ISolution matchedSolution : paretoFront) {
                if (!paretoFront.get(i).equals(matchedSolution)) {
                    if (storeDuplicates) {
                        if (paretoFront.get(i).compareTo(matchedSolution) < 0) {
                            isLargerSolution = false;
                        }
                    } else {
                        if (paretoFront.get(i).compareTo(matchedSolution) <= 0) {
                            isLargerSolution = false;
                        }
                    }

                }
            }

            if (!isLargerSolution) {
                paretoFront.remove(i);
            }
        }
    }

    @Override
    public ArrayList<ISolution> getParetoFront() {
        return paretoFront;
    }

    @Override
    public ISolution getRandomSolution() {
        if (paretoFront.size() > 0) {
            return paretoFront.get(random.nextInt(paretoFront.size()));
        }
        return null;
    }

    @Override
    public boolean isSolutionContained(ISolution solution) {
        return paretoFront.contains(solution);
    }
}
