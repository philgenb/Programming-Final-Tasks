package edu.kit.informatik.escaperoutenetwork.userinterface;

import edu.kit.informatik.escaperoutenetwork.core.EscapeRouteNetwork;
import edu.kit.informatik.escaperoutenetwork.core.EscapeRouteNetworkManager;
import edu.kit.informatik.escaperoutenetwork.core.Result;
import edu.kit.informatik.escaperoutenetwork.exception.ErrorMessages;
import edu.kit.informatik.escaperoutenetwork.exception.EscapeNetworkException;
import edu.kit.informatik.escaperoutenetwork.exception.EscapeNetworkInputException;

/**
 * Modelliert einen print-Befehls zur Ausgabe aller Kanten eines Fluchtwegenetzes.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class PrintCommand extends Command {

    /**
     * Eindeutiger Name des Print-Befehls.
     */
    public static final String PRINT_COMMAND_NAME = "print";
    private static final int MAXIMUM_ARGUMENT_LENGTH = 1;

    private final EscapeRouteNetworkManager escapeRouteNetworkManager;

    private String uniqueEscapeIdentifier;

    /**
     * Erstellt eine neue Instanz des Print-Befehls.
     * @param escapeRouteNetworkManager Fluchtwegenetz-Verwaltung
     */
    public PrintCommand(EscapeRouteNetworkManager escapeRouteNetworkManager) {
        super(MAXIMUM_ARGUMENT_LENGTH);
        this.escapeRouteNetworkManager = escapeRouteNetworkManager;
    }

    @Override
    public void parseCommandLine(String[] arguments) throws EscapeNetworkException {
        if (arguments.length != getMaximumArgumentLength()) {
            throw new EscapeNetworkInputException(ErrorMessages.LENGTH.toString());
        }

        if (!arguments[0].matches(EscapeRouteNetwork.IDENTIFIER_REGEX)) {
            throw new EscapeNetworkInputException(ErrorMessages.INVALID_IDENTIFIER.toString());
        }

        if (!escapeRouteNetworkManager.containsEscapeRouteNetworkWithIdentifier(arguments[0])) {
            throw new EscapeNetworkException(ErrorMessages.NETWORK_DOES_NOT_EXIST.toString());
        }

        this.uniqueEscapeIdentifier = arguments[0];

    }

    @Override
    public Result execute() {
        EscapeRouteNetwork escapeRouteNetwork = escapeRouteNetworkManager
                .getEscapeRouteNetworkByIdentifier(uniqueEscapeIdentifier);
        return new Result(Result.ResultType.SUCCESS, escapeRouteNetwork.getDirectedGraph().getEdgesString());
    }
}
