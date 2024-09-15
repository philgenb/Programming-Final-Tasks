package edu.kit.informatik.firebreaker.gameboard;

import edu.kit.informatik.firebreaker.core.Position;

/**
 * Modelliert eine Feuerwehrstation auf dem Spielbrett.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class FireStation extends GameCell {

    /**
     * Ein Regulärer Ausdruck für die Kennung einer Feuerwehrstation
     */
    public static final String FIRESTATION_REGEX = "[A-D]";

    private static final String BOARD_REPRESENTATION = "x";

    private final String fireStationIdentifier;

    /**
     * Erstellt eine neue Instanz einer Feuerwehrstation. Jede Feuerwehrstation
     * hat eine eindeutige Position auf dem Spielbrett und eine eindeutige Kennung
     * zu welchem Spieler die Feuerwehrstation gehört.
     * @param gameCellPosition Position der Feuerwehrstation
     * @param fireStationIdentifier Eindeutige Kennung der Feuerwehrstation
     */
    public FireStation(Position gameCellPosition, String fireStationIdentifier) {
        super(gameCellPosition, FieldState.SOLID);
        this.fireStationIdentifier = fireStationIdentifier;
    }

    @Override
    public boolean canPlaceOnTop() {
        return false;
    }

    @Override
    public GameCell copy() {
        return new FireStation(getPosition(), fireStationIdentifier);
    }

    /**
     * Gibt die textuelle Repräsentation der Station für den show-board Befehl zurück.
     * @return textuelle Repräsentation der Feuerwehrstation
     */
    public String getFieldRepresentation() {
        return BOARD_REPRESENTATION;
    }

    @Override
    public String toString() {
        return fireStationIdentifier;
    }
}
