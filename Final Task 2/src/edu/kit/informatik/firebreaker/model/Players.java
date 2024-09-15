package edu.kit.informatik.firebreaker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Eine Aufzählung aller Spieler im Firebreaker Spiel.
 * @author Phil Gengenbach
 * @version 1.0
 */
public enum Players {

    /**
     * Spieler A
     */
    PLAYER_A("A"),
    /**
     * Spieler B
     */
    PLAYER_B("B"),
    /**
     * Spieler C
     */
    PLAYER_C("C"),
    /**
     * Spieler D
     */
    PLAYER_D("D");

    private final String identifier;

    /**
     * Erstellt eine neuen Spieler mit einer eindeutigen Kennung.
     * @param identifier eindeutige Kennung des Spielers
     */
    Players(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Gibt die Eindeutige Kennung des Spielers zurück.
     * @return Eindeutige Kennung des Spielers
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Erstellt eine Liste aller Spieler mit den verschiedenen Kennungen.
     * @return Liste aller Spieler mit den Kennungen
     */
    public static List<Player> toPlayerList() {
        List<Player> players = new ArrayList<>();
        for (Players player : values()) {
            players.add(new Player(player.getIdentifier()));
        }
        return players;
    }

    /**
     * Erstellt eine Liste aller Spieler-Enum-Objekte und gibt diese zurück.
     * @return Liste aller Spieler-Enum-Objekte
     */
    public static List<Players> toList() {
        return List.of(values());
    }
}
