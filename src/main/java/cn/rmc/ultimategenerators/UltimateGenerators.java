package cn.rmc.ultimategenerators;

import cn.rmc.ultimategenerators.lists.UGCategories;
import cn.rmc.ultimategenerators.lists.UGItems;
import cn.rmc.ultimategenerators.lists.UGRecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Properties;

public final class UltimateGenerators extends JavaPlugin implements SlimefunAddon {

    private static UltimateGenerators plug;
    private static Properties pp;

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void onEnable() {

        plug = this;

        // load configuration
        try {

            File ppf = new File(getDataFolder().getPath() + File.separatorChar + "config.properties");
            if (!ppf.exists()) {
                info("Configuration is not exist, creating one...");

                if (!ppf.getParentFile().exists()) {
                    ppf.getParentFile().mkdirs();
                }
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("config.properties");
                try (FileOutputStream fos = new FileOutputStream(ppf)) {
                    byte[] b = new byte[is.available()];
                    while (is.read(b) != -1) {
                        fos.write(b);
                    }
                }
                info("Sucessfully created the default configuration!");
            }

            pp = new Properties();
            pp.load(new FileReader(ppf));

        } catch (IOException ex) {
            ex.printStackTrace();
            severe("Cannot initialize the configuration, self-disabling...");
            this.setEnabled(false);
            return;
        }

        // load
        try {
            new UGItems(this);
            new UGRecipeType();
            new UGCategories();

            UGImplementor implementor = new UGImplementor();
            implementor.implementIngredients();
            implementor.implementMachines();
            implementor.implementSingleGenerators();
//            implementor.implementModularGenerators();

            new ResearchSetup().setup();

            new UGListenersRegister(this).registerAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            severe("Cannot initialize the plugin, self-disabling...");
            this.setEnabled(false);
        }

        // initialize configurations
        /*
         * (Deprecated) try { Class<?> ugItemClass =
         * Class.forName("io.github.freeze_dolphin.ultimate_generators.lists.UGItems");
         * for (Field f : ugItemClass.getFields()) { if
         * (f.isAnnotationPresent(MachineItemStack.class)) { if
         * (SlimefunItem.getByID(f.getName()) == null) {
         * severe("An unexpected error occurred: Item '" + f.getName() +
         * "' is not loaded!"); continue; } else {
         * getUGConfig().setMachineValue(f.getName(), "speed", 1); } } else { continue;
         * } } } catch (ClassNotFoundException e) { e.printStackTrace();
         * severe("In-plugin file 'UGItems.class' is lost, self-disabling...");
         * this.setEnabled(false); }
         */
    }

    public static UltimateGenerators getImplement() {
        return plug;
    }

    public static void info(String msg) {
        getImplement().getLogger().info(msg);
    }

    public static void warn(String msg) {
        getImplement().getLogger().warning(msg);
    }

    public static void severe(String msg) {
        getImplement().getLogger().severe(msg);
    }

    public static Properties getProperties() {
        return pp;
    }

    public static boolean getDisplaySw() {

        return Boolean.parseBoolean(getProperties().getProperty("show-machine-indicator", "true"));

        /*
         * YamlConfiguration pdf = new YamlConfiguration(); try { pdf.load(new
         * InputStreamReader(Loader.class.getClassLoader().getResourceAsStream(
         * "plugin.yml"))); } catch (Exception e) { e.printStackTrace();
         * plug.getLogger()
         * .severe("The internal plugin description file 'plugin.yml' cannot be read, self-disabling..."
         * ); plug.getServer().getPluginManager().disablePlugin(plug); }
         *
         * return pdf.contains("show-machine-indicator") &&
         * pdf.isBoolean("show-machine-indicator") &&
         * pdf.getBoolean("show-machine-indicator");
         */
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return null;
    }
}
