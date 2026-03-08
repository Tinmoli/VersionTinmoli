package com.example.versiontinmoli;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

/**
 * CommandHandler handles plugin commands for reloading configuration and modifying version name.
 */
public class CommandHandler extends Command {
    private final VersionTinmoli plugin;

    public CommandHandler(VersionTinmoli plugin) {
        super("vt", "versiontinmoli.admin", "versiontinmoli");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        LanguageLoader lang = plugin.getLanguageLoader();
        
        if (args.length == 0) {
            sender.sendMessage(new TextComponent(ChatColor.GOLD + lang.getMessage("command.help_title")));
            sender.sendMessage(new TextComponent(ChatColor.YELLOW + lang.getMessage("command.help_usage")));
            sender.sendMessage(new TextComponent(ChatColor.GRAY + lang.getMessage("command.help_reload")));
            sender.sendMessage(new TextComponent(ChatColor.GRAY + lang.getMessage("command.help_modify")));
            return;
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
                sender.sendMessage(new TextComponent(ChatColor.RED + lang.getMessage("command.unknown_command")));
                break;
        }
    }

    private void handleReload(CommandSender sender) {
        try {
            plugin.getConfigLoader().load();
            plugin.getLanguageLoader().load(plugin.getConfigLoader().getLanguage());
            
            LanguageLoader lang = plugin.getLanguageLoader();
            String newVersion = plugin.getConfigLoader().getVersionName();
            sender.sendMessage(new TextComponent(ChatColor.GREEN + lang.getMessage("reload.success")));
            sender.sendMessage(new TextComponent(ChatColor.GRAY + lang.getMessage("reload.current_version", newVersion)));
        } catch (Exception e) {
            LanguageLoader lang = plugin.getLanguageLoader();
            sender.sendMessage(new TextComponent(ChatColor.RED + lang.getMessage("reload.failed", e.getMessage())));
        }
    }

    private void handleModify(CommandSender sender, String[] args) {
        LanguageLoader lang = plugin.getLanguageLoader();
        if (args.length < 2) {
            sender.sendMessage(new TextComponent(ChatColor.RED + lang.getMessage("modify.usage")));
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
            sender.sendMessage(new TextComponent(ChatColor.GREEN + lang.getMessage("modify.success", versionName.toString())));
            sender.sendMessage(new TextComponent(ChatColor.GRAY + lang.getMessage("modify.saved")));
        } catch (Exception e) {
            sender.sendMessage(new TextComponent(ChatColor.RED + lang.getMessage("modify.failed", e.getMessage())));
        }
    }
}
