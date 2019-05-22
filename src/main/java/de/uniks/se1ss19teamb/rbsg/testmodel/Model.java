package de.uniks.se1ss19teamb.rbsg.testmodel;

public class Model {

	private static Model instance;
	private final Game game;

	private Model() {
		Game game = new Game();
		this.game = game;
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
