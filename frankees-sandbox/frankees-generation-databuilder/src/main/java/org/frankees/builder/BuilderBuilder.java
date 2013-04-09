package org.frankees.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.lang.model.element.Element;

public class BuilderBuilder {

	public static BuilderDescription describe(Element element) {
		return new BuilderDescriptionElementVisitor().visit(element);
	}

	public static BuilderDescription describe(
			Class<? extends Object> objectClass) {
		BuilderDescription description = new BuilderDescription();
		description.setObjectClassName(objectClass.getSimpleName());
		description.setObjectClassName(objectClass.getPackage().getName());

		Map<String, String> properties = new HashMap<String, String>();
		for (Method method : objectClass.getMethods()) {
			if (method.getName().startsWith("get")) {
				String propertyName = method.getName().substring(3, 4)
						.toLowerCase()
						+ method.getName().substring(4);
				properties.put(propertyName, method.getReturnType()
						.getSimpleName());
			}
		}
		description.setProperties(properties);

		return description;
	}

	public static String build(BuilderDescription description)
			throws IOException {
		String classTemplate = getFileContent("BuilderClassDefinition.txt");
		String setterTemplate = getFileContent("BuilderPropertySetter.txt");
		String cloneTemplate = getFileContent("BuilderPropertyClone.txt");

		StringBuilder settersBuilder = new StringBuilder();
		StringBuilder cloneBuilder = new StringBuilder();
		for (Entry<String, String> property : description.getProperties()
				.entrySet()) {
			String propertyCapitalizedName = capitalizeString(property.getKey());
			settersBuilder.append(MessageFormat.format(setterTemplate,
					description.getBuilderClassName(), property.getKey(),
					propertyCapitalizedName, property.getValue()));
			cloneBuilder.append(MessageFormat.format(cloneTemplate,
					propertyCapitalizedName));
		}

		String classPrefix = getClassPrefix(description.getObjectClassName());
		return MessageFormat.format(classTemplate,
				description.getBuilderPackageName(),
				description.getBuilderClassName(),
				description.getObjectPackageName(),
				description.getObjectClassName(), classPrefix,
				settersBuilder.toString(), cloneBuilder.toString());
	}

	private static String getClassPrefix(String className) {
		String classPrefix = "a";
		if ("AEIOUY".contains(className.substring(0, 1))) {
			classPrefix = "an";
		}
		return classPrefix;
	}

	private static String capitalizeString(String propertyCapitalizedName) {
		return propertyCapitalizedName.substring(0, 1).toUpperCase()
				+ propertyCapitalizedName.substring(1);
	}

	private static String getFileContent(String fileName) throws IOException {
		StringBuilder contentBuilder = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				BuilderBuilder.class.getResourceAsStream(fileName)));
		String line = null;
		while ((line = reader.readLine()) != null) {
			contentBuilder.append(line).append("\n");
		}

		return contentBuilder.toString();
	}
}
