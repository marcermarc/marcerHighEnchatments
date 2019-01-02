package de.marcermarc.highenchantments;

import de.marcermarc.highenchantments.controller.ConfigController;
import de.marcermarc.highenchantments.controller.PluginController;
import de.marcermarc.highenchantments.listener.Anvil;
import de.marcermarc.highenchantments.listener.Command;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private PluginController controller = new PluginController();

    @Override
    public void onEnable() {
        controller.setMain(this);

        PluginManager pM = getServer().getPluginManager();

        registerEvents(pM);

        controller.setConfig(new ConfigController(controller));
    }

    private void registerEvents(PluginManager in_PM) {
        in_PM.registerEvents(new Anvil(controller), this);

        Command c = new Command(controller);
        in_PM.registerEvents(c, this);

        this.getCommand("marcerHighEnchants").setExecutor(c);
        this.getCommand("marcerHighEnchants").setTabCompleter(c);
    }

    @Override
    public void onDisable() {
        controller.getConfig().saveConfig();
    }
}