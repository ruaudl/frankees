package org.frankees.sample.domain.packagedriven;

import static org.fest.assertions.Assertions.*;

import org.frankees.annotation.DataBuilders;
//import org.frankees.sample.domain.packagedriven.test.PDCharacterAssembler;
import org.junit.Test;

//@DataBuilders(builderClassSuffix = "Assembler", builderPackageName = "org.frankees.sample.domain.packagedriven.test", beanClassName= "(?:(?!Test)\\w)+", beanPackageName = "org.frankees.sample.domain.packagedriven")
public class PDCharacterBuilderTest {

	@Test
	public void testBuilder() {
//		PDCharacter dto = PDCharacterAssembler.aPDCharacter().withName("John").build();
//
//		assertThat(dto).isNotNull();
//		assertThat(dto.getName()).isNotNull();
//		assertThat(dto.getName()).isEqualTo("John");
	}
}
