package me.korinku.moga.content.items;

import me.korinku.moga.MoreGamemodes;
import me.korinku.moga.content.items.food.EdibleBow;
import me.korinku.moga.content.items.food.EdibleChunk;
import me.korinku.moga.content.items.food.EdibleEnderman;
import me.korinku.moga.content.items.food.EdiblePickaxe;
import me.korinku.moga.content.items.food.EdibleSpawner;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {

	public static final Item EDIBLE_ENDERMAN = registerItem("edible_enderman",
			new EdibleEnderman(new FabricItemSettings()
					.group(MoreGamemodes.CUSTOM_GROUP).food(FoodComponents.CHORUS_FRUIT).rarity(Rarity.UNCOMMON)
					.maxCount(1)));

	public static final Item EDIBLE_PICKAXE = registerItem("edible_pickaxe",
			new EdiblePickaxe(new FabricItemSettings()
					.group(MoreGamemodes.CUSTOM_GROUP).food(FoodComponents.CHORUS_FRUIT).rarity(Rarity.UNCOMMON)
					.maxCount(1)));

	public static final Item EDIBLE_CHUNK = registerItem("edible_chunk",
			new EdibleChunk(new FabricItemSettings()
					.group(MoreGamemodes.CUSTOM_GROUP).food(FoodComponents.CHORUS_FRUIT).rarity(Rarity.UNCOMMON)
					.maxCount(1)));

	public static final Item EDIBLE_PORTALGUN = registerItem("edible_bow",
			new EdibleBow(new FabricItemSettings()
					.group(MoreGamemodes.CUSTOM_GROUP).food(FoodComponents.CHORUS_FRUIT).rarity(Rarity.UNCOMMON)
					.maxCount(1)));

	public static final Item EDIBLE_SPAWNER = registerItem("edible_spawner",
			new EdibleSpawner(new FabricItemSettings()
					.group(MoreGamemodes.CUSTOM_GROUP).food(FoodComponents.CHORUS_FRUIT).rarity(Rarity.UNCOMMON)
					.maxCount(1)));

	private static Item registerItem(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(MoreGamemodes.MOD_ID, name), item);
	}

	public static void init() {
	}

}
