package org.frankees.sample.service;

import static org.fest.assertions.Assertions.*;

import org.frankees.sample.domain.Monsters;
import org.junit.Test;

public class MonsterBattleTest {

	@Test
	public void testFrightBattle() {
		assertThat(
				new MonsterBattle().frightBattle(Monsters.getDracula(),
						Monsters.getWolfman()))
				.isEqualTo(Monsters.getDracula());
	}

	@Test
	public void testRunBattle() {
		assertThat(
				new MonsterBattle().runBattle(Monsters.getDracula(),
						Monsters.getWolfman()))
				.isEqualTo(Monsters.getWolfman());
	}
}
