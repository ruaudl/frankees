package org.frankees.making;

import static org.fest.assertions.Assertions.*;

import java.util.List;

import org.frankees.domain.Monster;
import org.frankees.domain.Weapon;
import org.junit.Test;

public class StaticMakerTest {

	@Test
	public void makeMonster() {
		Monster monster = new StaticMaker().makeBean(Monster.class);

		assertNotEmptyMonster(monster);
		assertEmptyMonster(monster.getEnemy());
		assertEmptyList(monster.getWeapons());
	}

	@Test
	public void makeMonsterWithMaxLevelOf0() {
		Monster monster = new StaticMaker().withMaxLevel(0).makeBean(Monster.class);

		assertEmptyMonster(monster);
	}

	@Test
	public void makeMonsterWithMaxLevelOf2() {
		Monster monster = new StaticMaker().withMaxLevel(2).makeBean(Monster.class);

		assertNotEmptyMonster(monster);
		assertNotEmptyMonster(monster.getEnemy());
		assertEmptyMonster(monster.getEnemy().getEnemy());

		assertNotEmptyList(monster.getWeapons());
		assertEmptyList(monster.getEnemy().getWeapons());

		assertEmptyWeapon(monster.getWeapons().get(0));
	}

	@Test
	public void makeMonsterWithMaxLevelOf3() {
		Monster monster = new StaticMaker().withMaxLevel(3).makeBean(Monster.class);

		assertNotEmptyMonster(monster);
		assertNotEmptyMonster(monster.getEnemy());
		assertNotEmptyMonster(monster.getEnemy().getEnemy());
		assertEmptyMonster(monster.getEnemy().getEnemy().getEnemy());

		assertNotEmptyList(monster.getWeapons());
		assertNotEmptyList(monster.getEnemy().getWeapons());
		assertEmptyList(monster.getEnemy().getEnemy().getWeapons());

		assertNotEmptyWeapon(monster.getWeapons().get(0));
		assertEmptyWeapon(monster.getEnemy().getWeapons().get(0));
	}

	@Test
	public void makeWeapon() {
		Weapon weapon = new StaticMaker().makeBean(Weapon.class);

		assertNotEmptyWeapon(weapon);
	}

	private void assertEmptyMonster(Monster monster) {
		assertThat(monster).isNotNull();
		assertThat(monster.getName()).isNull();
		assertThat(monster.getLegsCount()).isNull();
		assertThat(monster.getFrightLevel()).isNull();
		assertThat(monster.getEnemy()).isNull();
		assertThat(monster.getFriends()).isNull();
		assertThat(monster.getWeapons()).isNull();
		assertThat(monster.getLegends()).isNull();
	}

	private void assertNotEmptyMonster(Monster monster) {
		assertThat(monster).isNotNull();
		assertThat(monster.getName()).isNotNull();
		assertThat(monster.getLegsCount()).isNotNull();
		assertThat(monster.getFrightLevel()).isNotNull();
		assertThat(monster.getEnemy()).isNotNull();
		assertThat(monster.getFriends()).isNotNull();
		assertThat(monster.getWeapons()).isNotNull();
		assertThat(monster.getLegends()).isNotNull();
	}

	private void assertEmptyList(List<?> list) {
		assertThat(list).isEmpty();
	}

	private void assertNotEmptyList(List<?> list) {
		assertThat(list).hasSize(1);
		assertThat(list.get(0)).isNotNull();
	}

	private void assertEmptyWeapon(Weapon weapon) {
		assertThat(weapon.getName()).isNull();
		assertThat(weapon.getCategory()).isNull();
		assertThat(weapon.getProperties()).isNull();
	}

	private void assertNotEmptyWeapon(Weapon weapon) {
		assertThat(weapon.getName()).isNotNull();
		assertThat(weapon.getCategory()).isNotNull();
		assertThat(weapon.getProperties()).isNotNull();
	}
}
