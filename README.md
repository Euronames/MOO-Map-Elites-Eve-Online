# Eve Online - Fitting Explorer

This project was created as a part of a bachelors project for the [Southern Danish University](https://www.sdu.dk/en/), where the goal was to implement a prototype and a proof of concept on using the [MAP-Elites](https://arxiv.org/pdf/1504.04909.pdf) algorithm for solving complex problems.
This program uses data from [Eve Online](https://www.eveonline.com/) to generate spaceship solutions based on the fitting of components.
The MAP-Elites algorithm is chosen for this project as the algorithm illuminates possible solutions and maps them into a featurespace based on their performance.
The algorithm implemented is modified a bit from the original MAP-Elites as it stores a pareto front (known from [multi-objective optimization](https://en.wikipedia.org/wiki/Multi-objective_optimization)) within each grain of the feature space. This allows for a very broad search which yields only the best solutions within fine grained spectrum.

The project is build using the [IntelliJ IDEA](https://www.jetbrains.com/).

The main language of the project is Java 8, which is choosen based on the criteria of readability and to realize the implementation in a object oriented approach.
As the project is still in a prototype stage, the need for communication of central concepts recieves a higher priority over the general overhead of such systems.

# System Overview
> The **.idea** folders contains the basic settings for the IntelliJ IDEA which is used for building the system.

> The **lib** folder contains all the library files used which is not part of the standard Java 8 framework.

> The **sql** folder contains SQL statements which have been saved for later use or general modification.

> The **src** folder contains the source code for the project.

> The **test** folder contains the tests generated for the project.

> The **pyfa_eve.db** is the database used for fetching Eve Online data.

# The System Commands
The system UI is still in development, and a very barebone CLI is the main operationel approach for using the system.
When the `main` class is running the help menu can be shown by typing `help`.

The following options will be shown:

| Command       | Description                                                                   |
| ------------- |-------------------------------------------------------------------------------|
| run           | Runs the algorithm with a new configuration                                   |
| rerun         | Run the previous configuration again                                          |
| report        | Prints a report of the run                                                    |
| save          | Saves results from the run as a Json file                                     |
| solution      | Finds the best solutions from the saved results                               |
| solutions     | Prints the entire feature space, complete with solutions and their components |
| generations   | Prints a generations report                                                   |
| help          | Prints the help menu                                                          |
| stop          | Stops the program                                                             |


The `run` command will ask you to input a `granularity`, and amount of `generations`, and an `initial` set of random generated `generations` and ask if the run should store duplicate solutions.

> The `granularity` defines how fine grained the search should be. Though mind the size of this factor, as it is one of the factors eliminating the ease of use. The `granularity` enhances the complexity of the search by the granularity to the power of the number of features. Another factor to take into perspective before running the algorithm, is the storing of duplicates, as the grains of the featurespace is maintained to only store the best solution, storing duplicates makes the sorting of these grains more complex, and in practice hurts the runtime of the algorithm.

