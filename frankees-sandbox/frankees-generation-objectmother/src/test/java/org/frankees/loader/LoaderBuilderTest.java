package org.frankees.loader;

import static org.fest.assertions.Assertions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.frankees.loader.LoaderBuilder;
import org.frankees.loader.LoaderDescription;
import org.frankees.loader.PropertyDescription;
import org.frankees.loader.TypeDescription;
import org.junit.Test;

public class LoaderBuilderTest {

	@Test
	public void testClassBuilding() throws IOException {

		Set<PropertyDescription> properties = new HashSet<PropertyDescription>();
		properties.add(new PropertyDescription("foo", "FOO",
				new TypeDescription("java.lang", "String")));

		Map<String, Set<PropertyDescription>> beans = new HashMap<String, Set<PropertyDescription>>();
		beans.put("fooBean", properties);

		properties.add(new PropertyDescription("bar", "BAR",
				new TypeDescription("java.lang", "Integer")));
		beans.put("fooBarBean", properties);

		LoaderDescription description = new LoaderDescription("beans",
				new TypeDescription("org.frankees.foo", "FooBarLoader"),
				new TypeDescription("org.frankees.foo", "FooBar"), beans);

		String builded = LoaderBuilder.build(description);

		assertThat(builded).isNotNull();
		assertThat(builded).contains("public final class FooBarLoader {");
		assertThat(builded).contains("public static FooBar getFooBean() {");
		assertThat(builded).contains("public static FooBar getFooBarBean() {");
	}

	@Test
	public void testClassBuildingExpectingName() throws IOException {

		LoaderDescription description = new LoaderDescription("dirties", null,
				new TypeDescription("org.frankees.onk", "OnkOnk"),
				new HashMap<String, Set<PropertyDescription>>());

		String builded = LoaderBuilder.build(description);

		assertThat(builded).isNotNull();
		assertThat(builded)
				.contains("public final class OnkOnkDirtiesLoader {");
	}
}
