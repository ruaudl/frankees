package org.frankees.loader;

import java.util.Map;
import java.util.Set;

public class LoaderDescription {
	
	private String loaderName;

	private TypeDescription loaderTypeDescription;

	private TypeDescription beanTypeDescription;

	private Map<String, Set<PropertyDescription>> beanDefinitions;

	public LoaderDescription() {
	}

	public LoaderDescription(String loaderName, TypeDescription builderTypeDescription,
			TypeDescription beanTypeDescription,
			Map<String, Set<PropertyDescription>> beanDefinitions) {
		super();
		this.loaderName = loaderName;
		this.loaderTypeDescription = builderTypeDescription;
		this.beanTypeDescription = beanTypeDescription;
		this.beanDefinitions = beanDefinitions;
	}

	public String getLoaderName() {
		return loaderName;
	}

	public void setLoaderName(String loaderName) {
		this.loaderName = loaderName;
	}

	public TypeDescription getLoaderTypeDescription() {
		return loaderTypeDescription;
	}

	public void setLoaderTypeDescription(TypeDescription loaderTypeDescription) {
		this.loaderTypeDescription = loaderTypeDescription;
	}

	public TypeDescription getBeanTypeDescription() {
		return beanTypeDescription;
	}

	public void setBeanTypeDescription(TypeDescription objectTypeDescription) {
		this.beanTypeDescription = objectTypeDescription;
	}

	public Map<String, Set<PropertyDescription>> getBeanDefinitions() {
		return beanDefinitions;
	}

	public void setBeanDefinitions(
			Map<String, Set<PropertyDescription>> beanDefinitions) {
		this.beanDefinitions = beanDefinitions;
	}

	@Override
	public String toString() {
		return "Loader " + loaderName + " (" + loaderTypeDescription + ") "
				+ beanTypeDescription;
	}

}
