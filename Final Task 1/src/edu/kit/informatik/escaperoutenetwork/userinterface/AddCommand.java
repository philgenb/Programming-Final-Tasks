package edu.kit.informatik.escaperoutenetwork.userinterface;

import edu.kit.informatik.escaperoutenetwork.core.EscapeRouteNetwork;
import edu.kit.informatik.escaperoutenetwork.core.EscapeRouteNetworkManager;
import edu.kit.informatik.escaperoutenetwork.core.OutputStrings;
import edu.kit.informatik.escaperoutenetwork.core.Result;
import edu.kit.informatik.escaperoutenetwork.exception.ErrorMessages;
import edu.kit.informatik.escaperoutenetwork.exception.EscapeNetworkException;
import edu.kit.informatik.escaperoutenetwork.exception.EscapeNetworkInputException;
import edu.kit.informatik.escaperoutenetwork.graph.Edge;
import edu.kit.informatik.escaperoutenetwork.graph.Graph;
import edu.kit.informatik.escaperoutenetwork.graph.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * Dieser Befehl fügt ein neues Fluchtwegenetz mit gegebener Kennung hinzu.
 * Falls das Fluchtwegenetz mit der gegebenen Kennung existiert, so wird eine neue Kante hinzufgefügt, oder
 * eine bestehende Kante überschrieben.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class AddCommand extends Command {

    /**
     * Eindeutige Name des add-Befehls
     */
    public static final String ADD_COMMAND_NAME = "add";

    private static final int MAXIMUM_ARGUMENT_LENGTH = 2;
    private static final int MAXIMUM_ADDING_SECTION_COUNT = 1;
    private static final int MINIMUM_EDGE_COUNT = 2;

    private static final String EMPTY_STRING = "";
    private static final String COMMA_SEPERATOR = ";";

    private static final String NODE_IDENTIFIER_REGEX = "[a-z]{1,6}";
    private static final String NUMBER_REGEX = "[0]*[1-9][0-9]*";
    private static final String ROUTE_SECTION_REGEX = NODE_IDENTIFIER_REGEX + NUMBER_REGEX + NODE_IDENTIFIER_REGEX;


    private static final int INVALID_WEIGHT = 0;
    private static final int SPLIT_LIMITER = -1;

    private final EscapeRouteNetworkManager escapeRouteNetworkManager;

    private Set<Edge> edges;
    private Set<Node> nodes;
    private Edge edgeToAdd;
    private EscapeRouteNetwork escapeRouteNetwork;
    private Graph newGraph;
    private String escapeNetworkIdentifier;

    /**
     * Erstellt eine neue Instanz eines Add-Commands zum Hinzufügen von neuen Fluchtwegenetzwerken.
     * @param escapeRouteNetworkManager Fluchtwegenetzwerkverwaltung
     */
    public AddCommand(EscapeRouteNetworkManager escapeRouteNetworkManager) {
        super(MAXIMUM_ARGUMENT_LENGTH);
        this.escapeRouteNetworkManager = escapeRouteNetworkManager;
    }

    @Override
    public void parseCommandLine(String[] arguments) throws EscapeNetworkInputException {
        resetLocalVariables();
        if (arguments.length != getMaximumArgumentLength()) {
            throw new EscapeNetworkInputException(ErrorMessages.LENGTH.toString());
        }
        if (!arguments[0].matches(EscapeRouteNetwork.IDENTIFIER_REGEX)) {
            throw new EscapeNetworkInputException(ErrorMessages.INVALID_FORMAT.toString());
        }
        this.escapeNetworkIdentifier = arguments[0];

        String[] edgeStrings = arguments[1].split(COMMA_SEPERATOR, SPLIT_LIMITER);

        if (escapeRouteNetworkManager.containsEscapeRouteNetworkWithIdentifier(escapeNetworkIdentifier)) {
            this.escapeRouteNetwork = escapeRouteNetworkManager.
                    getEscapeRouteNetworkByIdentifier(escapeNetworkIdentifier);
            this.newGraph = escapeRouteNetwork.getDirectedGraph();

            if (edgeStrings.length != MAXIMUM_ADDING_SECTION_COUNT) {
                throw new EscapeNetworkInputException(ErrorMessages.INVALID_FORMAT.toString());
            }

            String currentRouteSectionString = edgeStrings[0];
            edgeToAdd = parseRouteSectionString(currentRouteSectionString);

            if (escapeRouteNetwork.getDirectedGraph().containsEdge(edgeToAdd.getInvertedEdge())) {
                throw new EscapeNetworkInputException(ErrorMessages.PARALLEL_OPPOSITE_EDGE.toString());
            }

            //Überprüfe Knoten mit neu hinzugefügter Kante auf vorhandene Start- und Zielknoten
            Graph copyGraph = newGraph.copy();
            copyGraph.addEdge(edgeToAdd);
            checkForSinkAndSource(copyGraph);
            return;
        }

        for (String currentRouteSectionString : edgeStrings) {
            Edge edge = parseRouteSectionString(currentRouteSectionString);
            if (edges.contains(edge)) {
                throw new EscapeNetworkInputException(ErrorMessages.DUPLICATE_EDGE.toString());
            }
            addEdgeToList(edge);
        }

        if (edges.size() < MINIMUM_EDGE_COUNT) {
            throw new EscapeNetworkInputException(ErrorMessages.INVALID_NETWORK_SIZE.toString());
        }

        this.newGraph = new Graph(escapeNetworkIdentifier, edges, nodes);
        checkForSinkAndSource(newGraph);
    }

    /**
     * Setzt alle lokalen Variablen zurück.
     */
    private void resetLocalVariables() {
        this.newGraph = null;
        this.escapeRouteNetwork = null;
        this.edges = new HashSet<>();
        this.nodes = new HashSet<>();
    }

    private void addEdgeToList(Edge edge) {
        edges.add(edge);
        nodes.add(edge.getSource());
        nodes.add(edge.getTarget());
    }

    /**
     * Verarbeitet einen gegebene Zeichenkette zu einer Kante / Fluchtwegeabschnitt des Fluchtwegenetzes.
     * Dabei wird überprüft, ob das Format der Zeichenkette dem notwendigen Format eines
     * Fluchtwegeabschnittes entspricht.
     * @param routeSectionString Fluchtwegeabschnitt als Zeichenkette
     * @return Fluchtwegeabschnitt
     * @throws EscapeNetworkException falls das Format des Strings kein Fluchtwegeabschnitt darstellt
     * oder die invertierte Kante bereits in der Liste ist
     */
    private Edge parseRouteSectionString(String routeSectionString) throws EscapeNetworkInputException {
        if (!routeSectionString.matches(ROUTE_SECTION_REGEX)) {
            throw new EscapeNetworkInputException(ErrorMessages.INVALID_FORMAT.toString());
        }
        String[] currentRouteSectionStringIdentifiers = routeSectionString.split(NUMBER_REGEX);
        Node sourceNode = new Node(currentRouteSectionStringIdentifiers[0]);
        Node targetNode = new Node(currentRouteSectionStringIdentifiers[1]);
        String weightString = routeSectionString.replaceAll(NODE_IDENTIFIER_REGEX, EMPTY_STRING);
        int weight = parseWeight(weightString);

        Edge edge = new Edge(sourceNode, targetNode, weight);

        if (sourceNode.equals(targetNode)) {
            throw new EscapeNetworkInputException(ErrorMessages.GRAPH_CONTAINS_LOOPS.toString());
        }

        if (edges.contains(edge.getInvertedEdge())) {
            throw new EscapeNetworkInputException(ErrorMessages.PARALLEL_OPPOSITE_EDGE.toString());
        }
        return edge;
    }

    /**
     * Überprüft ob ein gegebener Graph über einen möglichen Startknoten mit Eingangsgrad 0 und
     * einen weiteren Zielknoten mit Ausgangsgrad 0 verfügt.
     * @param graph Fluchtwegenetzwerk-Graph
     * @throws EscapeNetworkInputException falls kein möglicher Start- oder Zielknoten gefunden wurde
     */
    private void checkForSinkAndSource(Graph graph) throws EscapeNetworkInputException {
        if (!graph.hasSink() || !graph.hasSource()) {
            throw new EscapeNetworkInputException(ErrorMessages.INVALID_START_TARGET_NODE.toString());
        }
    }

    private int parseWeight(String weightString) throws EscapeNetworkInputException {
        int weight;
        try {
            weight = Integer.parseInt(weightString);
            if (weight <= INVALID_WEIGHT) {
                throw new EscapeNetworkInputException(ErrorMessages.INVALID_CAPACITY.toString());
            }
        } catch (NumberFormatException numberFormatException) {
            throw new EscapeNetworkInputException(ErrorMessages.INVALID_CAPACITY.toString());
        }
        return weight;
    }

    @Override
    public Result execute() {
        if (escapeRouteNetwork == null) {
            //Kein Fluchtwegnetz mit der gegebenen Kennung vorhanden -> Erstelle ein neues Fluchtwegenetz
            EscapeRouteNetwork newEscapeRouteNetwork = new EscapeRouteNetwork(escapeNetworkIdentifier, newGraph);
            escapeRouteNetworkManager.addEscapeRouteNetwork(newEscapeRouteNetwork);
            return new Result(Result.ResultType.SUCCESS,
                    String.format(OutputStrings.ADDED_NETWORK.toString(), escapeNetworkIdentifier));
        }
        //Fluchtwegenetz existiert bereits -> Füge Kante bestehendem Netz hinzu
        newGraph.addEdge(edgeToAdd);
        escapeRouteNetwork.getCalculator().resetMaximumFlowMap();
        return new Result(Result.ResultType.SUCCESS, String.format(
                OutputStrings.ADDED_EDGE.toString(), edgeToAdd.toString(), escapeNetworkIdentifier));
    }

}
