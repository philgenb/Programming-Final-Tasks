package edu.kit.informatik.firebreaker.model;

import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.gameboard.FireFighter;
import edu.kit.informatik.firebreaker.gameboard.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Modelliert einen Spieler des Firebreaker Spiels.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class Player {

    private static final int INITIAL_REPUTATION_POINTS = 0;
    private static final int FIRE_ENGINE_PRICE = 5;

    private final String identifier;
    private final List<FireFighter> fireFighters;

    private int reputationPoints;
    private int currentFireFighterNumber;
    private boolean inGame;

    /**
     * Erstellt eine neue Instanz eines Spielers mit eine gegebenen eindeutigen Kennung
     * @param identifier eindeutige Kennung des Spielers
     */
    public Player(String identifier) {
        this.fireFighters = new ArrayList<>();
        this.reputationPoints = INITIAL_REPUTATION_POINTS;
        this.identifier = identifier;
        this.currentFireFighterNumber = 0;
        this.inGame = true;
    }

    /**
     * Gibt die eindeutige Kennung des Spielers zurück.
     * @return eindeutige Kennung des Spielers
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Gibt die Anzahl an Reputationspunkten zurück.
     * @return Anzahl der Reputationspunkte des Spielers
     */
    public int getReputationPoints() {
        return reputationPoints;
    }

    /**
     * Fügt dem Spieler ein Reputationspunkt hinzu.
     */
    public void addReputationPoint() {
        this.reputationPoints++;
    }

    /**
     * Gibt eine Liste aller Feuerwehren des Spielers als Kopie zurück.
     * @return Liste aller Feuerwehren des Spielers
     */
    public List<FireFighter> getFireFighters() {
        return List.copyOf(fireFighters);
    }

    /**
     * Überprüft, ob der Spieler noch im Spiel ist.
     * @return ob der Spieler noch im Spiel ist
     */
    public boolean inGame() {
        return this.inGame;
    }


    /**
     * Fügt dem Spieler eine Feuerwehr hinzu mit einer neuen eindeutigen Feuerwehrnummer.
     * Die Feuerwehren werden durchgehend durchnummeriert beginnend mit 0.
     * @return neu hinzugefügte Feuerwehr
     */
    public FireFighter addFireFighter() {
        FireFighter newFireFighter = new FireFighter(currentFireFighterNumber, this);
        this.fireFighters.add(newFireFighter);
        currentFireFighterNumber++;
        return newFireFighter;
    }

    /**
     * Entfernt eine gegebenes Spielobjekt aus der Liste aller Feuerwehren des Spielers.
     * Falls ein Spiel nach Entfernen des Spielobjektes keine Spielobjekte mehr besitzt,
     * so scheidet er bis zum Endes des Spieles aus.
     * @param gameObject Feuerwehr, welche entfernt werden soll
     */
    public void removeGameObject(GameObject gameObject) {
        this.fireFighters.remove(gameObject);
        if (this.fireFighters.isEmpty()) {
            this.inGame = false;
        }
    }

    /**
     * Ermittelt eine Feuerwehr anhand der textuellen Repräsentation der Feuerwehr.
     * Gibt {@code null} zurück, falls keine Feuerwehr mit der textuellen Repräsentation extistiert.
     * @param fireFighterIdentifier textuelle Repräsentation der Feuerwehr
     * @return Feuerwehr mit der gebebenen textuellen Repräsentation
     */
    public FireFighter getFireFighterByStringIdentifier(String fireFighterIdentifier) {
        for (FireFighter fireFighter : fireFighters) {
            if (fireFighter.getIdentifierString().equals(fireFighterIdentifier)) {
                return fireFighter;
            }
        }
        return null;
    }

    /**
     * Fügt dem Spieler eine neue Feuerwehr hinzu, falls dieser mehr als 5 Reputationspunkte besitzt.
     * Die Kosten der Feuerwehr von 5 Reputationspunkten werden von der akutellen Anzahl an Reputationspunkten
     * des Spielers abgezogen.
     * @return neu hinzugefügte Feuerwehr
     * @throws FireBreakerException falls der Spieler nicht genügend Reputationspunkte besitzt
     */
    public FireFighter buyFireEngine() throws FireBreakerException {
        if (getReputationPoints() < FIRE_ENGINE_PRICE) {
            throw new FireBreakerException(ErrorMessages.NOT_ENOUGH_REPUTATION);
        }
        this.reputationPoints = reputationPoints - FIRE_ENGINE_PRICE;
        return addFireFighter();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Player otherPlayer = (Player) object;

        return getIdentifier().equals(otherPlayer.getIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identifier);
    }

    @Override
    public String toString() {
        return identifier;
    }

}
