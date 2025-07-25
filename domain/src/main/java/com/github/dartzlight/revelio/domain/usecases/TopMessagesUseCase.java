package com.github.dartzlight.revelio.domain.usecases;

import com.github.dartzlight.revelio.domain.helpers.MessageOrders;
import com.github.dartzlight.revelio.domain.model.Message;
import com.github.dartzlight.revelio.domain.ports.MessageOutboundPort;
import com.github.dartzlight.revelio.domain.ports.TopMessagesInboundPort;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

public class TopMessagesUseCase implements TopMessagesInboundPort {

	private final ZoneId zoneId;
	private final MessageOutboundPort messageOutboundPort;

	public TopMessagesUseCase(ZoneId zoneId, MessageOutboundPort messageOutboundPort) {
		this.zoneId = zoneId;
		this.messageOutboundPort = messageOutboundPort;
	}

	@Override
	public List<Message> getTopMessages(String guildId, LocalDate startDate, LocalDate endDate, int limit) {
		Instant oldestMessage = startDate.atStartOfDay(zoneId).toInstant();
		Instant latestMessage = endDate.atTime(LocalTime.MAX).atZone(zoneId).toInstant();
		List<Message> allMessages = messageOutboundPort.retrieveAllMessages(guildId, oldestMessage, latestMessage);
		return allMessages.stream()
				.sorted(MessageOrders.getPopularityComparator())
				.limit(limit)
				.toList();
	}

}
