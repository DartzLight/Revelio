package com.github.dartzlight.revelio.domain.usecases;

import com.github.dartzlight.revelio.domain.model.Author;
import com.github.dartzlight.revelio.domain.model.Channel;
import com.github.dartzlight.revelio.domain.model.Message;
import com.github.dartzlight.revelio.domain.model.Reaction;
import com.github.dartzlight.revelio.domain.ports.MessageOutboundPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class TopMessagesUseCaseTest {

	private static final ZoneOffset ZONE = ZoneOffset.UTC;

	private final MessageOutboundPort messageOutboundPort = Mockito.mock(MessageOutboundPort.class);
	private final Clock clock = Clock.fixed(Instant.parse("2025-07-25T00:00:00Z"), ZONE);

	@Test
	void should_return_last_message() {
		// Given
		TopMessagesUseCase topMessagesUseCase = new TopMessagesUseCase(ZONE, messageOutboundPort);
		List<Message> allMessages = givenAllMessages();
		String guildId = "guildId";
		LocalDate startDate = LocalDate.of(2025, 7, 24);
		LocalDate endDate = LocalDate.of(2025, 7, 25);
		int limit = 4;
		when(messageOutboundPort.retrieveAllMessages(eq(guildId), any(Instant.class), any(Instant.class))).thenReturn(allMessages);

		// When
		List<Message> messages = topMessagesUseCase.getTopMessages(guildId, startDate, endDate, limit);

		// Then
		assertThat(messages)
				.map(Message::content)
				.containsExactly("Protego", "Expelliarmus", "Stupefix", "Impero");
	}

	private List<Message> givenAllMessages() {
		Author author1 = new Author("Dartz", "avatar", 0x000080);
		Author author2 = new Author("Yam", "avatar");
		Channel channel1 = new Channel("Welcome");
		Channel channel2 = new Channel("Fight");
		return List.of(
				new Message(author1, "Hello world", clock.instant().plusSeconds(1), List.of(new Reaction("😀", 2), new Reaction("😉", 1), new Reaction("😁", 1)), channel1, "url"),
				new Message(author2, "Hi!", clock.instant().plusSeconds(2), List.of(), channel1, "url"),
				new Message(author1, "Stupefix", clock.instant().plusSeconds(3), List.of(new Reaction("😱", 3), new Reaction("🤯", 1)), channel2, "url"),
				new Message(author2, "Expelliarmus", clock.instant().plusSeconds(4), List.of(new Reaction("😁", 1), new Reaction("😲", 3)), channel2, "url"),
				new Message(author1, "Protego", clock.instant().plusSeconds(5), List.of(new Reaction("💙", 5)), channel2, "url"),
				new Message(author2, "Impero", clock.instant().plusSeconds(6), List.of(new Reaction("💀", 3)), channel2, "url")
		);
	}

}