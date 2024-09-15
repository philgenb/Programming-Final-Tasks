package edu.kit.informatik.firebreaker.gameboard;

import edu.kit.informatik.firebreaker.core.Position;

/**
 * Modelliert einen Löschteich auf dem {@link GameBoard}.
 * Ein Löschteich ermöglicht einer angrenzenden Feuerwehr das Nachfüllen des Wassertanks.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class ExtinguishingPond extends GameCell {

    /**
     * textuelle Repräsentation eines Löschteiches.
     */
    public static final String POND_REPRESENTATION = "L";

    private static final String FIELD_REPRESENTATION = "x";

    /**
     * Erstellt eine neue Instanz eines Teiches
     * @param gameCellPosition Position des Teiches auf dem Spielbrett
     */
    public ExtinguishingPond(Position gameCellPosition) {
        super(gameCellPosition, FieldState.SOLID);
    }

    @Override
    public boolean canPlaceOnTop() {
        return false;
    }

    @Override
    public GameCell copy() {
        return new ExtinguishingPond(getPosition());
    }

    /**
     * Gibt eine textuelle Repräsentation zurück für den show-board Befehl.
     * @return textuelle Repräsentation
     */
    public String getFieldRepresentation() {
        return FIELD_REPRESENTATION;
    }

    @Override
    public String toString() {
        return POND_REPRESENTATION;
    }
}
