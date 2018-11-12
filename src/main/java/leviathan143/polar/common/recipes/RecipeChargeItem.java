package leviathan143.polar.common.recipes;

import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.api.capabilities.IPolarChargeStorage;
import leviathan143.polar.common.items.ItemRegistry;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class RecipeChargeItem extends Impl<IRecipe> implements IRecipe
{
	private static final int CHARGE_VALUE = 16;
	
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		boolean redFound = false, 
				blueFound = false; 
		ItemStack item = null;
		for (int s = 0; s < inv.getSizeInventory(); s++)
		{
			ItemStack stack = inv.getStackInSlot(s);
			if (stack.getItem() == ItemRegistry.RED_IRRADIATED_REDSTONE)
				redFound = true;
			else if (stack.getItem() == ItemRegistry.BLUE_IRRADIATED_LAPIS)
				blueFound = true;
			else if (stack.hasCapability(PolarAPI.CAPABILITY_CHARGEABLE, null))
				item = stack;
		}
		if (item != null)
		{
			if (redFound && blueFound)
				return false;
			Polarity itemPolarity = item.getCapability(PolarAPI.CAPABILITY_CHARGEABLE, null).getPolarity();
			return (itemPolarity == Polarity.RED && redFound) || (itemPolarity == Polarity.BLUE && blueFound); 
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack item = null;
		int chargeSources = 0;
		for (int s = 0; s < inv.getSizeInventory(); s++)
		{
			ItemStack stack = inv.getStackInSlot(s);
			if (stack.getItem() == ItemRegistry.RED_IRRADIATED_REDSTONE || stack.getItem() == ItemRegistry.BLUE_IRRADIATED_LAPIS)
				chargeSources++;
			else if (stack.hasCapability(PolarAPI.CAPABILITY_CHARGEABLE, null))
				item = stack.copy();
		}
		// The item must exist, since matches() must return true for this method to be called
		@SuppressWarnings("null")
		IPolarChargeStorage chargeable = item.getCapability(PolarAPI.CAPABILITY_CHARGEABLE, null);
		int remainder = chargeable.charge(chargeable.getPolarity(), CHARGE_VALUE * chargeSources, true);
		if (remainder == 0)
			chargeable.charge(chargeable.getPolarity(), CHARGE_VALUE * chargeSources, false);
		return item;
	}

	@Override
	public boolean isDynamic()
	{
		return true;
	}
	
	@Override
	public boolean canFit(int width, int height)
	{
		return width * height >= 9; 
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}
}