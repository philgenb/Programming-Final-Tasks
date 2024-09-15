package edu.kit.informatik.escaperoutenetwork.userinterface;

import edu.kit.informatik.escaperoutenetwork.core.EscapeRouteNetworkManager;

import java.util.Map;

/**
 * Diese Klasse bündelt alle Befehle.
 * @author Phil Gengenbach
 * @version 1.0
 */
public final class EscapeRouteCommands {

    private final Map<String, Command> commandsMap;

    /**
     * Erstellt einen neuen Befehlsgeber für die Fluchtwegeroutenverwaltung.
     * @param escapeRouteNetworkManager Fluchtwegenetz-Verwaltung
     * @param session Sitzung
     */
    public EscapeRouteCommands(final EscapeRouteNetworkManager escapeRouteNetworkManager, final Session session) {
        this.commandsMap = Map.of(
                AddCommand.ADD_COMMAND_NAME, new AddCommand(escapeRouteNetworkManager),
                PrintCommand.PRINT_COMMAND_NAME, new PrintCommand(escapeRouteNetworkManager),
                ListCommand.LIST_COMMAND_NAME, new ListCommand(escapeRouteNetworkManager),
                FlowCommand.FLOW_COMMAND_NAME, new FlowCommand(escapeRouteNetworkManager),
                QuitCommand.QUIT_COMMAND_NAME, new QuitCommand(session)
        );
    }

    /**
     * Gibt die Befehlsmap zurück, welche jedem Befehlsnamen den dazugehörigen Befehl zuordnet.
     * @return Befehls-Map
     */
    public Map<String, Command> get() {
        return this.commandsMap;
    }

}
