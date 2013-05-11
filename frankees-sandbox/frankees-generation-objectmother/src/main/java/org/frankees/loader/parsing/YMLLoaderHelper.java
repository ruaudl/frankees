package org.frankees.loader.parsing;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

public class YMLLoaderHelper {

	public static <T> Map<String, T> loadBeans(InputStream stream,
			Class<T> beanClass) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			IntrospectionException, InstantiationException {

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> objects = (List<Map<String, Object>>) new Yaml()
				.loadAs(stream, List.class);

		return loadBeans(objects, beanClass);
	}

	public static <T> Map<String, T> loadBeans(
			List<Map<String, Object>> objects, Class<T> beanClass)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, IntrospectionException,
			InstantiationException {
		Map<String, T> store = new HashMap<String, T>();

		for (Map<String, Object> object : objects) {
			String id = getId(object);
			T bean = loadBean(object, beanClass);
			store.put(id, bean);
		}

		return store;
	}

	public static String getId(Map<String, Object> object) {
		return object.get("id").toString();
	}

	public static <T> T loadBean(Map<String, Object> object, Class<T> beanClass)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, IntrospectionException,
			InstantiationException {
		T store = beanClass.newInstance();

		Map<String, PropertyDescriptor> properties = new HashMap<String, PropertyDescriptor>();
		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
		for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
			properties.put(property.getName(), property);
		}

		for (Entry<String, Object> entry : object.entrySet()) {
			PropertyDescriptor property = properties.get(entry.getKey());
			if (property != null
					&& property.getPropertyType().equals(String.class)
					&& property.getWriteMethod() != null
					&& entry.getValue() instanceof String) {
				property.getWriteMethod().invoke(store, entry.getValue());
			}
		}

		return store;
	}
}
