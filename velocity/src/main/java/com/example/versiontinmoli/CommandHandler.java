package com.example.versiontinmoli;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * CommandHandler handles plugin commands for reloading configuration and modifying version name.
 */
public class CommandHandler implements SimpleCommand {
    private final VersionTinmoli plugin;
    private final ProxyServer server;

    public CommandHandler(VersionTinmoli plugin, ProxyServer server) {
        this.plugin = plugin;
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        LanguageLoader lang = plugin.getLanguageLoader();
        
        if (args.length == 0) {
            invocation.source().sendMessage(Component.text(lang.getMessage("command.help_title"), NamedTextColor.GOLD));
            invocation.source().sendMessage(Component.text(lang.getMessage("command.help_usage"), NamedTextColor.YELLOW));
            invocation.source().sendMessage(Component.text(lang.getMessage("command.help_reload"), NamedTextColor.GRAY));
            invocation.source().sendMessage(Component.text(lang.getMessage("command.help_modify"), NamedTextColor.GRAY));
            return;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload":
                handleReload(invocation);
                break;
            case "md":
                handleModify(invocation, args);
                break;
            default:
                invocation.source().sendMessage(Component.text(lang.getMessage("command.unknown_command"), NamedTextColor.RED));
                break;
        }
    }

    private void handleReload(Invocation invocation) {
        try {
            plugin.getConfigLoader().load();
            plugin.getLanguageLoader().load(plugin.getConfigLoader().getLanguage());
            
            LanguageLoader lang = plugin.getLanguageLoader();
            String newVersion = plugin.getConfigLoader().getVersionName();
            invocation.source().sendMessage(Component.text(lang.getMessage("reload.success"), NamedTextColor.GREEN));
            invocation.source().sendMessage(Component.text(lang.getMessage("reload.current_version", newVersion), NamedTextColor.GRAY));
        } catch (Exception e) {
            LanguageLoader lang = plugin.getLanguageLoader();
            invocation.source().sendMessage(Component.text(lang.getMessage("reload.failed", e.getMessage()), NamedTextColor.RED));
        }
    }

    private void handleModify(Invocation invocation, String[] args) {
        LanguageLoader lang = plugin.getLanguageLoader();
        if (args.length < 2) {
            invocation.source().sendMessage(Component.text(lang.getMessage("modify.usage"), NamedTextColor.RED));
            return;
        }

        // Join all arguments after "md" as the version name
        StringBuilder versionName = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i > 1) versionName.append(" ");
            versionName.append(args[i]);
        }

        try {
            plugin.getConfigLoader().setVersionName(versionName.toString());
            plugin.getConfigLoader().save();
            invocation.source().sendMessage(Component.text(lang.getMessage("modify.success", versionName.toString()), NamedTextColor.GREEN));
            invocation.source().sendMessage(Component.text(lang.getMessage("modify.saved"), NamedTextColor.GRAY));
        } catch (Exception e) {
            invocation.source().sendMessage(Component.text(lang.getMessage("modify.failed", e.getMessage()), NamedTextColor.RED));
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("versiontinmoli.admin");
    }
}
