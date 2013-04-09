package org.frankees.builder;

import static org.fest.assertions.Assertions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class BuilderBuilderTest {

	@Test
	public void testClassBuilding() throws IOException {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("foo", "String");
		properties.put("bar", "Integer");

		BuilderDescription description = new BuilderDescription();
		description.setBuilderClassName("FooBarBuilder");
		description.setBuilderPackageName("org.frankees.foo");
		description.setObjectClassName("FooBar");
		description.setObjectPackageName("org.frankees.foo");
		description.setProperties(properties);

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

		BuilderDescription description = new BuilderDescription();
		description.setBuilderClassName("OnkOnkBuilder");
		description.setBuilderPackageName("org.frankees.onk");
		description.setObjectClassName("OnkOnk");
		description.setObjectPackageName("org.frankees.onk");
		description.setProperties(new HashMap<String, String>());

		String builded = BuilderBuilder.build(description);

		assertThat(builded).isNotNull();
		assertThat(builded).contains("public final class OnkOnkBuilder {");
		assertThat(builded)
				.contains("public static OnkOnkBuilder anOnkOnk() {");
	}

}
