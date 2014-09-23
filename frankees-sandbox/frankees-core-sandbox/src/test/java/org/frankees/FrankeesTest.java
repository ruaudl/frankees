package org.frankees;

import static org.fest.assertions.Assertions.*;
import static org.frankees.Frankees.*;

import org.frankees.domain.Monster;
import org.frankees.making.StaticMaker;
import org.junit.Test;

public class FrankeesTest {

	@Test
	public void spawnUsingWith() {
		Monster dracula = any(Monster.class).with("Dracula", "name")
				.with((short) 2, "legsCount").with(10, "frightLevel").with(1, "headsCount").with(false, "alive").with(null, "enemy").spawn();

		assertThat(dracula.getName()).isEqualTo("Dracula");
		assertThat(dracula.getLegsCount()).isEqualTo((short) 2);
		assertThat(dracula.getFrightLevel()).isEqualTo(10);
		assertThat(dracula.isAlive()).isFalse();
		assertThat(dracula.getHeadsCount()).isEqualTo(1);
		assertThat(dracula.getEnemy()).isNull();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void spawnUsingWithAndNullFieldShouldThrowException() {
		any(Monster.class).with("dracula", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void spawnUsingWithAndInvalidFieldShouldThrowException() {
		any(Monster.class).with("red", "bloodColor");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void spawnUsingWithAndNullValueForPrimitiveFieldShouldThrowException() {
		any(Monster.class).with(null, "headsCount");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void spawnUsingWithAndNonMatchingValueTypeForFieldShouldThrowException() {
		any(Monster.class).with(false, "headsCount");
	}

	@Test
	public void spawnUsingByStaticMaker() {
		Monster dracula = any(Monster.class).by(new StaticMaker().withMaxLevel(0)).spawn();

		assertThat(dracula.getName()).isNotNull();
		assertThat(dracula.getLegsCount()).isNotNull();
		assertThat(dracula.getFrightLevel()).isNotNull();
		assertThat(dracula.getEnemy()).isNotNull();
		assertThat(dracula.getEnemy().getName()).isNull();
	}

	@Test
	public void spawnUsingByStaticMakerWithMaxLevelOf2() {
		Monster dracula = any(Monster.class).by(new StaticMaker().withMaxLevel(1)).spawn();

		assertThat(dracula.getName()).isNotNull();
		assertThat(dracula.getLegsCount()).isNotNull();
		assertThat(dracula.getFrightLevel()).isNotNull();
		assertThat(dracula.getEnemy()).isNotNull();
		assertThat(dracula.getEnemy().getName()).isNotNull();
		assertThat(dracula.getEnemy().getEnemy()).isNotNull();
		assertThat(dracula.getEnemy().getEnemy().getName()).isNull();
	}
	
}
