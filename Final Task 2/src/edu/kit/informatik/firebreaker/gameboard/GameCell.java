package edu.kit.informatik.firebreaker.gameboard;

import edu.kit.informatik.firebreaker.core.GameObjectComperator;
import edu.kit.informatik.firebreaker.core.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Modelliert ein Spielfeld auf dem {@link GameBoard}.
 * Jedes Spielfeld hat einen eindeutigen {@link FieldState}.
 * @author Phil Gengenbach
 * @version 1.0
 */
public abstract class GameCell {

    private static final int MINIMUM_INDEX = 0;
    private static final int MINIMUM_SIZE = 1;
    private static final String COMMA_SEPERATOR = ",";

    private final Position gameCellPosition;
    private final List<GameObject> cellObjects;
    private FieldState fieldState;

    /**
     * Erstellt eine neue Instanz eines Spielfeldes.
     * Jedes Spielfeld hat eine eindeutige Position innerhalb des {@link GameBoard} und einen eindeutigen Zustand.
     * @param gameCellPosition eindeutige Position des Spielfeldes auf dem Spielbrett
     * @param fieldState Zustand des Spielfeldes
     */
    public GameCell(Position gameCellPosition, FieldState fieldState) {
        this.cellObjects = new ArrayList<>();
        this.gameCellPosition = gameCellPosition;
        this.fieldState = fieldState;
    }

    /**
     * Gibt den aktuellen Zustand des Spielfeldes zurück.
     * @return aktueller Zustand des Spielfeldes
     */
    public FieldState getFieldState() {
        return this.fieldState;
    }

    /**
     * Setzt den Zustand des Spielfeldes auf einen gegebenen Zustand.
     * @param fieldState neuer Zustand des Spielfeldes
     */
    public void setFieldState(FieldState fieldState) {
        this.fieldState = fieldState;
    }

    /**
     * Überprüft ob das Spielfeld passiert werden kann.
     * Ein Spielfeld kann passiert werden, wenn es nicht räumlich robust ist und auch nicht
     * stark brennt.
     * @return ob Spielfeld passiert werden kann
     */
    public boolean canBePassed() {
        return getFieldState() != FieldState.SOLID && getFieldState() != FieldState.STRONGLY_BURNING;
    }

    /**
     * Überprüft ob ein Spielfeld sich in einem Zustand befindet, sodass das Spielfeld gelöscht werden kann.
     * @return ob Spielfeld gelöscht werden kann
     */
    public boolean canBeExtinguished() {
        return getFieldState() == FieldState.DRY || isBurning();
    }

    /**
     * Ermittelt ob das Spielfeld leicht oder stark brennt.
     * @return ob Spielfeld brennt
     */
    public boolean isBurning() {
        return getFieldState() == FieldState.LIGHTLY_BURNING || getFieldState() == FieldState.STRONGLY_BURNING;
    }

    /**
     * Gibt die Position des Spielfelds zurück.
     * @return Position des Spielfeldes
     */
    public Position getPosition() {
        return gameCellPosition;
    }

    /**
     * Entfernt alle auf dem Spielfeld stehenden Spielobjekte.
     */
    public void clearCell() {
        for (GameObject gameObject : cellObjects) {
            gameObject.getOwningPlayer().removeGameObject(gameObject);
        }
        this.cellObjects.clear();
    }

    /**
     * Gibt eine Liste aller Spielobjekte zurück, welche sich auf dem Spielfeld befinden.
     * @return Liste aller Spielobjekte auf dem Spielfeld
     */
    public List<GameObject> getCellObjects() {
        return List.copyOf(this.cellObjects);
    }

    /**
     * Fügt eine Kollektion an Spielobjekten dem Spielfeld hinzu.
     * @param gameObjects Kollektion an Spielobjekten
     */
    public void addCellObjects(Collection<GameObject> gameObjects) {
        this.cellObjects.addAll(gameObjects);
    }

    /**
     * Überprüft ob ein direkt angrenzendes Spielobjekt an diesem Spielfeld auftanken kann.
     * @return ob direkt angrenzendes Spielboejtk an diesem Spiefleld auftanken kann
     */
    public boolean canRefill() {
        return getFieldState() == FieldState.SOLID;
    }

    /**
     * Überprüft ob ein Spielobjekt auf dem Spielfeld platziert werden kann.
     * @return ob Spielobjekt auf dem Spielfeld platziert werden kann.
     */
    public boolean canPlaceOnTop() {
        return true;
    }

    /**
     * Löscht ein brennendes Spielfeld.
     * Ein stark brennendes Spielfeld wird zu einem leicht brennenden Feld.
     * Ein leicht brennendes Spielfeld wird zu einem nassen Feld.
     * Ein trockenes Feld wird zu einem nassen Feld.
     * Gibt {@code true} zurück, falls ein brennendes Feuer gelöscht wurde und
     * {@code false} falls nicht.
     * @return ob ein Feuer gelöscht wurde
     */
    public boolean extinguish() {
        switch (getFieldState()) {
            case STRONGLY_BURNING:
                setFieldState(FieldState.LIGHTLY_BURNING);
                return true;
            case LIGHTLY_BURNING:
                setFieldState(FieldState.WET);
                return true;
            case DRY:
                setFieldState(FieldState.WET);
                return false;
            default:
                return false;
        }
    }

    /**
     * Verstärkt das Feuer eines Spielfeldes durch Ausbreitung des Feuers von einem Nachbarfeld aus.
     * Dabei wird ein nasses Feld zu einem trockenen Feld,
     * ein trockenes Feld zu einem leicht brennenden Feld
     * und ein leicht brennendes Feld zu einem stark brennenden Feld.
     */
    public void spreadFire() {
        switch (getFieldState()) {
            case LIGHTLY_BURNING:
                setFieldState(FieldState.STRONGLY_BURNING);
                break;
            case DRY:
                setFieldState(FieldState.LIGHTLY_BURNING);
                break;
            case WET:
                setFieldState(FieldState.DRY);
                break;
            default:
                break;
        }
    }

    /**
     * Entfacht ein leicht brennendes Feuer zu einem Stark brennenden Feuer.
     */
    public void doubleFire() {
        if (getFieldState() == FieldState.LIGHTLY_BURNING) {
            setFieldState(FieldState.STRONGLY_BURNING);
        }
    }

    /**
     * Fügt dem Spielfeld ein Spielobjekt hinzu.
     * @param gameObject zu hinzufügenden Spielobjekt
     */
    public void addCellObject(GameObject gameObject) {
        this.cellObjects.add(gameObject);
    }

    /**
     * Entfernt ein gegebenes Spielobjekt vom Spielfeld.
     * @param gameObject zu entfernendes Spielobjekt
     */
    public void removeCellObject(GameObject gameObject) {
        this.cellObjects.remove(gameObject);
    }

    /**
     * Überprüft ob das Spielfeld ein gegebenes Spielobjekt enthält.
     * @param gameObject Spielobjekt
     * @return ob Spielfeld das gegebene Spielobjekt enthält
     */
    public boolean containsCellObject(GameObject gameObject) {
        return this.cellObjects.contains(gameObject);
    }

    /**
     * Gibt eine textuelle Repräsenation aller auf dem Spielfeld befindlichen Spielobjekte zurück.
     * Die einzelnen Spielobjekte, welche sich auf dem Spielfeld befinden, werden dabei
     * durch ein Komma getrennt.
     * @return textuelle Repräsentation aller sich auf dem Spielfeld befindlichen Spielobjekte
     */
    public String getCellObjectsRepresentation() {
        StringBuilder builder = new StringBuilder();
        for (int i = MINIMUM_INDEX; i < cellObjects.size(); i++) {
            builder.append(cellObjects.get(i).toString());
            if (i < cellObjects.size() - MINIMUM_SIZE) {
                builder.append(COMMA_SEPERATOR);
            }
        }
        return builder.toString();
    }

    /**
     * Sortiert die Spielobjekte auf dem Spielfeld nach den in {@link GameObjectComperator} beschriebenen
     * Spezifikationen.
     */
    public void sortGameObjects() {
        this.cellObjects.sort(new GameObjectComperator());
    }

    /**
     * Überprüft ob das Spielfeld leer ist und somit keine Spielobjekte enthält.
     * @return ob Spielfeld leer ist
     */
    public boolean isEmpty() {
        return this.cellObjects.isEmpty();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        GameCell otherGameCell = (GameCell) object;

        return fieldState == otherGameCell.fieldState
                && getPosition().equals(otherGameCell.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition(), fieldState);
    }

    /**
     * Gibt eine textuelle Repräsentation des Spielfeldes zurück
     * @return textuelle Repräsentation des Spielfeldes
     */
    public abstract String getFieldRepresentation();

    /**
     * Gibt eine Kopie des Spielfeldes zurück.
     * @return Kopie des Spielfeldes
     */
    public abstract GameCell copy();

    @Override
    public abstract String toString();

}
