package com.example.versiontinmoli;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CommandHandler handles plugin commands for reloading configuration and modifying version name.
 */
public class CommandHandler implements CommandExecutor, TabCompleter {
    private final VersionTinmoli plugin;

    public CommandHandler(VersionTinmoli plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LanguageLoader lang = plugin.getLanguageLoader();
        
        if (!sender.hasPermission("versiontinmoli.admin")) {
            sender.sendMessage(ChatColor.RED + lang.getMessage("command.no_permission"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + lang.getMessage("command.help_title"));
            sender.sendMessage(ChatColor.YELLOW + lang.getMessage("command.help_usage"));
            sender.sendMessage(ChatColor.GRAY + lang.getMessage("command.help_reload"));
            sender.sendMessage(ChatColor.GRAY + lang.getMessage("command.help_modify"));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload":
                handleReload(sender);
                break;
            case "md":
                handleModify(sender, args);
                break;
            default:
                sender.sendMessage(ChatColor.RED + lang.getMessage("command.unknown_command"));
                break;
        }

        return true;
    }

    private void handleReload(CommandSender sender) {
        try {
            plugin.getConfigLoader().load();
            plugin.getLanguageLoader().load(plugin.getConfigLoader().getLanguage());
            
            LanguageLoader lang = plugin.getLanguageLoader();
            String newVersion = plugin.getConfigLoader().getVersionName();
            sender.sendMessage(ChatColor.GREEN + lang.getMessage("reload.success"));
            sender.sendMessage(ChatColor.GRAY + lang.getMessage("reload.current_version", newVersion));
        } catch (Exception e) {
            LanguageLoader lang = plugin.getLanguageLoader();
            sender.sendMessage(ChatColor.RED + lang.getMessage("reload.failed", e.getMessage()));
        }
    }

    private void handleModify(CommandSender sender, String[] args) {
        LanguageLoader lang = plugin.getLanguageLoader();
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + lang.getMessage("modify.usage"));
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
            sender.sendMessage(ChatColor.GREEN + lang.getMessage("modify.success", versionName.toString()));
            sender.sendMessage(ChatColor.GRAY + lang.getMessage("modify.saved"));
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + lang.getMessage("modify.failed", e.getMessage()));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("versiontinmoli.admin")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            return Arrays.asList("reload", "md");
        }

        return new ArrayList<>();
    }
}
