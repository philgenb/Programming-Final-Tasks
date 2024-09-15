/*
 * Copyright (c) 2021, KASTEL. All rights reserved.
 */

package edu.kit.informatik.escaperoutenetwork.core;

/**
 * Diese Klasse beschreibt ein Ergebnis der Befehlsausführung.
 * @author Lucas Alber
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class Result {

    private final ResultType resultType;
    private final String message;

    /**
     * Erstellt eine neue Instanz eines Ergebnisses
     * @param resultType der Typ des Ergebnisses einer Befehlsausführung
     * @param message Nachricht des Ergebnisses
     */
    public Result(ResultType resultType, String message) {
        this.resultType = resultType;
        this.message = message;
    }

    /**
     * Gibt den Typ des Ergebnisses zurück.
     * @return Typ des Ergebnisses
     */
    public ResultType getType() {
        return this.resultType;
    }

    /**
     * Gibt die Nachricht des Ergebnisses zurück.
     * @return Nachricht des Ergebnisses
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Modelliert ein Typ einer Befehlsauführung
     */
    public enum ResultType {
        /**
         * Die Befehlsausführung war nicht erfolgreich.
         */
        FAILURE,

        /**
         * Die Befehlsausführung war erfolgreich.
         */
        SUCCESS
    }

}
