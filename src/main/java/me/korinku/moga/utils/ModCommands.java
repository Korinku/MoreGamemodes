package me.korinku.moga.utils;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ModCommands {

	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
			dispatcher.register(CommandManager.literal("gamemode")
					.then(CommandManager.literal("4")
							.executes(context -> {
								PlayerEntity player = context.getSource().getPlayer();

								if (player.getScoreboardTags().contains("gamemode.enderman.able")) {
									if (player.getScoreboardTags().contains("moga.rage"))
										player.getScoreboardTags().remove("moga.rage");
									if (player.getScoreboardTags().contains("moga.creeper"))
										player.getScoreboardTags().remove("moga.creeper");
									if (player.getScoreboardTags().contains("moga.rabbit"))
										player.getScoreboardTags().remove("moga.rabbit");
									if (player.getScoreboardTags().contains("moga.mining"))
										player.getScoreboardTags().remove("moga.mining");
									if (player.getScoreboardTags().contains("moga.chunk"))
										player.getScoreboardTags().remove("moga.chunk");
									if (player.getScoreboardTags().contains("moga.bow"))
										player.getScoreboardTags().remove("moga.bow");
									if (player.getScoreboardTags().contains("moga.spawner"))
										player.getScoreboardTags().remove("moga.spawner");

									player.getScoreboardTags().add("moga.enderman");

									player.sendMessage(Text.literal("Set own gamemode to enderman mode"));

									player.sendMessage(Text
											.literal("    + segurar botão direito teleporta você")
											.formatted(Formatting.GREEN));
									player.sendMessage(
											Text.literal("    + agachar e botão direito lança poder")
													.formatted(Formatting.GREEN));
									player.sendMessage(
											Text.literal("    + todo mob dropa ender pearl")
													.formatted(Formatting.GREEN));
								} else {
									player.sendMessage(Text.literal(
											"Você ainda não pode usar este gamemode, precisa de comer uma pérolazinha.")
											.formatted(Formatting.RED));
								}

								return 1;
							}))
					.then(CommandManager.literal("5")
							.executes(context -> {
								PlayerEntity player = context.getSource().getPlayer();

								if (player.getScoreboardTags().contains("gamemode.mining.able")) {
									if (player.getScoreboardTags().contains("moga.rage"))
										player.getScoreboardTags().remove("moga.rage");
									if (player.getScoreboardTags().contains("moga.creeper"))
										player.getScoreboardTags().remove("moga.creeper");
									if (player.getScoreboardTags().contains("moga.rabbit"))
										player.getScoreboardTags().remove("moga.rabbit");
									if (player.getScoreboardTags().contains("moga.enderman"))
										player.getScoreboardTags().remove("moga.enderman");
									if (player.getScoreboardTags().contains("moga.chunk"))
										player.getScoreboardTags().remove("moga.chunk");
									if (player.getScoreboardTags().contains("moga.bow"))
										player.getScoreboardTags().remove("moga.bow");
									if (player.getScoreboardTags().contains("moga.spawner"))
										player.getScoreboardTags().remove("moga.spawner");

									player.getScoreboardTags().add("moga.mining");

									player.sendMessage(Text.literal("Set own gamemode to mining mode"));
									player.sendMessage(
											Text.literal("    + quebra bloco com 1 hit")
													.formatted(Formatting.GREEN));
									player.sendMessage(
											Text.literal("    + velocidade")
													.formatted(Formatting.GREEN));
									player.sendMessage(Text.literal("    + botão direito quebra 3x3")
											.formatted(Formatting.GREEN));
								} else {
									player.sendMessage(Text.literal(
											"Você ainda não pode usar este gamemode, precisa de comer uma picareta.")
											.formatted(Formatting.RED));
								}

								return 1;
							}))
					.then(CommandManager.literal("6")
							.executes(context -> {
								PlayerEntity player = context.getSource().getPlayer();

								if (player.getScoreboardTags().contains("gamemode.chunk.able")) {
									if (player.getScoreboardTags().contains("moga.rage"))
										player.getScoreboardTags().remove("moga.rage");
									if (player.getScoreboardTags().contains("moga.creeper"))
										player.getScoreboardTags().remove("moga.creeper");
									if (player.getScoreboardTags().contains("moga.rabbit"))
										player.getScoreboardTags().remove("moga.rabbit");
									if (player.getScoreboardTags().contains("moga.enderman"))
										player.getScoreboardTags().remove("moga.enderman");
									if (player.getScoreboardTags().contains("moga.mining"))
										player.getScoreboardTags().remove("moga.mining");
									if (player.getScoreboardTags().contains("moga.bow"))
										player.getScoreboardTags().remove("moga.bow");
									if (player.getScoreboardTags().contains("moga.spawner"))
										player.getScoreboardTags().remove("moga.spawner");

									player.getScoreboardTags().add("moga.chunk");

									player.sendMessage(Text.literal("Set own gamemode to chunk mode"));
									player.sendMessage(
											Text.literal("    + agachar destroi blocos em sua volta")
													.formatted(Formatting.GREEN));
									player.sendMessage(
											Text.literal("    + apertar o botão direito cria uma parede de bloco")
													.formatted(Formatting.GREEN));
									player.sendMessage(
											Text.literal(
													"    + segurar o botão esquerdo com uma picareta revela minérios")
													.formatted(Formatting.GREEN));
								} else {
									player.sendMessage(Text.literal(
											"Você ainda não pode usar este gamemode, já pensou comer o planeta terra?")
											.formatted(Formatting.RED));
								}

								return 1;
							}))
					.then(CommandManager.literal("8")
							.executes(context -> {
								PlayerEntity player = context.getSource().getPlayer();

								if (player.getScoreboardTags().contains("gamemode.bow.able")) {
									if (player.getScoreboardTags().contains("moga.rage"))
										player.getScoreboardTags().remove("moga.rage");
									if (player.getScoreboardTags().contains("moga.creeper"))
										player.getScoreboardTags().remove("moga.creeper");
									if (player.getScoreboardTags().contains("moga.rabbit"))
										player.getScoreboardTags().remove("moga.rabbit");
									if (player.getScoreboardTags().contains("moga.enderman"))
										player.getScoreboardTags().remove("moga.enderman");
									if (player.getScoreboardTags().contains("moga.mining"))
										player.getScoreboardTags().remove("moga.mining");
									if (player.getScoreboardTags().contains("moga.chunk"))
										player.getScoreboardTags().remove("moga.chunk");
									if (player.getScoreboardTags().contains("moga.mob"))
										player.getScoreboardTags().remove("moga.mob");

									player.getScoreboardTags().add("moga.bow");

									player.sendMessage(Text.literal("Set own gamemode to bow mode"));
									player.sendMessage(
											Text.literal("    + agachar e botão direito atira flecha")
													.formatted(Formatting.GREEN));
									player.sendMessage(
											Text.literal("    + 5% chance da seta ser explosiva")
													.formatted(Formatting.GREEN));
									player.sendMessage(
											Text.literal("    + 1% chance da seta matar com 1 hit")
													.formatted(Formatting.GREEN));
								} else {
									player.sendMessage(Text.literal(
											"Você ainda não pode usar este gamemode seu merda, precisa de comer um arco.")
											.formatted(Formatting.RED));
								}

								return 1;
							}))
					.then(CommandManager.literal("7")
							.executes(context -> {
								PlayerEntity player = context.getSource().getPlayer();

								if (player.getScoreboardTags().contains("gamemode.spawner.able")) {
									if (player.getScoreboardTags().contains("moga.rage"))
										player.getScoreboardTags().remove("moga.rage");
									if (player.getScoreboardTags().contains("moga.creeper"))
										player.getScoreboardTags().remove("moga.creeper");
									if (player.getScoreboardTags().contains("moga.rabbit"))
										player.getScoreboardTags().remove("moga.rabbit");
									if (player.getScoreboardTags().contains("moga.enderman"))
										player.getScoreboardTags().remove("moga.enderman");
									if (player.getScoreboardTags().contains("moga.mining"))
										player.getScoreboardTags().remove("moga.mining");
									if (player.getScoreboardTags().contains("moga.chunk"))
										player.getScoreboardTags().remove("moga.chunk");
									if (player.getScoreboardTags().contains("moga.bow"))
										player.getScoreboardTags().remove("moga.bow");

									player.getScoreboardTags().add("moga.spawner");

									player.sendMessage(Text.literal("Set own gamemode to spawner mode"));
									player.sendMessage(
											Text.literal("    + apertar botão direito spawna monstro")
													.formatted(Formatting.GREEN));
									player.sendMessage(
											Text.literal("    + monstros dropam item op")
													.formatted(Formatting.GREEN));
								} else {
									player.sendMessage(Text.literal(
											"Você ainda não pode usar este gamemode, precisa de comer um spawner.")
											.formatted(Formatting.RED));
								}

								return 1;
							})));
		});
	}

}
