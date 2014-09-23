package org.frankees.spawning;

public class SpawnerStepLike<F extends Object> implements
		SpawnerStep<F> {

	private String caseName;

	private Object dataset;

	public SpawnerStepLike(String caseName) {
		super();
		this.caseName = caseName;
	}

	public SpawnerStepLike(String caseName, Object dataset) {
		super();
		this.caseName = caseName;
		this.dataset = dataset;
	}

	public <T> T apply(T bean, Class<T> beanClass) {
		return bean;
	}

	public String getCaseName() {
		return caseName;
	}

	public Object getDataset() {
		return dataset;
	}

}
