package com.github.dartzlight.revelio.inbound.listeners;

import com.github.dartzlight.revelio.domain.model.Message;
import com.github.dartzlight.revelio.domain.ports.TopMessagesInboundPort;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TopMessagesListener extends ListenerAdapter {

	public static final String COMMAND_NAME = "top";
	public static final String COMMAND_OPTION_START_DATE = "start";
	public static final String COMMAND_OPTION_END_DATE = "end";
	public static final String COMMAND_OPTION_LIMIT = "limit";

	private static final int MAX_LIMIT = 10;
	private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private final TopMessagesInboundPort topMessagesInboundPort;

	public TopMessagesListener(TopMessagesInboundPort topMessagesInboundPort) {
		this.topMessagesInboundPort = topMessagesInboundPort;
	}

	public static CommandData getCommand() {
		return Commands.slash(
				COMMAND_NAME,
				"Afficher le classement des messages avec le plus de réactions"
		).addOptions(
				new OptionData(OptionType.STRING, COMMAND_OPTION_START_DATE, "A partir de quand ? (YYYY-MM-DD)")
						.setRequired(true)
						.setMaxLength("1970-01-01".length()),
				new OptionData(OptionType.STRING, COMMAND_OPTION_END_DATE, "Jusqu'à quand ? (YYYY-MM-DD)")
						.setRequired(true)
						.setMaxLength("1970-01-01".length()),
				new OptionData(OptionType.INTEGER, COMMAND_OPTION_LIMIT, "Combien de messages afficher ? (max : %d)".formatted(MAX_LIMIT))
						.setRequired(true)
						.setRequiredRange(1, MAX_LIMIT)
		);
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply().queue();
		if (event.getName().equals(COMMAND_NAME)) {
			Guild guild = event.getGuild();
			LocalDate startDate = Optional.ofNullable(event.getOption(COMMAND_OPTION_START_DATE)).map(OptionMapping::getAsString).map(option -> LocalDate.parse(option, ISO_DATE_FORMATTER)).orElseThrow();
			LocalDate endDate = Optional.ofNullable(event.getOption(COMMAND_OPTION_END_DATE)).map(OptionMapping::getAsString).map(option -> LocalDate.parse(option, ISO_DATE_FORMATTER)).orElse(startDate);
			int limit = Optional.ofNullable(event.getOption(COMMAND_OPTION_LIMIT)).map(OptionMapping::getAsInt).orElseThrow();
			List<Message> messages = topMessagesInboundPort.getTopMessages(guild.getId(), startDate, endDate, limit);
			List<MessageEmbed> embeds = messages.stream().map(TopMessagesListener::formatMessage).toList();
			MessageCreateData data = MessageCreateData.fromEmbeds(embeds);
			event.getHook().sendMessage(data).queue();
		}
	}

	private static MessageEmbed formatMessage(Message message) {
		return new EmbedBuilder()
				.setColor(message.author().color())
				.setAuthor(message.author().name(), null, message.author().avatarUrl())
				.setTitle(message.channel().name(), message.url())
				.setDescription(message.content())
				.addField("Reactions", message.reactions().stream()
						.map(reaction -> reaction.emoji() + " " + reaction.count())
						.collect(Collectors.joining(" | ")), true)
				.setTimestamp(message.timestamp())
				.build();
	}

}
