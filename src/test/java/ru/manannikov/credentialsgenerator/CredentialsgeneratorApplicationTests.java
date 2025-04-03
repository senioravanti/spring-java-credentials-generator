package ru.manannikov.credentialsgenerator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import java.util.stream.IntStream;

class CredentialsgeneratorApplicationTests {

	/**
	 * UUID составляет 36 символов
	 */
	@Test
	void generateUuid() {
		final boolean isAllHasFixedWidth = IntStream.range(0, 100)
			.mapToObj(i -> UUID.randomUUID().toString())
			.mapToInt(String::length)
			.allMatch(it -> it == 36)
		;
		assertTrue(isAllHasFixedWidth);
	}
}