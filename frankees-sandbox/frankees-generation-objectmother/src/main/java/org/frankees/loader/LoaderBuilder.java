package org.frankees.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Set;
import java.util.Map.Entry;

public class LoaderBuilder {

	public static final String DEFAULT_BUILDER_CLASS_SUFFIX = "Loader";

	public static String build(LoaderDescription description)
			throws IOException {
		String classTemplate = getFileContent("LoaderClassDefinition.txt");
		String loadingTemplate = getFileContent("LoaderBeanLoading.txt");
		String settingTemplate = getFileContent("LoaderPropertySetting.txt");

		if (description.getLoaderTypeDescription() == null) {
			description.setLoaderTypeDescription(new TypeDescription());
		}
		if (description.getLoaderTypeDescription().getPackageName() == null) {
			description.getLoaderTypeDescription().setPackageName(
					description.getBeanTypeDescription().getPackageName());
		}
		if (description.getLoaderTypeDescription().getClassName() == null) {
			String loaderCapitalizedName = capitalizeString(description
					.getLoaderName());
			description.getLoaderTypeDescription().setClassName(
					description.getBeanTypeDescription().getClassName()
							+ loaderCapitalizedName
							+ DEFAULT_BUILDER_CLASS_SUFFIX);
		}

		StringBuilder loadingBuilder = new StringBuilder();
		for (Entry<String, Set<PropertyDescription>> beanDefinition : description
				.getBeanDefinitions().entrySet()) {

			StringBuilder settingBuilder = new StringBuilder();
			for (PropertyDescription property : beanDefinition.getValue()) {
				String propertyCapitalizedName = capitalizeString(property
						.getPropertyName());
				settingBuilder.append(MessageFormat.format(settingTemplate,
						propertyCapitalizedName,
						'\"' + property.getPropertyValue() + '\"'));
			}

			String beanCapitalizedName = capitalizeString(beanDefinition
					.getKey());
			loadingBuilder.append(MessageFormat.format(loadingTemplate,
					description.getBeanTypeDescription().getClassName(),
					beanDefinition.getKey(), beanCapitalizedName,
					settingBuilder.toString()));
		}

		return MessageFormat.format(classTemplate, description
				.getLoaderTypeDescription().getPackageName(), description
				.getLoaderTypeDescription().getClassName(), description
				.getBeanTypeDescription().getPackageName(), description
				.getBeanTypeDescription().getClassName(), loadingBuilder
				.toString());
	}

	private static String capitalizeString(String propertyCapitalizedName) {
		return propertyCapitalizedName.substring(0, 1).toUpperCase()
				+ propertyCapitalizedName.substring(1);
	}

	private static String getFileContent(String fileName) throws IOException {
		StringBuilder contentBuilder = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				LoaderBuilder.class.getResourceAsStream(fileName)));
		String line = null;
		while ((line = reader.readLine()) != null) {
			contentBuilder.append(line).append("\n");
		}

		return contentBuilder.toString();
	}
}
