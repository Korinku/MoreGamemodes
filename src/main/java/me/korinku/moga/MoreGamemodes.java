package me.korinku.moga;

import me.korinku.moga.content.entities.EndProjectileEntity;
import me.korinku.moga.content.items.ModItems;
import me.korinku.moga.utils.ModCommands;
import me.korinku.moga.utils.ModItemGroup;
import me.korinku.moga.utils.ModModifyLoot;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MoreGamemodes implements ModInitializer {

	public static final String MOD_ID = "moga";

	public static final ModItemGroup CUSTOM_GROUP = new ModItemGroup(
			new Identifier(MOD_ID, "moga_group"));

	public static final EntityType<EndProjectileEntity> END_PROJECTILE = FabricEntityTypeBuilder
			.<EndProjectileEntity>create(SpawnGroup.MISC, EndProjectileEntity::new)
			.dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build();

	@Override
	public void onInitialize() {

		CUSTOM_GROUP.initialize();

		Registry.register(Registry.ENTITY_TYPE, new Identifier(MOD_ID, "end_projectile"), END_PROJECTILE);

		ModCommands.init();

		ModItems.init();

		ModModifyLoot.modifyRandomMobLootTable();
	}
}
