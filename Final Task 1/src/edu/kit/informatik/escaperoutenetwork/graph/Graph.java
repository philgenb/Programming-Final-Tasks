package edu.kit.informatik.escaperoutenetwork.graph;

import edu.kit.informatik.escaperoutenetwork.compare.EscapeRouteSectionComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Modelliert einen gerichteten Graphen im Fluchtwegenetzwerk.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class Graph implements Comparable<Graph> {

    private static final int MINIMUM_INDEX = 0;
    private static final int MINIMUM_SIZE = 1;

    private final String uniqueIdentifier;
    private final Set<Edge> edges;
    private final Set<Node> nodes;
    private final Map<Node, List<Node>> adjacencyMap;


    /**
     * Erstellt eine neue Instanz eines gerichteten Graphen mit einer eindutigen Kennung
     * und einer gegebenen Kanten- sowie Knotenmenge.
     * Dabei wird eine Adjazenzmap initialisert, welche jedem Knoten, eine Liste aller
     * Knoten auf den der jeweilige Knoten zeigt zuordnet.
     * @param uniqueIdentifier Eindeutige Kennung des Graphen
     * @param edges Kantenmenge des Graphen
     * @param nodes Knotenmenge des Graphen
     */
    public Graph(String uniqueIdentifier, Set<Edge> edges, Set<Node> nodes) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.edges = copyEdges(edges);
        this.nodes = copyNodes(nodes);
        this.adjacencyMap = new HashMap<>();

        initializeAdjacencyMap();
    }

    private void initializeAdjacencyMap() {
        for (Node node : this.nodes) {
            for (Edge edge : this.edges) {
                if (edge.getSource().equals(node)) {
                    addEdgeToMap(node, edge.getTarget());
                }
            }
        }
    }

    /**
     * Gibt die eindeutige Kennung des Graphen zurück.
     * @return Eindeutige Kennung des Graphen
     */
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    /**
     * Gibt die Menge an Kanten des Graphen zurück.
     * @return Kantenmenge des Graphen
     */
    public Set<Edge> getEdges() {
        return Set.copyOf(edges);
    }

    /**
     * Gibt alle Kanten im Graphen als Zeichenkette zurück.
     * Dabei werden die Kanten sortiert wie in {@link #getSortedEdgeList()} spezifiziert.
     * Jede neue Kante wird durch einen Zeilenseperator separiert.
     * @return Zeichenkette aller Kanten des Graphen
     */
    public String getEdgesString() {
        StringBuilder builder = new StringBuilder();
        List<Edge> sortedEdgeList = getSortedEdgeList();
        for (int i = MINIMUM_INDEX; i < sortedEdgeList.size(); i++) {
            builder.append(sortedEdgeList.get(i));
            if (i < sortedEdgeList.size() - MINIMUM_SIZE) {
                builder.append(System.lineSeparator());
            }
        }
        return builder.toString();
    }

    /**
     * Gibt eine Sortierterte Liste aller Kanten des Graphen zurück.
     * Die Kanten werden dabei Lexikographisch nach Startknoten und anschließend nach Zielknoten aufsteigend sortiert,
     * wie spezifiziert in {@link EscapeRouteSectionComparator}.
     * @return Sortierte Liste aller Kanten des Grpahen
     */
    public List<Edge> getSortedEdgeList() {
        List<Edge> sortedEdges = new ArrayList<>(this.edges);
        sortedEdges.sort(new EscapeRouteSectionComparator());
        return List.copyOf(sortedEdges);
    }

    /**
     * Überprüft ob ein gegebener Knoten im Graphen existiert.
     * @param node Knoten
     * @return Wahrheitswert, ob Knoten im Graphen existiert
     */
    public boolean containsNode(Node node) {
        return this.nodes.contains(node);
    }

    /**
     * Ermittelt eine Kante anhand des Startknotens und gibt diese zurück.
     * Falls keine Kante mit dem gegebenen Knoten als Startknoten existiert wird {@code null} zurückgegeben.
     * @param source Startknoten
     * @param target Zielknoten
     * @return Kante ausgehend vom Start- zum Zielknoten
     */
    public Edge getEdge(Node source, Node target) {
        for (Edge edge : this.edges) {
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                return edge;
            }
        }
        return null;
    }

    /**
     * Überprüft, ob der Graph eine gegebene Kante beinhaltet.
     * @param edge Kante
     * @return Wahrheitswert, ob der Graph die gegebene Kante beinhaltet
     */
    public boolean containsEdge(Edge edge) {
        return this.edges.contains(edge);
    }

    /**
     * Fügt dem Graphen eine Kante hinzu.
     * @param edge Kante die hinzugefügt werden soll
     */
    public void addEdge(Edge edge) {
        if (this.edges.contains(edge)) {
            overwriteEdge(edge);
            return;
        }
        this.edges.add(edge);
        this.nodes.add(edge.getSource());
        this.nodes.add(edge.getTarget());
        addEdgeToMap(edge.getSource(),  edge.getTarget());
    }

    /**
     * Überschreibt eine bereits existierende Kante mit einer anderen Kante.
     * Dabei bleiben Start- und Zielknoten der Kante erhalten, das Gewicht kann sich jedoch ändern.
     * Wird keine Kante gefunden mit dem selben Start- und Zielknoten wie die gegebene Kante,
     * so wird nichts verändert.
     * @param newEdge neue Kante mit der eine bestehende Kante überschrieben werden soll
     */
    public void overwriteEdge(Edge newEdge) {
        for (Edge edge : this.edges) {
            if (edge.equals(newEdge)) {
                edge.setCapacity(newEdge.getCapacity());
            }
        }
    }

    /**
     * Fügt dem Graphen eine Kante von einem gegebenem Start- zu einem Zielknoten hinzu.
     * @param source Startknoten
     * @param destination Zielknoten
     */
    public void addEdgeToMap(Node source, Node destination) {
        if (!adjacencyMap.containsKey(source)) {
            adjacencyMap.put(source, null);
        }
        if (!adjacencyMap.containsKey(destination)) {
            adjacencyMap.put(destination, null);
        }
        addNodeNeighbourToList(source, destination);
    }

    private void addNodeNeighbourToList(Node source, Node target) {
        List<Node> sourceList = adjacencyMap.get(source);
        if (sourceList != null) {
            sourceList.remove(target);
        } else {
            sourceList = new ArrayList<>();
        }
        sourceList.add(target);
        adjacencyMap.put(source, sourceList);
    }

    /**
     * Gibt eine Liste aller benachbarten Knoten, welche von einem gegebenen Knoten aus erreichbar sind zurück.
     * Gibt {@code null} zurück, falls keine Nachbarknoten zu einem gegebenen Knoten existieren.
     * @param node Ausgangsknoten
     * @return Liste aller benachbarter Knoten
     */
    public List<Node> getNeighbourNodes(Node node) {
        if (this.adjacencyMap.get(node) == null) {
            return new ArrayList<>();
        }
        return List.copyOf(this.adjacencyMap.get(node));
    }


    /**
     * Gibt die Anzahl an Knoten im Graph zurück.
     * @return Anzahl im Knoten im Graph
     */
    public int getNodeCount() {
        return this.nodes.size();
    }

    /**
     * Gibt eine Menge an Knoten des gerichteten Graphen zurück.
     * @return Menge an Knoten
     */
    public Set<Node> getNodes() {
        return copyNodes(this.nodes);
    }

    /**
     * Überprüft ob es einen möglichen Startknoten mit dem Eingangsgrad 0 gibt.
     * Falls ein Startknoten gefunden wurde, zu dem keine Kante hin zeigt, so wird true zurückgegeben.
     * @return ob Graph über einen Knoten mit Eingangsgrad 0 verfügt
     */
    public boolean hasSource() {
        for (Node node : this.nodes) {
            boolean nodeHasNoIncomingEdges = true;
            for (Edge edge : this.edges) {
                if (edge.getTarget().equals(node)) {
                    nodeHasNoIncomingEdges = false;
                }
            }
            if (nodeHasNoIncomingEdges) {
                return true;
            }
        }
        return false;
    }

    /**
     * Überprüft ob es einen möglichen Zielknoten mit dem Eingangsgrad 0 gibt.
     * Falls ein möglicher Endknoten gefunden wurde, von dem aus keine Kante weggeht, so wird true zurückgegeben.
     * @return ob Graph über einen Knoten mit Ausgangsgrad 0 verfügt
     */
    public boolean hasSink() {
        for (Node node : getNodes()) {
            List<Node> neighbourNodes = this.adjacencyMap.get(node);
            if (neighbourNodes == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Überprüft ob ein gegebener Knoten im Graph ein zulässiger Zielknoten ist.
     * Dazu wird überprüft, ob der Ausgangsgrad gleich 0 ist.
     * @param sinkNode Zielknoten
     * @return ob Knoten ein zulässiger Zielknoten ist
     */
    public boolean isSink(Node sinkNode) {
        return this.adjacencyMap.get(sinkNode) == null;
    }

    /**
     * Überprüft ob ein gegebener Knoten im Graph ein zulässiger Startknoten ist.
     * Dazu wird überprüft, ob der Eingangsgrad des Knotens gleich 0 ist.
     * @param sourceNode Startknoten
     * @return ob Knoten ein zulässiger Startknoten ist
     */
    public boolean isSource(Node sourceNode) {
        for (Edge edge : this.edges) {
            if (edge.getTarget().equals(sourceNode)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vergleicht zwei Graphen anhand ihrer Anzahl an Knoten numerisch.
     * @param otherGraph Vergleichsgraph
     * @return den Wert 0, wenn die Anzahl an Knoten der beiden Graphen übereinstimmt,
     * den Wert -1, wenn der Graph weniger Knoten hat als der Vergleichsgraph und
     * den Wert 1, wenn der Graph mehr Knoten hat als der Vergleichsgraph.
     */
    @Override
    public int compareTo(Graph otherGraph) {
        return Integer.compare(getNodeCount(), otherGraph.getNodeCount());
    }

    /**
     * Gibt eine Kopie des Graphen zurück. Dabei werden alle Kanten und Knoten vollständig kopiert.
     * @return Kopie des Graphen
     */
    public Graph copy() {
        return new Graph(uniqueIdentifier, copyEdges(edges), copyNodes(nodes));
    }

    private Set<Edge> copyEdges(Collection<Edge> edges) {
        Set<Edge> edgesCopy = new HashSet<>();
        for (Edge edge : edges) {
            edgesCopy.add(edge.copy());
        }
        return edgesCopy;
    }

    private Set<Node> copyNodes(Collection<Node> nodes) {
        Set<Node> nodesCopy = new HashSet<>();
        for (Node node : nodes) {
            nodesCopy.add(node);
        }
        return nodesCopy;
    }

}
