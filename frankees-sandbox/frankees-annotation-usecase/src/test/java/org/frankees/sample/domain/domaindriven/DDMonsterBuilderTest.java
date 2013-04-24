package org.frankees.sample.domain.domaindriven;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class DDMonsterBuilderTest {

	@Test
	public void testBuilder() {
		DDMonster dto = DDMonsterBuilder.aDDMonster().withName("Yeti").build();

		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("Yeti");
	}
	
}
