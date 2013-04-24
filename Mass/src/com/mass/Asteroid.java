package com.mass;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Asteroid {
	
	Body asteroid;
	
	Sprite region;
	
	public Fixture asteroidFixture;
	
	public Asteroid(Body body, float size, Sprite sprite) {
		this.asteroid = body;
		this.region = sprite;
		
		CircleShape circle2 = new CircleShape();
		circle2.setRadius((float) (size*1f)+1);
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = circle2;
		fixtureDef2.density = 0.4f; 
		fixtureDef2.friction = 0.2f;
		fixtureDef2.restitution = 0.5f; // Make it bounce a little bit
		
		asteroidFixture = asteroid.createFixture(fixtureDef2);
		asteroid.resetMassData();
		String userData = "asteroid";
		asteroid.setUserData(userData);
		circle2.dispose();
	}
	
	public Body getBody() {
		// TODO Auto-generated method stub
		return asteroid;
	}
}
