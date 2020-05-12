import MapElite.ElitesIlluminator;
import MapElite.FeatureSpace.IFeatureSpace;
import MapElite.FeatureSpace.ISector;
import MapElite.FeatureSpace.ISectorSpan;
import MapElite.IMapElitesFacade;
import MapElite.ISolution;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;


public class RuntimeTest {

    private IMapElitesFacade elitesIlluminator;
    private FileWriter fileWriter;
    private StringBuilder sb;
    private String newLine;
    private long startTime;
    private long endTime;

    private int[] granularity = {3, 9, 15};
    private int[] generations = {1000, 10000, 100000};
    private int[] initialGenerations = {50, 250, 500};
    //Add seed 
    private int testIterations = 20;
    //Test efter variance 

    // highter generations == better results 

    //Granularity 0 og sammenlign resultater

    //Jo længere du kører, jo bedre coverage 
    //Jo længere den kører, jo større er befolkningen
    

    //TEst med rigtig skibe 

    private RuntimeTest() throws IOException {
        newLine = "\n";
        String filename = "RuntimeTest.csv";
        fileWriter = new FileWriter(filename);
        sb = new StringBuilder();
        sb.append("Granularity, Generations, Initial generations, Sector coverage, no. of Solutions, number of space recalculations, number of sectors containing a solution," +
                "no. solutions with parents, avg no. of children pr. parent, avg no. new elite pr. parent, avg no. non-elite pr. parent, avg no. equal elite pr. parent, percent of new elite children, percent of non-elite children, percent of equal children");
        sb.append(newLine);
    }

    public static void main(String[] args) throws IOException {
        RuntimeTest runtimeTest = new RuntimeTest();
        runtimeTest.createTestResult();

        try {
            runtimeTest.fileWriter.write(runtimeTest.sb.toString());
            runtimeTest.fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTestResult() {

        for (int gran : granularity) {
            for (int generation : generations) {
                for (int initialGeneration : initialGenerations) {
                    for (int i = 0; i < testIterations; i++) {
                        runElitesIlluminator(gran, generation, initialGeneration);
                        buildCSV(gran, generation, initialGeneration);
                    }
                }
            }
        }
    }

    private void runElitesIlluminator(int granularity, int generations, int initialGenerations) {
        startTime = System.nanoTime();
        elitesIlluminator = new ElitesIlluminator(granularity);
        elitesIlluminator.illuminateElites(generations, initialGenerations, false);
        endTime = System.nanoTime();
    }

    private void buildCSV(int granularity, int generations, int initialGenerations) {
        long duration = (endTime - startTime);
        System.out.println("Writing: " + granularity + ", " + generations + ", " + initialGenerations + ", " + (duration / 1000000000L) + " sec");
        int solutionsCount = 0;
        int sectorsContainingSolutions = 0;

        IFeatureSpace featureSpaceHandler = elitesIlluminator.getIlluminatedFeatureSpace();
        Map<ISectorSpan, ISector> featureSpace = featureSpaceHandler.getFeatureSpace();
        for (ISectorSpan sectorSpan : featureSpace.keySet()) {
            if (featureSpace.get(sectorSpan).getParetoFront().size() > 0) {
                sectorsContainingSolutions++;
            }
            for (ISolution solution : featureSpace.get(sectorSpan).getParetoFront()) {
                solutionsCount++;
            }
        }

        elitesIlluminator.getGenerationsTree();
        int numberOfParentsWithChildren = 0;
        int numberOfChildren = 0;
        int numberOfChildrenBetterThanParent = 0;
        int numberOfChildrenWorseThanParent = 0;
        int numberOfChildrenEqualToParent = 0;
        for (ISolution parent : elitesIlluminator.getGenerationsTree().keySet()) {
            LinkedList<ISolution> children = elitesIlluminator.getGenerationsTree().get(parent);
            if (children.size() > 0) {
                numberOfParentsWithChildren++;
                numberOfChildren += children.size();
            }

            for (ISolution child : children) {
                if (child.compareTo(parent) > 0) {
                    numberOfChildrenBetterThanParent++;
                } else if (child.compareTo(parent) < 0) {
                    numberOfChildrenWorseThanParent++;
                } else {
                    numberOfChildrenEqualToParent++;
                }
            }
        }

        sb.append(granularity).append(",");
        sb.append(generations).append(",");
        sb.append(initialGenerations).append(",");
        sb.append((double) sectorsContainingSolutions / (double) featureSpaceHandler.getFeatureSpace().values().size() * 100).append(",");
        sb.append(solutionsCount).append(",");
        sb.append(featureSpaceHandler.getRecalculation()).append(",");
        sb.append(sectorsContainingSolutions).append(",");

        sb.append(numberOfParentsWithChildren).append(",");
        sb.append((double) numberOfChildren / (double) numberOfParentsWithChildren).append(",");
        sb.append((double) numberOfChildrenBetterThanParent / (double) numberOfParentsWithChildren).append(",");
        sb.append((double) numberOfChildrenWorseThanParent / (double) numberOfParentsWithChildren).append(",");
        sb.append((double) numberOfChildrenEqualToParent / (double) numberOfParentsWithChildren).append(",");

        sb.append((double) numberOfChildrenBetterThanParent / (double) numberOfChildren * 100).append(",");
        sb.append((double) numberOfChildrenWorseThanParent / (double) numberOfChildren * 100).append(",");
        sb.append((double) numberOfChildrenEqualToParent / (double) numberOfChildren * 100).append(",");
        sb.append(newLine);
    }

}

