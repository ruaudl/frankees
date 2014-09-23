package org.frankees.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Monster {

	private String name;

	private Short legsCount;

	private Integer frightLevel;

	private int headsCount;

	private boolean alive;

	private char mark;

	private Monster enemy;

	private Set<Monster> friends;

	private List<Weapon> weapons;

	private Map<String, List<String>> legends;

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

	public Monster getEnemy() {
		return enemy;
	}

	public void setEnemy(Monster enemy) {
		this.enemy = enemy;
	}

	public int getHeadsCount() {
		return headsCount;
	}

	public void setHeadsCount(int headsCount) {
		this.headsCount = headsCount;
	}

	public char getMark() {
		return mark;
	}

	public void setMark(char mark) {
		this.mark = mark;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public List<Weapon> getWeapons() {
		return weapons;
	}

	public void setWeapons(List<Weapon> weapons) {
		this.weapons = weapons;
	}

	public Set<Monster> getFriends() {
		return friends;
	}

	public void setFriends(Set<Monster> friends) {
		this.friends = friends;
	}

	public Map<String, List<String>> getLegends() {
		return legends;
	}

	public void setLegends(Map<String, List<String>> legends) {
		this.legends = legends;
	}

}
