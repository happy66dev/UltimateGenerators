package cn.rmc.ultimategenerators;

import cn.rmc.ultimategenerators.lists.UGCategories;
import cn.rmc.ultimategenerators.lists.UGItems;
import cn.rmc.ultimategenerators.lists.UGRecipeType;
import cn.rmc.ultimategenerators.object.abstracts.BContainer;
import cn.rmc.ultimategenerators.object.abstracts.BGenerator;
import cn.rmc.ultimategenerators.object.machines.BiofuelRefinery;
import cn.rmc.ultimategenerators.object.machines.OilRefinery;
import cn.rmc.ultimategenerators.object.tasks.ReinforcedRainbowTicker;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.AbstractEnergyProvider;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.Capacitor;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class UGImplementor {
    private static final ItemStack MOTOR = SlimefunItems.ELECTRIC_MOTOR;
    private static final ItemStack COIL = SlimefunItems.HEATING_COIL;
    private static final ItemStack BCIRCUIT = SlimefunItems.BASIC_CIRCUIT_BOARD;
    private static final ItemStack ACIRCUIT = SlimefunItems.ADVANCED_CIRCUIT_BOARD;
    private static final ItemStack HGLASS = SlimefunItems.HARDENED_GLASS;

    private static final ItemStack ALUI = SlimefunItems.ALUMINUM_INGOT;
    private static final ItemStack COPPI = SlimefunItems.COPPER_INGOT;

    public void implementIngredients() {
        register(
        new SlimefunItem(UGCategories.TECH_MISC, sis(UGItems.SOLID_STORAGE_EXPANSION, "SOLID_STORAGE_EXPANSION"),
                RecipeType.ENHANCED_CRAFTING_TABLE, Utils.buildRecipe(null, SlimefunItems.CARGO_OUTPUT_NODE, null, ALUI,
                SlimefunItems.ANDROID_INTERFACE_ITEMS, ALUI, ALUI, mat(Material.GLASS), ALUI)),
                new SlimefunItem(UGCategories.TECH_MISC, sis(UGItems.LIQUID_STORAGE_EXPANSION, "LIQUID_STORAGE_EXPANSION"),
                        RecipeType.ENHANCED_CRAFTING_TABLE, Utils.buildRecipe(null, SlimefunItems.CARGO_OUTPUT_NODE, null, ALUI,
                        SlimefunItems.ANDROID_INTERFACE_FUEL, ALUI, ALUI, mat(Material.GLASS), ALUI)),
                new SlimefunItem(
                        UGCategories.ELECTRICITY_STORAGE, sis(UGItems.ELECTRICITY_STORAGE_UNIT, "ELECTRICITY_STORAGE_UNIT"),
                        RecipeType.ENHANCED_CRAFTING_TABLE, Utils.buildRecipe(HGLASS, SlimefunItems.REDSTONE_ALLOY, HGLASS,
                        ALUI, SlimefunItems.BATTERY, ALUI, HGLASS, SlimefunItems.REDSTONE_ALLOY, HGLASS)),
                new SlimefunItem(UGCategories.ELECTRICITY_STORAGE, sis(UGItems.ADVANCED_BATTERY, "ADVANCED_BATTERY"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        Utils.buildRecipe(SlimefunItems.BATTERY, BCIRCUIT, SlimefunItems.BATTERY, COPPI, COPPI, COPPI,
                                SlimefunItems.BATTERY, BCIRCUIT, SlimefunItems.BATTERY)) {{
                    ItemStack output = UGItems.ADVANCED_BATTERY.clone();
                    output.setAmount(2);
                    setRecipeOutput(output);
                }},
                new SlimefunItem(UGCategories.ELECTRICITY_STORAGE, sis(UGItems.ALPHA_BATTERY, "ALPHA_BATTERY"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        Utils.buildRecipe(UGItems.ADVANCED_BATTERY, ACIRCUIT, UGItems.ADVANCED_BATTERY, COPPI,
                                UGItems.ELECTRICITY_STORAGE_UNIT, COPPI, UGItems.ADVANCED_BATTERY, ACIRCUIT,
                                UGItems.ADVANCED_BATTERY)) {{
                    ItemStack output = UGItems.ALPHA_BATTERY.clone();
                    output.setAmount(2);
                    setRecipeOutput(output);
                }},
                new SlimefunItem(UGCategories.ELECTRICITY_STORAGE,sis( UGItems.BETA_BATTERY, "BETA_BATTERY"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        Utils.buildRecipe(UGItems.ALPHA_BATTERY, ACIRCUIT, UGItems.ALPHA_BATTERY, COPPI,
                                UGItems.ELECTRICITY_STORAGE_UNIT, COPPI, UGItems.ALPHA_BATTERY, ACIRCUIT,
                                UGItems.ALPHA_BATTERY)) {{
                    ItemStack output = UGItems.BETA_BATTERY.clone();
                    output.setAmount(2);
                    setRecipeOutput(output);
                }},
                new SlimefunItem(UGCategories.ELECTRICITY_STORAGE, sis(UGItems.GAMMA_BATTERY, "GAMMA_BATTERY"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        Utils.buildRecipe(UGItems.BETA_BATTERY, ACIRCUIT, UGItems.BETA_BATTERY, COPPI,
                                UGItems.ELECTRICITY_STORAGE_UNIT, COPPI, UGItems.BETA_BATTERY, ACIRCUIT, UGItems.BETA_BATTERY)) {{
                    ItemStack output = UGItems.GAMMA_BATTERY.clone();
                    output.setAmount(2);
                    setRecipeOutput(output);
                }},
                new SlimefunItem(UGCategories.TECH_MISC,sis( UGItems.DIESEL_BUCKET, "DIESEL_BUCKET"),
                        new RecipeType(new NamespacedKey(UltimateGenerators.getImplement(),"DIESEL_REFINERY"),
                                new CustomItemStack(Material.PISTON, "&c柴油精炼器&r", "&a在柴油精炼器里将石油精炼为柴油")),
                        Utils.buildRecipe()),
                new SlimefunItem(UGCategories.TECH_MISC, sis(UGItems.BIOMASS_BUCKET, "BIOMASS_BUCKET"),
                        new RecipeType(new NamespacedKey(UltimateGenerators.getImplement(),"BIOMASS_EXTRACTION"),
                                new CustomItemStack(Material.LIME_STAINED_GLASS, "&a生物质萃取机&r",
                                "&a在生物质萃取机里提取一些作物中的生物质")),Utils.buildRecipe()),
                new SlimefunItem(UGCategories.TECH_MISC, sis(UGItems.BIOFUEL_BUCKET, "BIOFUEL_BUCKET"), 
                        new RecipeType(new NamespacedKey(UltimateGenerators.getImplement(),"BIOFUEL_REFINERY"),
                                new CustomItemStack(Material.PISTON, "&2生物燃油精炼器&r", 
                                "&a在生物燃油精炼器里将生物质精炼为生物燃油")),Utils.buildRecipe()),
//                new SlimefunItem(UGCategories.TECH_MISC,sis(UGItems.MAGNESIUM_SALT, "MAGNESIUM_SALT"), RecipeType.PRESSURE_CHAMBER,
//                        Utils.buildRecipe(SlimefunItems.MAGNESIUM_DUST, SlimefunItems.MAGNESIUM_DUST,
//                                Boolean.parseBoolean(DefaultConfig.getConfig("magnesium-salt-require-zinc-dust"))
//                                        ? SlimefunItems.ZINC_DUST
//                                        : null)),
                new SlimefunItem(UGCategories.TECH_MISC, sis(UGItems.ENDER_LUMP_4, "ENDER_LUMP_4"), RecipeType.ANCIENT_ALTAR,
                        new ItemStack[] { SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3,
                                SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3,
                                SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3 }),
                new SlimefunItem(UGCategories.TECH_MISC,sis( UGItems.RUNE_COMPLEX_ENDER, "RUNE_COMPLEX_ENDER"),
                        RecipeType.ANCIENT_ALTAR,
                        new ItemStack[] { SlimefunItems.ENDER_RUNE, SlimefunItems.GOLD_24K, SlimefunItems.ENDER_RUNE,
                                UGItems.ENDER_LUMP_4, SlimefunItems.STONE_CHUNK, UGItems.ENDER_LUMP_4, SlimefunItems.ENDER_RUNE,
                                SlimefunItems.GOLD_24K, SlimefunItems.ENDER_RUNE }),
//                new SlimefunItem(Categories.MISC, UGItems.ENDER_CRYSTAL_ENHANCER, "ENERGY_ACUMULATED_ENDER_CRYSTAL",
//                        RecipeType.ANCIENT_ALTAR,
//                        new ItemStack[] { mat(Material.GLASS), mat(Material.GLASS), mat(Material.GLASS), mat(Material.GLASS),
//                                UGItems.ENDER_LUMP_4, mat(Material.GLASS), mat(Material.GLASS), mat(Material.GLASS),
//                                mat(Material.GLASS) })
                new SlimefunItem(UGCategories.TECH_MISC, sis(UGItems.HEAVY_WATER_BUCKET, "HEAVY_WATER_BUCKET"), new RecipeType(
                        new NamespacedKey(UltimateGenerators.getImplement(),"HEAVY_WATER_REFINING_MACHINE"),
                        new CustomItemStack(Material.GRAY_STAINED_GLASS, "&7重水提炼机&r", "&a在重水提炼机里提取水中的重水")),
                        Utils.buildRecipe()),
                new SlimefunItem(UGCategories.TECH_MISC, sis(UGItems.NEUTRON_MODERATOR, "NEUTRON_MODERATOR"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        new ItemStack[] { mat(Material.GLASS), UGItems.HEAVY_WATER_BUCKET, mat(Material.GLASS),
                                UGItems.HEAVY_WATER_BUCKET, SlimefunItems.CARBON_CHUNK, UGItems.HEAVY_WATER_BUCKET,
                                mat(Material.GLASS), UGItems.HEAVY_WATER_BUCKET, mat(Material.GLASS) }),
                new SlimefunItem(UGCategories.TECH_MISC, sis(UGItems.THERMAL_NEUTRON_REACTOR_COOLANT_CELL,
                        "THERMAL_NEUTRON_REACTOR_COOLANT_CELL"), RecipeType.ENHANCED_CRAFTING_TABLE,
                        new ItemStack[] { null, null, UGItems.HEAVY_WATER_BUCKET, null, SlimefunItems.REACTOR_COOLANT_CELL,
                                null, UGItems.HEAVY_WATER_BUCKET, null, null })
//                new SlimefunItem(UGCategories.TECH_MISC, sis(UGItems.RAINBOW_ALLOY, "RAINBOW_ALLOY"), RecipeType.SMELTERY,
//                        new ItemStack[] {SlimefunItems.REDSTONE_ALLOY, SlimefunItems.CORINTHIAN_BRONZE_INGOT,
//                                SlimefunItems.COBALT_INGOT, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.RAINBOW_RUNE })
//



        );
//        Slimefun.getItemCfg().setValue("REINFORCED_RAINBOW_GLASS.allow-disenchanting", false);
//        BlockTicker fast = new ReinforcedRainbowTicker.Fast();
//        Slimefun.s("ENERGY_ACUMULATED_ENDER_CRYSTAL",
//                "https://gitee.com/freeze-dolphin/UltimateGenerators-wiki/blob/master/Generator-(Ender-Crystal-Generator).md");

        //        new SlimefunItem(UGCategories.TECH_MISC, UGItems.MODULAR_GENERATOR_REGULATOR, "MODULAR_GENERATOR_REGULATOR",
//                RecipeType.ENHANCED_CRAFTING_TABLE,
//                Utils.buildRecipe(SlimefunItems.BILLON_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD,
//                        SlimefunItems.COBALT_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD,
//                        SlimefunItems.ENERGY_REGULATOR, SlimefunItems.ADVANCED_CIRCUIT_BOARD,
//                        SlimefunItems.COBALT_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.BILLON_INGOT)))
//                                .register(false);

    }


    public void implementMachines() {
        register(
                new OilRefinery(UGCategories.MACHINES, sis(UGItems.DIESEL_REFINERY, "DIESEL_REFINERY"),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                Utils.buildRecipe(SlimefunItems.HEATING_COIL, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.HEATING_COIL,
                        SlimefunItems.HARDENED_GLASS, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.HARDENED_GLASS,
                        SlimefunItems.HARDENED_GLASS, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.HARDENED_GLASS))
                        .setCapacity(256).setProcessingSpeed(1).setEnergyConsumption(16),
                new BiofuelRefinery(UGCategories.MACHINES, sis(UGItems.BIOFUEL_REFINERY, "BIOFUEL_REFINERY"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        Utils.buildRecipe(SlimefunItems.HEATING_COIL, SlimefunItems.PLASTIC_SHEET, SlimefunItems.HEATING_COIL,
                                SlimefunItems.HARDENED_GLASS, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.HARDENED_GLASS,
                                SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.HEATING_COIL)) {
                }.setCapacity(256).setProcessingSpeed(1).setEnergyConsumption(18),
                new BContainer(UGCategories.MACHINES, sis(UGItems.BIOMASS_EXTRACTION_MACHINE, "BIOMASS_EXTRACTION_MACHINE"),
                        RecipeType.ENHANCED_CRAFTING_TABLE, Utils.buildRecipe(ALUI, mat(Material.PISTON), ALUI, HGLASS,
                        COIL, HGLASS, COIL, mat(Material.HOPPER), COIL)


                ) {
                    @Override
                    public ItemStack getProgressBar() {
                        return mat(Material.SLIME_BALL);
                    }


                    @Override
                    protected void registerDefaultRecipes() {
                        registerRecipe(4, new ItemStack[] { new ItemStack(Material.SLIME_BALL, 2), mat(Material.BUCKET) },
                                new ItemStack[] { UGItems.BIOMASS_BUCKET });
                        registerRecipe(4,
                                new ItemStack[] { new ItemStack(Material.NETHER_WART_BLOCK, 2), mat(Material.BUCKET) },
                                new ItemStack[] { UGItems.BIOMASS_BUCKET });
                        Arrays.asList(Material.ACACIA_LEAVES,Material.BIRCH_LEAVES,Material.OAK_LEAVES,Material.DARK_OAK_LEAVES,
                                Material.JUNGLE_LEAVES,Material.SPRUCE_LEAVES).forEach(material -> {
                            registerRecipe(8, new ItemStack[] { new ItemStack(material, 32), mat(Material.BUCKET) },
                                    new ItemStack[] { UGItems.BIOMASS_BUCKET });
                        });
                        registerRecipe(8, new ItemStack[] { new ItemStack(Material.WHEAT_SEEDS, 16), mat(Material.BUCKET) },
                                new ItemStack[] { UGItems.BIOMASS_BUCKET });
                        registerRecipe(8, new ItemStack[] { new ItemStack(Material.BEETROOT_SEEDS, 16), mat(Material.BUCKET) },
                                new ItemStack[] { UGItems.BIOMASS_BUCKET });
                        registerRecipe(8, new ItemStack[] { new ItemStack(Material.MELON_SEEDS, 16), mat(Material.BUCKET) },
                                new ItemStack[] { UGItems.BIOMASS_BUCKET });
                        registerRecipe(8, new ItemStack[] { new ItemStack(Material.PUMPKIN_SEEDS, 16), mat(Material.BUCKET) },
                                new ItemStack[] { UGItems.BIOMASS_BUCKET });
                    }
                }.setCapacity(128).setEnergyConsumption(3).setProcessingSpeed(1),
                new Capacitor(UGCategories.ELECTRICITY_STORAGE, 48,sis(UGItems.GLASS_ELECTRICITY_TRANSMITTER,
                        "GLASS_ELECTRICITY_TRANSMITTER"), RecipeType.ENHANCED_CRAFTING_TABLE,
                        new ItemStack[] { null, ALUI, null, ALUI, UGItems.ELECTRICITY_STORAGE_UNIT, ALUI, null, ALUI, null }){{
                            setRecipeOutput(new CustomItemStack(UGItems.GLASS_ELECTRICITY_TRANSMITTER,
                                    Integer.parseInt(
                                            DefaultConfig.getConfig("glass-electricity-transmitter-crafting-number-on-once"))));
                }},
                new Capacitor(UGCategories.ELECTRICITY_STORAGE, 640,sis(UGItems.BASIC_ELECTRICITY_STORAGE,
                        "BASIC_ELECTRICITY_STORAGE"), RecipeType.ENHANCED_CRAFTING_TABLE,
                        new ItemStack[] { SlimefunItems.BATTERY, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.BATTERY,
                                SlimefunItems.STEEL_INGOT, ALUI, SlimefunItems.STEEL_INGOT, SlimefunItems.BATTERY,
                                SlimefunItems.REDSTONE_ALLOY, SlimefunItems.BATTERY }),
                new Capacitor(UGCategories.ELECTRICITY_STORAGE, 2560,sis(UGItems.ADVANCED_ELECTRICITY_STORAGE,
                        "ADVANCED_ELECTRICITY_STORAGE"), RecipeType.ENHANCED_CRAFTING_TABLE,
                        new ItemStack[] { UGItems.ADVANCED_BATTERY, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.STEEL_INGOT,
                                SlimefunItems.REDSTONE_ALLOY, UGItems.BASIC_ELECTRICITY_STORAGE, SlimefunItems.REDSTONE_ALLOY,
                                SlimefunItems.STEEL_INGOT, SlimefunItems.REDSTONE_ALLOY, UGItems.ADVANCED_BATTERY }),
                new Capacitor(UGCategories.ELECTRICITY_STORAGE, 5120,sis(UGItems.ALPHA_ELECTRICITY_STORAGE,
                        "ALPHA_ELECTRICITY_STORAGE"), RecipeType.ENHANCED_CRAFTING_TABLE,
                        new ItemStack[] { UGItems.ALPHA_BATTERY, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY,
                                SlimefunItems.REDSTONE_ALLOY, UGItems.ADVANCED_ELECTRICITY_STORAGE,
                                SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY,
                                UGItems.ALPHA_BATTERY }),
                new Capacitor(UGCategories.ELECTRICITY_STORAGE, 40960,sis(UGItems.BETA_ELECTRICITY_STORAGE,
                        "BETA_ELECTRICITY_STORAGE"), RecipeType.ENHANCED_CRAFTING_TABLE,
                        new ItemStack[] { UGItems.BETA_BATTERY, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY,
                                SlimefunItems.REDSTONE_ALLOY, UGItems.ALPHA_ELECTRICITY_STORAGE, SlimefunItems.REDSTONE_ALLOY,
                                SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY, UGItems.BETA_BATTERY }),
                new Capacitor(UGCategories.ELECTRICITY_STORAGE, 327680,sis(UGItems.GAMMA_ELECTRICITY_STORAGE,
                        "GAMMA_ELECTRICITY_STORAGE"), RecipeType.ENHANCED_CRAFTING_TABLE,
                        new ItemStack[] { UGItems.GAMMA_BATTERY, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY,
                                SlimefunItems.REDSTONE_ALLOY, UGItems.BETA_ELECTRICITY_STORAGE, SlimefunItems.REDSTONE_ALLOY,
                                SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY, UGItems.GAMMA_BATTERY }),
                new Capacitor(UGCategories.ELECTRICITY_STORAGE, 2621440,sis(UGItems.LAMBDA_ELECTRICITY_STORAGE,
                        "LAMBDA_ELECTRICITY_STORAGE"), RecipeType.ENHANCED_CRAFTING_TABLE,
                        new ItemStack[] { UGItems.GAMMA_ELECTRICITY_STORAGE, UGItems.GAMMA_ELECTRICITY_STORAGE,
                                UGItems.GAMMA_ELECTRICITY_STORAGE, UGItems.GAMMA_ELECTRICITY_STORAGE,
                                SlimefunItems.REDSTONE_ALLOY, UGItems.GAMMA_ELECTRICITY_STORAGE,
                                UGItems.GAMMA_ELECTRICITY_STORAGE, UGItems.GAMMA_ELECTRICITY_STORAGE,
                                UGItems.GAMMA_ELECTRICITY_STORAGE }),
                new BContainer(UGCategories.MACHINES, sis(UGItems.HEAVY_WATER_REFINING_MACHINE, "HEAVY_WATER_REFINING_MACHINE"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        new ItemStack[] { HGLASS, null, HGLASS, COIL, SlimefunItems.ELECTRIC_DUST_WASHER_2, COIL, BCIRCUIT,
                                SlimefunItems.MEDIUM_CAPACITOR, BCIRCUIT }){

                    @Override
                    public ItemStack getProgressBar() {
                        return mat(Material.WATER_BUCKET);
                    }

                    @Override
                    protected void registerDefaultRecipes() {
                        registerRecipe(120, new ItemStack[] { mat(Material.WATER_BUCKET) },
                                new ItemStack[] { UGItems.HEAVY_WATER_BUCKET });
                    }
                }.setCapacity(256).setEnergyConsumption(9).setProcessingSpeed(300)

            
                );


    }

    public void implementSingleGenerators() {
        register(
                new BGenerator(UGCategories.SINGLE_GENERATOR, sis(UGItems.NETHER_STAR_GENERATOR, "NETHER_STAR_GENERATOR"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        Utils.buildRecipe(SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_PLATE,
                                SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.PLASTIC_SHEET,
                                SlimefunItems.WITHER_PROOF_GLASS, SlimefunItems.PLASTIC_SHEET, SlimefunItems.WITHER_PROOF_GLASS,
                                SlimefunItems.BIG_CAPACITOR, SlimefunItems.WITHER_PROOF_GLASS)) {
                    @Override
                    public ItemStack getProgressBar() {
                        return new ItemStack(Material.WITHER_SKELETON_SKULL);
                    }

                    @Override
                    protected void registerDefaultFuelTypes() {
                        registerFuel(new MachineFuel(480, mat(Material.NETHER_STAR)));
                    }
                }.setCapacity(1024).setEnergyProduction(256),
                new BGenerator(UGCategories.SINGLE_GENERATOR, sis(UGItems.DIESEL_GENERATOR, "DIESEL_GENERATOR"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        Utils.buildRecipe(null, SlimefunItems.DURALUMIN_INGOT, null, SlimefunItems.DURALUMIN_INGOT,
                                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.HEATING_COIL,
                                SlimefunItems.DURALUMIN_INGOT, SlimefunItems.HEATING_COIL)) {
                    @Override
                    public ItemStack getProgressBar() {
                        return mat(Material.FLINT_AND_STEEL);
                    }

                    @Override
                    protected void registerDefaultFuelTypes() {
                        registerFuel(new MachineFuel(90, UGItems.DIESEL_BUCKET));
                    }
                }.setCapacity(256).setEnergyProduction(18),
                new BGenerator(UGCategories.SINGLE_GENERATOR, sis(UGItems.BIOFUEL_GENERATOR, "BIOFUEL_GENERATOR"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        Utils.buildRecipe(SlimefunItems.HEATING_COIL, SlimefunItems.BILLON_INGOT, SlimefunItems.HEATING_COIL,
                                SlimefunItems.BILLON_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BILLON_INGOT,
                                SlimefunItems.HEATING_COIL, SlimefunItems.BILLON_INGOT, SlimefunItems.HEATING_COIL)) {
                    @Override
                    public ItemStack getProgressBar() {
                        return mat(Material.MAGMA_CREAM);
                    }


                    @Override
                    protected void registerDefaultFuelTypes() {
                        registerFuel(new MachineFuel(240, UGItems.BIOFUEL_BUCKET));
                    }
                }.setCapacity(256).setEnergyProduction(9),
                new BGenerator(UGCategories.SINGLE_GENERATOR, sis(UGItems.DRAGON_BREATH_GENERATOR, "DRAGON_BREATH_GENERATOR"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        Utils.buildRecipe(COIL, SlimefunItems.HARDENED_GLASS, COIL, SlimefunItems.PLASTIC_SHEET,
                                SlimefunItems.ENDER_RUNE, SlimefunItems.PLASTIC_SHEET, SlimefunItems.ENDER_LUMP_3,
                                SlimefunItems.MEDIUM_CAPACITOR, ACIRCUIT)) {
                    @Override
                    public ItemStack getProgressBar() {
                        return mat(Material.END_CRYSTAL);
                    }

                    @Override
                    protected void registerDefaultFuelTypes() {
                        registerFuel(new MachineFuel(30, new ItemStack(Material.DRAGON_BREATH)));
                    }
                }.setCapacity(256).setEnergyProduction(9),
                new BGenerator(UGCategories.SINGLE_GENERATOR, sis(UGItems.REACTION_GENERATOR, "REACTION_GENERATOR"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        Utils.buildRecipe(SlimefunItems.LEAD_INGOT, MOTOR, SlimefunItems.LEAD_INGOT, COIL,
                                SlimefunItems.HARDENED_GLASS, COIL, BCIRCUIT, SlimefunItems.MEDIUM_CAPACITOR, BCIRCUIT)) {
                    @Override
                    public ItemStack getProgressBar() {
                        return mat(Material.FLINT_AND_STEEL);
                    }

                    @Override
                    protected void registerDefaultFuelTypes() {
                        registerFuel(new MachineFuel(3, SlimefunItems.TINY_URANIUM));
                        registerFuel(new MachineFuel(27, SlimefunItems.SMALL_URANIUM));
                        registerFuel(new MachineFuel(108, SlimefunItems.URANIUM));
                    }
                }.setCapacity(256).setEnergyProduction(32),
                new SolarGenerator(UGCategories.SINGLE_GENERATOR, 832,104,sis(UGItems.QUANTUM_SOLAR_GENERATOR, "QUANTUM_SOLAR_GENERATOR"),
                        RecipeType.ENHANCED_CRAFTING_TABLE,
                        new ItemStack[] { SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.SOLAR_GENERATOR_4,
                                SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.SOLAR_GENERATOR_4,
                                SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.SOLAR_GENERATOR_4,
                                SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.SOLAR_GENERATOR_4,
                                SlimefunItems.SOLAR_GENERATOR_4 },2048)
        );


    }

    public void register(SlimefunItem... items){
        for (SlimefunItem item : items) {
            item.register(UltimateGenerators.getImplement());
        }
    }
    public SlimefunItemStack sis(ItemStack is,String str){
        return new SlimefunItemStack(str,is);
    }

    private ItemStack mat(Material m) {
        return new ItemStack(m);
    }
}
