package edu.kit.informatik.escaperoutenetwork.exception;

/**
 * Modelliert eine Exception, welche bei einer falschen Eingabe geworfen wird.
 * Diese Exception soll und muss gefangen werden und dem Nutzer eine Fehlermeldung ausgegeben werden,
 * ohne das Programm zu beenden.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class EscapeNetworkInputException extends EscapeNetworkException {

    /**
     * Erstellt eine neue Fluchtwegenetzeingabeexception mit einer gegebenen Fehlermeldung.
     * @param message Nachricht
     */
    public EscapeNetworkInputException(final String message) {
        super(message);
    }

}
