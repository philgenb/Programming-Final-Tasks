package edu.kit.informatik.firebreaker.exception;

/**
 * Alle Fehlernachrichten gebündelt an einem Ort.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class ErrorMessages {

    /**
     * Ungültige Anzahl an Befehls-Argumenten.
     */
    public static final String INVALID_ARGUMENT_LENGTH = "invalid argument length.";
    /**
     * Ungültiges Argument-Format.
     */
    public static final String INVALID_ARGUMENT_FORMAT = "you're arguments don't match the required format.";
    /**
     * Befehl existiert nicht.
     */
    public static final String COMMAND_DOES_NOT_EXIST = "this command does not exist.";
    /**
     * Ungültiger initialer Spielzustand.
     */
    public static final String INVALID_INITAL_GAMESTATE = "the game state by the command input is invalid.";
    /**
     * Position ist nicht innerhalb des Spielfeldes.
     */
    public static final String POSITION_OUT_OF_GAMEFIELD = "the position is outside of the gamefield.";
    /**
     * Nicht genug Reputationspunkte.
     */
    public static final String NOT_ENOUGH_REPUTATION = "you don't have enough reputation.";
    /**
     * Nicht genügend Aktionspunkte.
     */
    public static final String NOT_ENOUGH_ACTION_POINTS = "this firefighter, does not have enough action points.";
    /**
     * Ungültige Platzierungsposition.
     */
    public static final String INVALID_PLACE_POSITION = "this object can't be placed here.";
    /**
     * Ungültige Zielposition.
     */
    public static final String INVALID_MOVE_POSITION = "invalid position to move to.";
    /**
     * Kapazität vollständig ausgenutzt.
     */
    public static final String CAPACITY_USED = "this firefighter can't refill, as all his capacity is full.";
    /**
     * Feld ist nicht in Reichweite der Feuerwehr
     */
    public static final String NOT_IN_RANGE = "this field is not in range of the firefighter.";
    /**
     * Feuerwehr hat keine Wasserressourcen mehr.
     */
    public static final String OUT_OF_WATER = "the firefighter does not have enough water.";
    /**
     * Diese Aktion ist nur einmal pro Runde möglich
     */
    public static final String TOO_MANY_ACTIONS = "you can only do that once per round.";
    /**
     * Dieses Spielfeld kann nicht gelöscht werden.
     */
    public static final String CAN_NOT_BE_EXTINGUISHED = "this field can't be extinguished.";
    /**
     * Keine Feuerwehrstation in Reichweite der Zielposition.
     */
    public static final String NO_FIRESTATION_IN_RANGE = "no firestation in range of position.";
    /**
     * Keine Wasserquelle in Reichweite der Feuerwehr.
     */
    public static final String NO_WATER_SOURCE_IN_RANGE = "no water source in range of firefighter.";
    /**
     * Feuerwehr mit der Kennung existiert nicht.
     */
    public static final String FIREFIGHTER_DOES_NOT_EXIST = "a firefighter with this number does not exist.";
    /**
     * Runde ist zuende -> Würfel muss geworfen werden.
     */
    public static final String ROUND_FINISH
            = "the previous round is completed. Please roll a dice first before continue to play.";
    /**
     * Ein Würfel kann nur am Ende der Runde geworfen werden.
     */
    public static final String ROUND_NOT_FINISHED = "you have to finish the round before rolling a dice.";
    /**
     * Ungültiges Würfelergebnis.
     */
    public static final String INVALID_DICE_NUMBER = "this dice number does not exist.";

    /**
     * Programm befindet sich in einem ungültigen Zustand.
     */
    public static final String ILLEGAL_STATE = "the program is in an invalid state.";
    /**
     * Die Feuerwehr darf in dieser Runde nicht mehr bewegt werden.
     */
    public static final String CAN_NOT_BE_MOVED = "this firefighter can not be moved in this round anymore.";
    /**
     * Spiel wurde bereits beendet. Es darf kein Befehl ausgeführt werden.
     */
    public static final String GAME_ALREADY_FINISH = "you can't execute this command, while the game is finished.";


    /**
     * Privater Konstruktor, da Utillity-Klasse.
     */
    private ErrorMessages() {
        throw new IllegalStateException(ILLEGAL_STATE);
    }
}
