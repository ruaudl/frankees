package org.frankees.sample.service;

import org.frankees.sample.domain.Monster;

public class MonsterBattle {

	public Monster frightBattle(Monster monster1, Monster monster2) {
		if (monster1 == null && monster2 == null) {
			return null;
		} else if (monster1 == null) {
			return monster2;
		} else if (monster2 == null) {
			return monster1;
		} else if (monster1.getFrightLevel() > monster2.getFrightLevel()) {
			return monster1;
		} else {
			return monster2;
		}
	}

	public Monster runBattle(Monster monster1, Monster monster2) {
		if (monster1 == null && monster2 == null) {
			return null;
		} else if (monster1 == null) {
			return monster2;
		} else if (monster2 == null) {
			return monster1;
		} else if (monster1.getLegsCount() > monster2.getLegsCount()) {
			return monster1;
		} else {
			return monster2;
		}
	}
}
