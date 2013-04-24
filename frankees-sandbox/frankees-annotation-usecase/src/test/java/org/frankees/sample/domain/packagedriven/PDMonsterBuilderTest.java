package org.frankees.sample.domain.packagedriven;

import static org.fest.assertions.Assertions.*;

import org.frankees.sample.domain.packagedriven.test.PDMonsterAssembler;
import org.junit.Test;

public class PDMonsterBuilderTest {

	@Test
	public void testBuilder() {
		PDMonster dto = PDMonsterAssembler.aPDMonster().withName("Yeti").build();

		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("Yeti");
	}

}
