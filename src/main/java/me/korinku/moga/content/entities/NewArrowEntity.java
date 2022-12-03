package me.korinku.moga.content.entities;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion.DestructionType;

public class NewArrowEntity extends PersistentProjectileEntity {
	private static final TrackedData<Integer> COLOR = DataTracker.registerData(ArrowEntity.class,
			TrackedDataHandlerRegistry.INTEGER);
	private Potion potion = Potions.EMPTY;
	private final Set<StatusEffectInstance> effects = Sets.newHashSet();
	private boolean colorSet;

	public NewArrowEntity(EntityType<? extends NewArrowEntity> entityType, World world) {
		super((EntityType<? extends PersistentProjectileEntity>) entityType, world);
	}

	public NewArrowEntity(World world, double x, double y, double z) {
		super(EntityType.ARROW, x, y, z, world);
	}

	public NewArrowEntity(World world, LivingEntity owner) {
		super(EntityType.ARROW, owner, world);
	}

	public void initFromStack(ItemStack stack) {
		if (stack.isOf(Items.TIPPED_ARROW)) {
			int i;
			this.potion = PotionUtil.getPotion(stack);
			List<StatusEffectInstance> collection = PotionUtil.getCustomPotionEffects(stack);
			if (!collection.isEmpty()) {
				for (StatusEffectInstance statusEffectInstance : collection) {
					this.effects.add(new StatusEffectInstance(statusEffectInstance));
				}
			}
			if ((i = ArrowEntity.getCustomPotionColor(stack)) == -1) {
				this.initColor();
			} else {
				this.setColor(i);
			}
		} else if (stack.isOf(Items.ARROW)) {
			this.potion = Potions.EMPTY;
			this.effects.clear();
			this.dataTracker.set(COLOR, -1);
		}
	}

	public static int getCustomPotionColor(ItemStack stack) {
		NbtCompound nbtCompound = stack.getNbt();
		if (nbtCompound != null && nbtCompound.contains("CustomPotionColor", NbtElement.NUMBER_TYPE)) {
			return nbtCompound.getInt("CustomPotionColor");
		}
		return -1;
	}

	private void initColor() {
		this.colorSet = false;
		if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
			this.dataTracker.set(COLOR, -1);
		} else {
			this.dataTracker.set(COLOR, PotionUtil.getColor(PotionUtil.getPotionEffects(this.potion, this.effects)));
		}
	}

	public void addEffect(StatusEffectInstance effect) {
		this.effects.add(effect);
		this.getDataTracker().set(COLOR, PotionUtil.getColor(PotionUtil.getPotionEffects(this.potion, this.effects)));
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(COLOR, -1);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.world.isClient) {
			if (this.inGround) {
				if (this.inGroundTime % 5 == 0) {
					this.spawnParticles(1);
				}
			} else {
				this.spawnParticles(2);
			}
		} else if (this.inGround && this.inGroundTime != 0 && !this.effects.isEmpty() && this.inGroundTime >= 600) {
			this.world.sendEntityStatus(this, (byte) 0);
			this.potion = Potions.EMPTY;
			this.effects.clear();
			this.dataTracker.set(COLOR, -1);
		}
	}

	private void spawnParticles(int amount) {
		int i = this.getColor();
		if (i == -1 || amount <= 0) {
			return;
		}
		double d = (double) (i >> 16 & 0xFF) / 255.0;
		double e = (double) (i >> 8 & 0xFF) / 255.0;
		double f = (double) (i >> 0 & 0xFF) / 255.0;
		for (int j = 0; j < amount; ++j) {
			this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX(0.5), this.getRandomBodyY(),
					this.getParticleZ(0.5), d, e, f);
		}
	}

	public int getColor() {
		return this.dataTracker.get(COLOR);
	}

	private void setColor(int color) {
		this.colorSet = true;
		this.dataTracker.set(COLOR, color);
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);

		Vec3d pos = entityHitResult.getPos();

		int rnd = Random.create().nextBetween(1, 100);

		if (rnd <= 5) {
			world.createExplosion(null, DamageSource.GENERIC, null, pos.getX(), pos.getY(), pos.getZ(),
					3,
					false, DestructionType.BREAK);

		}

		this.remove(RemovalReason.KILLED);
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);

		Vec3d pos = blockHitResult.getPos();

		int rnd = Random.create().nextBetween(1, 100);

		if (rnd <= 5) {
			world.createExplosion(null, DamageSource.GENERIC, null, pos.getX(), pos.getY(), pos.getZ(),
					3,
					false, DestructionType.BREAK);

		}

		this.remove(RemovalReason.KILLED);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (this.potion != Potions.EMPTY) {
			nbt.putString("Potion", Registry.POTION.getId(this.potion).toString());
		}
		if (this.colorSet) {
			nbt.putInt("Color", this.getColor());
		}
		if (!this.effects.isEmpty()) {
			NbtList nbtList = new NbtList();
			for (StatusEffectInstance statusEffectInstance : this.effects) {
				nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
			}
			nbt.put("CustomPotionEffects", nbtList);
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("Potion", NbtElement.STRING_TYPE)) {
			this.potion = PotionUtil.getPotion(nbt);
		}
		for (StatusEffectInstance statusEffectInstance : PotionUtil.getCustomPotionEffects(nbt)) {
			this.addEffect(statusEffectInstance);
		}
		if (nbt.contains("Color", NbtElement.NUMBER_TYPE)) {
			this.setColor(nbt.getInt("Color"));
		} else {
			this.initColor();
		}
	}

	@Override
	protected void onHit(LivingEntity target) {
		super.onHit(target);
		Entity entity = this.getEffectCause();
		for (StatusEffectInstance statusEffectInstance : this.potion.getEffects()) {
			target.addStatusEffect(new StatusEffectInstance(statusEffectInstance.getEffectType(),
					Math.max(statusEffectInstance.getDuration() / 8, 1), statusEffectInstance.getAmplifier(),
					statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles()), entity);
		}
		if (!this.effects.isEmpty()) {
			for (StatusEffectInstance statusEffectInstance : this.effects) {
				target.addStatusEffect(statusEffectInstance, entity);
			}
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
	}

	@Override
	protected ItemStack asItemStack() {
		if (this.effects.isEmpty() && this.potion == Potions.EMPTY) {
			return new ItemStack(Items.ARROW);
		}
		ItemStack itemStack = new ItemStack(Items.TIPPED_ARROW);
		PotionUtil.setPotion(itemStack, this.potion);
		PotionUtil.setCustomPotionEffects(itemStack, this.effects);
		if (this.colorSet) {
			itemStack.getOrCreateNbt().putInt("CustomPotionColor", this.getColor());
		}
		return itemStack;
	}

	@Override
	public void handleStatus(byte status) {
		if (status == 0) {
			int i = this.getColor();
			if (i != -1) {
				double d = (double) (i >> 16 & 0xFF) / 255.0;
				double e = (double) (i >> 8 & 0xFF) / 255.0;
				double f = (double) (i >> 0 & 0xFF) / 255.0;
				for (int j = 0; j < 20; ++j) {
					this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX(0.5), this.getRandomBodyY(),
							this.getParticleZ(0.5), d, e, f);
				}
			}
		} else {
			super.handleStatus(status);
		}
	}
}
