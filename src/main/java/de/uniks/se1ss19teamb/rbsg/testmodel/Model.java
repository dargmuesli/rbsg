package de.uniks.se1ss19teamb.rbsg.testmodel;

public class Model {

	private static Model instance;
	private final Game game;

	private Model() {
		this.game = new Game();
	}

	public static Model getInstance() {
		if(instance == null) {
			instance = new Model();
		}
		return instance;
	}

	public Game getGame() {
		return game;
	}
}
