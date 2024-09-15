package edu.kit.informatik.firebreaker.core;

import edu.kit.informatik.firebreaker.gameboard.GameObject;

import java.util.Comparator;

/**
 * Diese Klasse beschreibt einen Comperator zum sortieren von Spielobjekten.
 * Die Sortierung erfolgt lexikographisch nach der {@link Object#toString()} Repräsentation des Spielers
 * und numerisch aufsteigend nach der Kennungsnummer des Objektes.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class GameObjectComperator implements Comparator<GameObject> {

    @Override
    public int compare(final GameObject gameObject1, final GameObject gameObject2) {
        if (!gameObject1.getOwningPlayer().toString().equals(gameObject2.getOwningPlayer().toString())) {
            return gameObject1.getOwningPlayer().toString().compareTo(gameObject2.getOwningPlayer().toString());
        }
        return Integer.compare(gameObject1.getIdentifier(), gameObject2.getIdentifier());
    }
}
