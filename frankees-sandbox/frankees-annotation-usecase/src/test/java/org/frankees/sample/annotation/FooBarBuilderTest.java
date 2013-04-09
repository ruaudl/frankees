package org.frankees.sample.annotation;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class FooBarBuilderTest {

	@Test
	public void testBuilder() {
		FooBarDTO dto = FooBarDTOBuilder.aFooBarDTO().withFoo("Foo").build();
		
		assertThat(dto).isNotNull();
		assertThat(dto.getFoo()).isNotNull();
		assertThat(dto.getFoo()).isEqualTo("Foo");
	}
	
}
