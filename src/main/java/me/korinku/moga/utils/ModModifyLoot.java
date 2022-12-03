package me.korinku.moga.utils;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModModifyLoot {
	public static void modifyRandomMobLootTable() {
		int size = Registry.ENTITY_TYPE.size();

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, table, setter) -> {
			for (int i = 0; i < size; i++) {
				EntityType<?> randomMob = Registry.ENTITY_TYPE.get(i);
				Identifier TABLE_IDS = randomMob.getLootTableId();
				if (TABLE_IDS.equals(id)) {
					LootPool.Builder poolBuilder = LootPool.builder().conditionally(
							DamageSourcePropertiesLootCondition
									.builder(DamageSourcePredicate.Builder.create()
											.sourceEntity(EntityPredicate.Builder.create().team("enderman")))
									.build())
							.rolls(ConstantLootNumberProvider.create(1)).apply(
									SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build())
							.with(ItemEntry.builder(Items.ENDER_PEARL));

					table.pool(poolBuilder.build());
				}
			}
		});

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, table, setter) -> {
			for (int i = 0; i < size; i++) {
				EntityType<?> randomMob = Registry.ENTITY_TYPE.get(i);
				if (randomMob.getSpawnGroup().equals(SpawnGroup.MONSTER)) {
					Identifier TABLE_IDS = randomMob.getLootTableId();
					if (TABLE_IDS.equals(id)) {
						LootPool.Builder poolBuilder = LootPool.builder().conditionally(
								DamageSourcePropertiesLootCondition
										.builder(DamageSourcePredicate.Builder.create()
												.sourceEntity(EntityPredicate.Builder.create().team("spawner")))
										.build())
								.rolls(ConstantLootNumberProvider.create(1)).apply(
										SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))
												.build())
								.with(ItemEntry.builder(Items.DIAMOND))
								.with(ItemEntry.builder(Items.DIAMOND_SWORD))
								.with(ItemEntry.builder(Items.DIAMOND_BOOTS))
								.with(ItemEntry.builder(Items.DIAMOND_CHESTPLATE))
								.with(ItemEntry.builder(Items.NETHERITE_LEGGINGS))
								.with(ItemEntry.builder(Items.NETHERITE_PICKAXE))
								.with(ItemEntry.builder(Items.GOLDEN_APPLE))
								.with(ItemEntry.builder(Items.ENCHANTED_GOLDEN_APPLE))
								.with(ItemEntry.builder(Items.BLAZE_POWDER))
								.with(ItemEntry.builder(Items.TNT))
								.with(ItemEntry.builder(Items.NETHERITE_HELMET));

						table.pool(poolBuilder.build());
					}
				}
			}
		});
	}
}
