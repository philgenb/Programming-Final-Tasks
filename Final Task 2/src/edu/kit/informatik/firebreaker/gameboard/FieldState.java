package edu.kit.informatik.firebreaker.gameboard;

/**
 * Beschreibt den Zustand eines Spielfeldes.
 * @author Phil Gengenbach
 * @version 1.0
 */
public enum FieldState {

    /**
     * Wald im trockenen Zustand.
     */
    DRY("d"),
    /**
     * Wald im nassen Zustand.
     */
    WET("w"),
    /**
     * Wald leicht am brennen.
     */
    LIGHTLY_BURNING("+"),
    /**
     * Wald stark am brennen.
     */
    STRONGLY_BURNING("*"),
    /**
     * Festes unpassierbares Feld
     */
    SOLID("s");

    private final String stateRepresentation;

    /**
     * Erstellt eine neuen möglichen Zustand einer {@link GameCell}.
     * @param stateRepresentation textuelle Repräsentation des Spielfeldstatus
     */
    FieldState(final String stateRepresentation) {
        this.stateRepresentation = stateRepresentation;
    }

    /**
     * Gibt die textuelle Repräsentation des Status zurück.
     * @return textuelle Repräsentation des Status eines Spielfeldes
     */
    public String getStateRepresentation() {
        return this.stateRepresentation;
    }

    /**
     * Ermittelt einen Status anhand der dazugehörigen textuellen Repräsentation des Status.
     * @param representation textuelle Repräsentation
     * @return der dazugehörige Spielfeldstatus
     */
    public static FieldState getForestState(String representation) {
        for (FieldState fieldState : values()) {
            if (fieldState.getStateRepresentation().equals(representation)) {
                return fieldState;
            }
        }
        return null;
    }

}
