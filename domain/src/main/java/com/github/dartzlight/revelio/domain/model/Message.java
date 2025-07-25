package com.github.dartzlight.revelio.domain.model;

import java.time.Instant;
import java.util.List;

public record Message(
		Author author,
		String content,
		Instant timestamp,
		List<Reaction> reactions,
		Channel channel,
		String url
) {
}
