package edu.kit.informatik.firebreaker.dice;

/**
 * Modelliert die verschiedenen Würfelergebnisse nach dem Beenden einer Runde.
 * @author Phil Gengenbach
 * @version 1.0
 */
public enum DiceResults {

    /**
     * Feuer-Ausbreitung in alle Richtungen
     * Würfelzahl 1
     */
    DICE_ONE(1, SpreadDirection.ALL_DIRECTIONS),
    /**
     * Feuer-Ausbreitung nach Norden
     * Würfelzahl 2
     */
    DICE_TWO(2, SpreadDirection.NORTH),
    /**
     * Feuer-Ausbreitung nach Osten
     * Würfelzahl 3
     */
    DICE_THREE(3, SpreadDirection.EAST),
    /**
     * Feuer-Ausbreitung nach Süden
     * Würfelzahl 4
     */
    DICE_FOUR(4, SpreadDirection.SOUTH),
    /**
     * Feuer-Ausbreitung nach Westen
     * Würfelzahl 5
     */
    DICE_FIVE(5, SpreadDirection.WEST);

    private final int diceNumber;
    private final SpreadDirection spreadDirection;

    /**
     * Erstellt ein neues Würfelergebnis mit der dazugehörigen Würfelaktion.
     * @param diceNumber Zahl des Würfelwurfes
     * @param spreadDirection Ausbreitungsrichtung des Feuers
     */
    DiceResults(int diceNumber, SpreadDirection spreadDirection) {
        this.diceNumber = diceNumber;
        this.spreadDirection = spreadDirection;
    }

    /**
     * Gibt die Würfelzahl des Würfelergebnisses zurück
     * @return Zahl des Würfels
     */
    public int getDiceNumber() {
        return diceNumber;
    }

    /**
     * Gibt die Ausbreitungsrichtung des Würfelergebnisses zurück.
     * @return Ausbreitungsrichtung des Würfelergebnisses
     */
    public SpreadDirection getSpreadDirection() {
        return this.spreadDirection;
    }

    /**
     * Ermittelt anhand einer Würfelzahl das dazugehörige Würfelergebnis.
     * Falls kein Würfelergebnis mit der gegebenen Würfelzahl existiert, so wird
     * {@code null} zurückgegeben.
     * @param diceNumber Zahl des Würfels
     * @return Würfelergebnis
     */
    public static DiceResults valueOfDiceNumber(int diceNumber) {
        for (DiceResults diceResult : values()) {
            if (diceResult.getDiceNumber() == diceNumber) {
                return diceResult;
            }
        }
        return null;
    }

}
