package edu.kit.informatik.escaperoutenetwork.userinterface;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.escaperoutenetwork.core.EscapeRouteNetworkManager;
import edu.kit.informatik.escaperoutenetwork.core.Result;
import edu.kit.informatik.escaperoutenetwork.exception.ErrorMessages;
import edu.kit.informatik.escaperoutenetwork.exception.EscapeNetworkException;

/**
 * Verarbeitung von Benutzereingaben und -ausgaben. Hier findet außerdem die Fehlerbehandlung statt.
 * Erlaubt die Ausführung von {@link Command}.
 * Eine laufende Sitzung kann gestoppt werden.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class Session {

    private static final String SPACE_SAPARATOR = " ";
    private static final int COMMAND_LINE_LIMITER = 2;
    private static final int ARGUMENT_LIMITER = -1;

    private boolean running = true;

    private final EscapeRouteNetworkManager escapeRouteNetworkManager;
    private final EscapeRouteCommands escapeRouteCommands;

    /**
     * Erstellt eine neue Sitzung zur Befehlsausführung.
     */
    public Session() {
        this.escapeRouteNetworkManager = new EscapeRouteNetworkManager();
        this.escapeRouteCommands = new EscapeRouteCommands(escapeRouteNetworkManager, this);
    }

    /**
     * Führt das Programm aus und wartet auf neue Eingaben des Nutzers.
     */
    public void run() {
        while (running) {
            String input = Terminal.readLine();
            String[] lineContent = input.split(SPACE_SAPARATOR, COMMAND_LINE_LIMITER);

            String commandName = lineContent[0];
            String[] commandArguments;

            if (lineContent.length >= COMMAND_LINE_LIMITER) {
                commandArguments = lineContent[1].split(SPACE_SAPARATOR, ARGUMENT_LIMITER);
            } else {
                commandArguments = new String[0];
            }

            if (escapeRouteCommands.get().containsKey(commandName)) {
                Command command = escapeRouteCommands.get().get(commandName);
                try {
                    command.parseCommandLine(commandArguments);
                    executeCommand(command);
                } catch (EscapeNetworkException escapeNetworkException) {
                    Terminal.printError(escapeNetworkException.getMessage());
                }
            } else {
                Terminal.printError(ErrorMessages.COMMAND_DOES_NOT_EXIST.toString());
            }
        }
    }

    private void executeCommand(final Command command) {
        final Result result = command.execute();
        switch (result.getType()) {
            case SUCCESS:
                if (result.getMessage() != null) {
                    Terminal.printLine(result.getMessage());
                }
                break;
            case FAILURE:
                if (result.getMessage() != null) {
                    Terminal.printError(result.getMessage());
                }
                break;
            default:
                throw new IllegalStateException(ErrorMessages.ILLEGAL_STATE.toString());
        }
    }

    /**
     * Stoppt das Programm.
     */
    public void stop() {
        this.running = false;
    }

}
