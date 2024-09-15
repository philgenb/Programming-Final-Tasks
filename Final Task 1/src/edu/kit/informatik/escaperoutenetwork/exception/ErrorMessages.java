package edu.kit.informatik.escaperoutenetwork.exception;

/**
 * Alle Fehlermeldungen an einem Platz. Nicht vergessen {@link #toString()} aufzurufen.
 *
 * @author Phil Gengenbach
 * @version 1.0
 */
public enum ErrorMessages {

    /**
     * Der Befehl existiert nicht.
     */
    COMMAND_DOES_NOT_EXIST("this command does not exist"),
    /**
     * Eingabe hat eine Ungültige Anzahl an Argumenten.
     */
    LENGTH("invalid argument length"),
    /**
     * Ungültiges Eingabeformat der Argumente.
     */
    INVALID_FORMAT("invalid argument format."),
    /**
     * Schleifen im Graphen sind nicht erlaubt
     */
    GRAPH_CONTAINS_LOOPS("Loops aren't allowed in your Escaperoutenetwork."),
    /**
     * Kein gültiger Start- oder Zielknoten.
     */
    INVALID_START_TARGET_NODE("invalid start or targetnode."),
    /**
     * Ungültige Kapazität
     */
    INVALID_CAPACITY("invalid capacity."),
    /**
     * Ungültige Netzwerkgröße. Das Netzwerk muss mindestens zwei Kanten beinhalten.
     */
    INVALID_NETWORK_SIZE("invalid network size. Your network has to include minimum two edges."),
    /**
     * Doppelte Kanten innerhalb eines Graphen.
     */
    DUPLICATE_EDGE("duplicate edges are not allowed."),
    /**
     * Parallel Gegenläufige Fluchtwegeabschnitte.
     */
    PARALLEL_OPPOSITE_EDGE("parallel opposing edges must not exist."),
    /**
     * Dieses Netzwerk existiert nicht.
     */
    NETWORK_DOES_NOT_EXIST("this network does not exist."),
    /**
     * Kennung ist nicht eindeutig.
     */
    INVALID_IDENTIFIER("this identifier is not unique."),
    /**
     * Ungültiger Programmzustand
     */
    ILLEGAL_STATE("the programm is in an illegal state.");


    private final String message;

    /**
     * Erstellt eine neue Fehlermeldung mit einem gegebenen Fehlertext.
     * @param message Fehlernachricht
     */
    ErrorMessages(final String message) {
        this.message = message;
    }

    /**
     * Gibt die Fehlernachricht als Zeichenkette zurück.
     * @return Fehlernachricht als Zeichenkette
     */
    @Override
    public String toString() {
        return this.message;
    }
}
