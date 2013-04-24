package org.frankees.sample.domain.packagedriven;

import static org.fest.assertions.Assertions.*;

import org.frankees.sample.domain.packagedriven.test.PDCharacterAssembler;
import org.junit.Test;

public class PDCharacterBuilderTest {

	@Test
	public void testBuilder() {
		PDCharacter dto = PDCharacterAssembler.aPDCharacter().withName("John").build();

		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("John");
	}
}
