package edu.kit.informatik.firebreaker.exception;

/**
 * Modelliert einen Fehler, der bei einer fehlerhaften Befehlsauführung des Benutzers auftritt.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class FireBreakerInputException extends FireBreakerException {

    /**
     * Erstellt einen neue Eingabe Fehlermeldung beim verarbeiten von Befehlen.
     * @param message Fehlernachricht
     */
    public FireBreakerInputException(String message) {
        super(message);
    }
}
