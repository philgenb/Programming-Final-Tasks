package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.core.Position;
import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.exception.FireBreakerInputException;
import edu.kit.informatik.firebreaker.gameboard.GameCell;
import edu.kit.informatik.firebreaker.model.FireBreaker;

/**
 * Modelliert den Befehl show-field, welcher den Zustand eines ausgewählten
 * Spielfeldes angibt. Der Befehl ist in jedem Spielzug und auch nach Spielausgang
 * durchführbar.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class ShowFieldCommand extends Command {

    /**
     * Name des Befehls Show-Field.
     */
    public static final String SHOW_FIELD_NAME = "show-field";

    private static final String NUMBER_REGEX = "[0-9]*";
    private static final int MAXIMUM_ARGUMENT_LENGTH = 2;
    private final FireBreaker fireBreaker;

    private GameCell gameCell;

    /**
     * Erstellt einen neuen Show-Field Befehl.
     * @param fireBreaker Instanz des Firebreaker Spiels
     */
    protected ShowFieldCommand(FireBreaker fireBreaker) {
        super(MAXIMUM_ARGUMENT_LENGTH);
        this.fireBreaker = fireBreaker;
    }

    @Override
    public void parseCommandLine(String[] arguments) throws FireBreakerException {
        if (arguments.length != getMaximumArgumentLength()) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_LENGTH);
        }

        if (!arguments[0].matches(NUMBER_REGEX) || !arguments[1].matches(NUMBER_REGEX)) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_FORMAT);
        }
        try {
            int rowIndex = Integer.parseInt(arguments[0]);
            int columnIndex = Integer.parseInt(arguments[1]);
            Position fieldPosition = new Position(rowIndex, columnIndex);
            this.gameCell = fireBreaker.getGameBoard().getGameCellCopy(fieldPosition);
        } catch (NumberFormatException exception) {
            throw new FireBreakerInputException(exception.getMessage());
        }
    }

    @Override
    public Result execute() {
        return new Result(Result.ResultType.SUCCESS, gameCell.toString());
    }
}
