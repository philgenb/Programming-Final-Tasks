package edu.kit.informatik.escaperoutenetwork.core;

import edu.kit.informatik.escaperoutenetwork.compare.MaximumFlowComparator;
import edu.kit.informatik.escaperoutenetwork.graph.Edge;
import edu.kit.informatik.escaperoutenetwork.graph.Graph;
import edu.kit.informatik.escaperoutenetwork.graph.Node;
import edu.kit.informatik.escaperoutenetwork.graph.NodePair;
import edu.kit.informatik.escaperoutenetwork.graph.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Modelliert eine Berechnungseinheit eines Fluchtwegenetzes, welche für die Berechnung
 * des maximalen Durchflusses zuständig ist.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class EscapeRouteCalculator {

    private static final String EMPTY_STATUS = "EMPTY";
    private static final String SPACE_SEPERATOR = " ";
    private static final int INITIAL_FLOW = 0;
    private static final int MINIMUM_SIZE = 1;
    private static final int START_INDEX = 0;
    private static final int DIVIDER = 2;

    private final EscapeRouteNetwork escapeRouteNetwork;
    private final Graph escapeRouteGraph;

    private Graph remainingCapacityNetworkGraph;
    private final Map<NodePair, Long> maximumFlowMap;

    /**
     * Erstellt eine neue Berechnungseinheit innerhalb eines Fluchtwegenetzes.
     * @param escapeRouteNetwork Fluchtwegenetz
     */
    public EscapeRouteCalculator(EscapeRouteNetwork escapeRouteNetwork) {
        this.escapeRouteNetwork = escapeRouteNetwork;
        this.escapeRouteGraph = escapeRouteNetwork.getDirectedGraph();
        this.maximumFlowMap = new HashMap<>();
    }

    /**
     * Gibt eine sortierte Liste an Schlüsseln der Maximalen Fluss Map zurück.
     * Die Einträge werden mittels eines {@link MaximumFlowComparator} sortiert und
     * anschließend zurückgegeben.
     * @return sortierte Liste an Schlüsseln der Maximalen Fluss Map
     */
    private List<NodePair> getSortedMaximumFlowMapKeys() {
        Set<Map.Entry<NodePair, Long>> mapEntries = maximumFlowMap.entrySet();
        List<Map.Entry<NodePair, Long>> listOfMapEntries = new ArrayList<>(mapEntries);
        Collections.sort(listOfMapEntries, new MaximumFlowComparator());
        List<NodePair> keys = new ArrayList<>();
        for (Map.Entry<NodePair, Long> mapEntry : listOfMapEntries) {
            keys.add(mapEntry.getKey());
        }
        return keys;
    }

    /**
     * Entfernt alle berechneten Maximalen Flüsse von beliebigen Start- zu Zielknoten.
     * Dazu wird die MaximumFlowMap geleert.
     */
    public void resetMaximumFlowMap() {
        this.maximumFlowMap.clear();
    }

    /**
     * Gibt alle berechneten Maximalen Flussgeschwindigkeiten als Zeichenkette zurück.
     * Dabei folgt immer auf die Maximale Flussgeschwindigkeit der dazugehörige
     * Start- und Endknoten im Folgenden Format:
     * [n] [v_1] [v_2]
     * @return Maximale Flussgeschwindigkeiten des Fluchtwegenetzes als Zeichenkette
     */
    public String getMaximumFlowsString() {
        if (maximumFlowMap.isEmpty()) {
            return EMPTY_STATUS;
        }
        List<NodePair> sortedKeyNodePairs = getSortedMaximumFlowMapKeys();

        StringBuilder builder = new StringBuilder();
        for (int i = START_INDEX; i < sortedKeyNodePairs.size(); i++) {
            NodePair nodePairKey = sortedKeyNodePairs.get(i);
            builder.append(maximumFlowMap.get(nodePairKey) + SPACE_SEPERATOR
                    + nodePairKey.getStartNode().getUniqueIdentifier() + SPACE_SEPERATOR
                    + nodePairKey.getTargetNode().getUniqueIdentifier());
            if (i < sortedKeyNodePairs.size() - MINIMUM_SIZE) {
                builder.append(System.lineSeparator());
            }
        }
        return builder.toString();
    }

    /**
     * Berechnet den Maximalen Fluss von einem gegebenen Start- zu einem gegebenen Zielknoten.
     * Wurde der Maximale Fluss für ein gegebenes Start-Zielknoten-Paar bereits berechnet,
     * so wird das Ergebnis direkt zurückgegeben.
     * Andernfalls wird das Ergebnis über eine Implementation des Edmond-Karp-Algorithmus berechnet.
     * @param startNode Startknoten
     * @param endNode Zielknoten
     * @return Maximaler Fluss vom Start- zum Zielknoten
     */
    public long calculateMaximumFlow(Node startNode, Node endNode) {
        NodePair nodePair = new NodePair(startNode, endNode);
        if (!maximumFlowMap.isEmpty() && maximumFlowMap.containsKey(nodePair)) {
            return maximumFlowMap.get(nodePair);
        }

        initalizeEmptyFlowGraph();

        while (true) {
            determineRemainingCapacityNetwork();
            Path path = breadthFirstSearch(remainingCapacityNetworkGraph, startNode, endNode);

            if (path == null) {
                break;
            }

            long minimumRestCapacity = determineMinimumRemainingCapacityAlongPath(path);

            for (Edge edge : escapeRouteGraph.getEdges()) {
                if (path.containsEdge(edge)) {
                    edge.augment(minimumRestCapacity);
                }
            }

            path.getPathEdges().forEach(edge -> edge.augment(minimumRestCapacity));
        }
        this.maximumFlowMap.put(nodePair, getMaximumFlow(endNode));
        return getMaximumFlow(endNode);
    }


    /**
     * Bestimmt den Maximalen Fluss des Graphen. Der maximale Fluss wird dabei berechnet über die Summe aller Flüsse
     * der Kanten die hin zum Zielknoten zeigen.
     * @param targetNode Zielknoten
     * @return Maximaler Fluss
     */
    private long getMaximumFlow(Node targetNode) {
        long newMaximumFlow = INITIAL_FLOW;
        for (Edge edge : escapeRouteGraph.getEdges()) {
            if (edge.getTarget().equals(targetNode)) {
                newMaximumFlow += edge.getFlow();
            }
        }
        return newMaximumFlow;
    }

    /**
     * Initialisiert den Fluss im Graphen.
     * Setzt den Fluss aller Kanten im Graphen auf {@code 0}, entfernt alle
     * Rezidualkanten und setzt das Kapazitätennetzwerk auf {@code null}.
     */
    private void initalizeEmptyFlowGraph() {
        for (Edge edge : escapeRouteNetwork.getDirectedGraph().getEdges()) {
            edge.setFlow(INITIAL_FLOW);
            edge.setResidualEdge(null);
        }
        this.remainingCapacityNetworkGraph = null;
    }

    /**
     * Ermittelt einen Weg im gegebenen Restkapazitätennetzwerkgraphen von einem
     * gegebenen Start- zu einem weiteren Zielknoten.
     * Die Suche nach einem solchen Pfad erfolgt über einen Breitensuche-Algorithmus.
     * Falls ein solcher Weg gefunden wurde, wird der dazugehörige Pfad von Start- zum Zielknoten zurückgegeben.
     * Falls es kein Pfad vom Start- zum Zielknoten gibt, so wird {@code null} zurückgegeben
     * @param startNode Startknoten
     * @param endNode Zielknoten
     * @return Pfad vom Start- zum Zielknoten
     */
    private Path breadthFirstSearch(Graph graph, Node startNode, Node endNode) {
        List<Node> queue = new ArrayList<>();
        List<Node> visitedNodes = new ArrayList<>();
        Map<Node, Node> nodeDiscoveredBy = new HashMap<>();
        visitedNodes.add(startNode);
        queue.add(startNode);

        List<Edge> pathEdges = new ArrayList<>();

        while (!queue.isEmpty()) {
            Node currentQueueElement = queue.get(queue.size() - MINIMUM_SIZE);
            queue.remove(queue.size() - MINIMUM_SIZE);
            if (currentQueueElement.equals(endNode)) {
                //Zielknoten gefunden -> Pfad wird rekonstruiert
                Node nodeFoundByPreviousNode = endNode;
                while (nodeFoundByPreviousNode != startNode) {
                    Node previousNode = nodeDiscoveredBy.get(nodeFoundByPreviousNode);
                    pathEdges.add(graph.getEdge(previousNode, nodeFoundByPreviousNode));
                    nodeFoundByPreviousNode = previousNode;
                }
                Path path = new Path(pathEdges);
                //Pfad ist in falscher Reihenfolge -> Pfad wird umgekehrt
                path.invert();
                return path;
            }

            if (graph == null) {
                return null;
            }

            if (graph.getNeighbourNodes(currentQueueElement) == null) {
                continue;
            }

            for (Node node : graph.getNeighbourNodes(currentQueueElement)) {
                if (!visitedNodes.contains(node)) {
                    queue.add(node);
                    visitedNodes.add(node);
                    nodeDiscoveredBy.put(node, currentQueueElement);
                }
            }
        }
        return  null;
    }

    /**
     * Bestimmt die minimale Restkapazität entlang eines gegebenen Pfades.
     * Dazu wird die Liste an Kanten des Pfades durchlaufen und die geringste Restkapazität
     * zurückgegeben.
     * @param path Pfad
     * @return minimale Restkapazität entlang eines Pfades
     */
    private long determineMinimumRemainingCapacityAlongPath(Path path) {
        long minimumRestCapacity = Long.MAX_VALUE / DIVIDER;
        for (Edge edge : path.getPathEdges()) {
            if (edge.getRemainingCapacity() > INITIAL_FLOW && edge.getRemainingCapacity() < minimumRestCapacity) {
                minimumRestCapacity = edge.getRemainingCapacity();
            }
        }
        return minimumRestCapacity;
    }

    /**
     * Erstellt einen neuen Restkapazitätengraphen / Residualgraphen und setzt den
     * {@link #remainingCapacityNetworkGraph} neu.
     * Der Residualgraph teilt sich mit dem ursprünglichen Fluchtwegenetz-Graphen dieselbe Knotenmenge und enthält
     * alle Kanten die nicht ausgelastet sind, also alle Kanten, deren Restkapazität größer als {@code 0} ist.
     * Zusätzlich ergänzt werden die Kanten durch Rückkanten.
     */
    private void determineRemainingCapacityNetwork() {
        Set<Edge> remainingCapacityNetworkEdges = new HashSet<>();
        for (Edge edge : escapeRouteGraph.getEdges()) {
            if (edge.isRemaining()) {
                if (edge.getResidualEdge() == null) {
                    Edge residualEdge = new Edge(edge.getTarget(), edge.getSource(), INITIAL_FLOW);
                    residualEdge.setResidualEdge(edge);
                    edge.setResidualEdge(residualEdge);
                }
                remainingCapacityNetworkEdges.add(edge);
                if (edge.getFlow() > INITIAL_FLOW) {
                    remainingCapacityNetworkEdges.add(edge.getResidualEdge());
                }

            }
            if (edge.getResidualEdge().isRemaining()) {
                remainingCapacityNetworkEdges.add(edge.getResidualEdge());
            }
        }

        this.remainingCapacityNetworkGraph = new Graph(escapeRouteGraph.getUniqueIdentifier(),
                remainingCapacityNetworkEdges, escapeRouteGraph.getNodes());
    }


}
