package cn.rmc.ultimategenerators.object.machines;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import com.xzavier0722.mc.plugin.slimefun4.storage.controller.ASlimefunDataContainer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class EndlessGenerator extends SlimefunItem implements EnergyNetProvider {
    public EndlessGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public int getGeneratedOutput(Location location, ASlimefunDataContainer data) {
        return location.getBlock().getBlockPower()*256;
    }

    @Override
    public int getCapacity() {
        return 128;
    }
}
