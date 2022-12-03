package me.korinku.moga.content.entities;

import java.util.HashMap;

import me.korinku.moga.MoreGamemodes;
import me.korinku.moga.utils.ParticleEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EndProjectileEntity extends SoulEntity {

	private float damage = 3.5f;
	private static final HashMap<EndProjectileEntity, Entity> TARGET_ENTITIES = new HashMap<>();
	private final TargetPredicate UNIQUE_CLOSEST;

	public EndProjectileEntity(World world, LivingEntity owner) {
		super(MoreGamemodes.END_PROJECTILE, world);
		setOwner(owner);
		UNIQUE_CLOSEST = TargetPredicate.createAttackable().setBaseMaxDistance(8)
				.setPredicate(livingEntity -> livingEntity.isAlive()
						&& (!TARGET_ENTITIES.containsValue(livingEntity) || TARGET_ENTITIES.get(this) == livingEntity));
	}

	public EndProjectileEntity(EntityType<EndProjectileEntity> entityType, World world) {
		super(entityType, world);
		UNIQUE_CLOSEST = TargetPredicate.createAttackable().setBaseMaxDistance(8)
				.setPredicate(livingEntity -> livingEntity.isAlive()
						&& (!TARGET_ENTITIES.containsValue(livingEntity) || TARGET_ENTITIES.get(this) == livingEntity));
	}

	@Override
	protected void initDataTracker() {

	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound tag) {
		super.writeCustomDataToNbt(tag);
		tag.putFloat("Damage", damage);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound tag) {
		super.readCustomDataFromNbt(tag);
		damage = tag.getFloat("Damage");
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);

		if (!(entityHitResult.getEntity() instanceof LivingEntity e)
				|| entityHitResult.getEntity() instanceof PlayerEntity)
			return;
		this.remove(RemovalReason.KILLED);

		e.damage(createDamageSource(), damage);

		LightningEntity lEntity = EntityType.LIGHTNING_BOLT.create(world);
		lEntity.setPos(e.getX(), e.getY(), e.getZ());
		world.spawnEntity(lEntity);

		if (!e.isAlive() && damage == 1.5f) {
			ParticleEvents.EXTRACTION_RITUAL_FINISHED.spawn(world, Vec3d.of(entityHitResult.getEntity().getBlockPos()),
					true);

			if (!e.world.isClient) {
				BlockPos pos = e.getBlockPos();
				World world = e.world;

				world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.5f, 0);
			}
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		TARGET_ENTITIES.remove(this);
	}

	@Override
	public void tick() {
		Entity closest = world.getClosestEntity(MobEntity.class, UNIQUE_CLOSEST, null, getX(), getY(), getZ(),
				getBoundingBox().expand(3, 2, 3));
		if (closest == null && TARGET_ENTITIES.containsKey(this))
			closest = TARGET_ENTITIES.get(this);
		if (closest != null) {
			Vec3d targetVector = closest.getPos().add(0, closest.getHeight() * 0.5, 0).subtract(getPos());
			setVelocity(targetVector.multiply(0.25f));
			TARGET_ENTITIES.put(this, closest);
		}

		super.tick();
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		this.remove(RemovalReason.KILLED);
	}

	public DamageSource createDamageSource() {
		return new ProjectileDamageSource("soul_projectile", this, getOwner()).setProjectile();
	}

}
