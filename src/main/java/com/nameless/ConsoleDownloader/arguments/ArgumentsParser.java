package com.nameless.ConsoleDownloader.arguments;

import java.util.ArrayList;
import java.util.List;

class ArgumentsParser {
    private final List<Argument> arguments = new ArrayList<>();

    public void addArgument(final Argument argument) {
        arguments.add(argument);
    }

    public void parse(final String[] args) throws IllegalArgumentException {

        int i = 0;

        while (i < args.length) {
            Argument arg = getArgWithFlag(args[i]);

            try {
                if (arg != null)
                    arg.setValue(args[i + 1]);
                else {
                    throw new IllegalArgumentException(String.format("Unknown flag %s", args[i]));
                }
            } catch (ArrayIndexOutOfBoundsException e ) {
                throw new IllegalArgumentException(String.format("%s is require value < %s >", arg.getFlag(), arg.getDescription()), e);
            }

            i += 2;
        }

        checkAllArgumentsPassed();
    }

    private Argument getArgWithFlag(String flag) {
        for (Argument arg : arguments)
            if (arg.getFlag().equals(flag))
                return arg;

        return null;
    }

    private void checkAllArgumentsPassed() {
        for (Argument arg : arguments)
            if (arg.getValue() == null && arg.isRequired()) {
                throw new IllegalArgumentException(String.format("Flag %s <%s> is required", arg.getFlag(), arg.getDescription()));
            }
    }

}
