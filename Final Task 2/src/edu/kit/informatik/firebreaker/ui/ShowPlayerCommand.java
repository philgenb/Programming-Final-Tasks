package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.exception.FireBreakerInputException;
import edu.kit.informatik.firebreaker.model.FireBreaker;
import edu.kit.informatik.firebreaker.model.Player;

/**
 * Modelliert den Befehl show-player. Dieser parameterlose Befehl zeigt
 * bestimmte Eigenschaften des aktiven Spielers an.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class ShowPlayerCommand extends Command {

    /**
     * Name des Show-Player Befehls.
     */
    public static final String SHOWPLAYER_COMMAND_NAME = "show-player";

    private static final int MAXIMUM_ARGUMENT_LENGTH = 0;
    private final FireBreaker fireBreaker;
    private Player playerOnTurn;

    /**
     * Erstellt eine neue Instanz des Show-Player Befehls.
     * @param fireBreaker Instanz des Firebreaker Spiels
     */
    protected ShowPlayerCommand(FireBreaker fireBreaker) {
        super(MAXIMUM_ARGUMENT_LENGTH);
        this.fireBreaker = fireBreaker;
    }

    @Override
    public void parseCommandLine(String[] arguments) throws FireBreakerException {
        if (arguments.length != getMaximumArgumentLength()) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_LENGTH);
        }
        if (fireBreaker.isGameFinish()) {
            throw new FireBreakerException(ErrorMessages.GAME_ALREADY_FINISH);
        }
        this.playerOnTurn = fireBreaker.getPlayerOnTurn();
    }

    @Override
    public Result execute() {
        return new Result(Result.ResultType.SUCCESS, fireBreaker.getPlayerStats(playerOnTurn));
    }
}
