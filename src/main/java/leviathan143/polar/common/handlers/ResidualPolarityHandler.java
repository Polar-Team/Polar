package leviathan143.polar.common.handlers;

import leviathan143.polar.api.CommonWords;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.advancements.triggers.TriggerRegistry;
import leviathan143.polar.common.capabilities.CapabilityPlayerDataPolar.PlayerDataPolar;
import leviathan143.polar.common.items.IPolarisedItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Polar.MODID)
public class ResidualPolarityHandler
{
	@SubscribeEvent
	public static void handleArmour(LivingAttackEvent event)
	{
		/*Ignore unblockable damage sources because they ignore armour, so are not considered to
		 * "activate" it; and to avoid stack overflow*/
		if (event.getEntityLiving() instanceof EntityPlayer && !event.getSource().isUnblockable())
		{
			for (ItemStack armour : event.getEntityLiving().getArmorInventoryList())
			{
				if (isPolarisedItem(armour))
				{
					itemActivated(armour, (EntityPlayer) event.getEntityLiving());
				}
			}
		}
	}

	@SubscribeEvent
	public static void handleAttackEntity(AttackEntityEvent event)
	{
		if (isPolarisedItem(event.getEntityPlayer().getHeldItemMainhand()))
			itemActivated(event.getEntityPlayer().getHeldItemMainhand(), event.getEntityPlayer());
	}

	@SubscribeEvent
	public static void handleAttackBlock(PlayerInteractEvent.LeftClickBlock event)
	{
		if (isPolarisedItem(event.getItemStack()))
			itemActivated(event.getItemStack(), event.getEntityPlayer());
	}
	
	@SubscribeEvent
	public static void handleUseItem(PlayerInteractEvent.RightClickItem event)
	{
		if (isPolarisedItem(event.getItemStack()))
			itemActivated(event.getItemStack(), event.getEntityPlayer());
	}
	
	@SubscribeEvent
	public static void handleUseItemOnBlock(PlayerInteractEvent.RightClickBlock event)
	{
		if (isPolarisedItem(event.getItemStack()))
			itemActivated(event.getItemStack(), event.getEntityPlayer());
	}
	
	@SubscribeEvent
	public static void handleUseItemOnEntity(PlayerInteractEvent.EntityInteract event)
	{
		if (isPolarisedItem(event.getItemStack()))
			itemActivated(event.getItemStack(), event.getEntityPlayer());
	}
	
	private static boolean isPolarisedItem(ItemStack stack)
	{
		if (stack.getItem() instanceof IPolarisedItem) return true;
		if (!stack.hasTagCompound()) return false;
		return stack.getTagCompound().hasKey(CommonWords.POLARITY);
	}
	
	public static void itemActivated(ItemStack stack, EntityPlayer player)
	{
		PlayerDataPolar playerData = PlayerDataPolar.get(player);
		Polarity residualCharge = playerData.getResidualPolarity();
		Polarity itemPolarity = stack.getItem() instanceof IPolarisedItem 
			? ((IPolarisedItem) stack.getItem()).getPolarity(stack)
			: Enum.valueOf(Polarity.class, stack.getTagCompound().getString(CommonWords.POLARITY).toUpperCase());
		// Ifs are nested to avoid shocking if a residual charge is not left, regardless of polarities
		if (residualCharge == Polarity.NONE)
		{
			// 1 in 10 activations should leave a residual charge
			if (player.world.rand.nextFloat() <= 0.1F)
				playerData.setResidualPolarity(itemPolarity);
		}
		else if (residualCharge != itemPolarity)
		{
			// Damage source must be unblockable to avoid stack overflow
			player.attackEntityFrom(DamageSource.MAGIC, 0.5F);
			playerData.setResidualPolarity(Polarity.NONE);
			if (player instanceof EntityPlayerMP)
				TriggerRegistry.POLAR_REACTION.trigger((EntityPlayerMP) player, 0);
		}
	}
}
