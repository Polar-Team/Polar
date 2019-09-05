package io.github.daomephsta.polar.common.handlers.wearables;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.capabilities.IPolarChargeStorage;
import io.github.daomephsta.polar.common.CompatibilityTags;
import io.github.daomephsta.polar.common.blocks.BlockRegistry;
import io.github.daomephsta.polar.common.config.PolarConfig;
import io.github.daomephsta.polar.common.handlers.ResidualPolarityHandler;
import io.github.daomephsta.polar.common.tileentities.StabilisedBlockBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FallingBlockStabiliserHandler
{
//	static void stabiliseFallingBlocks(BlockEvent.BreakEvent event)
//	{
//		boolean realPlayer = event.getPlayer() != null
//				&& !(event.getPlayer() instanceof FakePlayer);
//		if (!realPlayer)
//			return;
//
//		IBlockState stateAbove = event.getWorld()
//				.getBlockState(event.getPos().up());
//		boolean unstableBlock = isUnstableBlock(stateAbove);
//		if (!unstableBlock)
//			return;
//		ItemStack baubleStack = BaubleHandler.findEquippedBauble(
//				event.getPlayer(), ItemRegistry.FALLING_BLOCK_STABILISER);
//		if (baubleStack.isEmpty())
//			return;
//
//		FallingBlockStabiliserHandler.placeStabilisedBlock(event.getPlayer(),
//				baubleStack, event.getWorld(), event.getPos().up(), stateAbove);
//	}

	public static boolean isUnstableBlock(BlockState state)
	{
		return CompatibilityTags.GRAVITY_AFFECTED.contains(state.getBlock());
	}

	public static boolean placeStabilisedBlock(PlayerEntity player, ItemStack baubleStack, World world, BlockPos pos, BlockState camo)
	{
		IPolarChargeStorage chargeable = port.Dummy.CHARGE_STORAGE;
		int cost = PolarConfig.charge.graviticStabiliserActivationCost;
		if (WearablesHandler.checkCharge(player, baubleStack, Polarity.RED, cost, PolarConfig.charge.graviticStabiliserActivationCost * 8))
		{
			chargeable.discharge(Polarity.RED, cost, false);
			ResidualPolarityHandler.itemActivated(baubleStack, player);
			world.setBlockState(pos,
					BlockRegistry.STABILISED_BLOCK.getDefaultState());
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof StabilisedBlockBlockEntity)
				((StabilisedBlockBlockEntity) be).setCamoBlockState(camo);
			return true;
		}
		return false;
	}
}
