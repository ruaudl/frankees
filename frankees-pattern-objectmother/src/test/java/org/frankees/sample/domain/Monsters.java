package org.frankees.sample.domain;

public final class Monsters {

	private static Monster dracula;
	private static Monster wolfman;

	private Monsters() {
	}

	public static Monster getDracula() {
		if (dracula == null) {
			dracula = new Monster();
			dracula.setName("Dracula");
			dracula.setLegsCount((short) 2);
			dracula.setFrightLevel(10);
		}
		return dracula;
	}

	public static Monster getWolfman() {
		if (wolfman == null) {
			wolfman = new Monster();
			wolfman.setName("Wolfman");
			wolfman.setLegsCount((short) 4);
			wolfman.setFrightLevel(8);
		}
		return wolfman;
	}
}
