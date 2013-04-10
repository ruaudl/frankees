package org.frankees.builder;

public class PropertyDescription {

	private String propertyName;

	private TypeDescription propertyType;

	public PropertyDescription() {
	}

	public PropertyDescription(String propertyName, TypeDescription propertyType) {
		this.propertyName = propertyName;
		this.propertyType = propertyType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public TypeDescription getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(TypeDescription propertyType) {
		this.propertyType = propertyType;
	}

}
