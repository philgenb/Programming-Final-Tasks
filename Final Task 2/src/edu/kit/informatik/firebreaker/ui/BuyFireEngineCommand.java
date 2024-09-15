package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.core.Position;
import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.exception.FireBreakerInputException;
import edu.kit.informatik.firebreaker.model.FireBreaker;
import edu.kit.informatik.firebreaker.model.Player;

/**
 * Modelliert den buy-fire-engine Befehl mit dem ein aktiver Spieler eine Feuerwehr erwerben kann.
 * Der Kauf kostet den Spieler 5 Reputationspunkte. Ein Spieler kann eine Feuerwehr nur kaufen,
 * wenn er genügend Reputationspunkte besitzt.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class BuyFireEngineCommand extends Command {

    /**
     * Name des Befehls buy-fire-engine.
     */
    public static final String BUY_FIRE_ENGINE_COMMAND_NAME = "buy-fire-engine";

    private static final int MAXIMUM_ARGUMENT_LENGTH = 2;
    private static final String NUMBER_REGEX = "[0-9]*";

    private final FireBreaker fireBreaker;
    private Player playerOnTurn;
    private Position fieldPosition;

    /**
     * Erstellt eine neue Instanz des Befehls buy-fire-engine.
     * @param fireBreaker Instanz eines Firebreaker Spiels
     */
    protected BuyFireEngineCommand(FireBreaker fireBreaker) {
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
        this.playerOnTurn = fireBreaker.getPlayerOnTurn();

        if (!arguments[0].matches(NUMBER_REGEX) || !arguments[1].matches(NUMBER_REGEX)) {
            throw new FireBreakerInputException(ErrorMessages.INVALID_ARGUMENT_FORMAT);
        }

        try {
            int columnIndex = Integer.parseInt(arguments[0]);
            int rowIndex = Integer.parseInt(arguments[1]);
            this.fieldPosition = new Position(columnIndex, rowIndex);
            fireBreaker.getGameBoard().validatePosition(fieldPosition);
        } catch (NumberFormatException exception) {
            throw new FireBreakerInputException(exception.getMessage());
        }
    }

    @Override
    public Result execute() {
        try {
            fireBreaker.buyFireFighter(fieldPosition);
            return new Result(Result.ResultType.SUCCESS, String.valueOf(playerOnTurn.getReputationPoints()));
        } catch (FireBreakerException exception) {
            return new Result(Result.ResultType.FAILURE, exception.getMessage());
        }
    }
}
