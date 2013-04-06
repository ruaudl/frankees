package org.frankees.sample.service;

import static org.fest.assertions.Assertions.*;

import org.frankees.sample.domain.Monster;
import org.frankees.sample.domain.MonsterBuilder;
import org.junit.Before;
import org.junit.Test;

public class MonsterBattleTest {

	private Monster dracula;
	private Monster wolfman;

	@Before
	public void setUpMonsters() {
		dracula = MonsterBuilder.aMonster().withName("Dracula")
				.withLegsCount((short) 2).withFrightLevel(10).build();
		wolfman = MonsterBuilder.aMonster().withName("Wolfman")
				.withLegsCount((short) 4).withFrightLevel(8).build();
	}

	@Test
	public void testFrightBattle() {
		assertThat(new MonsterBattle().frightBattle(dracula, wolfman))
				.isEqualTo(dracula);
	}

	@Test
	public void testRunBattle() {
		assertThat(new MonsterBattle().runBattle(dracula, wolfman)).isEqualTo(
				wolfman);
	}
}
