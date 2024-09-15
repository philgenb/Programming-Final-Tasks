package edu.kit.informatik.firebreaker.gameboard;

import edu.kit.informatik.firebreaker.exception.ErrorMessages;
import edu.kit.informatik.firebreaker.exception.FireBreakerException;
import edu.kit.informatik.firebreaker.model.Player;
import edu.kit.informatik.firebreaker.status.InOutputStrings;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelliert eine Feuerwehr eines {@link Player} auf dem {@link GameBoard}.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class FireFighter extends GameObject {

    /**
     * Regulärer Ausdruck, welcher eine Feuerwehr im Ausgangszustand auf dem Feld beschreibt.
     */
    public static final String INITAL_FIREFIGHTER_REGEX = "[A-D]0";
    /**
     * Regulärer Ausdruck, welcher eine Feuerwehr beschreibt.
     */
    public static final String FIREFIGHTER_REGEX = "[A-D][0-9]*";

    private static final int INITAL_CAPACITY = 3;
    private static final int ROUND_ACTION_POINTS = 3;
    private static final int ACTION_COST = 1;
    private static final int WATER_COST = 1;

    private final List<GameCell> extinguishedCellsInRound;

    private boolean hasPerfomedAction;
    private int actionPoints;
    private int waterCapacity;

    /**
     * Erstellt eine neue Instanz einer Feuerwehr.
     * Jede Feuerwehr verfügt über eine eindeutige Nummer und hat einen Spieler als Attribut,
     * welcher der Besitzer der Feuerwehr auf dem Spielfeld ist.
     * @param identifier Eindeutige Nummer der Feuerwehr
     * @param owningPlayer besitzender Spieler
     */
    public FireFighter(int identifier, Player owningPlayer) {
        super(identifier, owningPlayer);
        this.actionPoints = ROUND_ACTION_POINTS;
        this.waterCapacity = INITAL_CAPACITY;
        this.extinguishedCellsInRound = new ArrayList<>();
        this.hasPerfomedAction = false;
    }

    /**
     * Gibt die Anzahl der Aktionspunkte der Feuerwehr zurück.
     * @return Aktionspunkte der Feuerwehr
     */
    public int getActionPoints() {
        return this.actionPoints;
    }

    /**
     * Gibt die verfügbare Anzahl an Wassersteinen im Wasserspeicher der Feuerwehr zurück.
     * @return verfügbare Anzahl an Wassersteinen
     */
    public int getWaterCapacity() {
        return waterCapacity;
    }

    /**
     * Überprüft ob die Feuerwehr im aktuellen Zug schon eine Aktion ausgeführt hat.
     * @return ob Feuerwehr im aktuellen Zug schon eine Aktion durchgeführt hat.
     */
    public boolean hasPerformedAction() {
        return this.hasPerfomedAction;
    }

    /**
     * Setzt den Status den performedAction Status der Feuerwehr auf {@code true},
     * welcher anzeigt, ob diese Runde schon eine Aktion ausgeführt wurde.
     */
    private void performedAction() {
        hasPerfomedAction = true;
    }

    /**
     * Verringert die Anzahl an Aktionspunkten der Feuerwehr um 1.
     */
    public void removeActionPoint() {
        this.actionPoints--;
    }

    private void setWaterCapacity(int waterCapacity) {
        this.waterCapacity = waterCapacity;
    }

    /**
     * Füllt den Wassertank der Feuerwehr auf, falls diese über mindestens
     * einen Aktionspunkt verfügt. Gibt anschließend die aktuelle Anzahl an Aktionspunkten zurück.
     * @throws FireBreakerException falls die Feuerwehr nicht genügend Aktionspunkte hat.
     * @return aktuelle Anzahl an Aktionspunkten der Feuerwehr
     */
    public int refill() throws FireBreakerException {
        if (!canPerformAction()) {
            throw new FireBreakerException(ErrorMessages.NOT_ENOUGH_ACTION_POINTS);
        }
        if (getWaterCapacity() == INITAL_CAPACITY) {
            throw new FireBreakerException(ErrorMessages.CAPACITY_USED);
        }
        setWaterCapacity(INITAL_CAPACITY);
        removeActionPoint();
        performedAction();
        return getActionPoints();
    }


    /**
     * Wird ausgeführt, wenn die Feuerwehr ein Spielfeld löscht.
     * Ein Spielfeld kann nur gelöscht werden, wenn die Feuerwehr mindestens einen Wasserstein
     * und einen Aktionspunkt hat.
     * Eine Feuerwehr kann ein angrenzendes Feld nur ein mal pro Runde löschen
     * @param gameCell Spielzelle, welche gelöscht werden soll
     * @throws FireBreakerException falls die Feuerwehr nicht genügend Aktionspunkte, nicht genügend Wassersteine
     * oder bereits in der Runde dasselbe Spielfeld gelöscht hat
     */
    public void extinguish(GameCell gameCell) throws FireBreakerException {
        checkIfCanExtinguish(gameCell);
        this.extinguishedCellsInRound.add(gameCell);
        setWaterCapacity(getWaterCapacity() - ACTION_COST);
        removeActionPoint();
        performedAction();
    }

    private void checkIfCanExtinguish(GameCell gameCell) throws FireBreakerException {
        if (extinguishedCellsInRound.contains(gameCell)) {
            throw new FireBreakerException(ErrorMessages.TOO_MANY_ACTIONS);
        }
        if (getWaterCapacity() < WATER_COST) {
            throw new FireBreakerException(ErrorMessages.OUT_OF_WATER);
        }
        if (!canPerformAction()) {
            throw new FireBreakerException(ErrorMessages.NOT_ENOUGH_ACTION_POINTS);
        }
    }

    /**
     * Überprüft, ob die Feuerwehr noch genügend Aktionspunkte hat um eine Aktion auszuführen.
     * Gibt {@code true} zurück, falls die Feuerwehr noch eine Aktion ausführen kann.
     * @return ob die Feuer noch eine Aktion auführen kann
     */
    public boolean canPerformAction() {
        return getActionPoints() >=  ACTION_COST;
    }

    /**
     * Setzt die verfübaren Aktionspunkte der Feuerwehr zurück auf den Ausgangswert,
     * am Start einer neuen Runde.
     */
    public void reset() {
        this.extinguishedCellsInRound.clear();
        this.actionPoints = ROUND_ACTION_POINTS;
        this.hasPerfomedAction = false;
    }

    /**
     * Gibt eine textuelle Repräsentation des Status der Feuerwehr zurück.
     * Dazu gehört die eindeutige Kennung der Feuerwehr, die aktuelle Wasserkapazität
     * und die aktuelle Anzahl an Aktionspunkten.
     * @return textuelle Repräsentation der Feuerwehrstats
     */
    public String getStats() {
        return String.format(InOutputStrings.FIREFIGHTER_OUTPUT,
                getIdentifierString(),
                getWaterCapacity(),
                getActionPoints());
    }

    @Override
    public String toString() {
        return getIdentifierString();
    }
}
