package Printer;

public enum Command {

    HELP("help"),
    REPORT("report"),
    RUN("run"),
    RERUN("rerun"),
    SAVE("save"),
    SOLUTION("solution"),
    SOLUTIONS("solutions"),
    GENERATIONS("generations"),
    STOP("stop");

    private final String command;

    Command(String command) {
        this.command = command;

    }

    public String getCommand() {
        return command;
    }
}
