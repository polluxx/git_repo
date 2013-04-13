package com.mass;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Asteroid {
	
	Body asteroid;
	
	TextureRegion region;
	
	public Asteroid(Body body, float size, TextureRegion srpite) {
		this.asteroid = body;
		this.region = srpite;
		
		CircleShape circle2 = new CircleShape();
		circle2.setRadius((float) (size*1f));
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = circle2;
		fixtureDef2.density = 0.4f; 
		fixtureDef2.friction = 0.2f;
		fixtureDef2.restitution = 0.5f; // Make it bounce a little bit
		
		asteroid.createFixture(fixtureDef2);
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
