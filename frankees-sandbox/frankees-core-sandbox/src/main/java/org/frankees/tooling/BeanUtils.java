package org.frankees.tooling;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class BeanUtils {

	/** A map from primitive types to their corresponding wrapper types. */
	private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;

	static {
		Map<Class<?>, Class<?>> primToWrap = new HashMap<Class<?>, Class<?>>(16);

		primToWrap.put(boolean.class, Boolean.class);
		primToWrap.put(byte.class, Byte.class);
		primToWrap.put(char.class, Character.class);
		primToWrap.put(double.class, Double.class);
		primToWrap.put(float.class, Float.class);
		primToWrap.put(int.class, Integer.class);
		primToWrap.put(long.class, Long.class);
		primToWrap.put(short.class, Short.class);
		primToWrap.put(void.class, Void.class);

		PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(primToWrap);

	}

	private BeanUtils() {
	}

	public static <T> Map<String, BeanField<T>> readFields(Class<T> beanClass) {
		Map<String, BeanField<T>> fields = new HashMap<String, BeanField<T>>();

		PropertyDescriptor[] descriptors = new PropertyDescriptor[0];
		try {
			descriptors = Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		for (PropertyDescriptor descriptor : descriptors) {
			if (descriptor.getWriteMethod() != null && descriptor.getReadMethod() != null) {
				fields.put(descriptor.getName(), new BeanField<T>(descriptor, beanClass));
			}
		}

		return fields;
	}

	public static <T> Class<T> getBoxingClass(Class<T> type) {
		@SuppressWarnings("unchecked")
		Class<T> wrapped = (Class<T>) PRIMITIVE_TO_WRAPPER_TYPE.get(type);
		return (wrapped == null) ? type : wrapped;
	}
}
