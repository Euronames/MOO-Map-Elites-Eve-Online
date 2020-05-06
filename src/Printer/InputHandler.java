package Printer;

import JsonParser.Reader;
import JsonParser.Writer;
import MapElite.ElitesIlluminator;
import MapElite.FeatureSpace.IFeatureSpace;
import MapElite.FeatureSpace.ISector;
import MapElite.FeatureSpace.ISectorSpan;
import MapElite.IFeature;
import MapElite.IMapElitesFacade;
import MapElite.ISolution;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class InputHandler implements IInputHandler {
    private Scanner scanner;
    private int granularity;
    private int generations;
    private int initialGenerations;
    private boolean storeDuplicates;
    private long startTime;
    private long endTime;
    private Writer writer;
    private Reader reader;
    private IMapElitesFacade elitesIlluminator;
    private String input;
    private DecimalFormat df;


    public InputHandler() {
        scanner = new Scanner(System.in);
        writer = new Writer();
        reader = new Reader();
        granularity = 0;
        generations = 0;
        initialGenerations = 0;
        input = "";
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
    }

    private void primeAlgorithm() {
        System.out.println("Granularity:");
        while (!scanner.hasNextInt()) {
            System.out.println("That is not an integer, try again");
            scanner.next();
        }
        granularity = scanner.nextInt();

        System.out.println("Generations:");
        while (!scanner.hasNextInt()) {
            System.out.println("That is not an integer, try again");
            scanner.next();
        }
        generations = scanner.nextInt();

        System.out.println("Initial Generations:");
        while (!scanner.hasNextInt()) {
            System.out.println("That is not an integer, try again");
            scanner.next();
        }
        initialGenerations = scanner.nextInt();

        System.out.println("Store Duplicates?:");
        while (!scanner.hasNextBoolean()) {
            System.out.println("That is not a boolean, try again");
            System.out.println("Booleans are either 'true' or 'false'");
            scanner.next();
        }
        storeDuplicates = scanner.nextBoolean();

        System.out.println("Running with following configuration: ");
        System.out.println("Granularity: " + granularity + " Generations: " + generations + " Initial Generations: " + initialGenerations + " Store Duplicates?: " + storeDuplicates);
        System.out.println("---------------------------------------------------------------------------------------");
    }

    private void blingBling() {
        try {
            String ANSI_RESET = "\u001B[0m";
            String ANSI_CYAN = "\u001B[36m";
            System.out.println(ANSI_CYAN + "                        ___\n" +
                    "                    __,' __`.                _..----....____\n" +
                    "        __...--.'``;.   ,.   ;``--..__     .'    ,-._    _.-'\n" +
                    "  _..-''-------'   `'   `'   `'     O ``-''._   (,;') _,'\n" +
                    ",'________________                          \\`-._`-','\n" +
                    " `._              ```````````------...___   '-.._'-:\n" +
                    "    ```--.._      ,.                     ````--...__\\-.\n" +
                    "            `.--. `-`                       ____    |  |`\n" +
                    "              `. `.                       ,'`````.  ;  ;`\n" +
                    "                `._`.        __________   `.      \\'__/`\n" +
                    "                   `-:._____/______/___/____`.     \\  `\n" +
                    "                               |       `._    `.    \\\n" +
                    "                               `._________`-.   `.   `.___\n" +
                    "                                             -----`------'`" + ANSI_RESET);
            Thread.sleep(500);
            String ANSI_GREEN = "\u001B[32m";
            System.out.println(ANSI_GREEN + "3" + ANSI_RESET);
            Thread.sleep(500);
            String ANSI_YELLOW = "\u001B[33m";
            System.out.println(ANSI_YELLOW + "2" + ANSI_RESET);
            Thread.sleep(500);
            String ANSI_RED = "\u001B[31m";
            System.out.println(ANSI_RED + "1" + ANSI_RESET);
            Thread.sleep(500);
            String ANSI_BLUE = "\u001B[34m";
            String ANSI_PURPLE = "\u001B[35m";
            System.out.println(ANSI_PURPLE + "L" + ANSI_GREEN + "i" + ANSI_BLUE + "f" + ANSI_RED + "t" + ANSI_YELLOW + " o" + ANSI_CYAN + "f" + ANSI_BLUE + "f" + ANSI_PURPLE + "!" + ANSI_YELLOW);
            System.out.println(ANSI_RESET);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("What do you want to do? ");
        System.out.println("For a help menu type: " + Command.HELP.getCommand());
        System.out.println("---------------------------------------------------------------------------------------");
        while (!input.equals(Command.STOP.getCommand())) {
            input = scanner.nextLine();
            executeCommand(input);
        }
        scanner.close();
    }

    private void executeCommand(String input) {
        if (input.equalsIgnoreCase(Command.RUN.getCommand())) {
            resetAlgorithm();
            startAlgorithm();
        } else if (input.equalsIgnoreCase(Command.REPORT.getCommand())) {
            printReport();
        } else if (input.equalsIgnoreCase(Command.SAVE.getCommand())) {
            saveResultsAsJson();
        } else if (input.equalsIgnoreCase(Command.HELP.getCommand())) {
            printHelp();
        } else if (input.equalsIgnoreCase(Command.SOLUTION.getCommand())) {
            findSolution();
        } else if (input.equalsIgnoreCase((Command.SOLUTIONS.getCommand()))) {
            printSolutions();
        } else if (input.equalsIgnoreCase(Command.RERUN.getCommand())) {
            startAlgorithm();
        } else if (input.equalsIgnoreCase(Command.GENERATIONS.getCommand())) {
            printGenerationsReport();
        } else if (input.equalsIgnoreCase(Command.STOP.getCommand())) {
            System.out.println("Stopping...");
        } else if (!input.equalsIgnoreCase("")) {
            System.out.println("I did not understand that command, you can type " + Command.HELP.getCommand() + " to see your commands");
        }
    }

    private void findSolution() {
        System.out.println("---------------------------------------------------------------------------------------");
        if (new File("illuminatedFeatureSpace.json").exists()) {
            System.out.println("What features do you want to find the best solution for? ");
            System.out.println("Write one feature at a time and then press enter.");
            System.out.println("Your options are: ");

            for (Features feature : Features.values()) {
                System.out.print("'" + feature.getFeature() + "' ");
            }
            System.out.println();
            System.out.println("Finish by writing 'done' and then press enter");

            List<String> features = new ArrayList<>();

            while (!input.equalsIgnoreCase("done")) {
                input = scanner.nextLine();

                if (!input.equalsIgnoreCase("done")) {
                    for (Features feature : Features.values()) {
                        if (input.equalsIgnoreCase(String.valueOf(feature))) {
                            features.add(input.toLowerCase());
                        }
                    }
                }
            }
            System.out.println("Searching the Result set...");
            System.out.println(features);
            reader.readShipList(features);
        } else {
            System.out.println("You need to save a run before you can find a solution");
        }
        System.out.println("---------------------------------------------------------------------------------------");
    }

    private void printHelp() {
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println(Command.RUN.getCommand() + ":\t\t\t Runs the algorithm with a new setup");
        System.out.println(Command.RERUN.getCommand() + ":\t\t\t Runs the algorithm with same setup");
        System.out.println(Command.REPORT.getCommand() + ":\t\t\t Prints a report of the run");
        System.out.println(Command.SAVE.getCommand() + ":\t\t\t Saves results from the run as a Json file");
        System.out.println(Command.SOLUTION.getCommand() + ":\t\t Finds the best sollutions from the saved result set");
        System.out.println(Command.SOLUTIONS.getCommand() + ":\t\t Prints the entire featurespace, complete with solutions");
        System.out.println(Command.GENERATIONS.getCommand() + ":\t Prints the generation report");
        System.out.println(Command.HELP.getCommand() + ":\t\t\t Prints this menu");
        System.out.println(Command.STOP.getCommand() + ":\t\t\t Stops the program");
        System.out.println("---------------------------------------------------------------------------------------");
    }

    private void startAlgorithm() {
        System.out.println("---------------------------------------------------------------------------------------");
        if (!algorithmHasRun()) {
            primeAlgorithm();
        }
        blingBling();
        startTime = System.nanoTime();
        elitesIlluminator = new ElitesIlluminator(granularity);
        elitesIlluminator.illuminateElites(generations, initialGenerations, storeDuplicates);
        endTime = System.nanoTime();
        System.out.println("Done running the algorithm");
        System.out.println("---------------------------------------------------------------------------------------");
    }

    private void printReport() {
        System.out.println("---------------------------------------------------------------------------------------");
        if (algorithmHasRun()) {

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

            System.out.println("Runtime Report: ");
            long duration = (endTime - startTime);
            System.out.println("--> Runtime in sec: " + duration / 1000000000L);
            System.out.println("--> Runtime in min: " + duration / 60000000000L);
            System.out.println("--> Granularity: " + granularity);
            System.out.println("--> Generations in total: " + generations);
            System.out.println("--> Initial generations: " + initialGenerations);
            System.out.println("--> Does store duplicates: " + storeDuplicates);
            System.out.println("--> Number of dimensions: " + featureSpaceHandler.getAmountOfFeatures());
            System.out.println("--> Number of solutions found: " + solutionsCount);
            System.out.println("--> Number of sectors: " + featureSpaceHandler.getFeatureSpace().values().size());
            System.out.println("--> Sectors containing at least one solution: " + sectorsContainingSolutions);
            System.out.println("--> Sector coverage: " + df.format((double) sectorsContainingSolutions / (double) featureSpaceHandler.getFeatureSpace().values().size() * 100) + "%");
            System.out.println("--> Number of times the featurespace was recalculated: " + featureSpaceHandler.getRecalculation());
        } else {
            System.out.println("You need to run the algorithm before you can get a report");
        }
        System.out.println("---------------------------------------------------------------------------------------");
    }

    private void printSolutions() {
        System.out.println("---------------------------------------------------------------------------------------");
        if (algorithmHasRun()) {
            IFeatureSpace featureSpaceHandler = elitesIlluminator.getIlluminatedFeatureSpace();
            Map<ISectorSpan, ISector> featureSpace = featureSpaceHandler.getFeatureSpace();
            for (ISectorSpan sectorSpan : featureSpace.keySet()) {
                System.out.println("Sector span : ");
                for (IFeature feature : sectorSpan.getSectorSpans().keySet()) {
                    System.out.println("Feature: " + feature.getFeatureDescription() + " - " + "min: " + Math.round(sectorSpan.getSectorSpans().get(feature).getMin()) + ", max: " + Math.round(sectorSpan.getSectorSpans().get(feature).getMax()));


                }
                for (ISolution solution : featureSpace.get(sectorSpan).getParetoFront()) {
                    System.out.println();
                    System.out.println("--> Solution:");
                    for (IFeature feature : solution.getFeatureDescriptor().getFeatureDescriptions().keySet()) {
                        System.out.println(solution);
                        System.out.println("--> Feature: " + feature.getFeatureDescription() + " - " + "value: " + solution.getFeatureDescriptor().getFeatureDescriptions().get(feature).getScore());
                    }
                }
            }
        } else {
            System.out.println("You need to run the algorithm before you can print solutions");
        }
        System.out.println("---------------------------------------------------------------------------------------");
    }

    private void printGenerationsReport() {
        System.out.println("---------------------------------------------------------------------------------------");
        if (algorithmHasRun()) {
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
            System.out.println("Generations Report: ");
            System.out.println("--> Number of solutions with parents: " + numberOfParentsWithChildren);
            System.out.println("--> Average number of children pr. parent: " + df.format((double) numberOfChildren / (double) numberOfParentsWithChildren));
            System.out.println("--> Average number of new elite children pr. parent: " + df.format((double) numberOfChildrenBetterThanParent / (double) numberOfParentsWithChildren));
            System.out.println("--> Average number of new non-elite children pr. parent: " + df.format((double) numberOfChildrenWorseThanParent / (double) numberOfParentsWithChildren));
            System.out.println("--> Average number of new equal children pr. parent: " + df.format((double) numberOfChildrenEqualToParent / (double) numberOfParentsWithChildren));
            System.out.println();
            System.out.println("--> Percent of all children that is new elite children: " + df.format((double) numberOfChildrenBetterThanParent / (double) numberOfChildren * 100) + "%");
            System.out.println("--> Percent of all children that is new non-elite children: " + df.format((double) numberOfChildrenWorseThanParent / (double) numberOfChildren * 100) + "%");
            System.out.println("--> Percent of all children that is new equal children: " + df.format((double) numberOfChildrenEqualToParent / (double) numberOfChildren * 100) + "%");
        } else {
            System.out.println("You need to run the algorithm before you can see the generations tree");
        }
        System.out.println("---------------------------------------------------------------------------------------");
    }

    private void saveResultsAsJson() {
        System.out.println("---------------------------------------------------------------------------------------");
        if (algorithmHasRun()) {
            System.out.println("Saving results");
            writer.writeObjectAsJsonFile(elitesIlluminator.getIlluminatedFeatureSpace().getFeatureSpace());
        } else {
            System.out.println("You need to run the algorithm before you can save it");
        }
        System.out.println("---------------------------------------------------------------------------------------");
    }

    private boolean algorithmHasRun() {
        return granularity != 0 || generations != 0 || initialGenerations != 0;
    }

    private void resetAlgorithm() {
        granularity = 0;
        generations = 0;
        initialGenerations = 0;
    }
}