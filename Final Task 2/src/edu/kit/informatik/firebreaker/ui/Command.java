package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.core.Result;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;

/**
 * Modelliert einen ausführbaren Befehl.
 * Jeder Befehl, welcher vom Benutzer ausgeführt werden soll muss von dieser Abstraken Klasse erben.
 * @author Phil Gengenbach
 * @version 1.0
 */
public abstract class Command {

    private final int maxArgumentLength;

    /**
     * Erstellt eine neue Instanz eines Befehls.
     * @param maxArgumentLength Maximale Anzahl an Argumenten
     */
    protected Command(int maxArgumentLength) {
        this.maxArgumentLength = maxArgumentLength;
    }

    /**
     * Verarbeitet ein String-Array an Argumenten des Nutzers, welche über die Kommandozeile eingegeben werden.
     * Dabei wird die Eingabe auf Syntaktische und Semantische Fehler überprüft und
     * im Falle einer fehlerhaften Eingabe ein Fehler geworfen.
     * @param arguments Befehls-Argumente
     * @throws FireBreakerException falls die Eingabe des Nutzers fehlerhaft ist
     */
    public abstract void parseCommandLine(String[] arguments) throws FireBreakerException;

    /**
     * Führt den Befehl aus.
     * @return Ergebnis der Befehlsausführung
     */
    public abstract Result execute();

    /**
     * Gibt die maximal zulässige Anzahl an Argumenten zurück.
     * @return maximal zulässige Anzahl an Argumenten
     */
    public int getMaximumArgumentLength() {
        return maxArgumentLength;
    }
}
