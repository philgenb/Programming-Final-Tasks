package edu.kit.informatik.firebreaker.core;

import edu.kit.informatik.firebreaker.dice.SpreadDirection;
import edu.kit.informatik.firebreaker.gameboard.GameBoard;
import edu.kit.informatik.firebreaker.status.InOutputStrings;
import java.util.Objects;

/**
 * Modelliert eine Position innerhalb des {@link GameBoard}.
 * Jede Position verfügt über eine X-Koordinate und eine Y-Koordinate
 * @author Phil Gengenbach
 * @version 1.0
 */
public class Position {

    private static final int MAX_BORDER_DIFFERENCE = 1;
    private static final int NO_DISTANCE = 0;

    private final int positionX;
    private final int positionY;

    /**
     * Erstellt eine Instanz der Klasse Position
     * @param positionY Y-Position
     * @param positionX X-Position

     */
    public Position(int positionY, int positionX) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * Gibt die X-Koordinate der Position auf dem Spielfeld zurück.
     * @return X-Koordinate der Position
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Gibt die Y-Koordinate der Position auf dem Spielfeld zurück.
     * @return Y-Koordinate der Position
     */
    public int getPositionY() {
        return positionY;
    }


    /**
     * Bestimmt die absolute X-Koordinaten Distanz zwischen Start- und Zielpunkt.
     * @param position Zielposition.
     * @return absolute X-Distanz zwischen Start und Zielpunkt
     */
    public int getAbsoluteXDistanceTo(Position position) {
        return Math.abs(getPositionX() - position.getPositionX());
    }

    /**
     * Bestimmt die absolute Y-Koordinaten Distanz zwischen Start- und Zielpunkt.
     * @param position Zielposition.
     * @return absolute Y-Distanz zwischen Start und Zielpunkt
     */
    public int getAbsoluteYDistanceTo(Position position) {
        return Math.abs(getPositionY() - position.getPositionY());
    }

    /**
     * Ermittelt, ob die Position einer anderen gegebenen Position angrenzt.
     * @param position Position an die Position angrenzt
     * @return Status, ob Position an andere Position angrenzt
     */
    public boolean borders(Position position) {
        if (equals(position)) {
            return false;
        }
        return getAbsoluteXDistanceTo(position) <= MAX_BORDER_DIFFERENCE
                && getAbsoluteYDistanceTo(position) <= MAX_BORDER_DIFFERENCE;
    }

    /**
     * Ermittelt, ob die Position einer anderen gegebenen Position unmittelbar angrenzt.
     * Dabei darf die gegebene Position nicht diagonal erreicht werden.
     * @param position Position an die Position angrenzt
     * @return Status, ob Position an andere Position direkt angrenzt
     */
    public boolean bordersDirectly(Position position) {
        if (equals(position)) {
            return false;
        }
        return (getAbsoluteXDistanceTo(position) <= MAX_BORDER_DIFFERENCE
                && getAbsoluteYDistanceTo(position) == NO_DISTANCE)
                || (getAbsoluteYDistanceTo(position) <= MAX_BORDER_DIFFERENCE
                && getAbsoluteXDistanceTo(position) == NO_DISTANCE);
    }

    /**
     * Überprüft, ob die Position einer anderen Position quer angrenzt.
     * Gibt {@code true} zurück, falls die Position der gegebenen Position quer / schräg angrenzt
     * @param position Position, die überprüft werden soll
     * @return ob Position der gegebenen Position quer angrenzt
     */
    public boolean bordersAcross(Position position) {
        return borders(position) && !hasEqualCoordinate(position);
    }

    private boolean hasEqualCoordinate(Position position) {
        return this.positionX == position.getPositionX() || this.positionY == position.getPositionY();
    }

    /**
     * Ermittelt eine Position einer Nachbarzelle in gegebener Richtung von einer gegebenen Position aus.
     * @param startPosition Startposition
     * @param spreadDirection Richtung
     * @return Position einer Nachbarzelle in gegebener Richtung
     */
    public static Position getPositionInDirection(Position startPosition, SpreadDirection spreadDirection) {
        return new Position(startPosition.getPositionY() + spreadDirection.getYSpread(),
                startPosition.getPositionX() + spreadDirection.getXSpread());
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Position otherPosition = (Position) object;

        return getPositionX() == otherPosition.getPositionX() && getPositionY() == otherPosition.getPositionY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionX, positionY);
    }

    @Override
    public String toString() {
        return getPositionY() + InOutputStrings.VALUE_SAPARATOR + getPositionX();
    }
}
