package org.frankees.sample.domain.domaindriven;

import org.frankees.annotation.Buildable;

@Buildable(builderClassSuffix = "Assembler", builderPackageName = "org.frankees.sample.domain.domaindriven.test")
public class DDCharacter {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
