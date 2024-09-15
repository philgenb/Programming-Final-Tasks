package edu.kit.informatik.escaperoutenetwork.userinterface;

import edu.kit.informatik.escaperoutenetwork.core.Result;
import edu.kit.informatik.escaperoutenetwork.exception.ErrorMessages;
import edu.kit.informatik.escaperoutenetwork.exception.EscapeNetworkException;
import edu.kit.informatik.escaperoutenetwork.exception.EscapeNetworkInputException;

/**
 * Modelliert den Quit-Befehl, welcher bei Ausführung zum Stopp des Programms führt.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class QuitCommand extends Command {

    /**
     * Eindeutiger Name des Quit-Befehls.
     */
    public static final String QUIT_COMMAND_NAME = "quit";
    private static final int MAX_ARGUMENTS = 0;

    private final Session session;

    /**
     * Erstellt eine neue Instanz eines Quit Befehls.
     * @param session Sitzung
     */
    public QuitCommand(Session session) {
        super(MAX_ARGUMENTS);
        this.session = session;
    }

    @Override
    public void parseCommandLine(String[] arguments) throws EscapeNetworkException {
        if (arguments.length != getMaximumArgumentLength()) {
            throw new EscapeNetworkInputException(ErrorMessages.LENGTH.toString());
        }
    }

    @Override
    public Result execute() {
        this.session.stop();
        return new Result(Result.ResultType.SUCCESS, null);
    }

}
