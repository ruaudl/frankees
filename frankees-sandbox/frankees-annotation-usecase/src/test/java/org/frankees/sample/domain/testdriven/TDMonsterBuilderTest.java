package org.frankees.sample.domain.testdriven;

import static org.fest.assertions.Assertions.*;

import org.frankees.annotation.DataBuilder;
import org.junit.Test;

@DataBuilder(TDMonster.class)
public class TDMonsterBuilderTest {

	@Test
	public void testBuilder() {
		TDMonster dto = TDMonsterBuilder.aTDMonster().withName("Yeti").build();
		
		assertThat(dto).isNotNull();
		assertThat(dto.getName()).isNotNull();
		assertThat(dto.getName()).isEqualTo("Yeti");
	}
	
}
