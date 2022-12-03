package me.korinku.moga.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.korinku.moga.content.entities.EndProjectileEntity;
import me.korinku.moga.content.entities.NewArrowEntity;
import me.korinku.moga.utils.BlockBreaker;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.explosion.Explosion.DestructionType;

@Mixin({ PlayerEntity.class })
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
	public void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {

		PlayerEntity player = (PlayerEntity) (Object) this;

		if (player.getScoreboardTags().contains("moga.creeper")) {
			if (damageSource.isMagic()) {
				cir.setReturnValue(true);
			}
		}
	}

	private int ticks;

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		PlayerEntity player = (PlayerEntity) (Object) this;

		if (player.getScoreboardTags().contains("moga.creeper")) {
			if (player.isSneaking()) {
				ticks++;
				if (ticks == 40)
					world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_CREEPER_PRIMED,
							SoundCategory.NEUTRAL,
							1.0f, 0.5f);
				if (ticks > 60) {
					world.createExplosion(null, DamageSource.MAGIC, null, player.getX(), player.getY(), player.getZ(),
							10,
							false, DestructionType.BREAK);
					ticks = 0;
				}
			} else {
				ticks = 0;
			}
		}

		if (player.getScoreboardTags().contains("moga.rabbit")) {
			player.addStatusEffect(
					new StatusEffectInstance(StatusEffects.JUMP_BOOST, 10, 10, false, false, false));
		}

		if (player.getScoreboardTags().contains("moga.rage")) {
			ticks++;
			if (ticks > 10) {
				HitResult trace = BlockBreaker.calcRayTrace(world, player, RaycastContext.FluidHandling.ANY, 100.0);

				if (trace.getType() == HitResult.Type.BLOCK) {
					BlockHitResult blockTrace = (BlockHitResult) trace;
					BlockPos pos = blockTrace.getBlockPos();

					world.createExplosion(null, DamageSource.GENERIC, null, pos.getX(), pos.getY(), pos.getZ(),
							10,
							false, DestructionType.BREAK);
				}
				ticks = 0;
			}
		}

		if (player.getScoreboardTags().contains("moga.mining")) {

			if (MinecraftClient.getInstance().options.useKey.isPressed() && player.getMainHandStack().isEmpty()) {
				ticks++;
				if (ticks > 5) {
					HitResult trace = BlockBreaker.calcRayTrace(world, player, RaycastContext.FluidHandling.ANY, 5.0);

					if (trace.getType() == HitResult.Type.BLOCK) {
						BlockHitResult blockTrace = (BlockHitResult) trace;
						BlockPos pos = blockTrace.getBlockPos();

						world.breakBlock(pos, true);
						BlockBreaker.areaAttempt(world, pos, player, false);
					}
					ticks = 0;
				}
			}

			if (MinecraftClient.getInstance().options.attackKey.isPressed()) {
				HitResult trace = BlockBreaker.calcRayTrace(world, player, RaycastContext.FluidHandling.ANY, 5.0);

				if (trace.getType() == HitResult.Type.BLOCK) {
					BlockHitResult blockTrace = (BlockHitResult) trace;
					BlockPos pos = blockTrace.getBlockPos();

					world.breakBlock(pos, true);
				}
				ticks = 0;

			}
			player.addStatusEffect(
					new StatusEffectInstance(StatusEffects.SPEED, 10, 2, false, false, false));
		}

		if (player.getScoreboardTags().contains("moga.enderman")) {

			// * Passiva

			if (player.getScoreboardTeam() == null) {
				player.getScoreboard().addTeam("enderman");
				player.getScoreboard().addPlayerToTeam(player.getName().getString(),
						player.getScoreboard().getTeams().stream().findFirst().get());
			} else if (player.getScoreboardTeam().getName().equals("spawner")) {
				world.getScoreboard().removeTeam(player.getScoreboard().getTeams().stream().findFirst().get());
			}

			if (MinecraftClient.getInstance().options.useKey.isPressed()
					&& MinecraftClient.getInstance().options.sneakKey.isPressed()) {
				ticks++;
				if (ticks > 20) {
					float damage = (float) ((player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)
							+ EnchantmentHelper.getAttackDamage(player.getMainHandStack(), EntityGroup.DEFAULT)) * 1.5f
							* 0.2f * 0);

					for (int i = 0; i < 1; i++) {
						EndProjectileEntity projectile = new EndProjectileEntity(world, player);
						projectile.refreshPositionAndAngles(player.getX(), player.getEyeY(), player.getZ(), 0,
								0);
						projectile.setVelocity(player, player.getPitch(), player.getYaw() + 5 * i, 0f, 1.5f, 1);
						projectile.setDamage(damage);
						projectile.setOwner(player);

						player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f,
								1.0f);
						world.spawnEntity(projectile);
					}

					ticks = 0;
				}

			}

			if (MinecraftClient.getInstance().options.useKey.isPressed()
					&& !MinecraftClient.getInstance().options.sneakKey.isPressed() && player.getMainHandStack()
							.isEmpty()) {
				ticks++;
				if (ticks > 20) {
					HitResult trace = BlockBreaker.calcRayTrace(world, player, RaycastContext.FluidHandling.ANY, 60.0);

					if (trace.getType() == HitResult.Type.BLOCK) {
						BlockHitResult blockTrace = (BlockHitResult) trace;
						BlockPos pos = blockTrace.getBlockPos();

						player.teleport(pos.getX(), pos.getY() + 1, pos.getZ());
						player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
					}
					ticks = 0;
				}
			}

		}

		if (player.getScoreboardTags().contains("moga.chunk")) {

			if (MinecraftClient.getInstance().options.attackKey.isPressed()
					&& player.getMainHandStack().getName().toString().contains("pickaxe")) {
				ticks++;
				if (ticks > 5) {
					BlockPos positionClicked = player.getBlockPos();
					boolean found = false;

					final List<String> blockName = new ArrayList<>();

					final List<String> alreadyExists = new ArrayList<>();

					Chunk chunk = world.getChunk(positionClicked);

					// ! - PERCORRER CHUNK TODO
					for (int x = chunk.getPos().x * 16; x <= chunk.getPos().x * 16 + 16; x++)
						for (int z = chunk.getPos().z * 16; z <= chunk.getPos().z * 16 + 16; z++)
							for (int i = 0; i <= positionClicked.getY() + 64; i++) {
								BlockPos chunkPos = new BlockPos(x, positionClicked.down(i).getY(), z);
								Block block = chunk.getBlockState(chunkPos).getBlock();

								if (block.asItem().getName().toString().contains("ore")
										&& block != Blocks.LAPIS_ORE && block != Blocks.DEEPSLATE_LAPIS_ORE) {
									found = true;
									blockName.add(block.asItem().getName().getString());
									break;
								}
							}

					if (found) {
						player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
						player.sendMessage(
								Text.literal(
										"Minérios encontrados no chunk:")
										.formatted(Formatting.BOLD),
								false);
						for (int i = 0; i < blockName.size(); i++) {
							if (!alreadyExists.contains(blockName.get(i))) {
								player.sendMessage(
										Text.literal(
												"    - " + blockName.get(i))
												.formatted(Formatting.ITALIC),
										false);
								alreadyExists.add(blockName.get(i));
							}
						}

					}
					blockName.clear();
					alreadyExists.clear();
					ticks = 0;
				}
			}

			if (MinecraftClient.getInstance().options.sneakKey.isPressed()) {
				ticks++;
				if (ticks > 20) {
					BlockPos pos = player.getBlockPos();
					int rad = 5;
					for (int y = -rad; y < rad; y++)
						for (int x = -rad; x < rad; x++)
							for (int z = -rad; z < rad; z++)
								if (Math.sqrt((x * x) + (y * y) + (z * z)) <= rad) {
									BlockPos newBlockPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
									Block block = world.getBlockState(newBlockPos).getBlock();
									if (!block.getDefaultState().isAir()
											&& !block.asItem().getName().toString().contains("ore")) {
										world.breakBlock(newBlockPos, false);
									}
								}
					ticks = 0;
				}
			}

			if (MinecraftClient.getInstance().options.useKey.isPressed() && player.getMainHandStack().isEmpty()) {
				ticks++;
				if (ticks > 20) {

					HitResult trace = BlockBreaker.calcRayTrace(world, player, RaycastContext.FluidHandling.ANY, 40.0);

					if (trace.getType() == HitResult.Type.BLOCK) {
						BlockHitResult blockTrace = (BlockHitResult) trace;
						BlockPos pos = blockTrace.getBlockPos();

						int rad = 3;
						for (int y = -rad; y < rad; y++)
							for (int x = -rad; x < rad; x++)
								for (int z = -rad; z < rad; z++)
									if (Math.sqrt((x * x) + (y * y) + (z * z)) <= rad) {
										BlockPos newBlockPos = new BlockPos(pos.getX() + x, pos.getY() + y,
												pos.getZ() + z);
										Block block = world.getBlockState(newBlockPos).getBlock();
										if (block.getDefaultState().isAir()) {
											world.setBlockState(newBlockPos, Blocks.STONE.getDefaultState());
										}
									}
					}
					ticks = 0;
				}
			}
		}

		if (player.getScoreboardTags().contains("moga.bow")) {
			if (MinecraftClient.getInstance().options.useKey.isPressed()
					&& MinecraftClient.getInstance().options.sneakKey.isPressed()) {
				ticks++;
				if (ticks > 5) {
					for (int i = 0; i < 1; i++) {
						NewArrowEntity projectile = new NewArrowEntity(world, player);
						projectile.refreshPositionAndAngles(player.getX(), player.getEyeY(), player.getZ(), 0, 0);
						projectile.setVelocity(player, player.getPitch(), player.getYaw() + 5 * i, 0f, 3.5f, 1);

						int rnd = Random.create().nextBetween(1, 100);

						if (rnd == 1)
							projectile.setDamage(1000.0);
						else {
							projectile.setDamage(3.0);
						}

						player.playSound(SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f);
						world.spawnEntity(projectile);
					}
					ticks = 0;
				}

			}

		}

		if (player.getScoreboardTags().contains("moga.spawner")) {

			// * Passiva

			if (player.getScoreboardTeam() == null) {
				player.getScoreboard().addTeam("spawner");
				player.getScoreboard().addPlayerToTeam(player.getName().getString(),
						player.getScoreboard().getTeams().stream().findFirst().get());
			} else if (player.getScoreboardTeam().getName().equals("enderman")) {
				world.getScoreboard().removeTeam(player.getScoreboard().getTeams().stream().findFirst().get());
			}

			if (MinecraftClient.getInstance().options.useKey.isPressed() && player.getMainHandStack().isEmpty()) {
				HitResult hit = MinecraftClient.getInstance().crosshairTarget;
				if (hit.getType().equals(Type.BLOCK)) {

					BlockHitResult blockHit = (BlockHitResult) hit;
					BlockPos pos = blockHit.getBlockPos();

					int size = Registry.ENTITY_TYPE.size();

					int rnd = Random.create().nextInt(size);

					EntityType<?> randomMob = Registry.ENTITY_TYPE.get(rnd);
					while (!randomMob.getSpawnGroup().equals(SpawnGroup.MONSTER)
							&& randomMob != EntityType.ENDER_DRAGON) {
						int random2 = Random.create().nextInt(size);

						randomMob = Registry.ENTITY_TYPE.get(random2);
					}

					// ! mobs spawnados tem hp aumentado

					randomMob.spawn((ServerWorld) world, null, null, null, pos.add(0, 1, 0), SpawnReason.EVENT, false,
							false);
				}
			}
		}

		if (!player.getScoreboardTags().contains("moga.enderman")
				&& !player.getScoreboardTags().contains("moga.spawner")) {
			if (player.getScoreboardTeam() != null)
				world.getScoreboard().removeTeam(player.getScoreboard().getTeams().stream().findFirst().get());
		}
	}

}
