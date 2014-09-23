package org.frankees.spawning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.frankees.loading.Dataset;
import org.frankees.making.Maker;
import org.frankees.tooling.BeanField;
import org.frankees.tooling.BeanUtils;

public class Spawner<T extends Object> {

	private static final String INVALID_VALUE_TYPE = "[%s] of type [%s] has not a valid type for field [%s] of type [%s] for class [%s]";

	private static final String INVALID_FIELD_FOR_CLASS = "[%s] is not a valid field for class [%s]";

	private static final String NULL_INVALID_FOR_PRIMITIVE = "Null is not valid for field [%s] of primitive type [%s]";

	private Class<T> beanClass;

	private Map<String, BeanField> fields;

	private List<SpawnerStep<? extends Object>> steps = new ArrayList<SpawnerStep<? extends Object>>();

	public Spawner(Class<T> beanClass) {
		this.beanClass = beanClass;

		fields = BeanUtils.readFields(beanClass);
	}

	public <F> Spawner<T> with(F fieldValue, String fieldName) {
		BeanField field = fields.get(fieldName);
		if (field == null) {
			throw new IllegalArgumentException(String.format(INVALID_FIELD_FOR_CLASS, fieldName, beanClass.getName()));
		}

		if (fieldValue == null) {
			if (field.isPrimitive()) {
				throw new IllegalArgumentException(String.format(NULL_INVALID_FOR_PRIMITIVE, fieldName, field.getFieldType().toString()));
			}
		} else {
			Class<?> propertyType = field.getFieldDescriptor().getPropertyType();
			if (field.isPrimitive()) {
				propertyType = BeanUtils.getBoxingClass(propertyType);
			}
			if (!propertyType.isAssignableFrom(fieldValue.getClass())) {
				throw new IllegalArgumentException(String.format(INVALID_VALUE_TYPE, fieldValue, fieldValue.getClass(), fieldName, field.getFieldType().toString(),
						beanClass.getName()));
			}
		}
		steps.add(new SpawnerStepWith<F>(fieldValue, field));
		return this;
	}

	private <F> Spawner<T> with(F fieldValue, F fieldGetter) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public Spawner<T> like(String datacaseName) {
		steps.add(new SpawnerStepLike<T>(datacaseName));
		return this;
	}

	public Spawner<T> like(String datasetCase, Dataset dataset) {
		steps.add(new SpawnerStepLike<T>(datasetCase, dataset));
		return this;
	}

	public Spawner<T> by(Maker maker) {
		steps.add(new SpawnerStepBy<T>(maker, fields));
		return this;
	}

	public T spawn() {
		T bean = null;
		try {
			bean = beanClass.newInstance();
			for (SpawnerStep<?> step : steps) {
				bean = step.apply(bean, beanClass);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return bean;
	}

}
