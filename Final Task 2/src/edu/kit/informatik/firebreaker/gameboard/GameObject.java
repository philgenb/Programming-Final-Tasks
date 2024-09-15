package edu.kit.informatik.firebreaker.gameboard;

import edu.kit.informatik.firebreaker.model.Player;

import java.util.Objects;

/**
 * Modelliert ein Spielobjekt in einer {@link GameCell} auf dem {@link GameBoard}.
 * @author Phil Gengenbach
 * @version 1.0
 */
public abstract class GameObject {

    private final Player owningPlayer;
    private final int identifier;

    /**
     * Erstellt eine neue Instanz eines Spielobjektes.
     * @param identifier eindeutige ID
     * @param owningPlayer besitzender Spieler
     */
    public GameObject(int identifier, Player owningPlayer) {
        this.identifier = identifier;
        this.owningPlayer = owningPlayer;
    }

    /**
     * Gibt die eindeutige Spielerobjekt Kennung zurück.
     * @return Spielerobjekt-Kennung
     */
    public String getIdentifierString() {
        return this.owningPlayer.toString() + this.identifier;
    }

    /**
     * Gibt die numerische ID des Spielobjektes zurück.
     * @return ID des Spielobjektes
     */
    public int getIdentifier() {
        return this.identifier;
    }

    /**
     * Gibt den Spieler zurück, dem das Spielobjekt gehört.
     * @return besitzender Spieler
     */
    public Player getOwningPlayer() {
        return this.owningPlayer;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        GameObject that = (GameObject) object;

        return getOwningPlayer().equals(that.owningPlayer)
                && identifier == that.identifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public abstract String toString();

}
