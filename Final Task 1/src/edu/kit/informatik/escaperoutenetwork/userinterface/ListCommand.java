package edu.kit.informatik.escaperoutenetwork.userinterface;

import edu.kit.informatik.escaperoutenetwork.core.EscapeRouteNetwork;
import edu.kit.informatik.escaperoutenetwork.core.EscapeRouteNetworkManager;
import edu.kit.informatik.escaperoutenetwork.core.Result;
import edu.kit.informatik.escaperoutenetwork.exception.ErrorMessages;
import edu.kit.informatik.escaperoutenetwork.exception.EscapeNetworkException;
import edu.kit.informatik.escaperoutenetwork.exception.EscapeNetworkInputException;

/**
 * Modelliert den List-Befehl.
 * Dieser Befehl hat ja nach Anzahl an Parametern zwei Funktionen.
 * Wird der List Befehl ohne irgendwelche Parameter angegeben, so werdene alle hinzugefügten Fluchtwegenetze
 * aufgelistet. Wenn noch kein Fluchtwegenetz hinzugefügt wurde, so wird {@code EMPTY} abegegeben.
 * Wenn als Parameter eine eindeutige Kennung eines Fluchtwegenetzes angegeben wird, so
 * listet der Befehl alle zuvor berechneten maximalen Durchflüsse auf.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class ListCommand extends Command {

    /**
     * Eindeutiger Name des List-Befehls.
     */
    public static final String LIST_COMMAND_NAME = "list";
    private static final int MAXIMUM_ARGUMENT_LENGTH = 1;

    private final EscapeRouteNetworkManager escapeRouteNetworkManager;

    private String uniqueEscapeIdentifier;

    /**
     * Erstellt eine neue Instanz des List-Befehls.
     * @param escapeRouteNetworkManager
     */
    public ListCommand(EscapeRouteNetworkManager escapeRouteNetworkManager) {
        super(MAXIMUM_ARGUMENT_LENGTH);
        this.escapeRouteNetworkManager = escapeRouteNetworkManager;
    }

    @Override
    public void parseCommandLine(String[] arguments) throws EscapeNetworkException {
        this.uniqueEscapeIdentifier = null;
        if (arguments.length > getMaximumArgumentLength()) {
            throw new EscapeNetworkInputException(ErrorMessages.LENGTH.toString());
        }

        if (arguments.length == getMaximumArgumentLength()) {
            if (!arguments[0].matches(EscapeRouteNetwork.IDENTIFIER_REGEX)) {
                throw new EscapeNetworkInputException(ErrorMessages.INVALID_IDENTIFIER.toString());
            }

            if (!escapeRouteNetworkManager.containsEscapeRouteNetworkWithIdentifier(arguments[0])) {
                throw new EscapeNetworkException(ErrorMessages.NETWORK_DOES_NOT_EXIST.toString());
            }
            this.uniqueEscapeIdentifier = arguments[0];
        }
    }

    @Override
    public Result execute() {
        if (uniqueEscapeIdentifier == null) {
            escapeRouteNetworkManager.sortEscapeRouteNetworks();
            return new Result(Result.ResultType.SUCCESS, escapeRouteNetworkManager.toString());
        }
        EscapeRouteNetwork escapeRouteNetwork = escapeRouteNetworkManager.
                getEscapeRouteNetworkByIdentifier(uniqueEscapeIdentifier);
        return new Result(Result.ResultType.SUCCESS, escapeRouteNetwork.getCalculator().getMaximumFlowsString());
    }
}
