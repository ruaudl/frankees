package org.frankees.making;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.frankees.tooling.BeanField;

public interface Maker {
	
	Object make(String name, Type type);

	<T> T makeBean(Class<T> beanClass);

	<B, F> F makeField(BeanField<B> beanField);

	<B> Collection<?> makeCollection(BeanField<B> field);

	<B> Map<?, ?> makeMap(BeanField<B> field);

}
