package edu.kit.informatik.escaperoutenetwork.exception;

/**
 * Wird geworfen, wenn ein Fehler im Programm auftritt. Diese Exception sollte gefangen werden
 * und dem Nutzer eine Fehlermeldung ausgegeben werden ohne, dass das Programm terminiert.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class EscapeNetworkException extends Exception {

    /**
     * Erstellt eine neue Fluchtwegenetzeexception mit einer gegebenen Fehlernachricht.
     * @param message Ursache der Exception
     */
    public EscapeNetworkException(final String message) {
        super(message);
    }

}
