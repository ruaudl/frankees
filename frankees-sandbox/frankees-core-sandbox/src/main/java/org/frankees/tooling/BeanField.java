package org.frankees.tooling;

import java.beans.PropertyDescriptor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BeanField<B> {

	private PropertyDescriptor fieldDescriptor;

	private Class<B> beanClass;

	public BeanField(PropertyDescriptor fieldDescriptor, Class<B> beanClass) {
		super();
		this.fieldDescriptor = fieldDescriptor;
		this.beanClass = beanClass;
	}

	public PropertyDescriptor getFieldDescriptor() {
		return fieldDescriptor;
	}

	public Type getFieldType() {
		try {
			return beanClass.getDeclaredField(fieldDescriptor.getName()).getGenericType();
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public Class<?> getFieldClass() {
		return fieldDescriptor.getPropertyType();
	}

	public Class<B> getBeanClass() {
		return beanClass;
	}

	public boolean isPrimitive() {
		return fieldDescriptor.getPropertyType().isPrimitive();
	}

	public boolean isGeneric() {
		return ParameterizedType.class.isAssignableFrom(getFieldType().getClass());
	}

	public boolean isArray() {
		return fieldDescriptor.getPropertyType().isArray();
	}

	public boolean isGenericArray() {
		return GenericArrayType.class.isAssignableFrom(getFieldType().getClass());
	}

	public <F> void writeValue(B bean, F value) {
		try {
			fieldDescriptor.getWriteMethod().invoke(bean, value);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
