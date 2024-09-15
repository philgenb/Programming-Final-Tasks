package edu.kit.informatik.escaperoutenetwork.core;

import edu.kit.informatik.escaperoutenetwork.graph.Graph;
import java.util.Objects;

/**
 * Modelliert ein Fluchtwegenetzwerk. Jedes Netzwerk hat eine eindeutige Kennung und besteht aus
 * einem Gerichteten Graphen sowie einer Berechnungseinheit.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class EscapeRouteNetwork {

    /**
     * Regulärer Ausdruck für die Kennung von Fluchtwegenetzen.
     */
    public static final String IDENTIFIER_REGEX = "[A-Z]{1,6}";

    private final String uniqueIdentifier;
    private final Graph directedGraph;
    private final EscapeRouteCalculator escapeRouteCalculator;

    /**
     * Erstellt eine neue Instanz eines Fluchtwegenetzwerks. Jedes Fluchtwegenetzwerk hat eine eindeutige Kennung.
     * @param uniqueIdentifier eindeutige Kennung des Fluchtwegenetzes
     * @param directedGraph gerichteter Graph
     */
    public EscapeRouteNetwork(String uniqueIdentifier, Graph directedGraph) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.directedGraph = directedGraph.copy();
        this.escapeRouteCalculator = new EscapeRouteCalculator(this);
    }

    /**
     * Gibt die Berechnungseinheit des Fluchtwegenetzes zurück.
     * @return Berechnungseinheit
     */
    public EscapeRouteCalculator getCalculator() {
        return this.escapeRouteCalculator;
    }

    /**
     * Gibt die eindeutige Kennung des Fluchtwegenetzes zurück.
     * @return Kennung des Fluchtwegenetzes
     */
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    /**
     * Gibt den zum Fluchtwegenetz eindeutig zugehörigen gerichteten Graphen zurück.
     * @return zugehöriger Gerichteter Graph
     */
    public Graph getDirectedGraph() {
        return directedGraph;
    }

    /**
     * Gibt die Anzahl an Räumen im Fluchtwegenetz zurück.
     * @return Anzahl an Räumen im Fluchtwegenetz
     */
    public int getNodeCount() {
        return getDirectedGraph().getNodeCount();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        EscapeRouteNetwork escapeRouteNetwork = (EscapeRouteNetwork) object;
        return uniqueIdentifier.equals(escapeRouteNetwork.getUniqueIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueIdentifier());
    }
}
