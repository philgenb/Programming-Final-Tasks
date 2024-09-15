package edu.kit.informatik.escaperoutenetwork;

import edu.kit.informatik.escaperoutenetwork.exception.ErrorMessages;
import edu.kit.informatik.escaperoutenetwork.userinterface.Session;

/**
 * Einstiegspunkt des Programmes.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class Application {


    /**
     * Privater Konstruktor, da Utillity Klasse
     */
    private Application() {
        throw new IllegalStateException(ErrorMessages.ILLEGAL_STATE.toString());
    }

    /**
     * Einstiegspunkt des Programmes. Instanziert die Benutzerinteraktion.
     * Sämtliche Kommandozeilenparameter werden ignoriert.
     * @param args Komandozeilenparameter
     */
    public static void main(String[] args) {
        Session session = new Session();
        session.run();
    }

}
