package org.frankees.loader.parsing;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

public class JSONLoaderHelper {

	public static <T> Map<String, T> loadBeans(InputStream stream,
			Class<T> beanClass) throws IntrospectionException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		JsonReader jsonReader = null;
		Map<String, T> beans = Collections.emptyMap();
		try {
			jsonReader = Json.createReader(stream);
			JsonStructure jsonStructure = jsonReader.read();
			switch (jsonStructure.getValueType()) {
			case ARRAY:
				beans = loadBeans((JsonArray) jsonStructure, beanClass);
				break;
			default:
			}
		} finally {
			if (jsonReader != null) {
				jsonReader.close();
			}
		}
		return beans;
	}

	public static <T> Map<String, T> loadBeans(JsonArray array,
			Class<T> beanClass) throws IntrospectionException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		Map<String, T> store = new HashMap<String, T>();

		for (JsonValue value : array) {
			switch (value.getValueType()) {
			case OBJECT:
				String id = getId((JsonObject) value);
				T bean = loadBean((JsonObject) value, beanClass);
				store.put(id, bean);
				break;
			default:
			}
		}

		return store;
	}

	public static String getId(JsonObject object) {
		return object.getString("id");
	}

	public static <T> T loadBean(JsonObject object, Class<T> beanClass)
			throws IntrospectionException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		T store = beanClass.newInstance();

		Map<String, PropertyDescriptor> properties = new HashMap<String, PropertyDescriptor>();
		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
		for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
			properties.put(property.getName(), property);
		}

		Set<Entry<String, JsonValue>> attributes = object.entrySet();
		for (Entry<String, JsonValue> attribute : attributes) {
			PropertyDescriptor property = properties.get(attribute.getKey());
			if (property != null
					&& property.getPropertyType().equals(String.class)
					&& property.getWriteMethod() != null
					&& ValueType.STRING.equals(attribute.getValue()
							.getValueType())) {
				property.getWriteMethod().invoke(store,
						((JsonString) attribute.getValue()).getString());
			}
		}

		return store;
	}

}
