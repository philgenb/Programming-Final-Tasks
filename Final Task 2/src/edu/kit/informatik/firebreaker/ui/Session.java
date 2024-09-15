package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.firebreaker.core.Application;
import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.model.FireBreaker;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.status.InOutputStrings;

/**
 * Verarbeitung von Benutzereingaben und -ausgaben. Hier findet außerdem die Fehlerbehandlung statt.
 * Erlaubt die Ausführung von {@link Command}.
 * Eine laufende Sitzung kann gestoppt werden.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class Session {

    private static final int COMMAND_LINE_LIMITER = 2;
    private static final int ARGUMENT_LIMITER = -1;

    private boolean running = true;

    private FireBreakerCommands fireBreakerCommands;
    private FireBreaker fireBreakerGameInstance;

    private final String[] startParameters;

    /**
     * Erstellt eine neue Instanz einer Verarbeitungsklasse von Benutzereingaben.
     * @param startParameters Startparameter, welche das Startspielfeld beschreiben
     * @param fireBreakerGameInstance Instanz des FireBreaker Spiels
     */
    public Session(FireBreaker fireBreakerGameInstance, String[] startParameters) {
        this.fireBreakerGameInstance = fireBreakerGameInstance;
        this.startParameters = startParameters;
        this.fireBreakerCommands = new FireBreakerCommands(fireBreakerGameInstance, this);
    }

    /**
     * Führt das Programm aus, welches anschließend auf neue Eingaben des Benutzers
     * in der Kommandozeile wartet.
     */
    public void run() {
        while (running) {
            String input = Terminal.readLine();
            String[] lineContent = input.split(InOutputStrings.COMMAND_SAPARATOR, COMMAND_LINE_LIMITER);

            String commandName = lineContent[0];
            String[] commandArguments;

            if (lineContent.length >= COMMAND_LINE_LIMITER) {
                commandArguments = lineContent[1].split(InOutputStrings.ARGUMENT_SAPARATOR, ARGUMENT_LIMITER);
            } else {
                commandArguments = new String[0];
            }

            if (fireBreakerCommands.containsCommand(commandName)) {
                Command command = fireBreakerCommands.getCommandMap().get(commandName);
                try {
                    command.parseCommandLine(commandArguments);
                    executeCommand(command);
                } catch (FireBreakerException fireBreakerException) {
                    Terminal.printError(fireBreakerException.getMessage());
                }
            } else {
                Terminal.printError(ErrorMessages.COMMAND_DOES_NOT_EXIST);
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
                throw new IllegalStateException(ErrorMessages.ILLEGAL_STATE);
        }
    }


    /**
     * Stoppt das Programm.
     */
    public void stop() {
        this.running = false;
    }

    /**
     * Setzt ein aktuelle laufendes Spiel zurück.
     */
    public void reset() {
        Terminal.printLine(InOutputStrings.SUCCESS);
        this.fireBreakerGameInstance = Application.buildGame(startParameters);
        this.fireBreakerCommands = new FireBreakerCommands(fireBreakerGameInstance, this);
        run();
    }

}
