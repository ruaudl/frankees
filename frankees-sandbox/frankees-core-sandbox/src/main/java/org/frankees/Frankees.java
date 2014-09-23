package org.frankees;

import org.frankees.loading.Dataset;
import org.frankees.making.RandomMaker;
import org.frankees.spawning.Spawner;


public class Frankees {

	public static final String DEFAULT_DATASET = null;
	public static final RandomMaker RANDOM = null;

	public static <T> Spawner<T> any(Class<T> beanClass) {
		return new Spawner<T>(beanClass);
	}

	public static Object as(String string) {
		return null;
	}

	public static <T> T as(Class<T> pojoClass) {
		try {
			return pojoClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Dataset from(String defaultDataset) {
		return null;
	}

	public static RandomMaker generator() {
		return null;
	}

}
