package edu.kit.informatik.escaperoutenetwork.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Modelliert einen Pfad von Start- zu Endknoten über mehrere Kanten im Graphen.
 * Jeder Pfad verfügt über eine Liste an Kanten.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class Path {

    private static final String SEPERATOR = ", ";

    private final List<Edge> pathEdges;

    /**
     * Erstellt eine neue Instanz eines Pfades innerhalb eines Graphen.
     * @param pathEdges Kanten ausgehend vom Start- zum Zielknoten
     */
    public Path(Collection<Edge> pathEdges) {
        this.pathEdges = new ArrayList<>(pathEdges);
    }

    /**
     * Dreht die Reihenfolge des Pfades um. Die letzte Kante wird zur ersten Kante und umgekehrt.
     */
    public void invert() {
        Collections.reverse(pathEdges);
    }

    /**
     * Gibt eine Liste an Kanten zurück, die auf dem Weg von Start- zu Endknoten durchlaufen werden.
     * @return Pfadkanten vom Start- zum Zielknoten
     */
    public List<Edge> getPathEdges() {
        return List.copyOf(pathEdges);
    }

    /**
     * Überprüft, über eine gegebene Kante verfügt.
     * @param edge Kante
     * @return ob Pfad eine gegebene Kante enthält
     */
    public boolean containsEdge(Edge edge) {
        return this.pathEdges.contains(edge);
    }

    /**
     * Gibt eine textuelle Repräsentation eines Pfades zurück.
     * Diese stellt eine Aneinanderreihung der einzelnen Kante im Pfad textuell Repräsentiert dar.
     * @return textuelle Repräsentation des Pfades
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Edge edge : getPathEdges()) {
            builder.append(edge.toString() + SEPERATOR);
        }
        return builder.toString();
    }
}
