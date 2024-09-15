package edu.kit.informatik.escaperoutenetwork.compare;

import edu.kit.informatik.escaperoutenetwork.core.EscapeRouteNetwork;
import edu.kit.informatik.escaperoutenetwork.graph.Graph;
import java.util.Comparator;

/**
 * Modelliert eine Vergleichfunktion, die einer Sammlung
 * von {@link EscapeRouteNetwork} eine Gesamtreihenfolge auferlegt.
 * Dabei werden die Fluchtwegenetze anhand ihrer dazugehörigen Graphen verglichen.
 * Die Sortierung erfolgt anhand der Anzahl an Knoten im Graphen.
 * Die Fluchtwegenetze werden zunächst absteigend nach der Anzahl ihrer Knoten sortiert, das bedeutet
 * die Fluchtwegenetze mit den meisten Knoten werden zuerst aufgelistet. Ist die Anzahl der Knoten
 * identisch, werden die entsprechenden Fluchtwegenetze anschließend noch alphabetisch
 * aufsteigend nach ihrer Kennung sortiert, das heißt die lexikografisch kleinsten Werte kommen zuerst.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class EscapeRouteNetworkComparator implements Comparator<EscapeRouteNetwork> {

    private static final int COMPARE_EQUAL = 0;

    /**
     * Vergleicht zwei Fluchtwegenetz nach den Spezifikationen von {@link EscapeRouteNetworkComparator}.
     * @param escapeRouteNetwork Fluchtwegenetz
     * @param escapeRouteNetwork2 Vergleichs-Fluchtwegenetz
     * @return den Wert 0, wenn beide Fluchtwegenetze gleich einsortiert werden,
     * den Wert -1, wenn der Graph in der Sortierten Collection weiter vorne einsortiert wird
     * den Wert 1, wenn der Graph in der Sortieren Collection weiter hinten einsortiert wird
     */
    @Override
    public int compare(EscapeRouteNetwork escapeRouteNetwork, EscapeRouteNetwork escapeRouteNetwork2) {
        Graph graph1 = escapeRouteNetwork.getDirectedGraph();
        Graph graph2 = escapeRouteNetwork2.getDirectedGraph();

        if (graph2.compareTo(graph1) != COMPARE_EQUAL) {
            return graph2.compareTo(graph1);
        }

        return graph1.getUniqueIdentifier().compareTo(graph2.getUniqueIdentifier());
    }
}
