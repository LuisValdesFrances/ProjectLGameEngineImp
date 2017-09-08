package com.luis.projectlgameengineimp;

public class GameState {

	private static GameState gameState;
	
	public static GameState getInstance(){
		if(gameState == null){
			gameState = new GameState();
		}
		return gameState;
	}
	
	private int level;

	public static GameState getGameState() {
		return gameState;
	}

	public static void setGameState(GameState gameState) {
		GameState.gameState = gameState;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
