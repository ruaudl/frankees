package org.frankees.loader;

public class PropertyDescription {

	private String propertyName;

	private String propertyValue;

	private TypeDescription propertyType;

	public PropertyDescription() {
	}

	public PropertyDescription(String propertyName, String propertyValue, TypeDescription propertyType) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.propertyType = propertyType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public TypeDescription getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(TypeDescription propertyType) {
		this.propertyType = propertyType;
	}

}
