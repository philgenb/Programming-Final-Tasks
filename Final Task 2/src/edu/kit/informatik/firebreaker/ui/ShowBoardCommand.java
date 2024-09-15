package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.exception.FireBreakerInputException;
import edu.kit.informatik.firebreaker.model.FireBreaker;

/**
 * Modelliert den Befehl show-board. Mit diesem parameterlosen Befehl
 * wird das Spielbrett mit den leicht- und stark brennenden Spielfeldern auf der
 * Konsole ausgegeben.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class ShowBoardCommand extends Command {

    /**
     * Name des show-board Befehls.
     */
    public static final String SHOW_BOARD_NAME = "show-board";

    private static final int ARGUMENT_LENGTH = 0;

    private FireBreaker fireBreaker;

    /**
     * Erstellt eine neue Instanz eines Show-Board Befehls.
     * @param fireBreaker Instanz des Firebreaker Spiels
     */
    protected ShowBoardCommand(FireBreaker fireBreaker) {
        super(ARGUMENT_LENGTH);
        this.fireBreaker = fireBreaker;
    }

    @Override
    public void parseCommandLine(String[] arguments) throws FireBreakerException {
        if (arguments.length != getMaximumArgumentLength()) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_LENGTH);
        }

    }

    @Override
    public Result execute() {
        return new Result(Result.ResultType.SUCCESS, fireBreaker.getGameBoard().getBoardRepresentation());
    }
}
