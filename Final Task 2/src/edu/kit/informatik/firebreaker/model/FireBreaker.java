package edu.kit.informatik.firebreaker.model;

import edu.kit.informatik.firebreaker.core.Position;
import edu.kit.informatik.firebreaker.dice.DiceResults;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.gameboard.FireFighter;
import edu.kit.informatik.firebreaker.gameboard.Forest;
import edu.kit.informatik.firebreaker.gameboard.GameBoard;
import edu.kit.informatik.firebreaker.gameboard.GameCell;
import edu.kit.informatik.firebreaker.status.InOutputStrings;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Modelliert eine Instanz eines Firebreaker Spiels.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class FireBreaker {

    private static final int MINIMUM_PLAYER_INDEX = 0;

    private final GameBoard gameBoard;

    private final List<Player> players;

    private Player playerOnTurn;
    private int playerOnTurnIndex;
    private int playerStartRoundIndex;
    private int turnCount;
    private boolean roundFinish;
    private boolean isGameFinish;

    /**
     * Erstellt eine neue Instanz eins Firebreaker Spiels.
     * @param boardRowSize Höhe des Spielbrettes
     * @param boardColumnSize Länge des Spielbrettes
     */
    public FireBreaker(int boardRowSize, int boardColumnSize) {
        this.gameBoard = new GameBoard(boardRowSize, boardColumnSize, this);
        this.players = Players.toPlayerList();
        this.playerOnTurn = players.get(MINIMUM_PLAYER_INDEX);
    }

    /**
     * Überprüft, ob das Spiel bereits fertig gespielt wurde.
     * Gibt {@code true} zurück, falls das Spiel bereits zuende ist.
     * @return ob das Spiel bereits zuende ist
     */
    public boolean isGameFinish() {
        return this.isGameFinish;
    }

    /**
     * Gibt das Spielbrett der aktuellen Spielinstanz zurück.
     * @return Spielbrett der Spielinstanz
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Gibt den Index eines gegebenen Spielers innerhalb der Spielerliste zurück.
     * @param player Spieler
     * @return Index des Spielers innerhalb der Liste
     */
    public int getPlayerIndex(Player player) {
        return this.players.indexOf(player);
    }

    /**
     * Gibt eine Liste aller Spieler als Kopie zurück.
     * @return Liste aller Spieler
     */
    public List<Player> getPlayers() {
        return List.copyOf(players);
    }

    /**
     * Gibt den Spieler, welcher am Zug ist zurück.
     * @return Spieler, der am Zug ist
     */
    public Player getPlayerOnTurn() {
        return this.playerOnTurn;
    }


    /**
     * Überprüft ob die aktuelle Runde beendet ist.
     * @return ob Runde beendet ist
     */
    public boolean isRoundFinish() {
        return roundFinish;
    }


    /**
     * Ermittelt einen Spieler anhand der eindeutigen Kennung des Spielers und gibt diesen zurück.
     * Ist kein Spieler mit der gegebenen ID vorhanden, so wird {@code null} zurückgegeben.
     * @param identifier ID des Spielers
     * @return Spieler mit gegebener ID
     */
    public Player getPlayerByIdentifier(String identifier) {
        for (Player player : players) {
            if (player.getIdentifier().equals(identifier)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Überprüft ob eine Feuerwehr von einer gegebenen Position auf eine gegebene Zielposition
     * bewegt werden kann.
     * Eine Feuerwehr, kann sich nur zu einer gegebenen Zielposition bewegen wenn die Folgenden
     * Anforderungen erfüllt sind.<br>
     * * Auf dem Spielfeld an der Zielposition kann eine Feuerwehr platziert werden <br>
     * * Es kann ein Pfad mit der Maximalen Länge von 2 zum Zielfeld gefunden werden, wobei
     *   nur bis zu leicht brennende Felder passiert werden dürfen <br>
     * * Die Feuerwehr hat noch keine andere Aktion ausgeführt und verfügt über genügend Aktionspunkte.
     * @param fireFighter Feuerwehr
     * @param toPosition Zielposition
     * @throws FireBreakerException falls die Zielposition zu dem die Feuerwehr bewegt werden soll
     * unzulässig ist
     */
    public void checkMove(final FireFighter fireFighter, final Position toPosition)
            throws FireBreakerException {
        GameCell fromGameCell = gameBoard.getGameCellByGameObject(fireFighter);
        GameCell toGameCell = gameBoard.getGameCellCopy(toPosition);
        if (!toGameCell.canPlaceOnTop() || toPosition.equals(fromGameCell.getPosition())) {
            throw new FireBreakerException(ErrorMessages.INVALID_MOVE_POSITION);
        }
        Forest forest = (Forest) toGameCell;
        if (forest.isBurning() || !gameBoard.canFindPath(fromGameCell, toGameCell)) {
            throw new FireBreakerException(ErrorMessages.INVALID_MOVE_POSITION);
        }
        if (fireFighter.hasPerformedAction() || !fireFighter.canPerformAction()) {
            throw new FireBreakerException(ErrorMessages.CAN_NOT_BE_MOVED);
        }
    }

    /**
     * Füllt den Wassertank einer Feuerwehr auf.
     * @param fireFighter Feuerwehr, deren Wassertank aufgefüllt werden soll
     * @return neuer Aktionspunktestand der Feuerwehr
     * @throws FireBreakerException falls Keine Wasserquelle in Reichweite der Feuerwehr ist
     */
    public int refillFireFighter(FireFighter fireFighter) throws FireBreakerException {
        GameCell gameCell = getGameBoard().getGameCellByGameObject(fireFighter);
        if (!getGameBoard().isPondNearBy(gameCell.getPosition())) {
            throw new FireBreakerException(ErrorMessages.NO_WATER_SOURCE_IN_RANGE);
        }
        return fireFighter.refill();
    }

    /**
     * Überpüft ob die Spieler das Spiel gewonnen haben. Wenn die Spieler das Spiel
     * gewonnen haben, so wird {@code true} zurückgegeben und der Spielstatus des Spiels
     * {@link #isGameFinish} wird auf {@code true} gesetzt.
     * Falls noch ein Spielfeld brennt, so haben die Spieler das Spiel noch nicht gewonnen
     * und es wird {@code false} zurückgegeben.
     * @return ob Spieler das Spiel gewonnen haben
     */
    public boolean checkWin() {
        if (gameBoard.getBurningFieldsCount() > MINIMUM_PLAYER_INDEX) {
            return false;
        }
        this.isGameFinish = true;
        return true;
    }


    /**
     * Erwirbt dem Spieler der aktuell am Zug ist eine neue Feuerwehr.
     * Diese kostet den Spieler 5 Reputationspunkte.
     * Die neu erworbene Feuerwehr wird auf dem Spielfeld mit der gegebenen Zielp+osition positioniert.
     * @param targetPosition Zielposition
     * @throws FireBreakerException falls die Position an dem die Feuerwehr platziert werden soll ungültig ist
     */
    public void buyFireFighter(Position targetPosition) throws FireBreakerException {
        getGameBoard().checkIfCanPlaceOn(targetPosition);
        FireFighter newFireFighter = getPlayerOnTurn().buyFireEngine();
        getGameBoard().placeObject(newFireFighter, targetPosition);
    }

    /**
     * Bewegt eine gegebene Feuerwehr von einer gegebenen Spielfeldposition zu einer weiteren gegebenen
     * Zielposition.
     * @param fireFighter Feuerwehr, welche bewegt werden soll
     * @param fromPosition Ausgangsposition
     * @param toPosition Zielposition
     * @throws FireBreakerException falls eine der beiden Positionen ungültig ist, oder kein Spielobjekt auf
     * der Zielzelle platziert werden kann.
     */
    public void move(FireFighter fireFighter, Position fromPosition, Position toPosition) throws FireBreakerException {
        getGameBoard().validatePosition(fromPosition);
        getGameBoard().validatePosition(toPosition);
        getGameBoard().placeGameObject(fireFighter, toPosition);
        getGameBoard().takeGameObject(fireFighter, fromPosition);
        fireFighter.removeActionPoint();
    }

    /**
     * Gibt eine textuelle Repräsentation eines Spielers zurück.
     * Die Repräsentation ist mehrzeilig. In der ersten Zeile werden der Spielerbezeichner sowie
     * die Reputationspunkte ausgegeben. In den darauffolgenden Zeilen werden Eigenschaften seiner
     * im Besitz befindlichen Feuerwehren angezeigt. Neben dem Feuerwehrbezeichner, die aufsteigend
     * sortiert nach der Nummer ausgegeben werden, werden jeweils die Anzahl der Wassersteine, die Aktionspunkte
     * und die Zeilen- und Spaltennummer ausgegeben auf denen sich die jeweilige Feuerwehr befindet.
     * @param player Spieler
     * @return textuelle Repräsentation der Spielerinformationen
     */
    public String getPlayerStats(Player player) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(InOutputStrings.SHOWPLAYER_OUTPUT,
                player.getIdentifier(), player.getReputationPoints()));
        if (!player.getFireFighters().isEmpty()) {
            for (FireFighter fireFighter : player.getFireFighters()) {
                Position fireFighterPosition = getGameBoard().getGameCellByGameObject(fireFighter).getPosition();
                builder.append(System.lineSeparator());
                builder.append(fireFighter.getStats());
                builder.append(String.format(InOutputStrings.POSITION_OUTPUT,
                        fireFighterPosition.getPositionY(), fireFighterPosition.getPositionX()));
            }
        }
        return builder.toString();
    }

    /**
     * Gibt die Anzahl an Spielern, die aktuell noch am Spiel teilnehmen.
     * @return Anzahl an Spielern, die im Spiel noch teilnehmen
     */
    public int getPlayersInGameCount() {
        return getPlayersInGameList().size();
    }

    /**
     * Gibt eine Liste aller Spieler zurück, welcher noch am Spiel teilnehmen
     * @return Liste aller Spieler, welche noch am Spiel teilnehmen.
     */
    private List<Player> getPlayersInGameList() {
        return getPlayers().stream()
                .filter(Player::inGame)
                .collect(Collectors.toList());
    }

    /**
     * Beendet den Zug des aktuellen Spielers.
     * Überspringt Spieler, welche bereits aus dem Spiel ausgeschieden sind.
     * Am Ende einer Runde wird die Reihenfolge der Spieler mindestens einmal und dann
     * solange nach links rotiert, bis der erste Spieler in der Reihe kein ausgeschiedener Spieler ist.
     */
    public void finishTurn() {
        this.turnCount++;
        if (turnCount == getPlayersInGameCount()) {
            rotateRoundStartPlayer();
            this.playerOnTurn = players.get(playerStartRoundIndex);
            this.playerOnTurnIndex = playerStartRoundIndex;
            this.roundFinish = true;
            this.turnCount = MINIMUM_PLAYER_INDEX;
        } else {
            rotatePlayerOrder();
        }
        initializeNewPlayerOnTurn();
    }

    private void rotateRoundStartPlayer() {
        incrementPlayerStartRoundIndex();
        while (!players.get(playerStartRoundIndex).inGame() && getPlayersInGameCount() != MINIMUM_PLAYER_INDEX) {
            incrementPlayerStartRoundIndex();
        }
    }

    private void rotatePlayerOrder() {
        incrementPlayerOnTurn();
        while (!playerOnTurn.inGame() && getPlayersInGameCount() != MINIMUM_PLAYER_INDEX) {
            incrementPlayerOnTurn();
        }
    }

    private void incrementPlayerStartRoundIndex() {
        this.playerStartRoundIndex++;
        if (playerStartRoundIndex >= this.players.size()) {
            this.playerStartRoundIndex = MINIMUM_PLAYER_INDEX;
        }
    }

    private void incrementPlayerOnTurn() {
        this.playerOnTurnIndex++;
        if (playerOnTurnIndex >= this.players.size()) {
            this.playerOnTurnIndex = MINIMUM_PLAYER_INDEX;
        }
        this.playerOnTurn = players.get(playerOnTurnIndex);
    }

    /**
     * Zu Beginn des Spielzuges eines Spielers erhält jede seiner Feuerwehren 3 Aktionspunkte,
     * welche bestimmen wie viele Bewegungen und Aktionenen jede seiner Feuerwehren maximal ausführen darf.
     */
    private void initializeNewPlayerOnTurn() {
        if (playerOnTurn != null && !playerOnTurn.getFireFighters().isEmpty()) {
            for (FireFighter fireFighter : playerOnTurn.getFireFighters()) {
                fireFighter.reset();
            }
        }
    }

    /**
     * Beendet die aktuelle Runde und startet die nächste Runde.
     * Dazu wird das Ergebnis des Würfels ausgewertet und das Feuer je nach Ergebnis
     * ausgebreitet.
     * Im Anschluss werden alle Spielobjekte von stark brennenden Felder entfernt.
     * Spieler, welche keine Feuerwehren mehr haben werden aus dem Spiel entfernt.
     * Gibt den Spieler der jetzt am Zug ist zurück, falls ein Spieler druch die Feuerausbreitung
     * ausgeschieden ist.
     * @param diceResult Würfelaktion des Würfelergebnisses
     * @return Spieler der am Zug ist
     */
    public Player startNextRound(DiceResults diceResult) {
        int oldPlayerAliveCount = getPlayersInGameCount();
        this.roundFinish = false;
        if (diceResult != null) {
            getGameBoard().spreadFireInDirection(diceResult.getSpreadDirection());
        }
        getGameBoard().clearStronglyBurningCells();

        if (checkLose()) {
            this.isGameFinish = true;
            return null;
        }

        if (!playerOnTurn.inGame())  {
            rotatePlayerOrder();
        }

        if (getPlayersInGameCount() < oldPlayerAliveCount) {
            return playerOnTurn;
        }
        return null;
    }

    /**
     * Überprüft, ob die Spieler das Spiel verloren haben, also ob alle Spieler ausgeschieden sind.
     * Gibt {@code true} zurück, falls die Spieler verloren haben und {@code false}, falls nicht.
     * @return ob die Spieler das Spiel verloren haben
     */
    public boolean checkLose() {
        return getPlayersInGameCount() == MINIMUM_PLAYER_INDEX;
    }

}
