package com.github.dartzlight.revelio.domain.ports;

import com.github.dartzlight.revelio.domain.model.Message;

import java.time.LocalDate;
import java.util.List;

public interface TopMessagesInboundPort {
	List<Message> getTopMessages(String guildId, LocalDate startDate, LocalDate endDate, int limit);
}
