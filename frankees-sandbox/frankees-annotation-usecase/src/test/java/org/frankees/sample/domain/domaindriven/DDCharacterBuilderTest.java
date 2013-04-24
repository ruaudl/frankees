package org.frankees.sample.domain.domaindriven;

import static org.fest.assertions.Assertions.*;

import org.frankees.sample.domain.domaindriven.test.DDCharacterAssembler;
import org.junit.Test;

public class DDCharacterBuilderTest {

	@Test
	public void testBuilder() {
		DDCharacter dto = DDCharacterAssembler.aDDCharacter().withName("John").build();

		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("John");
	}
	
}
