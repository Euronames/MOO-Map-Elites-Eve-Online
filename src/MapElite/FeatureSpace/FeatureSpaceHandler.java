package MapElite.FeatureSpace;

import MapElite.IFeature;
import MapElite.ISolution;
import Seed.Seed;

import java.util.*;

/**
 * The main concern of the feature space handler is to map sector spans with sectors which defines the feature space.
 * Within this concern the feature space handler has been given the responsibility of rescaling the entire feature space
 * when a solution is found which lies beyond.
 * This class also makes sure that the appropriate sector is found for a new solution insertion or when a random solution is needed.
 */
public class FeatureSpaceHandler implements IFeatureSpace {
    private Map<IFeature, ISpan[]> featureSpans; // Keeps track of the total max and min for a feature
    private Map<ISectorSpan, ISector> featureSpace;
    private IFeature[] features;
    private boolean storeDuplicates;
    private int numberOfSectors;
    private int granularity;
    private Random random; // An instance of Random is stored in the object as the random method is used a lot when program runs.
    private int recalculation;

    /**
     * Construct the handler with initial parameters.
     * Used for creating an empty feature space with minimal settings.
     *
     * @param features    the features in which the space is created upon.
     * @param granularity defines the amount of sectors in which the solutions are stored. A high granularity leads to more para-to fronts, though increased memory usage and slower runtime.
     */
    public FeatureSpaceHandler(IFeature[] features, int granularity) {
        this.numberOfSectors = (int) Math.pow(granularity, features.length);
        this.featureSpace = new HashMap<>();
        this.featureSpans = new HashMap<>();
        this.granularity = granularity;
        this.storeDuplicates = false;
        this.random = new Random(Seed.seed);
        this.features = features;
        this.recalculation = 0;

        initializeBaseSpans();
    }

    /**
     * Create the initial feature space where there exist no solutions,
     * and all spans are collapsed at a center of 0.
     */
    private void initializeBaseSpans() {
        for (int i = 0; i < numberOfSectors; i++) {
            featureSpace.put(new SectorSpan(features), new Sector(storeDuplicates));
        }

        for (IFeature feature : features) {
            featureSpans.put(feature, new Span[granularity]);
            for (int i = 0; i < granularity; i++) {
                featureSpans.get(feature)[i] = new Span(0, 0);
            }
        }
    }

    @Override
    public Map<ISectorSpan, ISector> getFeatureSpace() {
        return featureSpace;
    }

    @Override
    public void setStoreDuplicates(boolean storeDuplicates) {
        this.storeDuplicates = storeDuplicates;
    }

    @Override
    public int getAmountOfFeatures() {
        return featureSpans.size();
    }

    @Override
    public ISolution getRandomSolution() {
        int randomIndex;
        ISector[] sectors = getSectorsWithSolutions();

        ISolution solution;
        do {
            randomIndex = random.nextInt(sectors.length);
            solution = sectors[randomIndex].getRandomSolution();
        }
        while (solution == null);

        return solution;
    }

    private void printFeatureSpans() {
        System.out.println("---------------------------------------------------------");
        StringBuilder stringBuilder;
        for (IFeature feature : featureSpans.keySet()) {
            stringBuilder = new StringBuilder();
            for (ISpan span : featureSpans.get(feature)) {
                stringBuilder.append(" { Span: " + "(max: " + span.getMax() + ", min: " + span.getMin() + ") }");
            }
            System.out.println(stringBuilder.toString());
        }
    }

    @Override
    public void insertSolution(ISolution solution) {
        IPoint solutionPoint = getPointInSpace(solution);
        IFeature[] featuresSpansToUpdate = findFeaturesToUpdate(solutionPoint);

        if (featuresSpansToUpdate.length > 0) { // The solution did not match any of the spans in the feature space

            recalculateFeatureSpans(featuresSpansToUpdate, solution);
            recalculateFeatureSpace(solution);
            recalculation++;
        } else {
            featureSpace.get(findSectorSpan(solutionPoint)).insertIntoSector(solution);
        }
    }

    /**
     * Find a sector span belonging to a point in the feature space.
     * Uses a binary search for each feature.
     *
     * @param point a point in the feature space.
     * @return a sector span which spans the point in the feature space.
     */
    private ISectorSpan findSectorSpan(IPoint point) {
        ISectorSpan associatedSectorSpan = new SectorSpan(features);
        for (IFeature feature : featureSpans.keySet()) {
            int left = 0;
            int right = featureSpans.get(feature).length - 1;
            while  (left <= right) {
                int median = (int) Math.floor((left + right) / 2);
                ISpan currentSpan = featureSpans.get(feature)[median];
                double currentPoint = point.getFeaturePoint(feature);
                if (currentSpan.isWithinSpan(currentPoint)) {
                    associatedSectorSpan.addNewFeatureSpan(feature, currentSpan);
                    break;
                } else if (currentSpan.isSmallerThan(currentPoint)) {
                    left = median + 1;
                } else {
                    right = median - 1;
                }
            }
        }

        return associatedSectorSpan;
    }

    /**
     * Fetches the sectors in the feature space which has solutions.
     *
     * @return a Array of ISectors which defines the sectors whom contains solutions.
     */
    private ISector[] getSectorsWithSolutions() {
        ISector[] sectors = featureSpace.values().toArray(ISector[]::new);
        ArrayList<ISector> sectorsWithSolutions = new ArrayList<>();
        for (ISector sector : sectors) {
            if (sector.getParetoFront().size() > 0) {
                sectorsWithSolutions.add(sector);
            }
        }
        return sectorsWithSolutions.toArray(ISector[]::new);
    }

    /**
     * Insert a solution which is known to fit within the feature space.
     *
     * @param solution the ISolution which is inserted into the feature space.
     */
    private void insertOldSolution(ISolution solution) {
        IPoint solutionPoint = getPointInSpace(solution);


        ISectorSpan sectorSpan = findSectorSpan(solutionPoint);

        /*
        if (!featureSpace.containsKey(sectorSpan)) {
            System.out.println("SolutionPoint");
            for (IFeature feature : features) {
                System.out.println(feature);
                System.out.println(solutionPoint.getFeaturePoint(feature));
            }
            System.out.println("SectorSpans");
            for (IFeature feature : features) {
                System.out.println(feature);
                System.out.println("Max: " + findSectorSpan(solutionPoint).getFeatureSpan(feature).getMax());
                System.out.println("Min: " + findSectorSpan(solutionPoint).getFeatureSpan(feature).getMin());
            }
            throw new RuntimeException("FeatureSpace do not contain the solution points sector span");
        }*/

        if (featureSpace.containsKey(sectorSpan)) {
            featureSpace.get(sectorSpan).insertIntoSector(solution);
        }

    }

    /**
     * Used for transforming the solution into a point in the given feature space.
     * This point is used to match a solution with the sector spans to find which sector the solution belongs to.
     *
     * @param solution a solution which is transformed to a point in the feature space.
     * @return a map which maps features with its value forming a point in the feature space
     */
    private IPoint getPointInSpace(ISolution solution) {
        IPoint solutionPoint = new Point();
        for (IFeature feature : features) {
            double solutionMax = solution.getFeatureDescriptor().getFeatureDescriptions().get(feature).getScore();
            solutionPoint.insertValue(feature, solutionMax);
        }

        return solutionPoint;
    }

    /**
     * Get the features in the current feature space in which the provided point is beyond.
     *
     * @param point a point in the feature space.
     * @return a List of IFeatures representing the features which the point lies beyond.
     */
    private IFeature[] findFeaturesToUpdate(IPoint point) {
        List<IFeature> featuresToUpdate = new ArrayList<>();
        for (IFeature feature : features) {
            boolean isWithinSpan = false;
            for (ISpan span : featureSpans.get(feature)) {
                if (span.isWithinSpan(point.getFeaturePoint(feature))) {
                    isWithinSpan = true;
                }
            }

            if (!isWithinSpan) {
                featuresToUpdate.add(feature);
            }
        }
        return featuresToUpdate.toArray(IFeature[]::new);
    }

    /**
     * Used for recalculating the feature spans which defines the max and minimum value of each feature in the feature space.
     *
     * @param solution             a solution which does not fit into the current feature space.
     * @param featureSpansToUpdate uses a list of features to specify which features to update
     */
    private void recalculateFeatureSpans(IFeature[] featureSpansToUpdate, ISolution solution) {
        for (IFeature feature : featureSpansToUpdate) {
            double solutionPoint = solution.getFeatureDescriptor().getFeatureDescriptions().get(feature).getScore();
            if (this.featureSpans.get(feature)[granularity - 1].getMax() < solutionPoint) {
                ISpan[] spans = new Span[granularity];
                //LinkedList<Span> spans = new LinkedList<>();

                double spanLength;
                spanLength = solutionPoint / granularity;
                double nextMin = 0;
                double nextMax = 0;
                for (int k = 0; k < granularity; k++) {
                    if (k == 0) {
                        spans[k] = new Span(spanLength, 0);
                    } else {
                        spans[k] = new Span(nextMax, nextMin);
                    }

                    nextMin += spanLength + Double.MIN_VALUE;
                    nextMax = spanLength + nextMin + 1;
                }

                this.featureSpans.put(feature, spans);
            }
        }
    }

    /**
     * Used for recalculating the whole feature space.
     * This method ensures that the solution of the feature space are stored before the new feature space is built.
     *
     * @param solutionWithLargerDimension the solution which have triggered the recalculation.
     */
    private void recalculateFeatureSpace(ISolution solutionWithLargerDimension) {
        List<ISolution> solutions = new ArrayList<>();
        solutions.add(solutionWithLargerDimension);

        for (ISector sector : featureSpace.values()) {
            if (sector.getParetoFront().size() > 0) {
                solutions.addAll(sector.getParetoFront());
            }
        }

        featureSpace.clear();
        for (ISectorSpan sectorSpan : buildFeatureSpace()) {
            featureSpace.put(sectorSpan, new Sector(storeDuplicates)); // storeDuplicates is provided on object creation and defines if the sectors should keep duplicate solutions.
        }

        for (ISolution solution : solutions) {
            insertOldSolution(solution);
        }
    }

    /**
     * Used for rebuilding the feature space by resizing the sector spans based upon the defined feature spans, features and granularity.
     * Is a mediator for using the permutateSectorSpans method as its recursive nature defines a complex parameter list.
     *
     * @return a List of ISectorSpan which defines the new feature space.
     */
    private List<ISectorSpan> buildFeatureSpace() {
        List<ISectorSpan> newSectorSpans = new ArrayList<>();
        createSectorSpanCombinations(newSectorSpans, null, 0);
        return newSectorSpans;
    }

    /**
     * Used for creating a list which consists of all combinations of feature spans defined and store them as sector span.
     * Essenitally building a new feature space after the feature spans have been updated.
     * <p>
     * Creates a recursive tree which depth is bound by the number of features,
     * and uses this to create "granularity" to the power of "features length" number of sector spans.
     *
     * @param combinationsOfSectorSpans a pre-initialized List of ISectorSpans where the found combinations are added.
     * @param permutationSectorSpan     initially set as null, though used for building combinations recursively.
     * @param featureDepth              initially set as 0, and is incremented for the whole "features length".
     */
    private void createSectorSpanCombinations(List<ISectorSpan> combinationsOfSectorSpans, ISectorSpan permutationSectorSpan, int featureDepth) {
        if (featureDepth == 0) {
            permutationSectorSpan = new SectorSpan(features);
        }

        IFeature feature;
        ISpan[] spans;

        if (featureDepth == features.length) {
            combinationsOfSectorSpans.add(new SectorSpan(permutationSectorSpan));
        } else if (featureDepth < features.length) {
            for (int i = 0; i < granularity; i++) {
                feature = features[featureDepth];
                spans = featureSpans.get(feature);

                permutationSectorSpan.addNewFeatureSpan(feature, spans[i]);

                int newFeatureDepth = featureDepth + 1;
                createSectorSpanCombinations(combinationsOfSectorSpans, permutationSectorSpan, newFeatureDepth);
            }
        }
    }

    @Override
    public int getRecalculation() {
        return recalculation;
    }
}

