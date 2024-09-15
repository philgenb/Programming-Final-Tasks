package edu.kit.informatik.escaperoutenetwork.userinterface;

import edu.kit.informatik.escaperoutenetwork.core.EscapeRouteCalculator;
import edu.kit.informatik.escaperoutenetwork.core.EscapeRouteNetwork;
import edu.kit.informatik.escaperoutenetwork.core.EscapeRouteNetworkManager;
import edu.kit.informatik.escaperoutenetwork.core.Result;
import edu.kit.informatik.escaperoutenetwork.exception.ErrorMessages;
import edu.kit.informatik.escaperoutenetwork.exception.EscapeNetworkException;
import edu.kit.informatik.escaperoutenetwork.exception.EscapeNetworkInputException;
import edu.kit.informatik.escaperoutenetwork.graph.Graph;
import edu.kit.informatik.escaperoutenetwork.graph.Node;

/**
 * Modelliert den Befehl Flow zum Berechnen des Maximalen Durchflusses innerhalb eines Fluchtwegenetzes.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class FlowCommand extends Command {

    /**
     * Eindeutiger Name des Flow-Befehls.
     */
    public static final String FLOW_COMMAND_NAME = "flow";
    private static final int ARGUMENT_LENGTH =  3;

    private final EscapeRouteNetworkManager escapeRouteNetworkManager;

    private EscapeRouteCalculator escapeRouteCalculator;
    private Node startNode;
    private Node endNode;

    /**
     * Erstellt eine neue Instanz eines Flow-Befehls zur Berechnung des maximalen Durchflusses.
     * @param escapeRouteNetworkManager Fluchtwegenetz-Verwaltung
     */
    public FlowCommand(EscapeRouteNetworkManager escapeRouteNetworkManager) {
        super(ARGUMENT_LENGTH);
        this.escapeRouteNetworkManager = escapeRouteNetworkManager;
    }

    @Override
    public void parseCommandLine(String[] arguments) throws EscapeNetworkException {
        if (arguments.length != ARGUMENT_LENGTH) {
            throw new EscapeNetworkInputException(ErrorMessages.LENGTH.toString());
        }
        if (!arguments[0].matches(EscapeRouteNetwork.IDENTIFIER_REGEX) || !arguments[1].matches(Node.IDENTIFIER_REGEX)
                || !arguments[2].matches(Node.IDENTIFIER_REGEX)) {
            throw new EscapeNetworkInputException(ErrorMessages.INVALID_FORMAT.toString());
        }

        if (!escapeRouteNetworkManager.containsEscapeRouteNetworkWithIdentifier(arguments[0])) {
            throw new EscapeNetworkException(ErrorMessages.NETWORK_DOES_NOT_EXIST.toString());
        }
        this.startNode = new Node(arguments[1]);
        this.endNode = new Node(arguments[2]);

        EscapeRouteNetwork escapeRouteNetwork = escapeRouteNetworkManager.
                getEscapeRouteNetworkByIdentifier(arguments[0]);
        Graph escapeRouteGraph = escapeRouteNetwork.getDirectedGraph();
        this.escapeRouteCalculator = escapeRouteNetwork.getCalculator();

        if (startNode.equals(endNode)) {
            throw new EscapeNetworkInputException(ErrorMessages.INVALID_START_TARGET_NODE.toString());
        }
        if (!escapeRouteGraph.containsNode(startNode) || !escapeRouteGraph.containsNode(endNode)) {
            throw new EscapeNetworkInputException(ErrorMessages.INVALID_START_TARGET_NODE.toString());
        }
        if (!escapeRouteGraph.isSource(startNode) || !escapeRouteGraph.isSink(endNode)) {
            throw new EscapeNetworkInputException(ErrorMessages.INVALID_START_TARGET_NODE.toString());
        }
    }

    @Override
    public Result execute() {
        long maximumFlow = escapeRouteCalculator.calculateMaximumFlow(startNode, endNode);
        return new Result(Result.ResultType.SUCCESS, String.valueOf(maximumFlow));
    }

}
