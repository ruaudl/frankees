package org.frankees.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;

public class BuilderBuilder {

	public static final String DEFAULT_BUILDER_CLASS_SUFFIX = "Builder";

	public static String build(BuilderDescription description)
			throws IOException {
		String classTemplate = getFileContent("BuilderClassDefinition.txt");
		String setterTemplate = getFileContent("BuilderPropertySetter.txt");
		String cloneTemplate = getFileContent("BuilderPropertyClone.txt");

		if (description.getBuilderTypeDescription() == null) {
			description.setBuilderTypeDescription(new TypeDescription());
		}
		if (description.getBuilderTypeDescription().getPackageName() == null) {
			description.getBuilderTypeDescription().setPackageName(
					description.getObjectTypeDescription().getPackageName());
		}
		if (description.getBuilderTypeDescription().getClassName() == null) {
			description.getBuilderTypeDescription().setClassName(
					description.getObjectTypeDescription().getClassName()
							+ DEFAULT_BUILDER_CLASS_SUFFIX);
		}

		StringBuilder settersBuilder = new StringBuilder();
		StringBuilder cloneBuilder = new StringBuilder();
		for (PropertyDescription property : description.getProperties()) {
			String propertyCapitalizedName = capitalizeString(property
					.getPropertyName());
			settersBuilder.append(MessageFormat.format(setterTemplate,
					description.getBuilderTypeDescription().getClassName(),
					property.getPropertyName(), propertyCapitalizedName,
					property.getPropertyType().getClassName()));
			cloneBuilder.append(MessageFormat.format(cloneTemplate,
					propertyCapitalizedName));
		}

		String classPrefix = getClassPrefix(description
				.getObjectTypeDescription().getClassName());
		return MessageFormat.format(classTemplate, description
				.getBuilderTypeDescription().getPackageName(), description
				.getBuilderTypeDescription().getClassName(), description
				.getObjectTypeDescription().getPackageName(), description
				.getObjectTypeDescription().getClassName(), classPrefix,
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
