package org.frankees.sample.domain.packagedriven.all;

import static org.fest.assertions.Assertions.*;

import org.frankees.sample.domain.packagedriven.PDCharacter;
import org.frankees.sample.domain.packagedriven.all.PDCharacterCreator;
import org.junit.Test;

public class PDCharacterBuilderTest {

	@Test
	public void testBuilder() {
		PDCharacter dto = PDCharacterCreator.aPDCharacter().withName("John").build();

		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("John");
	}
}
