package edu.kit.informatik.escaperoutenetwork.graph;

import java.util.Objects;

/**
 * Modelliert ein Knotentupel bestehend aus einem beliebigen Start- und Zielknoten-Paar eines Graphen.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class NodePair {

    private final Node startNode;
    private final Node targetNode;

    /**
     * Erstellt eine neue Instanz eines Knotentupels aus gegebenen Start- und Zielknoten.
     * @param startNode Startknoten
     * @param targetNode Zielknoten
     */
    public NodePair(Node startNode, Node targetNode) {
        this.startNode = startNode;
        this.targetNode = targetNode;
    }

    /**
     * Gibt den Startknoten des Tupels zurück.
     * @return Startknoten
     */
    public Node getStartNode() {
        return startNode;
    }

    /**
     * Gibt den Endknoten des Tupels zurück.
     * @return Endknoten
     */
    public Node getTargetNode() {
        return targetNode;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        NodePair nodePair = (NodePair) object;
        return startNode.equals(nodePair.startNode) && targetNode.equals(nodePair.targetNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startNode) * Objects.hash(targetNode);
    }

}
