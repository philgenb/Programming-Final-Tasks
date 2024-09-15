package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.core.Position;
import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.exception.FireBreakerInputException;
import edu.kit.informatik.firebreaker.gameboard.FireFighter;
import edu.kit.informatik.firebreaker.gameboard.GameCell;
import edu.kit.informatik.firebreaker.model.FireBreaker;
import edu.kit.informatik.firebreaker.model.Player;
import edu.kit.informatik.firebreaker.status.InOutputStrings;

/**
 * Modelliert den Befehl extinguish mit dem ein aktiver Spieler mit einer seiner Feuerwehren
 * ein benachbartes Waldfeld löschen kann.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class ExtinguishCommand extends Command {

    /**
     * Name des Befehls extinguish.
     */
    public static final String EXTINGUISH_COMMAND_NAME = "extinguish";

    private static final int MAXIMUM_ARGUMENT_LENGTH = 3;
    private static final String NUMBER_REGEX = "[0-9]*";

    private final FireBreaker fireBreaker;
    private Position targetPosition;
    private FireFighter fireFighter;

    /**
     * Erstellt eine neue Instanz des Extinguish Befehls.
     * @param fireBreaker FireBreaker Spielinstanz
     */
    protected ExtinguishCommand(FireBreaker fireBreaker) {
        super(MAXIMUM_ARGUMENT_LENGTH);
        this.fireBreaker = fireBreaker;
    }

    @Override
    public void parseCommandLine(String[] arguments) throws FireBreakerException {
        if (arguments.length != MAXIMUM_ARGUMENT_LENGTH) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_LENGTH);
        }
        if (fireBreaker.isRoundFinish()) {
            throw new FireBreakerException(ErrorMessages.ROUND_FINISH);
        }
        if (fireBreaker.isGameFinish()) {
            throw new FireBreakerException(ErrorMessages.GAME_ALREADY_FINISH);
        }
        Player playerOnTurn = fireBreaker.getPlayerOnTurn();

        if (!arguments[0].matches(FireFighter.FIREFIGHTER_REGEX)) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_FORMAT);
        }
        fireFighter = playerOnTurn.getFireFighterByStringIdentifier(arguments[0]);
        if (fireFighter == null) {
            throw new FireBreakerInputException(ErrorMessages.FIREFIGHTER_DOES_NOT_EXIST);
        }

        if (!arguments[1].matches(NUMBER_REGEX) || !arguments[2].matches(NUMBER_REGEX)) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_FORMAT);
        }
        try {
            int rowIndex = Integer.parseInt(arguments[1]);
            int columnIndex = Integer.parseInt(arguments[2]);
            this.targetPosition = new Position(rowIndex, columnIndex);
        } catch (NumberFormatException exception) {
            throw new FireBreakerInputException(exception.getMessage());
        }
    }

    @Override
    public Result execute() {
        try {
            fireBreaker.getGameBoard().extinguish(fireFighter, targetPosition);
            GameCell targetGameCell = fireBreaker.getGameBoard().getGameCellCopy(targetPosition);
            if (!fireBreaker.checkWin()) {
                return new Result(Result.ResultType.SUCCESS,
                        String.format(InOutputStrings.EXTINGUISH_OUPUT,
                                targetGameCell.getFieldState().getStateRepresentation(),
                                fireFighter.getActionPoints()));
            }
            return new Result(Result.ResultType.SUCCESS, InOutputStrings.WIN);
        } catch (FireBreakerException fireBreakerException) {
            return new Result(Result.ResultType.FAILURE, fireBreakerException.getMessage());
        }
    }
}
