package com.mass;

import java.util.ArrayList;

import com.mass.GameScreen;

import com.badlogic.gdx.Game;
public class Mass extends   Game  {
	//public GameScreen game;
	public GameScreen game;
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
		game = new GameScreen();
		setScreen(game);
	}
	
	
	@Override
	public void resize(int width, int height) {
		
	}
}
	