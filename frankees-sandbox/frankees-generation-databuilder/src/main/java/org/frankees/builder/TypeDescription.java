package org.frankees.builder;

public class TypeDescription {

	private String packageName;

	private String className;

	public TypeDescription() {
	}

	public TypeDescription(String packageName, String className) {
		this.packageName = packageName;
		this.className = className;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return packageName + "." + className;
	}

}
