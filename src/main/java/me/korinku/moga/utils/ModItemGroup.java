package me.korinku.moga.utils;

import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup extends OwoItemGroup {

	public ModItemGroup(Identifier id) {
		super(id);
	}

	@Override
	protected void setup() {

	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(Blocks.COMMAND_BLOCK);
	}
}
