package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.core.Position;
import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.exception.FireBreakerInputException;
import edu.kit.informatik.firebreaker.gameboard.FireFighter;
import edu.kit.informatik.firebreaker.gameboard.GameCell;
import edu.kit.informatik.firebreaker.model.FireBreaker;
import edu.kit.informatik.firebreaker.status.InOutputStrings;

/**
 * Modelliert den Befehl move, mit welchem ein aktiver Spieler
 * eine seiner Feuerwehren um bis zu zwei Felder zu einem Zielfeld bewegen kann.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class MoveCommand extends Command {

    /**
     * Eindeutiger Name des Firetoroll-Befehls.
     */
    public static final String FIRETOROLL_COMMAND_NAME = "move";
    private static final String NUMBER_REGEX = "[0-9]*";
    private static final int MAXIMUM_ARGUMENT_LENGTH = 3;


    private final FireBreaker fireBreaker;

    private FireFighter fireFighter;
    private Position toPosition;
    private GameCell fromCell;

    /**
     * Erstellt eine neue Instanz des Befehls Move.
     * @param fireBreaker Instanz des FireBreaker Spiels
     */
    protected MoveCommand(FireBreaker fireBreaker) {
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
        if (!arguments[0].matches(FireFighter.FIREFIGHTER_REGEX)) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_FORMAT);
        }
        fireFighter = fireBreaker.getPlayerOnTurn().getFireFighterByStringIdentifier(arguments[0]);
        if (fireFighter == null) {
            throw new FireBreakerInputException(ErrorMessages.FIREFIGHTER_DOES_NOT_EXIST);
        }
        this.fromCell = fireBreaker.getGameBoard().getGameCellByGameObject(fireFighter);

        if (!arguments[1].matches(NUMBER_REGEX) || !arguments[2].matches(NUMBER_REGEX)) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_FORMAT);
        }
        try {
            int rowIndex = Integer.parseInt(arguments[1]);
            int columnIndex = Integer.parseInt(arguments[2]);
            this.toPosition = new Position(rowIndex, columnIndex);
        } catch (NumberFormatException exception) {
            throw new FireBreakerInputException(exception.getMessage());
        }
        fireBreaker.checkMove(fireFighter, toPosition);
    }

    @Override
    public Result execute() {
        try {
            fireBreaker.move(fireFighter, fromCell.getPosition(), toPosition);
            return new Result(Result.ResultType.SUCCESS, InOutputStrings.SUCCESS);
        } catch (FireBreakerException exception) {
            return new Result(Result.ResultType.FAILURE, exception.getMessage());
        }
    }
}
