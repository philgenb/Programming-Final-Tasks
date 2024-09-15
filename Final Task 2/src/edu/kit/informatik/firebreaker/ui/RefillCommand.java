package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.exception.FireBreakerInputException;
import edu.kit.informatik.firebreaker.gameboard.FireFighter;
import edu.kit.informatik.firebreaker.model.FireBreaker;
import edu.kit.informatik.firebreaker.model.Player;

/**
 * Modelliert den Befehl refill, zum Nachfüllen von Feuerwehren.
 * Eine Feuerwehr kann seinen Wassertank nachfüllen, wenn sie an einer Feuerwehrstation
 * oder einem Löschteich angrenzt.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class RefillCommand extends Command {

    /**
     * Eindeutiger Name des refill Befehl.
     */
    public static final String REFILL_COMMAND_NAME = "refill";

    private static final int MAXIMUM_ARGUMENT_LENGTH = 1;

    private final FireBreaker fireBreaker;
    private Player playerOnTurn;
    private FireFighter fireFighter;

    /**
     * Erstellt eine neue Instanz des Refill Befehls.
     * @param fireBreaker Instanz des Firebreaker Spiels
     */
    protected RefillCommand(FireBreaker fireBreaker) {
        super(MAXIMUM_ARGUMENT_LENGTH);
        this.fireBreaker = fireBreaker;
    }

    @Override
    public void parseCommandLine(String[] arguments) throws FireBreakerException {
        this.playerOnTurn = fireBreaker.getPlayerOnTurn();
        if (arguments.length != MAXIMUM_ARGUMENT_LENGTH) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_LENGTH);
        }

        if (fireBreaker.isGameFinish()) {
            throw new FireBreakerException(ErrorMessages.GAME_ALREADY_FINISH);
        }

        if (fireBreaker.isRoundFinish()) {
            throw new FireBreakerException(ErrorMessages.ROUND_FINISH);
        }

        fireFighter = playerOnTurn.getFireFighterByStringIdentifier(arguments[0]);
        if (fireFighter == null) {
            throw new FireBreakerInputException(ErrorMessages.FIREFIGHTER_DOES_NOT_EXIST);
        }
    }

    @Override
    public Result execute() {
        try {
            return new Result(Result.ResultType.SUCCESS, String.valueOf(fireBreaker.refillFireFighter(fireFighter)));
        } catch (FireBreakerException fireBreakerException) {
            return new Result(Result.ResultType.FAILURE, fireBreakerException.getMessage());
        }
    }
}
