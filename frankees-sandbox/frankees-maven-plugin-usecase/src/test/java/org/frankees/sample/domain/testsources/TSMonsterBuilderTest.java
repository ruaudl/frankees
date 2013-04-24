package org.frankees.sample.domain.testsources;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class TSMonsterBuilderTest {

	@Test
	public void testBuilder() {
		TSMonster dto = TSMonsterBuilder.aTSMonster().withName("Yeti").withPower(9).build();
		
		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("Yeti");
                assertThat(dto.getPower()).isEqualTo(9);
	}
	
}
