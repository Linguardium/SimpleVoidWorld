package me.modmuss50.svw;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class BlockPortal extends Block {
	public BlockPortal() {
		super(Settings.of(Material.AIR));
	}

	@Override
	public ActionResult onUse(BlockState stateBlock, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
		if (!world.isClient) {
			ServerWorld serverWorld = (ServerWorld) playerEntity.getEntityWorld();
			if (serverWorld.getRegistryKey() == SimpleVoidWorld.VOID_WORLD) {
				FabricDimensions.teleport(playerEntity, serverWorld.getServer().getWorld(World.OVERWORLD), VoidPlacementHandler.leave(blockPos));
			} else {
				ServerWorld voidWorld = serverWorld.getServer().getWorld(SimpleVoidWorld.VOID_WORLD);
				if (voidWorld == null) {
					playerEntity.sendMessage(new LiteralText("Failed to find void world, was it registered?"), false);
					return ActionResult.FAIL;
				}
				FabricDimensions.teleport(playerEntity, voidWorld, VoidPlacementHandler.enter(blockPos));
			}
		}
		return ActionResult.SUCCESS;
	}
}
