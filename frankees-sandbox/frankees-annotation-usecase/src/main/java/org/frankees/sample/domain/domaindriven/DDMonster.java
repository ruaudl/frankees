package org.frankees.sample.domain.domaindriven;

import org.frankees.annotation.Buildable;

@Buildable
public class DDMonster {

	private String name;

	private Short legsCount;

	private Integer frightLevel;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Short getLegsCount() {
		return legsCount;
	}

	public void setLegsCount(Short legsCount) {
		this.legsCount = legsCount;
	}

	public Integer getFrightLevel() {
		return frightLevel;
	}

	public void setFrightLevel(Integer frightLevel) {
		this.frightLevel = frightLevel;
	}
}
