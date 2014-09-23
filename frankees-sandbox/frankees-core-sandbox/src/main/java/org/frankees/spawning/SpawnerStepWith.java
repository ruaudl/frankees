package org.frankees.spawning;

import java.lang.reflect.InvocationTargetException;

import org.frankees.tooling.BeanField;

public class SpawnerStepWith<F extends Object> implements SpawnerStep<F> {

	private F value;

	private BeanField field;

	public SpawnerStepWith(F value, BeanField field) {
		super();
		this.value = value;
		this.field = field;
	}

	public <T> T apply(T bean, Class<T> beanClass) {
		try {
			field.getFieldDescriptor().getWriteMethod().invoke(bean, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return bean;
	}

}
