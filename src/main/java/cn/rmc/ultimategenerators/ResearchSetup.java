package cn.rmc.ultimategenerators;

import cn.rmc.ultimategenerators.lists.UGItems;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ResearchSetup {

    public void setup() {
        register("power_basics", 201, "电力基础", 10,
                UGItems.ELECTRICITY_STORAGE_UNIT,
                UGItems.ADVANCED_BATTERY,
                UGItems.ALPHA_BATTERY,
                UGItems.GLASS_ELECTRICITY_TRANSMITTER);

        register("basic_capacitors", 202, "基础电容", 15,
                UGItems.BASIC_ELECTRICITY_STORAGE,
                UGItems.ADVANCED_ELECTRICITY_STORAGE,
                UGItems.ALPHA_ELECTRICITY_STORAGE,
                UGItems.BETA_ELECTRICITY_STORAGE,
                UGItems.BETA_BATTERY);

        register("advanced_capacitors", 203, "高级电容", 20,
                UGItems.GAMMA_ELECTRICITY_STORAGE,
                UGItems.GAMMA_BATTERY);

        register("ultimate_capacitor", 204, "终极电容", 25,
                UGItems.LAMBDA_ELECTRICITY_STORAGE);

        register("diesel_system", 205, "柴油系统", 12,
                UGItems.DIESEL_REFINERY,
                UGItems.DIESEL_BUCKET);

        register("biomass_system", 206, "生物质系统", 15,
                UGItems.BIOMASS_EXTRACTION_MACHINE,
                UGItems.BIOMASS_BUCKET,
                UGItems.BIOFUEL_REFINERY,
                UGItems.BIOFUEL_BUCKET);

        register("basic_generators", 207, "基础发电机", 12,
                UGItems.DIESEL_GENERATOR,
                UGItems.BIOFUEL_GENERATOR);

        register("heavy_water_nuclear", 208, "重水与核能", 18,
                UGItems.HEAVY_WATER_BUCKET,
                UGItems.HEAVY_WATER_REFINING_MACHINE,
                UGItems.NEUTRON_MODERATOR,
                UGItems.THERMAL_NEUTRON_REACTOR_COOLANT_CELL);

        register("advanced_generators", 209, "高级发电机", 20,
                UGItems.REACTION_GENERATOR,
                UGItems.DRAGON_BREATH_GENERATOR,
                UGItems.NETHER_STAR_GENERATOR);

        register("ender_system", 210, "终界系统", 18,
                UGItems.ENDER_LUMP_4,
                UGItems.RUNE_COMPLEX_ENDER,
                UGItems.QUANTUM_SOLAR_GENERATOR);

        register("storage_expansion", 211, "储存拓展", 5,
                UGItems.SOLID_STORAGE_EXPANSION,
                UGItems.LIQUID_STORAGE_EXPANSION);
    }

    @ParametersAreNonnullByDefault
    private void register(String key, int id, String name, int defaultCost, ItemStack... items) {
        Research research = new Research(new NamespacedKey(UltimateGenerators.getImplement(), key), id, name, defaultCost);
        for (ItemStack item : items) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item);
            if (sfItem != null) {
                research.addItems(sfItem);
            }
        }
        research.register();
    }
}
