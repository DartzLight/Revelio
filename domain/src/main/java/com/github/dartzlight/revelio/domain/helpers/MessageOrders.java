package com.github.dartzlight.revelio.domain.helpers;

import com.github.dartzlight.revelio.domain.model.Message;
import com.github.dartzlight.revelio.domain.model.Reaction;

import java.util.Comparator;

public class MessageOrders {

	private static int getMessageReactionMaxCount(Message message) {
		return message.reactions().stream().mapToInt(Reaction::count).max().orElse(0);
	}

	private static int getMessageReactionTotalCount(Message message) {
		return message.reactions().stream().mapToInt(Reaction::count).sum();
	}

	public static Comparator<Message> getPopularityComparator() {
		return Comparator.comparingInt(MessageOrders::getMessageReactionMaxCount)
				.thenComparing(MessageOrders::getMessageReactionTotalCount)
				.thenComparing(Message::timestamp)
				.reversed();
	}

}
