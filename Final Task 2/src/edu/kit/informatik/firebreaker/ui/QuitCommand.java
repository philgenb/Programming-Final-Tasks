package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;

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
    public void parseCommandLine(String[] arguments) throws FireBreakerException {
        if (arguments.length != getMaximumArgumentLength()) {
            throw new FireBreakerException(ErrorMessages.INVALID_ARGUMENT_LENGTH);
        }
    }

    @Override
    public Result execute() {
        this.session.stop();
        return new Result(Result.ResultType.SUCCESS, null);
    }


}
