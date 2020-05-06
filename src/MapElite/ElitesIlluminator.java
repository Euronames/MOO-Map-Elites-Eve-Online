package MapElite;

import EveOnline.ShipBuilder;
import MapElite.FeatureSpace.FeatureSpaceHandler;
import MapElite.FeatureSpace.IFeatureSpace;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The ElitesIlluminator is meant as the facade class of this layer.
 * It handles the procedure of iterating thorough the feature space providing the IFeatureSpace with new ISolutions and
 * requesting new ISolutions from the IElitesController.
 */
public class ElitesIlluminator implements IMapElitesFacade {
    private IElitesController mapElitesInterface;
    private IFeatureSpace featureSpaceHandler;
    private Map<ISolution, LinkedList<ISolution>> generationsTree;

    /**
     * Construct a illuminator for searching through the space of solution within the features provided.
     *
     * @param featureSpaceGranularity defines how fine grained the search should be. A high granularity leads to more stored solutions, though increased memory usage and slower runtime.
     */
    public ElitesIlluminator(int featureSpaceGranularity) {
        this.mapElitesInterface = new ShipBuilder();
        featureSpaceHandler = new FeatureSpaceHandler(mapElitesInterface.getFeatures().toArray(IFeature[]::new), featureSpaceGranularity);
        generationsTree = new LinkedHashMap<>();
    }

    @Override
    public void illuminateElites(int iterations, int solutions, boolean storeDuplicates) {
        featureSpaceHandler.setStoreDuplicates(storeDuplicates);
        ISolution elite;
        ISolution modifiedElite;

        for (int i = 0; i < iterations; i++) {

            if (i % 1000 == 0 && i != 0) {
                System.out.println("Iteration : " + i);
            }

            if (i < solutions) { // Add a number of random solution to the elites array
                modifiedElite = mapElitesInterface.generateRandomSolution(); // Adds a random solution to the elites array
                generationsTree.put(modifiedElite, new LinkedList<>());
            } else {
                elite = featureSpaceHandler.getRandomSolution(); // Randomly select a solution from the elites array
                modifiedElite = mapElitesInterface.randomMutation(elite); // Randomly modify the selected elite
                if (generationsTree.containsKey(elite)) {
                    generationsTree.get(elite).add(modifiedElite);
                } else {
                    LinkedList<ISolution> children = new LinkedList<>();
                    children.add(modifiedElite);
                    generationsTree.put(elite, children);
                }
            }
            featureSpaceHandler.insertSolution(modifiedElite); // Handles the logic of throwing away duplicate solutions and those not part of the para-to front
        }
    }

    @Override
    public IFeatureSpace getIlluminatedFeatureSpace() {
        return featureSpaceHandler;
    }

    @Override
    public Map<ISolution, LinkedList<ISolution>> getGenerationsTree() {
        return generationsTree;
    }
}
