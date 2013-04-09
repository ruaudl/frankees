package org.frankees.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Map.Entry;

public class BuilderBuilder {

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
