package me.korinku.moga.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class BlockBreaker {

	/**
	 * Attempt to break blocks around the given pos in a 3x3x1 square relative to
	 * the targeted face.
	 */
	public static void areaAttempt(World world,
			BlockPos pos,
			PlayerEntity player,
			boolean checkHarvestLevel) {

		HitResult trace = calcRayTrace(world, player, RaycastContext.FluidHandling.ANY, 0.0);

		if (trace.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockTrace = (BlockHitResult) trace;
			Direction face = blockTrace.getSide();

			for (int a = -1; a <= 1; a++) {
				for (int b = -1; b <= 1; b++) {
					if (a == 0 && b == 0)
						continue;

					BlockPos target = null;

					if (face == Direction.UP || face == Direction.DOWN)
						target = pos.add(a, 0, b);
					if (face == Direction.NORTH || face == Direction.SOUTH)
						target = pos.add(a, b, 0);
					if (face == Direction.EAST || face == Direction.WEST)
						target = pos.add(0, a, b);

					attemptBreak(world, target, player, checkHarvestLevel);
				}
			}
		}
	}

	/**
	 * Attempt to break a block. Fails if the tool is not effective on the given
	 * block, or if the origin broken block
	 * is significantly easier to break than the target block (i.e. don't let
	 * players use sandstone to quickly area
	 * break obsidian).
	 */
	public static void attemptBreak(World world,
			BlockPos target,
			PlayerEntity player,
			boolean checkHarvestLevel) {

		BlockState state = world.getBlockState(target);

		if (checkHarvestLevel) {
			return; // We are checking harvest level and this tool doesn't qualify.
		}

		// Prevent players from using low-hardness blocks to quickly break adjacent
		// high-hardness blocks.

		Block block = state.getBlock();
		block.onBreak(world, target, state, player);
		world.breakBlock(target, true);

	}

	/**
	 * Copy-pasted from "Item.rayTrace" which is protected static, making it
	 * unusable in my own static helper methods.
	 */
	public static HitResult calcRayTrace(World worldIn, PlayerEntity player,
			RaycastContext.FluidHandling fluidMode, double addition) {

		MinecraftClient client = MinecraftClient.getInstance();

		float f = player.getPitch();
		float f1 = player.getYaw();
		Vec3d vector3d = player.getEyePos();
		float f2 = MathHelper.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = MathHelper.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * ((float) Math.PI / 180F));
		float f5 = MathHelper.sin(-f * ((float) Math.PI / 180F));
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d0 = client.interactionManager.getReachDistance() + addition;
		Vec3d vector3d1 = vector3d.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
		return worldIn
				.raycast(new RaycastContext(vector3d, vector3d1, RaycastContext.ShapeType.OUTLINE, fluidMode, player));
	}
}
