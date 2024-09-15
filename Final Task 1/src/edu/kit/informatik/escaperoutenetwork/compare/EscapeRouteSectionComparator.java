package edu.kit.informatik.escaperoutenetwork.compare;

import edu.kit.informatik.escaperoutenetwork.graph.Edge;
import edu.kit.informatik.escaperoutenetwork.graph.Node;

import java.util.Comparator;

/**
 * Modelliert eine Vergleichfunktion zum Vergleichen von Kanten.
 * Die Fluchtwegeabschnitte werden zuerst aufsteigend nach der Kennung ihres ausgehenden Knotens
 * und dann aufsteigend nach der Kennung ihres eingehenden Knotens sortiert.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class EscapeRouteSectionComparator implements Comparator<Edge> {

    private static final int COMPARE_EQUAL = 0;

    @Override
    public int compare(Edge edge1, Edge edge2) {
        Node source1 = edge1.getSource();
        Node source2 = edge2.getSource();

        if (source1.compareTo(source2) != COMPARE_EQUAL) {
            return source1.compareTo(source2);
        }

        Node target1 = edge1.getTarget();
        Node target2 = edge2.getTarget();

        return target1.compareTo(target2);
    }
}
