package org.frankees.builder;

import static org.fest.assertions.Assertions.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class BuilderBuilderTest {

	@Test
	public void testClassBuilding() throws IOException {
		Set<PropertyDescription> properties = new HashSet<PropertyDescription>();
		properties.add(new PropertyDescription("foo", new TypeDescription(
				"java.lang", "String")));
		properties.add(new PropertyDescription("bar", new TypeDescription(
				"java.lang", "Integer")));

		BuilderDescription description = new BuilderDescription(
				new TypeDescription("org.frankees.foo", "FooBarBuilder"),
				new TypeDescription("org.frankees.foo", "FooBar"), properties);

		String builded = BuilderBuilder.build(description);

		assertThat(builded).isNotNull();
		assertThat(builded).contains("public final class FooBarBuilder {");
		assertThat(builded).contains(
				"public FooBarBuilder withFoo(String foo) {");
		assertThat(builded).contains(
				"public FooBarBuilder withBar(Integer bar) {");
		assertThat(builded).contains("public static FooBarBuilder aFooBar() {");
	}

	@Test
	public void testClassBuildingExpectingAn() throws IOException {

		BuilderDescription description = new BuilderDescription(
				new TypeDescription("org.frankees.onk", "OnkOnkBuilder"),
				new TypeDescription("org.frankees.onk", "OnkOnk"),
				new HashSet<PropertyDescription>());

		String builded = BuilderBuilder.build(description);

		assertThat(builded).isNotNull();
		assertThat(builded).contains("public final class OnkOnkBuilder {");
		assertThat(builded)
				.contains("public static OnkOnkBuilder anOnkOnk() {");
	}
}
