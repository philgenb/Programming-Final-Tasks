package edu.kit.informatik.firebreaker.exception;

/**
 * Modelliert einen auftretenden Fehler im FireBreaker Spiel.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class FireBreakerException extends Exception {

    /**
     * Erstellt eine neue Spiellogik Fehlermeldung.
     * @param message Fehlernachricht
     */
    public FireBreakerException(String message) {
        super(message);
    }
}
