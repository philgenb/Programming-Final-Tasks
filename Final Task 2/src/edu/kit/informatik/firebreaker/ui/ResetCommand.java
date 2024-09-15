package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.exception.FireBreakerInputException;

/**
 * Modelliert den Befehl reset zum zurücksetzen des laufenden Spiels.
 * Dabei wird das Spielfeld und die Programmlogik zurück auf den Ausgangszustand gesetzt.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class ResetCommand extends Command {

    /**
     * Name des Reset Befehls.
     */
    public static final String RESET_COMMAND_NAME = "reset";
    private static final int MAXIMUM_ARGUMENT_LENGTH = 0;

    private final Session session;

    /**
     * Erstellt eine neue Instanz eines Reset-Befehls.
     * @param session aktuelle Sitzung
     */
    protected ResetCommand(Session session) {
        super(MAXIMUM_ARGUMENT_LENGTH);
        this.session = session;
    }

    @Override
    public void parseCommandLine(String[] arguments) throws FireBreakerException {
        if (arguments.length != MAXIMUM_ARGUMENT_LENGTH) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_LENGTH);
        }
    }

    @Override
    public Result execute() {
        session.reset();
        return new Result(Result.ResultType.SUCCESS, null);
    }
}
