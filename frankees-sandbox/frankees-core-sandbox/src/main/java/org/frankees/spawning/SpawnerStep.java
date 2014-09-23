package org.frankees.spawning;

public interface SpawnerStep<F extends Object> {

	<T> T apply(T bean, Class<T> beanClass);

}
