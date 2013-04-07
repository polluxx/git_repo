package com.mass;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Planet {
	final static float MAX_VELOCITY = 3f;
	public static float SPEED = 5;
	public final static float SIZE = 0.8f;
	public float radius = 20f;
	
	public CircleShape planetPhysicsFixture;
	public Fixture planetSensorFixture;
	Body box;
	//Body box2;
	
	public Planet(Body b, float rad) {
		box = b;		
		radius = rad;
		CircleShape circle2 = new CircleShape();
		circle2.setRadius(radius);
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = circle2;
		fixtureDef2.density = 0.9f; 
		fixtureDef2.friction = 0.8f;
		fixtureDef2.restitution = 0.7f; // Make it bounce a little bit
		
		planetSensorFixture = box.createFixture(fixtureDef2);
		
		circle2.dispose();		

	}

	public Body getBody() {
		// TODO Auto-generated method stub
		return box;
	}
	
	
	public Vector2 getPosition(){
		return box.getPosition();
	}
	
}
