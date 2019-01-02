package de.marcermarc.highenchantments.listener;

import de.marcermarc.highenchantments.Util;
import de.marcermarc.highenchantments.controller.PluginController;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Command implements CommandExecutor, TabCompleter, Listener {
    private static final String SAVE_CONFIG = "saveconfig";
    private static final String LOAD_CONFIG = "loadconfig";
    private static final String ENCHANTMENT = "enchantment";

    private static final String MESSAGE_PREFIX = ChatColor.DARK_GREEN + "[marcerVeinminer]" + ChatColor.RESET + " ";

    private static final String MESSAGE_NO_PERMISSION = MESSAGE_PREFIX + ChatColor.RED + "You have no permission for this command";
    private static final String MESSAGE_ENCHANTMENT_ARGS = MESSAGE_PREFIX + ChatColor.RED + "Command needs 2 or 3 arguments.";
    private static final String MESSAGE_ENCHANTMENT_ARG2 = MESSAGE_PREFIX + ChatColor.RED + "This enchantment does not exists.";
    private static final String MESSAGE_ENCHANTMENT_ARG3 = MESSAGE_PREFIX + ChatColor.RED + "Max Tier has to be a number.";
    private static final String MESSAGE_ENCHANTMENT_LOWLEVEL = MESSAGE_PREFIX + ChatColor.RED + "You can't set the level under the normal maximum.";
    private static final String MESSAGE_ENCHANTMENT_SUCCESS = MESSAGE_PREFIX + ChatColor.GREEN + "Max Tier has been changed.";
    private static final String MESSAGE_ENCHANTMENT_INFO = MESSAGE_PREFIX + ChatColor.GREEN + "The maximum tier of %s is %d";
    private static final String MESSAGE_SAVE = MESSAGE_PREFIX + ChatColor.GREEN + "Saved config.";
    private static final String MESSAGE_SAVE_FAILED = ChatColor.RED + "Failed to save the config!";
    private static final String MESSAGE_LOAD = MESSAGE_PREFIX + ChatColor.GREEN + "Loaded config.";
    private static final String MESSAGE_LOAD_FAILED = ChatColor.RED + "Failed to load the config!";

    private static final List<String> ARG1_OP = Arrays.asList(SAVE_CONFIG, LOAD_CONFIG, ENCHANTMENT);
    private static final List<String> ARG2_LISTS = Stream.of(Enchantment.values()).map(x -> x.getKey().toString()).collect(Collectors.toList());

    private PluginController controller;

    public Command(PluginController controller) {
        this.controller = controller;
    }

    //region CommandExecutor
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {

        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case SAVE_CONFIG:
                    if (!commandSender.isOp()) {
                        commandSender.sendMessage(MESSAGE_NO_PERMISSION);
                        return false;
                    } else if (controller.getConfig().saveConfig()) {
                        commandSender.sendMessage(MESSAGE_SAVE);
                        return true;
                    } else {
                        commandSender.sendMessage(MESSAGE_SAVE_FAILED);
                        return false;
                    }

                case LOAD_CONFIG:
                    if (!commandSender.isOp()) {
                        commandSender.sendMessage(MESSAGE_NO_PERMISSION);
                        return false;
                    } else if (controller.getConfig().loadConfig()) {
                        commandSender.sendMessage(MESSAGE_LOAD);
                        return true;
                    } else {
                        commandSender.sendMessage(MESSAGE_LOAD_FAILED);
                        return false;
                    }

                case ENCHANTMENT:
                    if (!commandSender.isOp()) {
                        commandSender.sendMessage(MESSAGE_NO_PERMISSION);
                        return false;
                    } else if (args.length != 3 && args.length != 2)
                        commandSender.sendMessage(MESSAGE_ENCHANTMENT_ARGS);

                    return enchantment(commandSender, args);
            }
        }
        return false;
    }

    private boolean enchantment(CommandSender commandSender, String[] args) {
        if (!ARG2_LISTS.contains(args[1])) {
            commandSender.sendMessage(MESSAGE_ENCHANTMENT_ARG2);
        } else if (args.length == 2) {
            Enchantment enchantment = Util.stringToEntchantment(args[1]);
            if (enchantment == null) {
                commandSender.sendMessage(MESSAGE_ENCHANTMENT_ARG2);
                return false;
            }

            commandSender.sendMessage(String.format(MESSAGE_ENCHANTMENT_INFO, Util.keyedToString(enchantment), controller.getConfig().getMaxLevels().get(enchantment)));

        } else if (!StringUtils.isNumeric(args[2])) {
            commandSender.sendMessage(MESSAGE_ENCHANTMENT_ARG3);
        } else {

            Enchantment enchantment = Util.stringToEntchantment(args[1]);
            if (enchantment == null) {
                commandSender.sendMessage(MESSAGE_ENCHANTMENT_ARG2);
                return false;
            }

            int level = Integer.parseInt(args[2]);

            if (enchantment.getMaxLevel() > level) {
                commandSender.sendMessage(MESSAGE_ENCHANTMENT_LOWLEVEL);
                return false;
            }

            controller.getConfig().getMaxLevels().put(enchantment, level);

            commandSender.sendMessage(MESSAGE_ENCHANTMENT_SUCCESS);

            return true;
        }
        return false;
    }
    //endregion

    //region TabComplete

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (!sender.isOp()) {
            return null;
        }

        switch (args.length) {
            case 1:
                return onTabCompleteArg1(sender, args);
            case 2:
                return onTabCompleteArg2(sender, args);
            case 3:
                return onTabCompleteArg3(sender, args);
            default:
                return null;
        }
    }

    private List<String> onTabCompleteArg1(CommandSender sender, String[] args) {
        return Util.tabCompleteFilter(ARG1_OP, args[0]);

    }

    private List<String> onTabCompleteArg2(CommandSender sender, String[] args) {
        if (args[0].equals(ENCHANTMENT) && sender.isOp()) {
            return Util.tabCompleteFilter(ARG2_LISTS, args[1]);
        }
        return null;
    }

    private List<String> onTabCompleteArg3(CommandSender sender, String[] args) {
        if (args[0].equals(ENCHANTMENT) && ARG2_LISTS.contains(args[1]) && sender.isOp()) {
            return Collections.singletonList("[<max_level>]");
        }
        return null;
    }

    //endregion
}