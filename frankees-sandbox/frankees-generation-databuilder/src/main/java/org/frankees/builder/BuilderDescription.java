package org.frankees.builder;

import java.util.Set;

public class BuilderDescription {

	private TypeDescription builderTypeDescription;

	private TypeDescription objectTypeDescription;

	private Set<PropertyDescription> properties;

	public BuilderDescription() {
	}

	public BuilderDescription(TypeDescription builderTypeDescription,
			TypeDescription objectTypeDescription,
			Set<PropertyDescription> properties) {
		super();
		this.builderTypeDescription = builderTypeDescription;
		this.objectTypeDescription = objectTypeDescription;
		this.properties = properties;
	}

	public TypeDescription getBuilderTypeDescription() {
		return builderTypeDescription;
	}

	public void setBuilderTypeDescription(TypeDescription builderTypeDescription) {
		this.builderTypeDescription = builderTypeDescription;
	}

	public TypeDescription getObjectTypeDescription() {
		return objectTypeDescription;
	}

	public void setObjectTypeDescription(TypeDescription objectTypeDescription) {
		this.objectTypeDescription = objectTypeDescription;
	}

	public Set<PropertyDescription> getProperties() {
		return properties;
	}

	public void setProperties(Set<PropertyDescription> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "Builder " + builderTypeDescription + " for "
				+ objectTypeDescription;
	}

}
