package com.mass;

import java.util.ArrayList;

import com.mass.GameScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
public class Mass extends   Game  {
	//public GameScreen game;
	public GameScreen game;
	
	public GameScreen menu;
	
	public GameScreen currentScreen;
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
		game = new GameScreen();
		
		currentScreen = game;
		
		setScreen(currentScreen);
	}
	
	public void changeScreen(GameScreen type) {
		this.currentScreen = type;
	}
	
	
	@Override
	public void resize(int width, int height) {
		
	}
}
	