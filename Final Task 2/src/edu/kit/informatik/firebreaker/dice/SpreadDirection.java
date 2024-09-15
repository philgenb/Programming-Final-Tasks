package edu.kit.informatik.firebreaker.dice;

/**
 * Diese Klasse fasst alle Ausbreitungsrichtungen des Feuers zusammen.
 * Jede Ausbreitungsrichtung besteht aus einer Ausbreitung in X-Richtung und einer in Y-Richtung.
 * @author Phil Gengenbach
 * @version 1.0
 */
public enum SpreadDirection {

    /**
     * Ausbreitung in alle Richtungen
     */
    ALL_DIRECTIONS(0, 0),
    /**
     * Ausbreitungsrichtung Norden
     */
    NORTH(-1, 0),
    /**
     * Ausbreitungsrichtung Westen
     */
    WEST(0, -1),
    /**
     * Ausbreitungsrichtung Osten
     */
    EAST(0, 1),
    /**
     * Ausbreitungsrichtung Süden
     */
    SOUTH(1, 0);

    private final int xSpread;
    private final int ySpread;

    /**
     * Erstellt eine neue Instanz einer Ausbreitungsrichtung
     * @param ySpread Ausbreitung in Y-Richtung
     * @param xSpread Ausbreitung in X-Richtung
     */
    SpreadDirection(int ySpread, int xSpread) {
        this.ySpread = ySpread;
        this.xSpread = xSpread;
    }

    /**
     * Gibt die Ausbreitung in X Richtung zurück.
     * @return Ausbreitung in X Richtung
     */
    public int getXSpread() {
        return this.xSpread;
    }

    /**
     * Gibt die Ausbreitung in Y Richtung zurück.
     * @return Ausbreitung in Y Richtung
     */
    public int getYSpread() {
        return this.ySpread;
    }

}
