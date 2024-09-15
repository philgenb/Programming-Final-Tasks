package edu.kit.informatik.escaperoutenetwork.core;

/**
 * String Konstanten für die Kommandozeile.
 * Diese Klasse enthält die Meldungen, welche an den Benutzer ausgegeben werden.
 * @author Phil Gengenbach
 * @version 1.0
 */
public enum OutputStrings {

    /**
     * Ein neues Fluchtwegenetz wurde erstellt.
     */
    ADDED_NETWORK("Added new escape network with identifier %s."),
    /**
     * Eine neue Kante wurde einem bereits bestehendem Fluchtwegenetz hinzugefügt.
     */
    ADDED_EDGE("Added new section %1$s to escape network %2$s.");

    private final String text;

    /**
     * Erstellt eine neuen Ausgabe mit einem gegebenen Text.
     * @param text Ausgabenachricht
     */
    OutputStrings(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
