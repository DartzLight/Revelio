package com.github.dartzlight.revelio.app;

import com.github.dartzlight.revelio.domain.ports.MessageOutboundPort;
import com.github.dartzlight.revelio.domain.ports.TopMessagesInboundPort;
import com.github.dartzlight.revelio.domain.usecases.TopMessagesUseCase;
import com.github.dartzlight.revelio.inbound.listeners.TopMessagesListener;
import com.github.dartzlight.revelio.outbound.adapters.MessageAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.util.EnumSet;

public class Bot {

	private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);
	private static final Path BOT_TOKEN_PATH = Path.of("configuration/bot.token");
	private static final EnumSet<GatewayIntent> GATEWAY_INTENTS = EnumSet.of(
			GatewayIntent.MESSAGE_CONTENT,
			GatewayIntent.GUILD_MESSAGES,
			GatewayIntent.GUILD_MESSAGE_REACTIONS,
			GatewayIntent.GUILD_MEMBERS,
			GatewayIntent.GUILD_EXPRESSIONS
	);

	private Bot() {
	}

	public static void start() {
		JDA jda = initializeJDA();
		try {
			jda.awaitReady();
			addListeners(jda);
			jda.awaitShutdown();
		} catch (InterruptedException e) {
			LOGGER.warn("Shuting down");
			jda.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	private static void addListeners(JDA jda) {
		ZoneId zoneId = ZoneId.systemDefault();
		MessageOutboundPort messageOutboundPort = new MessageAdapter(jda);
		TopMessagesInboundPort topMessagesInboundPort = new TopMessagesUseCase(zoneId, messageOutboundPort);
		jda.addEventListener(new TopMessagesListener(topMessagesInboundPort));
	}

	public static void updateCommands() {
		JDA jda = initializeJDA();
		try {
			jda.awaitReady();
			for (var guild : jda.getGuilds()) {
				guild.updateCommands()
						.addCommands(TopMessagesListener.getCommand())
						.queue();
			}
			jda.shutdown();
			jda.awaitShutdown();
		} catch (InterruptedException e) {
			LOGGER.warn("Shuting down");
			jda.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	private static JDA initializeJDA() {
		String token = findBotToken(BOT_TOKEN_PATH);
		return JDABuilder.createDefault(token, GATEWAY_INTENTS)
				.setChunkingFilter(ChunkingFilter.ALL)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.build();
	}

	private static String findBotToken(Path tokenPath) {
		try {
			return Files.readString(tokenPath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}