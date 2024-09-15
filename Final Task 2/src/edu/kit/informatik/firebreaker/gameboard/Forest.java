package edu.kit.informatik.firebreaker.gameboard;

import edu.kit.informatik.firebreaker.core.Position;
import edu.kit.informatik.firebreaker.status.InOutputStrings;

/**
 * Modelliert ein Wald Spielfeld auf dem {@link GameBoard}.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class Forest extends GameCell {

    private static final String NOT_BURNING_REPRESENTATION = "x";

    /**
     * Erstellt eine neue Instanz eines Waldes.
     * @param position Position auf dem Spielbrett
     * @param fieldState Status des Spielfeldes
     */
    public Forest(Position position, FieldState fieldState) {
        super(position, fieldState);
    }

    /**
     * Gibt die textuelle Repräsentation des Waldzustandes zurück für den show-board Befehl.
     * @return textuelle Repräsentation des Waldzustandes
     */
    public String getFieldRepresentation() {
        if (!isBurning()) {
            return NOT_BURNING_REPRESENTATION;
        }
        return getFieldState().getStateRepresentation();
    }

    @Override
    public GameCell copy() {
        Forest forest = new Forest(getPosition(), getFieldState());
        forest.addCellObjects(getCellObjects());
        return forest;
    }

    @Override
    public String toString() {
        if (getCellObjectsRepresentation().isBlank()) {
            return getFieldState().getStateRepresentation();
        }
        sortGameObjects();
        return getFieldState().getStateRepresentation()
                + InOutputStrings.ARGUMENT_SAPARATOR
                + getCellObjectsRepresentation();
    }


}
