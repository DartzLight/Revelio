package com.github.dartzlight.revelio.domain.helpers;

import com.github.dartzlight.revelio.domain.model.Message;
import com.github.dartzlight.revelio.domain.model.Reaction;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MessageOrdersTest {

	private final Clock clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);

	@Test
	void given_messages_with_different_reactions_max() {
		// Given
		Message message1 = new Message(null, "first", clock.instant(),
				List.of(new Reaction("like", 20), new Reaction("love", 5)), null, null);
		Message message2 = new Message(null, "first 2", clock.instant().plusSeconds(1),
				List.of(new Reaction("like", 15), new Reaction("love", 15)), null, null);

		// When
		Comparator<Message> comparator = MessageOrders.getPopularityComparator();
		int comparisonResult = comparator.compare(message1, message2);

		// Then
		assertThat(comparisonResult).isNegative();
	}

	@Test
	void given_messages_with_same_reactions_max_but_different_total_reactions() {
		// Given
		Message message1 = new Message(null, "first", clock.instant(),
				List.of(new Reaction("like", 20), new Reaction("love", 10)), null, null);
		Message message2 = new Message(null, "first 2", clock.instant().plusSeconds(1),
				List.of(new Reaction("like", 5), new Reaction("love", 20)), null, null);

		// When
		Comparator<Message> comparator = MessageOrders.getPopularityComparator();
		int comparisonResult = comparator.compare(message1, message2);

		// Then
		assertThat(comparisonResult).isNegative();
	}

	@Test
	void given_messages_with_same_reactions_max_and_same_total_reactions_but_different_timestamp() {
		// Given
		Message message1 = new Message(null, "first", clock.instant(),
				List.of(new Reaction("like", 15), new Reaction("love", 5)), null, null);
		Message message2 = new Message(null, "first 2", clock.instant().plusSeconds(1),
				List.of(new Reaction("like", 5), new Reaction("love", 15)), null, null);

		// When
		Comparator<Message> comparator = MessageOrders.getPopularityComparator();
		int comparisonResult = comparator.compare(message1, message2);

		// Then
		assertThat(comparisonResult).isPositive();
	}
	
}