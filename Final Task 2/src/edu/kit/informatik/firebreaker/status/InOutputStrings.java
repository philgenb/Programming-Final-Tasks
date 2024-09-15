package edu.kit.informatik.firebreaker.status;

import edu.kit.informatik.firebreaker.exception.ErrorMessages;

/**
 * Sammlung aller Zeichenketten zur Befehlseingabe und Befehlsausgabe.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class InOutputStrings {

    /**
     * Befehl erfolgreich ausgeführt.
     */
    public static final String SUCCESS = "OK";
    /**
     * Separiert einen Befehl von seinen potenziellen Argumenten.
     */
    public static final String COMMAND_SAPARATOR = " ";
    /**
     * Separiert die verschiedenen Argumente.
     */
    public static final String ARGUMENT_SAPARATOR = ",";
    /**
     * Separiert verschiedene Werte.
     */
    public static final String VALUE_SAPARATOR = ";";
    /**
     * Spieler haben das Spiel gewonnen.
     */
    public static final String WIN = "win";
    /**
     * Die Spieler haben das Spiel verloren.
     */
    public static final String LOST_GAME = "lose";
    /**
     * Ausgabe des Extinguish Befehls.
     */
    public static final String EXTINGUISH_OUPUT = "%s" + ARGUMENT_SAPARATOR + "%d";
    /**
     * Ausgabe des Show-Player Befehls.
     */
    public static final String SHOWPLAYER_OUTPUT = "%s,%d";
    /**
     * Ausgabeformat einer Feuerwehr.
     */
    public static final String FIREFIGHTER_OUTPUT = "%s,%d,%d,";
    /**
     * Ausgabeformat einer Position auf dem Spielbrett.
     */
    public static final String POSITION_OUTPUT = "%d,%d";

    /**
     * Privater Konstruktor, da Utillity Klasse
     */
    private InOutputStrings() {
        throw new IllegalStateException(ErrorMessages.ILLEGAL_STATE);
    }

}
