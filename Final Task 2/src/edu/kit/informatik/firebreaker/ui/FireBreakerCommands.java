package edu.kit.informatik.firebreaker.ui;

import edu.kit.informatik.firebreaker.model.FireBreaker;

import java.util.Map;

import static java.util.Map.entry;

/**
 * Diese Klasse bündelt alle Befehle des Firebreaker Spiels.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class FireBreakerCommands {

    private final Map<String, Command> commandsMap;

    /**
     * Erstellt einen neuen Befehlsgeber für das Firebreaker Spiel.
     * @param fireBreaker Instanz eines Firebreaker Spieles
     * @param session Sitzung
     */
    public FireBreakerCommands(final FireBreaker fireBreaker, final Session session) {
        this.commandsMap = Map.ofEntries(
                entry(QuitCommand.QUIT_COMMAND_NAME, new QuitCommand(session)),
                entry(ResetCommand.RESET_COMMAND_NAME, new ResetCommand(session)),
                entry(ShowBoardCommand.SHOW_BOARD_NAME, new ShowBoardCommand(fireBreaker)),
                entry(ShowFieldCommand.SHOW_FIELD_NAME, new ShowFieldCommand(fireBreaker)),
                entry(ExtinguishCommand.EXTINGUISH_COMMAND_NAME, new ExtinguishCommand(fireBreaker)),
                entry(RefillCommand.REFILL_COMMAND_NAME, new RefillCommand(fireBreaker)),
                entry(TurnCommand.TURN_COMMAND_NAME, new TurnCommand(fireBreaker)),
                entry(FireToRollCommand.FIRETOROLL_COMMAND_NAME, new FireToRollCommand(fireBreaker)),
                entry(MoveCommand.FIRETOROLL_COMMAND_NAME, new MoveCommand(fireBreaker)),
                entry(ShowPlayerCommand.SHOWPLAYER_COMMAND_NAME, new ShowPlayerCommand(fireBreaker)),
                entry(BuyFireEngineCommand.BUY_FIRE_ENGINE_COMMAND_NAME, new BuyFireEngineCommand(fireBreaker))
        );
    }

    /**
     * Überprüft, ob ein Befehl mit dem gegebenen Namen existiert.
     * @param commandName Name des Befehls
     * @return ob ein Befehl mit diesem Name existiert
     */
    public boolean containsCommand(String commandName) {
        return this.commandsMap.containsKey(commandName);
    }

    /**
     * Gibt die Befehlsmap zurück, welche jedem Befehlsnamen den dazugehörigen Befehl zuordnet.
     * @return Befehls-Map
     */
    public Map<String, Command> getCommandMap() {
        return Map.copyOf(this.commandsMap);
    }

}
