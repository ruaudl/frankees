package org.frankees.sample.domain.mainsources;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class MSMonsterBuilderTest {

	@Test
	public void testBuilder() {
		MSMonster dto = MSMonsterBuilder.aMSMonster().withName("Yeti").withPower(9).build();
		
		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("Yeti");
                assertThat(dto.getPower()).isEqualTo(9);
	}
	
}
