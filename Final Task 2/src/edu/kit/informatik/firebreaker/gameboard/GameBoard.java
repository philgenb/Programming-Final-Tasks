package edu.kit.informatik.firebreaker.gameboard;

import edu.kit.informatik.firebreaker.core.Position;
import edu.kit.informatik.firebreaker.dice.SpreadDirection;
import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.model.FireBreaker;
import edu.kit.informatik.firebreaker.model.Player;
import edu.kit.informatik.firebreaker.model.Players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Diese Klasse beschreibt ein Spielbrett für ein Firebreaker Spiel.
 * @author Phil Gengenbach
 * @version 1.0
 */
public class GameBoard {

    private static final String NUMBER_REGEX = "[0-9]";
    private static final String STRING_SEPERATOR = ",";
    private static final String EMPTY_REPLACEMENT = "";
    private static final int MINIMUM_INDEX = 0;
    private static final int MINIMUM_SIZE = 1;
    private static final int HALVING_DIVIDER = 2;
    private static final int MINIMUM_BURNING_FIELD_COUNT = 1;
    private static final int MAXIMUM_MOVE_RANGE = 2;

    private final int boardRowSize;
    private final int boardColumnSize;

    private final FireBreaker fireBreaker;
    private final GameCell[][] gameField;

    /**
     * Erstellt eine neue Instanz eines Spielbrettes.
     * @param boardRowSize Höhe des Spielbrettes
     * @param boardColumnSize Länge des Spielbrettes
     * @param fireBreaker Instanz des Firebreaker Spiels
     */
    public GameBoard(int boardRowSize, int boardColumnSize, FireBreaker fireBreaker) {
        this.gameField = new GameCell[boardRowSize][boardColumnSize];
        this.boardRowSize = boardRowSize;
        this.boardColumnSize = boardColumnSize;
        this.fireBreaker = fireBreaker;
    }

    /**
     * Gibt die Höhe des Spielbrettes zurück.
     * @return Höhe des Spielbrettes
     */
    public int getBoardRowSize() {
        return boardRowSize;
    }

    /**
     * Gibt die Länge des Spielbrettes zurück
     * @return Länge des Spielbrettes
     */
    public int getBoardColumnSize() {
        return boardColumnSize;
    }

    private int getMaximumColumnIndex() {
        return getBoardColumnSize() - MINIMUM_SIZE;
    }

    private int getMaximumRowIndex() {
        return getBoardRowSize() - MINIMUM_SIZE;
    }

    /**
     * Überprüft ob eine Wasserzufuhr in Reichweite einer gegebenen Feuerwehrposition auf dem Spielfeld ist.
     * Gibt {@code true} zurück, falls dies der Fall ist und {@code false} falls nicht.
     * @param fireFighterPosition Position der Feuerwehr
     * @return Wahrheitswert, ob Wasserzufuhr in Reichweite der Feuerwehr ist
     */
    public boolean isPondNearBy(Position fireFighterPosition) {
        return getGameCellList().stream().
                anyMatch(cell -> cell.canRefill() && fireFighterPosition.borders(cell.getPosition()));
    }

    /**
     * Gibt eine Liste aller Positionen von Feuerwehrstationen auf dem {@link GameBoard} zurück.
     * Die Positionen der Feuerwehrstationen sind innerhalb der Liste an Positionen
     * in der auf dem Spielfeld auftauchenden Reihenfolge sortiert, wenn man das Spielfeld
     * Zeilenweise von Oben nach unten liest.
     * @return Liste aller Positionen von Feuerwehrstationen auf dem Spielbrett
     */
    private List<Position> getFireStationPositions() {
        return List.of(
                new Position(MINIMUM_INDEX, MINIMUM_INDEX),
                new Position(getMaximumRowIndex(), getMaximumColumnIndex()),
                new Position(getMaximumRowIndex(), MINIMUM_INDEX),
                new Position(MINIMUM_INDEX, getMaximumColumnIndex())
        );
    }

    /**
     * Überprüft, ob die gegebenen Position an einer Feuerwehrstation angrenzt.
     * In diesem Fall wird {@code true} zurückgegeben.
     * @param targetPosition Position des Spielfeldes
     * @return ob die Position an einer Feuerwehrstation angrenzt
     */
    public boolean doesPositionBordersFireStation(Position targetPosition) {
        return getFireStationPositions().stream()
                .anyMatch(position -> position.borders(targetPosition));
    }

    /**
     * Überprüft ob die Position sich innerhalb des Spielbrettes befindet und gibt
     * das an der Position befindliche Spielfeld zurück.
     * @param position Position des Spielfeldes
     * @return Spielfeld an der gegebenen Position
     * @throws FireBreakerException falls die Position ungültig ist
     */
    private GameCell validateAndGetGameCell(final Position position) throws FireBreakerException {
        validatePosition(position);
        return getGameCell(position);
    }

    /**
     * Gibt eine Kopie eines Spielfeldes an einer gegebenen Position zurück
     * @param position Position des Spielfeldes
     * @return Kopie eines Spielfeldes an der gegebenen Position
     * @throws FireBreakerException falls die Position ungültig ist
     */
    public GameCell getGameCellCopy(final Position position) throws FireBreakerException {
        return validateAndGetGameCell(position).copy();
    }

    /**
     * Platziert ein Spielobjekt auf einem Spielfeld an der gegebenen Position auf dem Spielbrett.
     * @param gameObject Spielobjekt
     * @param targetPosition Zielposition an dem das Objekt platziert werden soll
     * @throws FireBreakerException falls kein Spielobjekt an der gegebenen Position platziert werden kann
     */
    public void placeObject(GameObject gameObject, Position targetPosition) throws FireBreakerException {
        GameCell targetGameCell = validateAndGetGameCell(targetPosition);
        if (!doesPositionBordersFireStation(targetPosition)) {
            throw new FireBreakerException(ErrorMessages.NO_FIRESTATION_IN_RANGE);
        }
        if (!targetGameCell.canPlaceOnTop() || targetGameCell.isBurning()) {
            throw new FireBreakerException(ErrorMessages.INVALID_PLACE_POSITION);
        }
        targetGameCell.addCellObject(gameObject);
    }

    /**
     * Überprüft, ob an der gegebenen Position ein Spielobjekt platziert werden kann.
     * @param targetPosition Zielposition
     * @throws FireBreakerException falls kein Spielobjekt an der gegebenen Position
     * platziert werden kann
     */
    public void checkIfCanPlaceOn(Position targetPosition) throws FireBreakerException {
        GameCell targetGameCell = validateAndGetGameCell(targetPosition);
        if (!doesPositionBordersFireStation(targetPosition)) {
            throw new FireBreakerException(ErrorMessages.NO_FIRESTATION_IN_RANGE);
        }
        if (!targetGameCell.canPlaceOnTop() || targetGameCell.isBurning()) {
            throw new FireBreakerException(ErrorMessages.INVALID_PLACE_POSITION);
        }
    }

    /**
     * Löscht ein Spielfeld an einer gegebenen Position mit einer gegebenen Feuerwehr.
     * @param fireFighter Feuerwehr
     * @param targetPosition Position des zu löschenden Spielfeldes
     * @throws FireBreakerException falls die Zielposition ungültig ist oder das Spielfeld auf dem
     * die Feuerwehr positioniert ist nicht direkt an eine Feuerwehrstation angrenzt.
     */
    public void extinguish(FireFighter fireFighter, Position targetPosition)
            throws FireBreakerException {
        GameCell startingCell = getGameCellByGameObject(fireFighter);
        GameCell targetGameCell = validateAndGetGameCell(targetPosition);
        if (!startingCell.getPosition().bordersDirectly(targetPosition)) {
            throw new FireBreakerException(ErrorMessages.NOT_IN_RANGE);
        }
        if (!targetGameCell.canBeExtinguished()) {
            throw new FireBreakerException(ErrorMessages.CAN_NOT_BE_EXTINGUISHED);
        }
        fireFighter.extinguish(targetGameCell);
        if (targetGameCell.extinguish()) {
            fireFighter.getOwningPlayer().addReputationPoint();
        }
    }

    /**
     * Ermittelt eine Nachbarzelle in gegebener Richtung von einer gegebenen Startzelle aus.
     * @param gameCell Startzelle
     * @param spreadDirection Richtung von der Startzelle aus
     * @return Nachbarzelle in gegebener Richtung
     */
    private GameCell getGameCellInDirection(GameCell gameCell, SpreadDirection spreadDirection) {
        Position gameCellPosition = gameCell.getPosition();
        return getGameCell(Position.getPositionInDirection(gameCellPosition, spreadDirection));
    }

    /**
     * Ermittelt eine Spielzelle auf dem ein gegebenes Objekt positioniert ist und gibt eine Kopie dieser zurück.
     * Gibt {@code null} zurück, falls keine Spielzelle mit dem gegebenen Objekt gefunden wurde.
     * @param gameObject Objekt auf einem Spielfeld
     * @return Spielzelle auf dem gegebenes Objekt positioniert ist
     */
    public GameCell getGameCellByGameObject(final GameObject gameObject) {
        GameCell gameCell = getGameCellList().stream()
                .filter(cell -> cell.containsCellObject(gameObject))
                .findFirst().orElse(null);
        if (gameCell == null) {
            return null;
        }
        return gameCell.copy();
    }

    /**
     * Ermittelt eine Spielzelle an einer gegebenen Position und gibt diese zurück.
     * @param position Position der Spielzelle
     * @return Spielzelle an der gegebenen Position
     */
    private GameCell getGameCell(Position position) {
        return this.gameField[position.getPositionY()][position.getPositionX()];
    }

    private void setGameCell(Position position, GameCell gameCell) {
        this.gameField[position.getPositionY()][position.getPositionX()] = gameCell;
    }

    /**
     * Breitet das Feuer in eine gegebene Himmelsrichtung auf dem {@link GameBoard} aus.
     * Dabei springt von jedem stark brennenden Feld Feuer auf alle Nachbarfelder in die gegebene
     * Himmelsrichtung. Zusätzlich wird jedes leicht brennende Feld zu einem stark brennenden Feld.
     * @param spreadDirection Ausbreitungsrichtung des Feuers
     */
    public void spreadFireInDirection(SpreadDirection spreadDirection) {
        if (spreadDirection == SpreadDirection.ALL_DIRECTIONS) {
            List<GameCell> spreadFireCells = getCellsInState(FieldState.LIGHTLY_BURNING);
            for (GameCell gameCell : getGameCellList()) {
                for (GameCell stronglyBurningCell : getCellsInState(FieldState.STRONGLY_BURNING)) {
                    if (gameCell.getPosition().bordersDirectly(stronglyBurningCell.getPosition())
                            && !spreadFireCells.contains(gameCell)) {
                        spreadFireCells.add(gameCell);
                    }
                }
            }
            spreadFireCells.forEach(GameCell::spreadFire);
            return;
        }
        List<GameCell> lightFireGameCells = getCellsInState(FieldState.LIGHTLY_BURNING);
        for (GameCell gameCell : getCellsInState(FieldState.STRONGLY_BURNING)) {
            if (isPositionValid(Position.getPositionInDirection(gameCell.getPosition(), spreadDirection))) {
                GameCell neighbourCell = getGameCellInDirection(gameCell, spreadDirection);
                neighbourCell.spreadFire();
            }
        }
        lightFireGameCells.forEach(GameCell::doubleFire);
    }

    /**
     * Entfernt ein gegebenes Spielobjekt von dem Spielfeld an der gegebenen Position.
     * @param takePosition Position des Spielfeldes
     * @param gameObjectToRemove Objekt das vom Spielbrett entfernt werden soll
     * @throws FireBreakerException falls die Position ungültig ist, oder
     */
    public void takeGameObject(GameObject gameObjectToRemove, Position takePosition) throws FireBreakerException {
        GameCell gameCell = validateAndGetGameCell(takePosition);
        gameCell.removeCellObject(gameObjectToRemove);
    }

    private List<GameCell> getNeighbourCells(GameCell gameCell) {
        Position fromPosition = gameCell.getPosition();
        return getGameCellList().stream()
                .filter(cell -> fromPosition.bordersDirectly(cell.getPosition()))
                .collect(Collectors.toList());
    }

    /**
     * Findet den kürzesten Pfad von einer gegeben Start- zu einer gegebenen Zielposition und
     * überprüft ob dieser kleiner ist, als der maximale Bewegungsradius einer Feuerwehr.
     * Dazu wird eine Breitensuche durchgeführt um den kürzesten Pfad zu finden. Jedes Spielfeld
     * darf nur direkt durch ein angrenzendes Spielfeld erreicht werden ohne Diagonalen zu verwenden.
     * @param fromCell Startposition
     * @param toCell Zielposition
     * @return ob ein Pfad der Länge kleiner als zwei gefunden wurde
     */
    public boolean canFindPath(final GameCell fromCell, final GameCell toCell) {
        Queue<GameCell> queue = new LinkedList<>();
        List<GameCell> visitedCells = new ArrayList<>();
        Map<GameCell, GameCell> cellVisitedBy = new HashMap<>();
        visitedCells.add(fromCell);
        queue.add(fromCell);

        while (!queue.isEmpty()) {
            GameCell currentQueueElement = queue.remove();
            if (currentQueueElement.equals(toCell)) {
                int pathLength = MINIMUM_INDEX;
                GameCell cellFoundByPreviousCell = toCell;
                while (!cellFoundByPreviousCell.equals(fromCell)) {
                    GameCell previousGameCell = cellVisitedBy.get(cellFoundByPreviousCell);
                    pathLength++;
                    cellFoundByPreviousCell = previousGameCell;
                }
                return pathLength <= MAXIMUM_MOVE_RANGE;
            }
            for (GameCell gameCell : getNeighbourCells(currentQueueElement)) {
                if (!visitedCells.contains(gameCell) && gameCell.canBePassed()) {
                    queue.add(gameCell);
                    visitedCells.add(gameCell);
                    cellVisitedBy.put(gameCell, currentQueueElement);
                }
            }
        }
        return false;
    }

    /**
     * Platziert ein Spielobjekt auf dem Spielfeld, anhand einer textuellen Repräsentation.
     * Falls die Platzierung fehlerhaft war, so wird {@code false} zurückgegeben.
     * @param position Position
     * @param cellRepresentation textuelle Spielfeldrepräsentation
     * @return Wahrheitswert, ob Platzierung erfolgreich war
     */
    public boolean placeGameObject(Position position, String cellRepresentation) {
        if (FieldState.getForestState(cellRepresentation) != null) {
            Forest forest = new Forest(position, FieldState.getForestState(cellRepresentation));
            setGameCell(position, forest);
            return true;
        }
        if (cellRepresentation.equals(ExtinguishingPond.POND_REPRESENTATION) && isValidPondPosition(position)) {
            setGameCell(position, new ExtinguishingPond(position));
            return true;
        }
        if (cellRepresentation.matches(FireStation.FIRESTATION_REGEX)) {
            setGameCell(position, new FireStation(position, cellRepresentation));
            return true;
        }
        if (cellRepresentation.matches(FireFighter.INITAL_FIREFIGHTER_REGEX)) {
            setGameCell(position, new Forest(position, FieldState.DRY));
            String owningPlayerRepresentation = cellRepresentation.replaceAll(NUMBER_REGEX, EMPTY_REPLACEMENT);
            GameCell gameCell = getGameCell(position);
            Player owningPlayer = fireBreaker.getPlayerByIdentifier(owningPlayerRepresentation);
            if (!position.bordersAcross(getFireStationPositions().get(fireBreaker.getPlayerIndex(owningPlayer)))) {
                return false;
            }
            gameCell.addCellObject(new FireFighter(MINIMUM_INDEX, owningPlayer));
            owningPlayer.addFireFighter();
            return true;
        }
        return false;
    }

    /**
     * Überprüft, ob das Spielefeld die benötigten Anforderungen an ein gültiges Startspielbrett
     * erfüllt. Bei einem gültigen Startspielfeld müssen die Folgenden Bedingungen erfüllt sein:<br>
     * * Mindestens ein leicht brennendes und ein stark brennendes Spielfeld
     *   muss es auf dem {@link GameBoard} geben.<br>
     * * Feuerwehrstationen müssen korrekt platziert sein<br>
     * * Feuerwehren müssen korrekt platziert sein
     * @return ob es mindestens ein leicht und stark brennendes Spielfeld auf dem Spielbrett gibt
     */
    public boolean checkInitialGamefield() {
        return getFieldCountInCondition(FieldState.STRONGLY_BURNING) >= MINIMUM_BURNING_FIELD_COUNT
                && getFieldCountInCondition(FieldState.LIGHTLY_BURNING) >= MINIMUM_BURNING_FIELD_COUNT
                && areFireFightersPlacedCorrectly()
                && areFireStationsPlacedCorrectly();
    }

    private boolean isValidPondPosition(Position pondPosition) {
        return (pondPosition.getPositionY() == MINIMUM_INDEX
                    && pondPosition.getPositionX() == getMaximumColumnIndex() / HALVING_DIVIDER)
                || (pondPosition.getPositionX() == MINIMUM_INDEX
                    && pondPosition.getPositionY() == getMaximumRowIndex() / HALVING_DIVIDER)
                || (pondPosition.getPositionX() == getMaximumColumnIndex()
                    && pondPosition.getPositionY() == getMaximumRowIndex() / HALVING_DIVIDER)
                || (pondPosition.getPositionY() == getMaximumRowIndex()
                    && pondPosition.getPositionX() == getMaximumColumnIndex() / HALVING_DIVIDER);
    }

    private List<GameCell> getGameCellList() {
        return Arrays.stream(this.gameField)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    /**
     * Ermittelt die Anzahl an brennenden Feldern auf dem {@link GameBoard},
     * und gibt diese zurück.
     * @return Anzahl an brennenden Spielfeldern
     */
    public int getBurningFieldsCount() {
        return getFieldCountInCondition(FieldState.STRONGLY_BURNING)
                + getFieldCountInCondition(FieldState.LIGHTLY_BURNING);
    }

    private List<GameCell> getGameCellListWithGameObjects() {
        return getGameCellList().stream()
                .filter(cell -> !cell.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Entfernt alle Spielobjekte, aller Spielfelder, die stark brennen.
     */
    public void clearStronglyBurningCells() {
        getCellsInState(FieldState.STRONGLY_BURNING).forEach(GameCell::clearCell);
    }

    private boolean areFireFightersPlacedCorrectly() {
        return getGameCellListWithGameObjects().size() == fireBreaker.getPlayers().size();
    }

    private boolean areFireStationsPlacedCorrectly() {
        for (int i = MINIMUM_INDEX; i < getFireStationPositions().size(); i++) {
            GameCell fireStationCell = getGameCell(getFireStationPositions().get(i));
            Players playerEnum = Players.toList().get(i);
            if (!fireStationCell.toString().equals(playerEnum.getIdentifier())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Platziert eine neues Spielobjekt auf dem {@link GameBoard}.
     * @param cellObject Spielobjekt
     * @param toPosition Position auf dem das Spielobjekt platziert werden soll
     * @throws FireBreakerException falls keine gültige Spielzelle auf dem Spielbrett
     * mit der gegebenen Position existiert
     */
    public void placeGameObject(GameObject cellObject, Position toPosition) throws FireBreakerException {
        GameCell gamecell = validateAndGetGameCell(toPosition);
        if (!gamecell.canPlaceOnTop()) {
            throw new FireBreakerException(ErrorMessages.INVALID_PLACE_POSITION);
        }
        gamecell.addCellObject(cellObject);
    }

    /**
     * Überprüft ob eine gegebene Position sich innerhalb des Spielbrettes befindet.
     * @param position Position
     * @throws FireBreakerException falls Position sich nicht auf dem Spielbrett befindet
     */
    public void validatePosition(Position position) throws FireBreakerException {
        if (!isPositionValid(position)) {
            throw new FireBreakerException(ErrorMessages.POSITION_OUT_OF_GAMEFIELD);
        }
    }

    private boolean isPositionValid(Position position) {
        return position.getPositionX() <= getMaximumColumnIndex() && position.getPositionY() <= getMaximumRowIndex()
                && position.getPositionX() >= MINIMUM_INDEX && position.getPositionY() >= MINIMUM_INDEX;
    }

    private int getFieldCountInCondition(FieldState fieldState) {
        return (int) getGameCellList().stream()
                .filter(cell -> cell.getFieldState() == fieldState)
                .count();
    }

    private List<GameCell> getCellsInState(FieldState fieldState) {
        return getGameCellList().stream()
                .filter(cell -> cell.getFieldState() == fieldState)
                .collect(Collectors.toList());
    }

    /**
     * Gibt eine textuelle Repräsentation des Spielfelders zurück, wobei nur
     * der Zustand von Wälder repräsentiert wird und alle anderen Felder durch ein
     * x ersetzt werden.
     * @return textuelle Repräsentation des Spielfelders
     */
    public String getBoardRepresentation() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getBoardRowSize(); i++) {
            for (int j = 0; j < getBoardColumnSize(); j++) {
                GameCell gameCell = gameField[i][j];
                builder.append(gameCell.getFieldRepresentation());
                if (j < getMaximumColumnIndex()) {
                    builder.append(STRING_SEPERATOR);
                }
            }
            if (i < getMaximumRowIndex()) {
                builder.append(System.lineSeparator());
            }
        }
        return builder.toString();
    }
}
