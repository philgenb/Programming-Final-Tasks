package edu.kit.informatik.escaperoutenetwork.compare;

import edu.kit.informatik.escaperoutenetwork.graph.Node;
import edu.kit.informatik.escaperoutenetwork.graph.NodePair;

import java.util.Comparator;
import java.util.Map;

/**
 * Modelliert eine Vergleichfunktion zum Vergleichen von Mapeinträgen aus Knotenpaaren und Ganzzahlen.
 * Zuerst werden die Einträge nach aufsteigender Ganzzahl-Werte (Maximaler Fluss) sortiert.
 * Wenn die Werte identisch sind, wird zunächst nach dem Bezeichner des Startknotens und dann nach dem Bezeichner
 * des Zielknotens Alphabetisch aufsteigend sortiert.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class MaximumFlowComparator implements Comparator<Map.Entry<NodePair, Long>> {

    private static final int COMPARE_EQUAL = 0;

    @Override
    public int compare(Map.Entry<NodePair, Long> entry1, Map.Entry<NodePair, Long> entry2) {
        Long flow1 = entry1.getValue();
        Long flow2 = entry2.getValue();

        if (flow1.compareTo(flow2) != COMPARE_EQUAL) {
            return flow1.compareTo(flow2);
        }

        Node startNode1 = entry1.getKey().getStartNode();
        Node startNode2 = entry2.getKey().getStartNode();

        if (startNode1.compareTo(startNode2) != COMPARE_EQUAL) {
            return startNode1.compareTo(startNode2);
        }

        Node targetNode1 = entry1.getKey().getTargetNode();
        Node targetNode2 = entry2.getKey().getTargetNode();
        return targetNode1.compareTo(targetNode2);
    }


}
