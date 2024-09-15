package edu.kit.informatik.escaperoutenetwork.core;


import edu.kit.informatik.escaperoutenetwork.compare.EscapeRouteNetworkComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelliert einen Fluchtwegenetz-Manager, welcher alle bestehenden Fluchtwegenetze verwaltet.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class EscapeRouteNetworkManager {

    private static final int MINIMUM_INDEX = 0;
    private static final int MINIMUM_SIZE = 1;
    private static final String SPACE_SEPERATOR = " ";
    private static final String EMPTY_STATUS = "EMPTY";

    private final List<EscapeRouteNetwork> escapeRouteNetworks;

    /**
     * Erstellt eine neue Instanz eines Fluchtwege-Managers zur Verwaltung von Fluchtwegenetzen.
     */
    public EscapeRouteNetworkManager() {
        this.escapeRouteNetworks = new ArrayList<>();
    }

    /**
     * Sortiert alle Fluchtwegenetze nach den Spezifikationen in {@link EscapeRouteNetworkComparator}
     */
    public void sortEscapeRouteNetworks() {
        escapeRouteNetworks.sort(new EscapeRouteNetworkComparator());
    }

    /**
     * Gibt eine Liste aller Fluchtwegenetze zurück.
     * @return Liste aller Fluchtwegenetze
     */
    public List<EscapeRouteNetwork> getEscapeRouteNetworks() {
        return List.copyOf(this.escapeRouteNetworks);
    }

    /**
     * Fügt ein neues Fluchtwegenetz der Liste aller bestehenden Fluchtwegenetze hinzu.
     * @param escapeRouteNetwork Fluchtwegenetz
     */
    public void addEscapeRouteNetwork(EscapeRouteNetwork escapeRouteNetwork) {
        escapeRouteNetworks.add(escapeRouteNetwork);
    }

    /**
     * Ermittelt ein gespeichertes Fluchtwegenetzmittels einer gegebenen Kennung und gibt dieses zurück.
     * Existiert kein Fluchtwegenetz mit der gegebenen Kennung, so wird {@code null} zurückgegeben.
     * @param uniqueIdentifier Eindeutige Kennung des Fluchtwegenetzes
     * @return Fluchtwegenetz mit gegebener Kennung
     */
    public EscapeRouteNetwork getEscapeRouteNetworkByIdentifier(String uniqueIdentifier) {
        for (EscapeRouteNetwork escapeRouteNetwork : escapeRouteNetworks) {
            if (escapeRouteNetwork.getUniqueIdentifier().equals(uniqueIdentifier)) {
                return escapeRouteNetwork;
            }
        }
        return null;
    }

    /**
     * Überprüft ob ein Fluchtwegenetz mit der dazugehörigen Kennung existiert.
     * @param uniqueIdentifier Eindeutige Kennung des Fluchtwegenetzes
     * @return ob Fluchtwegenetz in der Liste enthalten ist
     */
    public boolean containsEscapeRouteNetworkWithIdentifier(String uniqueIdentifier) {
        return getEscapeRouteNetworkByIdentifier(uniqueIdentifier) != null;
    }

    @Override
    public String toString() {
        if (getEscapeRouteNetworks().isEmpty()) {
            return EMPTY_STATUS;
        }
        StringBuilder builder = new StringBuilder();

        for (int i = MINIMUM_INDEX; i < getEscapeRouteNetworks().size(); i++) {
            EscapeRouteNetwork escapeRouteNetwork = getEscapeRouteNetworks().get(i);
            builder.append(escapeRouteNetwork.getUniqueIdentifier()
                    + SPACE_SEPERATOR + escapeRouteNetwork.getNodeCount());
            if (i < getEscapeRouteNetworks().size() - MINIMUM_SIZE) {
                builder.append(System.lineSeparator());
            }
        }
        return builder.toString();
    }
}
