package cn.rmc.ultimategenerators.object.abstracts;

import cn.rmc.ultimategenerators.UltimateGenerators;
import cn.rmc.ultimategenerators.Utils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.*;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineProcessHolder;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.operations.CraftingOperation;
import io.github.thebusybiscuit.slimefun4.libraries.dough.inventory.InvUtils;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BContainer extends SlimefunItem implements InventoryBlock, EnergyNetComponent, MachineProcessHolder<CraftingOperation>, RecipeDisplayItem {

    private static final int[] BORDER = {
                0,
                9, 17,
                18, 19, 20, 21, 23, 24, 25, 26,
                27, 35,
                36, 44
    };
    private static final int[] BORDER_IN = {
            1, 7,
            10, 11, 12, 13, 14, 15, 16
    };
    private static final int[] BORDER_OUT = {
            28, 29, 30, 31, 32, 33, 34,
            37, 43
    };

    protected final List<MachineRecipe> recipes = new ArrayList<>();
    private final MachineProcessor<CraftingOperation> processor = new MachineProcessor<>(this);

    private int energyConsumedPerTick = -1;
    private int energyCapacity = -1;
    private int processingSpeed = -1;
    private String ID;

    protected BContainer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        processor.setProgressBar(getProgressBar());
        createPreset(this, getInventoryTitle(), this::constructMenu);

        addItemHandler(onBlockBreak());
    }

    protected BlockBreakHandler onBlockBreak() {
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

    protected BContainer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        this(itemGroup, item, recipeType, recipe);
        this.ID = item.getItemId();
        this.recipeOutput = recipeOutput;
    }

    @Override
    public MachineProcessor<CraftingOperation> getMachineProcessor() {
        return processor;
    }

    @Override
    public Class<CraftingOperation> getMachineOperationClass() {
        return CraftingOperation.class;
    }

    protected void constructMenu(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : BORDER_IN) {
            preset.addItem(i, ChestMenuUtils.getInputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : BORDER_OUT) {
            preset.addItem(i, ChestMenuUtils.getOutputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
        }

        preset.addItem(22, new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " "), ChestMenuUtils.getEmptyClickHandler());

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
//        preset.addItem(8, new CustomItemStack(Material.MAP, "&f机器信息", "&7 - &3耗电量: &e" + getEnergyConsumption() * 2 + " J/s", "&7 - &3工作速度: &e" + (getSpeed() == 1 ? "&f默认" : getSpeed())), new ChestMenu.MenuClickHandler() {
//
//            @Override
//            public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
//                return false;
//            }
//
//        });
    }

    /**
     * This method returns the title that is used for the {@link Inventory} of an
     * {@link AContainer} that has been opened by a Player.
     *
     * Override this method to set the title.
     *
     * @return The title of the {@link Inventory} of this {@link AContainer}
     */
    public String getInventoryTitle() {
        return getItemName();
    }

    /**
     * This method returns the {@link ItemStack} that this {@link AContainer} will
     * use as a progress bar.
     *
     * Override this method to set the progress bar.
     *
     * @return The {@link ItemStack} to use as the progress bar
     */
    public abstract ItemStack getProgressBar();

    /**
     * This method returns the max amount of electricity this machine can hold.
     *
     * @return The max amount of electricity this Block can store.
     */
    @Override
    public int getCapacity() {
        return energyCapacity;
    }

    /**
     * This method returns the amount of energy that is consumed per operation.
     *
     * @return The rate of energy consumption
     */
    public int getEnergyConsumption() {
        return energyConsumedPerTick;
    }

    /**
     * This method returns the speed at which this machine will operate.
     * This can be implemented on an instantiation-level to create different tiers
     * of machines.
     *
     * @return The speed of this machine
     */
    public int getSpeed() {
        return processingSpeed;
    }

    /**
     * This sets the energy capacity for this machine.
     * This method <strong>must</strong> be called before registering the item
     * and only before registering.
     *
     * @param capacity
     *            The amount of energy this machine can store
     *
     * @return This method will return the current instance of {@link AContainer}, so that can be chained.
     */
    public final BContainer setCapacity(int capacity) {
        Validate.isTrue(capacity > 0, "The capacity must be greater than zero!");

        if (getState() == ItemState.UNREGISTERED) {
            this.energyCapacity = capacity;
            return this;
        } else {
            throw new IllegalStateException("You cannot modify the capacity after the Item was registered.");
        }
    }

    /**
     * This sets the speed of this machine.
     *
     * @param speed
     *            The speed multiplier for this machine, must be above zero
     *
     * @return This method will return the current instance of {@link AContainer}, so that can be chained.
     */
    public final BContainer setProcessingSpeed(int speed) {
        Validate.isTrue(speed > 0, "The speed must be greater than zero!");

        this.processingSpeed = speed;
        return this;
    }

    /**
     * This method sets the energy consumed by this machine per tick.
     *
     * @param energyConsumption
     *            The energy consumed per tick
     *
     * @return This method will return the current instance of {@link AContainer}, so that can be chained.
     */
    public final BContainer setEnergyConsumption(int energyConsumption) {
        Validate.isTrue(energyConsumption > 0, "The energy consumption must be greater than zero!");
        Validate.isTrue(energyCapacity > 0, "You must specify the capacity before you can set the consumption amount.");
        Validate.isTrue(energyConsumption <= energyCapacity, "The energy consumption cannot be higher than the capacity (" + energyCapacity + ')');

        this.energyConsumedPerTick = energyConsumption;
        return this;
    }

    @Override
    public void register(SlimefunAddon addon) {
        this.addon = addon;

        if (getCapacity() <= 0) {
            warn("The capacity has not been configured correctly. The Item was disabled.");
            warn("Make sure to call '" + getClass().getSimpleName() + "#setEnergyCapacity(...)' before registering!");
        }

        if (getEnergyConsumption() <= 0) {
            warn("The energy consumption has not been configured correctly. The Item was disabled.");
            warn("Make sure to call '" + getClass().getSimpleName() + "#setEnergyConsumption(...)' before registering!");
        }

        if (getSpeed() <= 0) {
            warn("The processing speed has not been configured correctly. The Item was disabled.");
            warn("Make sure to call '" + getClass().getSimpleName() + "#setProcessingSpeed(...)' before registering!");
        }

        if (getCapacity() > 0 && getEnergyConsumption() > 0 && getSpeed() > 0) {
            super.register(addon);
        }

        // Fixes #3429 - Initialize Item Settings before recipes
        registerDefaultRecipes();
    }

    /**
     * This method returns an internal identifier that is used to identify this {@link AContainer}
     * and its recipes.
     *
     * When adding recipes to an {@link AContainer} we will use this identifier to
     * identify all instances of the same {@link AContainer}.
     * This way we can add the recipes to all instances of the same machine.
     *
     * <strong>This method will be deprecated and replaced in the future</strong>
     *
     * @return The identifier of this machine
     */
    public String getMachineIdentifier(){
        return ID;
    };

    /**
     * This method registers all default recipes for this machine.
     */
    protected abstract void registerDefaultRecipes();

    public List<MachineRecipe> getMachineRecipes() {
        return recipes;
    }

    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(recipes.size() * 2);

        for (MachineRecipe recipe : recipes) {
            if (recipe.getInput().length != 1) {
                continue;
            }

            displayRecipes.add(recipe.getInput()[0]);
            displayRecipes.add(recipe.getOutput()[0]);
        }

        return displayRecipes;
    }

    public int[] getInputSlots() {
        return new int[]{2, 3, 4, 5, 6};
    }

    public int[] getOutputSlots() {
        return new int[]{38, 39, 40, 41, 42};
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    public void registerRecipe(MachineRecipe recipe) {
        recipe.setTicks(recipe.getTicks() / getSpeed());
        recipes.add(recipe);
    }

    public void registerRecipe(int seconds, ItemStack[] input, ItemStack[] output) {
        registerRecipe(new MachineRecipe(seconds, input, output));
    }

    public void registerRecipe(int seconds, ItemStack input, ItemStack output) {
        registerRecipe(new MachineRecipe(seconds, new ItemStack[] { input }, new ItemStack[] { output }));
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, SlimefunBlockData data) {
                BContainer.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return false;
            }
        });
    }

    protected void tick(Block b) {
        BlockMenu inv = BlockStorage.getInventory(b);
        CraftingOperation currentOperation = processor.getOperation(b);

        if (currentOperation != null) {
            if (takeCharge(b.getLocation())) {

                if (!currentOperation.isFinished()) {
                    processor.updateProgressBar(inv, 22, currentOperation);
                    currentOperation.addProgress(1);
                } else {
                    inv.replaceExistingItem(22, new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));

                    for (ItemStack output : currentOperation.getResults()) {
                        inv.pushItem(output.clone(), getOutputSlots());
                    }

                    processor.endOperation(b);
                }
            }
        } else {
            MachineRecipe next = findNextRecipe(inv);

            if (next != null) {
                processor.startOperation(b, new CraftingOperation(next));
            }
        }
    }

    /**
     * This method will remove charge from a location if it is chargeable.
     *
     * @param l
     *            location to try to remove charge from
     * @return Whether charge was taken if its chargeable
     */
    protected boolean takeCharge(Location l) {
        Validate.notNull(l, "Can't attempt to take charge from a null location!");

        if (isChargeable()) {
            int charge = getCharge(l);

            if (charge < getEnergyConsumption()) {
                return false;
            }

            setCharge(l, charge - getEnergyConsumption());
            return true;
        } else {
            return true;
        }
    }

    protected MachineRecipe findNextRecipe(BlockMenu inv) {
        Map<Integer, ItemStack> inventory = new HashMap<>();

        for (int slot : getInputSlots()) {
            ItemStack item = inv.getItemInSlot(slot);

            if (item != null) {
                inventory.put(slot, ItemStackWrapper.wrap(item));
            }
        }

        Map<Integer, Integer> found = new HashMap<>();

        for (MachineRecipe recipe : recipes) {
            for (ItemStack input : recipe.getInput()) {
                for (int slot : getInputSlots()) {
                    if (SlimefunUtils.isItemSimilar(inventory.get(slot), input, true)) {
                        found.put(slot, input.getAmount());
                        break;
                    }
                }
            }

            if (found.size() == recipe.getInput().length) {
                if (!InvUtils.fitAll(inv.toInventory(), recipe.getOutput(), getOutputSlots())) {
                    return null;
                }

                for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
                    inv.consumeItem(entry.getKey(), entry.getValue());
                }

                return recipe;
            } else {
                found.clear();
            }
        }

        return null;
    }
//    public static Map<Block, MachineRecipe> processing = new HashMap<>();
//    public static Map<Block, Integer> progress = new HashMap<>();
//    protected List<MachineRecipe> recipes = new ArrayList<>();
//
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
//    protected static final int indicator = 22;
//    protected static final int machineInfo = 8;
//
//    private final String ID;
//    private final boolean displayMachineInfo;
//
//    protected BContainer(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, boolean displayMachineInfo) {
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
//                BContainer.progress.remove(b);
//                BContainer.processing.remove(b);
//            }
//        };
//    }
//
//
//
//    protected void constructMenu(BlockMenuPreset preset) {
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
//        preset.addItem(indicator, new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " "), (arg0, arg1, arg2, arg3) -> false);
//        for (int i : getOutputSlots()) {
//            preset.addMenuClickHandler(i, new ChestMenu.AdvancedMenuClickHandler() {
//                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
//                    return false;
//                }
//
//                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor,
//                                       ClickAction action) {
//                    if ((cursor == null)) return true;
//                    return cursor.getType() == Material.AIR;
//                }
//            });
//        }
//
//        if (displayMachineInfo) {
//            preset.addItem(machineInfo, new CustomItemStack(Material.MAP, "&f机器信息", "&7 - &3耗电量: &e" + getEnergyConsumption() * 2 + " J/s", "&7 - &3工作速度: &e" + (getSpeed() == 1 ? "&f默认" : getSpeed())), (arg0, arg1, arg2, arg3) -> false);
//        } else {
//            preset.addItem(machineInfo, new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " "), (arg0, arg1, arg2, arg3) -> false);
//        }
//    }
//
//    public abstract ItemStack getProgressBar();
//
//    public abstract void registerDefaultRecipes();
//
//    public abstract String getInventoryTitle();
//
//    public abstract int getEnergyConsumption();
//
//    public int getSpeed() {
//        return 1;
//    }
//
//    public String getMachineIdentifier() {
//        return ID;
//    }
//
//    public MachineRecipe getProcessing(Block b) {
//        return processing.get(b);
//    }
//
//    public boolean isProcessing(Block b) {
//        return getProcessing(b) != null;
//    }
//
//    public void registerRecipe(MachineRecipe recipe) {
//        recipe.setTicks(recipe.getTicks() / getSpeed());
//        recipes.add(recipe);
//    }
//
//    public void registerRecipe(int seconds, ItemStack[] input, ItemStack[] output) {
//        registerRecipe(new MachineRecipe(seconds, input, output));
//    }
//
//    private Inventory inject(Block b) {
//        int size = BlockStorage.getInventory(b).toInventory().getSize();
//        Inventory inv = Bukkit.createInventory(null, size);
//        for (int i = 0; i < size; i++) {
//            inv.setItem(i, new CustomItemStack(Material.BEDROCK, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US"));
//        }
//        for (int slot : getOutputSlots()) {
//            inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
//        }
//        return inv;
//    }
//
//    protected boolean fits(Block b, ItemStack[] items) {
//        return !inject(b).addItem(items).isEmpty();
//    }
//
//    protected void pushItems(Block b, ItemStack[] items) {
//        Inventory inv = inject(b);
//        inv.addItem(items);
//        for (int slot : getOutputSlots()) {
//            BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
//        }
//    }
//
//    public void register() {
//        addItemHandler(new BlockTicker() {
//
//            public void uniqueTick() {
//            }
//
//            public boolean isSynchronized() {
//                return false;
//            }
//
//            @Override
//            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
//                BContainer.this.tick(block);
//            }
//        });
//        super.register(UltimateGenerators.getImplement());
//    }
//
//    protected void tick(Block b) {
//
//        if (b.getBlockPower() > 1) {
//            return;
//        }
//
//        if (isProcessing(b)) {
//            int timeleft = progress.get(b);
//            if (timeleft > 0) {
//
//                ChestMenuUtils.updateProgressbar(BlockStorage.getInventory(b),
//                        indicator,timeleft,processing.get(b).getTicks(),getProgressBar().clone());
////                ItemStack item = getProgressBar().clone();
////                item.setDurability(MachineHelper.getDurability(item, timeleft, processing.get(b).getTicks()));
////                im = item.getItemMeta();
////                im.setDisplayName(" ");
////                List<String> lore = new ArrayList<>();
////                lore.add(MachineHelper.getProgress(timeleft, processing.get(b).getTicks()));
////                lore.add("");
////                lore.add(MachineHelper.getTimeLeft(timeleft / 2));
////                im.setLore(lore);
////                item.setItemMeta(im);
////
////                BlockStorage.getInventory(b).replaceExistingItem(indicator, item);
//                if (isChargeable()) {
//                    if (getCharge(b.getLocation()) < getEnergyConsumption()) {
//                        return;
//                    }
//                    addCharge(b.getLocation(), -getEnergyConsumption());
//                }
//                progress.put(b, timeleft - 1);
//            } else {
//                BlockStorage.getInventory(b).replaceExistingItem(indicator,
//                        new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
//                pushItems(b, processing.get(b).getOutput().clone());
//
//                progress.remove(b);
//                processing.remove(b);
//            }
//        } else {
//            MachineRecipe r = null;
//            Map<Integer, Integer> found = new HashMap<>();
//            for (MachineRecipe recipe : recipes) {
//                for (ItemStack input : recipe.getInput()) {
//                    for (int slot : getInputSlots()) {
//                        if (SlimefunUtils.isItemSimilar(BlockStorage.getInventory(b).getItemInSlot(slot), input,
//                                true)) {
//                            found.put(slot, input.getAmount());
//                            break;
//                        }
//                    }
//                }
//                if (found.size() == recipe.getInput().length) {
//                    r = recipe;
//                    break;
//                }
//                found.clear();
//            }
//            if (r != null) {
//                if (fits(b, r.getOutput())) {
//                    return;
//                }
//                for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
//                    BlockStorage.getInventory(b).replaceExistingItem(entry.getKey(),
//                            Utils.decreaseItem(
//                                    BlockStorage.getInventory(b).getItemInSlot(entry.getKey()),
//                                    entry.getValue()));
//                }
//                processing.put(b, r);
//                progress.put(b, r.getTicks());
//            }
//        }
//    }

}
