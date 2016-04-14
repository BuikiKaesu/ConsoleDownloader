package com.nameless.ConsoleDownloader.arguments;

public class AppArguments {

    private final Argument numberOfThreads = new Argument.Builder("-n", "number of threads").defaultValue("1").build();
    private final Argument speedLimit = new Argument.Builder("-l", "download speed limit").defaultValue("0").build();
    private final Argument urlsFile = new Argument.Builder("-f", "file with urls").isRequired(true).build();
    private final Argument outputFolder = new Argument.Builder("-o", "output folder").isRequired(true).build();

    public AppArguments(final String[] args) {
        ArgumentsParser argsParser = new ArgumentsParser();
        argsParser.addArgument(numberOfThreads);
        argsParser.addArgument(speedLimit);
        argsParser.addArgument(urlsFile);
        argsParser.addArgument(outputFolder);
        argsParser.parse(args);

        validateArguments();
    }

    public String getUrlsFile() {
        return urlsFile.getValue();
    }

    public String getOutputFolder() {
        return outputFolder.getValue();
    }

    public int getNumberOfThreads() {
        return Integer.parseInt(numberOfThreads.getValue());
    }

    public long getSpeedLimit() {
        String value = speedLimit.getValue();
        char unit = value.charAt(value.length() - 1);
        long limit;

        switch (unit) {
            case 'k': limit = Long.parseLong(value.substring(0, value.length() - 1)) * 1024;
                break;
            case 'm': limit = Long.parseLong(value.substring(0, value.length() - 1)) * 1024 * 1024;
                break;
            default:
                limit = Long.parseLong(value);
        }

        return limit;
    }

    private void validateArguments() {
        try {
            if (getSpeedLimit() < 0)
                throw new IllegalArgumentException("Speed limit must be positive value");
            if (getNumberOfThreads() < 1)
                throw new IllegalArgumentException("Number of threads must be more than zero");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid argument value");
        }
    }
}
