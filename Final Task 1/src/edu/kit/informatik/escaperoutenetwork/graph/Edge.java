package edu.kit.informatik.escaperoutenetwork.graph;

import java.util.Objects;

/**
 * Modelliert eine Kante eines gerichteten Graphen.
 * Jede Kante zeigt also von einem Startknoten auf einen Zielknoten.
 * Jede Kante verfügt über eine Kapazität, einen Durchflusswert und hat außerdem eine Referenz auf eine Rückkante.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class Edge {

    /**
     * Ungültige neutrale Kapazität
     */
    public static final int ZERO_CAPACITY = 0;

    private final Node source;
    private final Node target;
    private long capacity;
    private Edge residualEdge;
    private long flow;

    /**
     * Erstellt eine neue Instanz einer Kante eines gerichteten Graphen.
     * @param source Startknoten
     * @param endNode Zielknoten
     * @param capacity Kapazität
     */
    public Edge(Node source, Node endNode, long capacity) {
        this.source = source;
        this.target = endNode;
        this.capacity = capacity;
    }

    /**
     * Gibt den Startknoten zurück, von dem die Kante ausgeht.
     * @return Startknoten von dem Kante ausgeht
     */
    public Node getSource() {
        return source;
    }

    /**
     * Gibt den Zielknoten zurück, zu dem die Kante hin zeigt.
     * @return Zielknoten
     */
    public Node getTarget() {
        return target;
    }

    /**
     * Setzt die Rezidualkante der Kante zu einer gegebenen Kante.
     * @param residualEdge Rezidualkante
     */
    public void setResidualEdge(Edge residualEdge) {
        this.residualEdge = residualEdge;
    }

    /**
     * Gibt die Rezidualkante der Kante zurück.
     * @return Rezidualkante
     */
    public Edge getResidualEdge() {
        return this.residualEdge;
    }

    /**
     * Setzt die Flussrate des Fluchtwegeabschnittes auf eine gegebene Anzahl an Personen pro Minute.
     * @param flow Flussrate - Anzahl an Personen pro Minute
     */
    public void setFlow(long flow) {
        this.flow = flow;
    }

    /**
     * Setzt die Kapazität der Kante zu einer gegebenen Ganzzahl
     * @param capacity neue Kapazität
     */
    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    /**
     * Gibt die Kapazität des Fluchtwegeabschnitts zurück.
     * @return Kapazität des Fluchtwege
     */
    public long getCapacity() {
        return capacity;
    }

    /**
     * Gibt die Flussrate des Fluchtwegeabschnittes zurück.
     * @return Flussrate des Fluchtwegeabschnittes
     */
    public long getFlow() {
        return flow;
    }

    /**
     * Überprüft ob der Fluchtwegeabschnitt noch Restkapazitäten hat.
     * @return hat Fluchtwegeabschnitt noch Restkapazitäten
     */
    public boolean isRemaining() {
        return getRemainingCapacity() > ZERO_CAPACITY;
    }

    /**
     * Ermittelt die Restkapazität des Fluchtwegeabschnittes.
     * Diese wird berechnet über die Differenz von Kapazität und Flussrate.
     * @return Restkapazität des Fluchtwegeabschnittes
     */
    public long getRemainingCapacity() {
        return capacity - flow;
    }

    /**
     * Erhöht den Fluss der Kante und verringert den Fluss der Rückkante um einen gegebenen Wert.
     * @param throttleValue Wert, um den der Fluss erhöht und verringert werden soll
     */
    public void augment(long throttleValue) {
        setFlow(getFlow() + throttleValue);
        residualEdge.setFlow(residualEdge.getFlow() - throttleValue);
    }

    /**
     * Erstellt eine neue Kante mit vertauschtem Start- und Zielknoten und gibt sie zurück.
     * Die Kapazität der neuen Kante ist dabei mit der aktuellen identisch.
     * @return Kante mit vertauschtem Start- und Zielknoten
     */
    public Edge getInvertedEdge() {
        return new Edge(target, source, capacity);
    }

    /**
     * Gibt eine textuelle Repräsentation einer Kante zurück im folgenden Format.
     * [Startknoten][Kapazität][Zielknoten], jeweils textuell repräsentiert.
     * @return textuelle Repräsentation einer Kante
     */
    @Override
    public String toString() {
        return source.toString() + getCapacity() + target.toString();
    }

    /**
     * Vergleicht die Kante mit einem anderen Objekt. Handelt es sich bei dem anderen Objekt
     * ebenfalls um eine Kante, so wird überprüft ob die beiden Kanten gleich sind,
     * anhand von Start- und Zielknoten.
     * Sind bei beiden Kanten die Start- und Zielknoten jeweils gleich, so wird {@code true} zurückgegeben,
     * andernfalls {@code false}.
     * @param object Objekt
     * @return ob das gegebene Objekt eine Kante mit dem selben Start- und Zielknoten ist
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Edge edgeToCompare = (Edge) object;
        return getSource().equals(edgeToCompare.getSource()) && getTarget().equals(edgeToCompare.getTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }

    /**
     * Gibt eine vollständige Kopie der Kante zurück.
     * @return Kopie der Kante
     */
    public Edge copy() {
        Edge copyEdge = new Edge(source, target, capacity);
        copyEdge.setResidualEdge(getResidualEdge());
        copyEdge.setFlow(getFlow());
        return copyEdge;
    }
}
