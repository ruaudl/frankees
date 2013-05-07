package org.frankees.sample.domain.testdriven;

import static org.fest.assertions.Assertions.*;

import org.frankees.annotation.DataBuilders;
import org.frankees.sample.domain.testdriven.classes.TDMonsterFranker;
import org.frankees.sample.domain.testdriven.classes.TDCharacterFranker;
import org.junit.Test;

@DataBuilders(value = { TDCharacter.class, TDMonster.class }, builderClassSuffix = "Franker", builderPackageName = "org.frankees.sample.domain.testdriven.classes")
public class TDBothClassesTest {

	@Test
	public void testMonsterBuilder() {
		TDMonster dto = TDMonsterFranker.aTDMonster().withName("Yeti")
				.build();

		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("Yeti");
	}

	@Test
	public void testCharacterBuilder() {
		TDCharacter dto = TDCharacterFranker.aTDCharacter().withName("John")
				.build();

		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("John");
	}

}
