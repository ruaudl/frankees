package org.frankees.sample.domain.testdriven;

import static org.fest.assertions.Assertions.*;

import org.frankees.annotation.DataBuilder;
import org.frankees.sample.domain.testdriven.test.TDCharacterAssembler;
import org.junit.Test;

@DataBuilder(value = TDCharacter.class, builderClassSuffix = "Assembler", builderPackageName = "org.frankees.sample.domain.testdriven.test")
public class TDCharacterBuilderTest {

	@Test
	public void testBuilder() {
		TDCharacter dto = TDCharacterAssembler.aTDCharacter().withName("John").build();

		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("John");
	}
}
