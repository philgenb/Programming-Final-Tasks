package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.exception.FireBreakerInputException;
import edu.kit.informatik.firebreaker.model.FireBreaker;

/**
 * Modelliert den Turn-Befehl zum Beenden der Runde des aktiven Spielers.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class TurnCommand extends Command {

    /**
     * Name des Turn-Befehls.
     */
    public static final String TURN_COMMAND_NAME = "turn";

    private static final int MAXIMUM_ARGUMENT_LENGTH = 0;

    private final FireBreaker fireBreaker;

    /**
     * Erstellt eine neue Instanz des Turn-Befehls.
     * @param fireBreaker Instanz eines Firebreaker Spiels
     */
    protected TurnCommand(FireBreaker fireBreaker) {
        super(MAXIMUM_ARGUMENT_LENGTH);
        this.fireBreaker = fireBreaker;
    }

    @Override
    public void parseCommandLine(String[] arguments) throws FireBreakerException {
        if (arguments.length != MAXIMUM_ARGUMENT_LENGTH) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_LENGTH);
        }
        if (fireBreaker.isGameFinish()) {
            throw new FireBreakerException(ErrorMessages.GAME_ALREADY_FINISH);
        }
        if (fireBreaker.isRoundFinish()) {
            throw new FireBreakerException(ErrorMessages.ROUND_FINISH);
        }
    }

    @Override
    public Result execute() {
        fireBreaker.finishTurn();
        return new Result(Result.ResultType.SUCCESS, fireBreaker.getPlayerOnTurn().getIdentifier());
    }
}
