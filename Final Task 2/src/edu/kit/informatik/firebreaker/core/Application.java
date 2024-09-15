/*
 * Copyright (c) 2021, KASTEL. All rights reserved.
 */

package edu.kit.informatik.firebreaker.core;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.gameboard.GameBoard;
import edu.kit.informatik.firebreaker.model.FireBreaker;
import edu.kit.informatik.firebreaker.ui.Session;

/**
 * Die Applikation. Erstellt die benötigten Spielinstanzen und startet die Befehlsverarbeitung.
 *
 * @author Phil Gengenbach
 * @author Lucas Alber
 * @version 1.0
 */
public final class Application {

    private static final String ARGUMENT_NUMBER_REGEX = "[0-9]*";
    private static final String FIELD_SIZE_REGEX = "[0-9]*,[0-9]*,";

    private static final String EMPTY_STRING = "";
    private static final String ARGUMENT_SPLITTER = ",";
    private static final int REQUIRED_ARGUMENT_LENGTH = 1;
    private static final int ARGUMENT_DELIMITER = -1;
    private static final int SIZE_ARGUMENT_COUNT = 2;
    private static final int MINIMUM_GAME_SIZE = 5;
    private static final int MINIMUM_INDEX = 0;


    /**
     * Privater Konstruktor, da Utillity Klasse.
     */
    private Application() {
        throw new IllegalStateException(ErrorMessages.ILLEGAL_STATE);
    }

    /**
     * Einstiegspunkt der Applikation. Erstellt eine neue Instanz eines Firebreaker Spiels
     * anhand der übergebenen Kommandozeilenparameter und startet die Befehlsverarbeitung.
     * @param args Kommandozeilenparameter, welche den Startzustand des Spielfeldes beschreiben.
     *             Die einzelnen Spielfelder sind dabei durch ein Komma separiert
     */
    public static void main(String[] args) {
        FireBreaker fireBreaker = buildGame(args);
        if (fireBreaker == null) {
            Terminal.printError(ErrorMessages.INVALID_INITAL_GAMESTATE);
            return;
        }
        Session session = new Session(fireBreaker, args);
        session.run();
    }

    /**
     * Erstellt eine neue Instanz eines Firebreaker Spiels unter verarbeitung eines
     * String Arrays.
     * Gibt {@code null} zurück um einen Fehler bei der Verarbeitung zu signalisieren.
     * @param args Kommandozeilenparameter
     * @return Instanz eines Firebreaker Spiels oder null falls Eingabe fehlerhaft
     */
    public static FireBreaker buildGame(final String[] args) {
        if (args.length != REQUIRED_ARGUMENT_LENGTH) {
            return null;
        }
        String argumentString = args[0];
        String[] commandLineInput = argumentString.split(ARGUMENT_SPLITTER, ARGUMENT_DELIMITER);

        if (commandLineInput.length <= SIZE_ARGUMENT_COUNT
                || !commandLineInput[0].matches(ARGUMENT_NUMBER_REGEX)
                || !commandLineInput[1].matches(ARGUMENT_NUMBER_REGEX)) {
            return null;
        }

        int rowSize;
        int columnSize;
        try {
            rowSize = Integer.parseInt(commandLineInput[0]);
            columnSize = Integer.parseInt(commandLineInput[1]);
        } catch (NumberFormatException exception) {
            return null;
        }
        argumentString = argumentString.replaceFirst(FIELD_SIZE_REGEX, EMPTY_STRING);
        String[] gameFieldInput = argumentString.split(ARGUMENT_SPLITTER);
        FireBreaker gameInstance = new FireBreaker(rowSize, columnSize);
        GameBoard gameBoard = gameInstance.getGameBoard();

        if (rowSize < MINIMUM_GAME_SIZE || columnSize < MINIMUM_GAME_SIZE
                || isEven(rowSize) || isEven(columnSize)) {
            return null;
        }
        int neededArgumentCount = rowSize * columnSize + SIZE_ARGUMENT_COUNT;
        if (commandLineInput.length != neededArgumentCount) {
            return null;
        }

        for (int i = MINIMUM_INDEX; i < rowSize; i++) {
            for (int j = MINIMUM_INDEX; j < columnSize; j++) {
                Position position = new Position(i, j);
                String gameCellString = gameFieldInput[i * columnSize + j];
                if (!gameBoard.placeGameObject(position, gameCellString)) {
                    return null;
                }
            }
        }

        if (!gameBoard.checkInitialGamefield()) {
            return null;
        }
        return gameInstance;
    }


    /**
     * Überprüft ob eine gegebene Zahl gerade ist.
     * @param number Zahl
     * @return ob Zahl gerade ist
     */
    private static boolean isEven(int number) {
        int divider = 2;
        int zeroRest = 0;
        return number % divider == zeroRest;
    }


}
