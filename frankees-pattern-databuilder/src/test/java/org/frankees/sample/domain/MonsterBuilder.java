package org.frankees.sample.domain;

public final class MonsterBuilder {

	private Monster store;

	private MonsterBuilder() {
		store = new Monster();
	}

	public static MonsterBuilder aMonster() {
		return new MonsterBuilder();
	}

	public MonsterBuilder withName(String name) {
		store.setName(name);
		return this;
	}

	public MonsterBuilder withFrightLevel(Integer level) {
		store.setFrightLevel(level);
		return this;
	}

	public MonsterBuilder withLegsCount(Short count) {
		store.setLegsCount(count);
		return this;
	}

	public Monster build() {
		Monster builded = new Monster();
		builded.setName(store.getName());
		builded.setFrightLevel(store.getFrightLevel());
		builded.setLegsCount(store.getLegsCount());
		return builded;
	}
}
