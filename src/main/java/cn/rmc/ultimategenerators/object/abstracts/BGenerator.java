package cn.rmc.ultimategenerators.object.abstracts;

import cn.rmc.ultimategenerators.UltimateGenerators;
import cn.rmc.ultimategenerators.Utils;
import cn.rmc.ultimategenerators.lists.UGItems;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemState;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineProcessHolder;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.AbstractEnergyProvider;
import io.github.thebusybiscuit.slimefun4.implementation.operations.FuelOperation;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import com.xzavier0722.mc.plugin.slimefun4.storage.controller.ASlimefunDataContainer;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class BGenerator extends AbstractEnergyProvider implements MachineProcessHolder<FuelOperation> {

    private static final int[] border
            = {
            0,
            9, 17,
            18, 19, 20, 21, 23, 24, 25, 26,
            27, 35,
            36, 44
    };
    private static final int[] border_in
            = {
            1, 7,
            10, 11, 12, 13, 14, 15, 16
    };
    private static final int[] border_out
            = {
            28, 29, 30, 31, 32, 33, 34,
            37, 43
    };

    public final MachineProcessor<FuelOperation> processor = new MachineProcessor<>(this);

    private int energyProducedPerTick = -1;
    private int energyCapacity = -1;

    protected BGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        processor.setProgressBar(getProgressBar());

        new BlockMenuPreset(item.getItemId(), getInventoryTitle()) {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.INSERT) {
                    return getInputSlots();
                } else {
                    return getOutputSlots();
                }
            }
        };

        addItemHandler(onBlockBreak());
        registerDefaultFuelTypes();
    }

//    public abstract void registerDefaultFuelTypes();

    @Override
    public MachineProcessor<FuelOperation> getMachineProcessor() {
        return processor;
    }

    @Override
    public Class<FuelOperation> getMachineOperationClass() {
        return FuelOperation.class;
    }

    public BlockBreakHandler onBlockBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(Block b) {
                BlockMenu inv = BlockStorage.getInventory(b);

                if (inv != null) {
                    inv.dropItems(b.getLocation(), getInputSlots());
                    inv.dropItems(b.getLocation(), getOutputSlots());
                }

                processor.endOperation(b);
            }
        };
    }

    public void constructMenu(BlockMenuPreset preset) {
        for (int i : border) {
            preset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : border_in) {
            preset.addItem(i, ChestMenuUtils.getInputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : border_out) {
            preset.addItem(i, ChestMenuUtils.getOutputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, new ChestMenu.AdvancedMenuClickHandler() {

                @Override
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                    return false;
                }

                @Override
                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                    if (cursor == null) return true;
                    return cursor.getType() == Material.AIR;
                }
            });
        }
//        preset.addItem(8, new CustomItemStack(Material.MAP, "&f机器信息", "&7 - &3发电量: &e" + getEnergyProduction() * 2 + " J/s", "&7 - &3工作速度: &e" + "&f默认"), new ChestMenu.MenuClickHandler() {
//
//            @Override
//            public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
//                return false;
//            }
//
//        });
        preset.addItem(22, new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " "), ChestMenuUtils.getEmptyClickHandler());
    }

    public int[] getInputSlots() {
        return new int[]{2, 3, 4, 5, 6};
    }

    public int[] getOutputSlots() {
        return new int[]{38, 39, 40, 41, 42};
    }
    @Override
    public int getGeneratedOutput(Location l, ASlimefunDataContainer data) {
        BlockMenu inv = BlockStorage.getInventory(l);
        FuelOperation operation = processor.getOperation(l);

        if (operation != null) {
            if (!operation.isFinished()) {
                processor.updateProgressBar(inv, 22, operation);

                if (isChargeable()) {
                    long charge = getChargeLong(l);

                    if (getCapacity() - charge >= getEnergyProduction()) {
                        operation.addProgress(1);
                        return getEnergyProduction();
                    }

                    return 0;
                } else {
                    operation.addProgress(1);
                    return getEnergyProduction();
                }
            } else {
                ItemStack fuel = operation.getIngredient();

                if (isBucket(fuel)) {
                    inv.pushItem(new ItemStack(Material.BUCKET), getOutputSlots());
                }

                inv.replaceExistingItem(22, new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));

                processor.endOperation(l);
                return 0;
            }
        } else {
            Map<Integer, Integer> found = new HashMap<>();
            MachineFuel fuel = findRecipe(inv, found);

            if (fuel != null) {
                for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
                    inv.consumeItem(entry.getKey(), entry.getValue());
                }

                processor.startOperation(l, new FuelOperation(fuel));
            }

            return 0;
        }
    }

    private boolean isBucket(ItemStack item) {
        if (item == null) {
            return false;
        }

        ItemStackWrapper wrapper = ItemStackWrapper.wrap(item);
        return item.getType() == Material.LAVA_BUCKET ||
                SlimefunUtils.isItemSimilar(wrapper, SlimefunItems.FUEL_BUCKET, true)
                || SlimefunUtils.isItemSimilar(wrapper, SlimefunItems.OIL_BUCKET, true)
                || SlimefunUtils.isItemSimilar(wrapper, UGItems.BIOFUEL_BUCKET , true)
                || SlimefunUtils.isItemSimilar(wrapper, UGItems.BIOMASS_BUCKET, true)
                || SlimefunUtils.isItemSimilar(wrapper, UGItems.DIESEL_BUCKET, true);
    }

    private MachineFuel findRecipe(BlockMenu menu, Map<Integer, Integer> found) {
        for (MachineFuel fuel : fuelTypes) {
            for (int slot : getInputSlots()) {
                if (fuel.test(menu.getItemInSlot(slot))) {
                    found.put(slot, fuel.getInput().getAmount());
                    return fuel;
                }
            }
        }

        return null;
    }

    /**
     * This method returns the max amount of electricity this machine can hold.
     *
     * @return The max amount of electricity this Block can store.
     */
    public int getCapacity() {
        return energyCapacity;
    }

    /**
     * This method returns the amount of energy that is consumed per operation.
     *
     * @return The rate of energy consumption
     */
    @Override
    public int getEnergyProduction() {
        return energyProducedPerTick;
    }

    /**
     * This sets the energy capacity for this machine.
     * This method <strong>must</strong> be called before registering the item
     * and only before registering.
     *
     * @param capacity
     *            The amount of energy this machine can store
     *
     * @return This method will return the current instance of {@link BGenerator}, so that can be chained.
     */
    public final BGenerator setCapacity(int capacity) {
        Validate.isTrue(capacity >= 0, "The capacity cannot be negative!");

        if (getState() == ItemState.UNREGISTERED) {
            this.energyCapacity = capacity;
            return this;
        } else {
            throw new IllegalStateException("You cannot modify the capacity after the Item was registered.");
        }
    }

    /**
     * This method sets the energy produced by this machine per tick.
     *
     * @param energyProduced
     *            The energy produced per tick
     *
     * @return This method will return the current instance of {@link BGenerator}, so that can be chained.
     */
    public final BGenerator setEnergyProduction(int energyProduced) {
        Validate.isTrue(energyProduced > 0, "The energy production must be greater than zero!");

        this.energyProducedPerTick = energyProduced;
        return this;
    }

    @Override
    public void register(SlimefunAddon addon) {
        this.addon = addon;

        if (getCapacity() < 0) {
            warn("The capacity has not been configured correctly. The Item was disabled.");
            warn("Make sure to call '" + getClass().getSimpleName() + "#setEnergyCapacity(...)' before registering!");
        }

        if (getEnergyProduction() <= 0) {
            warn("The energy consumption has not been configured correctly. The Item was disabled.");
            warn("Make sure to call '" + getClass().getSimpleName() + "#setEnergyProduction(...)' before registering!");
        }

        if (getCapacity() >= 0 && getEnergyProduction() > 0) {
            super.register(addon);
        }
    }
//
//    protected static final int INDICATOR = 22;
//    protected static final int MACHINE_INFO = 8;
//    private static final int[] border
//            = {
//            0,
//            9, 17,
//            18, 19, 20, 21, 23, 24, 25, 26,
//            27, 35,
//            36, 44
//    };
//    private static final int[] border_in
//            = {
//            1, 7,
//            10, 11, 12, 13, 14, 15, 16
//    };
//    private static final int[] border_out
//            = {
//            28, 29, 30, 31, 32, 33, 34,
//            37, 43
//    };
//
//    public int[] getInputSlots() {
//        return new int[]{2, 3, 4, 5, 6};
//    }
//
//    public int[] getOutputSlots() {
//        return new int[]{38, 39, 40, 41, 42};
//    }
//
//    public static Map<Location, MachineFuel> processing = new HashMap<>();
//    public static Map<Location, Integer> progress = new HashMap<>();
//    private final String ID;
//    private final boolean displayMachineInfo;
//    private final Set<MachineFuel> recipes = new HashSet<>();
//    protected BGenerator(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, boolean displayMachineInfo) {
//        super(itemGroup, item, id, recipeType, recipe);
//        ID = id;
//        this.displayMachineInfo = displayMachineInfo;
//        new BlockMenuPreset(id, getInventoryTitle()) {
//            @Override
//            public void init() {
//                constructMenu(this);
//            }
//
//            @Override
//            public boolean canOpen(Block b, Player p) {
//                return p.hasPermission("slimefun.inventory.bypass") || Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.INTERACT_BLOCK);
//            }
//
//            @Override
//            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
//                if (itemTransportFlow.equals(ItemTransportFlow.INSERT)) {
//                    return getInputSlots();
//                }
//                return getOutputSlots();
//            }
//        };
//
//        addItemHandler(onBlockBreak());
//        registerDefaultRecipes();
//    }
//
//    protected BlockBreakHandler onBlockBreak() {
//        return new SimpleBlockBreakHandler() {
//            public void onBlockBreak(Block b) {
//                BlockMenu inv = BlockStorage.getInventory(b);
//                if (inv != null) {
//                    for (int slot : getInputSlots()) {
//                        if (inv.getItemInSlot(slot) != null) {
//                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
//                            inv.replaceExistingItem(slot, null);
//                        }
//                    }
//                    for (int slot : getOutputSlots()) {
//                        if (inv.getItemInSlot(slot) != null) {
//                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
//                            inv.replaceExistingItem(slot, null);
//                        }
//                    }
//                }
//                progress.remove(b.getLocation());
//                processing.remove(b.getLocation());
//            }
//        };
//    }
//
//
//    private void constructMenu(BlockMenuPreset preset) {
//        for (int i : border) {
//            preset.addItem(i,
//                    new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " "),
//                    (arg0, arg1, arg2, arg3) -> false);
//        }
//        for (int i : border_in) {
//            preset.addItem(i,
//                    new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE, " "),
//                    (arg0, arg1, arg2, arg3) -> false);
//        }
//        for (int i : border_out) {
//            preset.addItem(i,
//                    new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE, " "),
//                    (arg0, arg1, arg2, arg3) -> false);
//        }
//        for (int i : getOutputSlots()) {
//            preset.addMenuClickHandler(i, new ChestMenu.AdvancedMenuClickHandler() {
//
//                @Override
//                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
//                    return false;
//                }
//
//                @Override
//                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
//                    if (cursor == null) return true;
//                    return cursor.getType() == Material.AIR;
//                }
//            });
//        }
//
//        preset.addItem(INDICATOR, new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " "), (Player arg0, int arg1, ItemStack arg2, ClickAction arg3) -> false);
//
//        if (displayMachineInfo) {
//            preset.addItem(MACHINE_INFO, new CustomItemStack(Material.MAP, "&f机器信息", "&7 - &3耗电量: &e" + getEnergyProduction() * 2 + " J/s", "&7 - &3工作速度: &e" + (getSpeed() == 1 ? "&f默认" : getSpeed())), (arg0, arg1, arg2, arg3) -> false);
//        } else {
//            preset.addItem(MACHINE_INFO, new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " "), (arg0, arg1, arg2, arg3) -> false);
//        }
//
//    }
//    public abstract void registerDefaultRecipes();
//
//    public abstract String getInventoryTitle();
//
//    public abstract int getEnergyProduction();
//
//    public abstract int getSpeed();
//
//    public abstract ItemStack getProgressBar();
//
//    public String getMachineIdentifier() {
//        return ID;
//    }
//
//    public MachineFuel getProcessing(Location l) {
//        return processing.get(l);
//    }
//
//    public boolean isProcessing(Location l) {
//        return progress.containsKey(l);
//    }
//
//    public void registerFuel(MachineFuel fuel) {
//        this.recipes.add(fuel);
//    }
//
//    public void register() {
////        addItemHandler(new EnergyTicker() {
////
////            @Override
////            public double generateEnergy(Location l, SlimefunItem sf, Config data) {
////
////                if (l.getBlock().getBlockPower() > 1) {
////                    return 0D;
////                }
////
////                if (isProcessing(l)) {
////                    int timeleft = progress.get(l);
////                    if (timeleft > 0) {
////                        ItemStack item = getProgressBar().clone();
////                        item.setDurability(MachineHelper.getDurability(item, timeleft, processing.get(l).getTicks()));
////                        ItemMeta im = item.getItemMeta();
////                        im.setDisplayName(" ");
////                        List<String> lore = new ArrayList<>();
////                        lore.add(MachineHelper.getProgress(timeleft, processing.get(l).getTicks()));
////                        lore.add("");
////                        lore.add(MachineHelper.getTimeLeft(timeleft / 2));
////                        im.setLore(lore);
////                        item.setItemMeta(im);
////
////                        BlockStorage.getInventory(l).replaceExistingItem(INDICATOR, item);
////
////                        if (ChargableBlock.isChargable(l)) {
////                            if (ChargableBlock.getMaxCharge(l) - ChargableBlock.getCharge(l) >= getEnergyProduction()) {
////                                ChargableBlock.addCharge(l, getEnergyProduction());
////                                progress.put(l, timeleft - 1);
////                                return ChargableBlock.getCharge(l);
////                            }
////                            return 0;
////                        } else {
////                            progress.put(l, timeleft - 1);
////                            return getEnergyProduction();
////                        }
////                    } else {
////                        ItemStack fuel = processing.get(l).getInput();
////                        if (SlimefunUtils.isItemSimilar(fuel, new ItemStack(Material.LAVA_BUCKET), true)) {
////                            pushItems(l, new ItemStack[]{new ItemStack(Material.BUCKET)});
////                        } else if (SlimefunUtils.isItemSimilar(fuel, SlimefunItems.FUEL_BUCKET, true)) {
////                            pushItems(l, new ItemStack[]{new ItemStack(Material.BUCKET)});
////                        } else if (SlimefunUtils.isItemSimilar(fuel, SlimefunItems.OIL_BUCKET, true)) {
////                            pushItems(l, new ItemStack[]{new ItemStack(Material.BUCKET)});
////                        } else if (SlimefunUtils.isItemSimilar(fuel, UGItems.BIOFUEL_BUCKET, true)) {
////                            pushItems(l, new ItemStack[]{new ItemStack(Material.BUCKET)});
////                        } else if (SlimefunUtils.isItemSimilar(fuel, UGItems.BIOMASS_BUCKET, true)) {
////                            pushItems(l, new ItemStack[]{new ItemStack(Material.BUCKET)});
////                        } else if (SlimefunUtils.isItemSimilar(fuel, UGItems.DIESEL_BUCKET, true)) {
////                            pushItems(l, new ItemStack[]{new ItemStack(Material.BUCKET)});
////                        }
////                        BlockStorage.getInventory(l).replaceExistingItem(INDICATOR, new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
////
////                        progress.remove(l);
////                        processing.remove(l);
////                        return 0;
////                    }
////                } else {
////                    MachineFuel r = null;
////                    Map<Integer, Integer> found = new HashMap<>();
////                    outer:
////                    for (MachineFuel recipe : recipes) {
////                        for (int slot : getInputSlots()) {
////                            if (SlimefunUtils.isItemSimilar(BlockStorage.getInventory(l).getItemInSlot(slot), recipe.getInput(), true)) {
////                                found.put(slot, recipe.getInput().getAmount());
////                                r = recipe;
////                                break outer;
////                            }
////                        }
////                    }
////
////                    if (r != null) {
////                        found.forEach((key, value) -> BlockStorage.getInventory(l).replaceExistingItem(key, Utils.decreaseItem(BlockStorage.getInventory(l).getItemInSlot(key), value)));
////                        processing.put(l, r);
////                        progress.put(l, r.getTicks());
////                    }
////                    return 0;
////                }
////            }
////
////        });
////
//        super.register(UltimateGenerators.getImplement());
//    }
//
//    public Set<MachineFuel> getFuelTypes() {
//        return this.recipes;
//    }
//
//    private Inventory inject(Location l) {
//        int size = BlockStorage.getInventory(l).toInventory().getSize();
//        Inventory inv = Bukkit.createInventory(null, size);
//        for (int i = 0; i < size; i++) {
//            inv.setItem(i, new CustomItemStack(Material.BEDROCK, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US"));
//        }
//        for (int slot : getOutputSlots()) {
//            inv.setItem(slot, BlockStorage.getInventory(l).getItemInSlot(slot));
//        }
//        return inv;
//    }
//
//    protected void pushItems(Location l, ItemStack[] items) {
//        Inventory inv = inject(l);
//        inv.addItem(items);
//
//        for (int slot : getOutputSlots()) {
//            BlockStorage.getInventory(l).replaceExistingItem(slot, inv.getItem(slot));
//        }
//    }
}
