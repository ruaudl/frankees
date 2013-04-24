package org.frankees.sample.domain.testdriven;

import static org.fest.assertions.Assertions.*;

import org.frankees.annotation.DataBuilder;
import org.frankees.annotation.DataBuilders;
import org.frankees.sample.domain.testdriven.test.TDMonsterAssembler;
import org.frankees.sample.domain.testdriven.both.TDCharacterCreator;
import org.junit.Test;

@DataBuilders(builderClassSuffix = "Assembler", builderPackageName = "org.frankees.sample.domain.testdriven.test", value = {
		@DataBuilder(value = TDCharacter.class, builderClassSuffix = "Creator", builderPackageName = "org.frankees.sample.domain.testdriven.both"),
		@DataBuilder(value = TDMonster.class) })
public class TDBothBuilderTest {

	@Test
	public void testMonsterBuilder() {
		TDMonster dto = TDMonsterAssembler.aTDMonster().withName("Yeti").build();

		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("Yeti");
	}

	@Test
	public void testCharacterBuilder() {
		TDCharacter dto = TDCharacterCreator.aTDCharacter().withName("John").build();

		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("John");
	}

}
