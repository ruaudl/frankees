package org.frankees.sample.domain.testdriven;

import static org.fest.assertions.Assertions.*;

import org.frankees.annotation.DataBuilders;
import org.frankees.sample.domain.testdriven.all.TDCharacterFrankeeBuilder;
import org.frankees.sample.domain.testdriven.all.TDMonsterFrankeeBuilder;
import org.junit.Test;

@DataBuilders(builderClassSuffix = "FrankeeBuilder", builderPackageName = "org.frankees.sample.domain.testdriven.all", beanClassName = "(?:(?!Test)\\w)+", beanPackageName = "org.frankees.sample.domain.testdriven")
public class TDAllBuilderTest {

	@Test
	public void testMonsterBuilder() {
		TDMonster dto = TDMonsterFrankeeBuilder.aTDMonster().withName("Yeti")
				.build();

		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("Yeti");
	}

	@Test
	public void testCharacterBuilder() {
		TDCharacter dto = TDCharacterFrankeeBuilder.aTDCharacter()
				.withName("John").build();

		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("John");
	}

}
