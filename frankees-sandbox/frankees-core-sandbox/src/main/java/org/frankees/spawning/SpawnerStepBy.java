package org.frankees.spawning;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.frankees.making.Maker;
import org.frankees.tooling.BeanField;

public class SpawnerStepBy<F extends Object> implements SpawnerStep<F> {

	private Maker maker;
	private Map<String, BeanField> fields;

	public SpawnerStepBy(Maker maker, Map<String, BeanField> fields) {
		this.maker = maker;
		this.fields = fields;
	}

	@Override
	public <T> T apply(T bean, Class<T> beanClass) {
		for (BeanField field : fields.values()) {
			try {
				if (field.getFieldDescriptor().getReadMethod().invoke(bean, new Object[0]) == null) {
					Object value = maker.makeField(field);
					field.getFieldDescriptor().getWriteMethod().invoke(bean, value);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return bean;
	}

}
