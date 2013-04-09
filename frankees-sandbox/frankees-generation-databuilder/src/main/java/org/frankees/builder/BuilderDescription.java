package org.frankees.builder;

import java.util.Map;

public class BuilderDescription {

	private String builderPackageName;
	private String builderClassName;
	private String objectPackageName;
	private String objectClassName;
	private Map<String, String> properties;

	public String getBuilderPackageName() {
		return builderPackageName;
	}

	public void setBuilderPackageName(String builderPackageName) {
		this.builderPackageName = builderPackageName;
	}

	public String getBuilderClassName() {
		return builderClassName;
	}

	public void setBuilderClassName(String builderClassName) {
		this.builderClassName = builderClassName;
	}

	public String getObjectPackageName() {
		return objectPackageName;
	}

	public void setObjectPackageName(String objectPackageName) {
		this.objectPackageName = objectPackageName;
	}

	public String getObjectClassName() {
		return objectClassName;
	}

	public void setObjectClassName(String objectClassName) {
		this.objectClassName = objectClassName;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "BuilderDescription [builderPackageName=" + builderPackageName
				+ ", builderClassName=" + builderClassName
				+ ", objectPackageName=" + objectPackageName
				+ ", objectClassName=" + objectClassName + ", properties="
				+ properties + "]";
	}

}
