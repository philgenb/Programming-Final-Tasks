package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.dice.DiceResults;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.exception.FireBreakerInputException;
import edu.kit.informatik.firebreaker.model.FireBreaker;
import edu.kit.informatik.firebreaker.model.Player;
import edu.kit.informatik.firebreaker.status.InOutputStrings;

/**
 * Modelliert den Befehl fire-to-roll. Mit diesem Befehl kann der Spieler
 * im Rahmen der Regeln ein Symbol übergeben, das als Ergebnis eines Würfelwurfes
 * interpretiert wird. Das Symbol beschreibt die Ausbreitung des Feuers
 * nach Abschluss einer Spielrunde.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class FireToRollCommand extends Command {

    /**
     * Name des Befehls fire-to-roll.
     */
    public static final String FIRETOROLL_COMMAND_NAME = "fire-to-roll";
    private static final String DICE_REGEX = "[1-6]";
    private static final int MAXIMUM_ARGUMENT_LENGTH = 1;

    private final FireBreaker fireBreaker;

    private DiceResults diceResult;

    /**
     * Erstellt eine neue Instanz des Fire-To-Roll Befehls.
     * @param fireBreaker firebreaker Spielinstanz
     */
    protected FireToRollCommand(FireBreaker fireBreaker) {
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
        if (!fireBreaker.isRoundFinish()) {
            throw new FireBreakerException(ErrorMessages.ROUND_NOT_FINISHED);
        }

        if (!arguments[0].matches(DICE_REGEX)) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_DICE_NUMBER);
        }

        int diceNumber = Integer.parseInt(arguments[0]);
        this.diceResult = DiceResults.valueOfDiceNumber(diceNumber);
    }

    @Override
    public Result execute() {
        Player newPlayer = fireBreaker.startNextRound(diceResult);
        if (newPlayer != null) {
            return new Result(Result.ResultType.SUCCESS, newPlayer.toString());
        }
        if (fireBreaker.checkLose()) {
            return new Result(Result.ResultType.SUCCESS, InOutputStrings.LOST_GAME);
        }
        return new Result(Result.ResultType.SUCCESS, InOutputStrings.SUCCESS);
    }
}
