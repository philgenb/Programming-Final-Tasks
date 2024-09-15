package edu.kit.informatik.escaperoutenetwork.graph;

import edu.kit.informatik.escaperoutenetwork.exception.EscapeNetworkInputException;

import java.util.Objects;

/**
 * Modelliert einen Knoten eines gerichteten Graphen mit einer eindeutigen Kennung.
 * Ein Knoten implementiert das Comparable Interface und kann mit anderen Knoten
 * lexikographisch anhand der eindeutigen Kennung verglichen werden.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class Node implements Comparable<Node> {

    /**
     * Regulärer Ausdruck für die Eindeutige Kennung von Knoten.
     */
    public static final String IDENTIFIER_REGEX = "[a-z]{1,6}";
    private final String uniqueIdentifier;

    /**
     * Erstellt eine neue Instanz eines Knoten von einem gerichteten Graphen.
     * @param uniqueIdentifier Eindeutige Kennung des Knoten
     * @throws EscapeNetworkInputException wirft eine Exception, wenn die eindeutige Kennung nicht
     * aus mindestes einem und maximal sechs lateinischen Großbuchstaben besteht
     */
    public Node(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    /**
     * Gibt die eindeutige Kennung eines Knoten zurück
     * @return Eindeutige Kennung des Knoten
     */
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    /**
     * Gibt die eindeutige Kennung des Knotens zurück.
     * @return eindeutige Kennung des Knotens
     */
    @Override
    public String toString() {
        return uniqueIdentifier;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Node node = (Node) object;
        return uniqueIdentifier.equals(node.uniqueIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueIdentifier);
    }


    /**
     * Vergleicht zwei Knoten anhand ihrer eindeutigen Zeichenkennung lexikographisch.
     * Das Ergebnis ist eine negative Ganzzahl, wenn das String Objekt lexikographisch vorrangeht.
     * Das Ergebnis ist eine postive Ganzzahl, wenn das String Objekt lexikographisch folgt.
     * Das Ergebnis ist 0, wenn die Kennung der beiden Knoten gleich ist.
     * @param otherNode Vergleichsknoten
     * @return Vergleichswert
     */
    @Override
    public int compareTo(Node otherNode) {
        return getUniqueIdentifier().compareTo(otherNode.getUniqueIdentifier());
    }


}
