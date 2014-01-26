package com.mass;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

public class Player {
	final static float MAX_VELOCITY = 10;
	//final static float MAX_VELOCITY_STATE = (float) 120.0;
	public static float SPEED = 10f;
	
	public float CURRENT_VELOCITY = 0;
	
	public final static float SIZE = 0.8f;
	public static float DUMP = 2;
	public static float ANGULAR_DUMP = 0.1f;
	
	public static float MIN_DISTANCE = 10;
	
	public float energy_dump = (float) 0.05;
	public float MAX_ENERGY = 100;
	public float ENERGY = 100;
	
	public static int PTM_RATIO = 30;
	public static int ANGLE = 0;
	public static int SUB = 0;
	Vector2 position = new Vector2();
	Rectangle 	bounds = new Rectangle();
	Vector2 	acceleration = new Vector2();
	
	Vector2 force = new Vector2();
	
	public enum State {
		IDLE, FLYING, TURBO, DYING, COLLIDE
	}
	
	public FixtureDef playerPhysicsFixture;
	public Fixture playerSensorFixture;
	Body box;
	//Body box2;
	
	State	state = State.IDLE;
	
	public float Xset = 0;
	public float Yset = 0;
	
	public  Player(Body b){ 
		box = b;
		//box2 = b;
		
		
		CircleShape circle2 = new CircleShape();
		circle2.setRadius(3f);
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.density = 0.4f; 
		fixtureDef2.friction = 0.5f;
		fixtureDef2.restitution = 0.7f; // Make it bounce a little bit
		
		playerPhysicsFixture = fixtureDef2;
		box.setLinearDamping(DUMP);
		//box.setAngularDamping(ANGULAR_DUMP);
		
		
		String userData = "ship";
		box.setUserData(userData);
		
		
		circle2.dispose();		
		
		box.setFixedRotation(false);
		CURRENT_VELOCITY = 0;
		
	}
	
	public void setAngle(float pX, float pY) {
		box.setAngularVelocity(0);
		
		int degrees = (int) ((Math.atan2 (pX - box.getPosition().x, -(pY - box.getPosition().y))*180.0d/Math.PI)+180.0f);
		
		int degrSub = degrees + 90;
		if (degrSub > 360) {
			degrSub = degrSub - 360;
		}
		ANGLE = degrSub;
		isAngled = true;
		if (ANGLE > (int)getAngle()) {
			SUB = ANGLE - (int)getAngle();
		} else {
			SUB = (int)getAngle() - ANGLE;
		}
		
		if (SUB > 180) {
			int subsrt = SUB - 180;
			SUB = subsrt - 180;
		}
		
		//box.applyAngularImpulse(degrees);
		
	}
	
	public float getFriction(){
		return playerSensorFixture.getFriction();
	}
	
	public float getAngle() {
		float angle = box.getAngle() * MathUtils.radiansToDegrees;
		while (angle <= 0){
		   angle += 360;
		}
		while (angle > 360){
		   angle -= 360;
		}
		
		int del = (int) (angle % 2);
		if (del != 0) {
			angle += del;
			
		}
		return angle;
	}
	
	public Body getBody(){
		return box;
	}
	
	public void setMove(float x, float y) {
		Xset = x;
		Yset = y;
	}
	
	public void setPlayerStop() {
		state = State.COLLIDE;
	}
	
	public void setPlayerIdle() {
		state = State.IDLE;
	}
	
	public void setFriction(float f){
		//playerSensorFixture.setFriction(f); 
		//playerPhysicsFixture.setFriction(f); 
	}
	
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	Vector2 	velocity = new Vector2();
	public void update(float delta) {
		
		if (Xset != 0.0 && Yset != 0.0) {
			Vector2 vel = new Vector2(Xset, Yset);
			//System.out.println(vel);
			int distance = (int) vel.dst(box.getPosition());
			//System.out.println("distance: "+distance);
			
			float diffX = vel.x - this.getBody().getPosition().x;
			float diffY = (vel.y - this.getBody().getPosition().y);
			Vector2 touchedPoint = new Vector2(diffX, diffY);

			box.setTransform(box.getPosition().x, box.getPosition().y, touchedPoint.angle() * MathUtils.degreesToRadians);
			
			//System.out.println("angP: "+this.getBody().getAngle());
			//System.out.println("ang: "+touchedPoint.angle());
			//Vector2 vector = new Vector2(vel.x ,vel.y);
			
			
			float flyingAngle = (float) Math.atan2(vel.y, vel.x);
			//System.out.println("angle: "+flyingAngle);
			float forceAngular = flyingAngle * MathUtils.radiansToDegrees;
			//System.out.println(forceAngular);
			//box.setTransform(box.getPosition(), forceAngular);
			//box.setAngularVelocity(forceAngular);
			//box.applyLinearImpulse(vector, box.getPosition());
		}
		/*
		if (CURRENT_VELOCITY > MAX_VELOCITY) {
			CURRENT_VELOCITY = MAX_VELOCITY;
		}
		
		if (CURRENT_VELOCITY < 0) {
			CURRENT_VELOCITY = 0;
		}
		*/
		if (CURRENT_VELOCITY > 0) {
			
			float angle = (float) box.getAngle();
			Vector2 vect = new Vector2((float)(Math.cos(angle) * CURRENT_VELOCITY), (float)(Math.sin(angle) * CURRENT_VELOCITY));
			
			//Vector2 force = new Vector2(vect.x * box.getMass(), vect.y * box.getMass());
			box.applyLinearImpulse(vect, box.getPosition());
		}
		
		
		/*
		if (state == State.FLYING) {
			//int angle = (int) getAngle();
			int veloX = (int) box.getLinearVelocity().x;
			int veloY = (int) box.getLinearVelocity().y;
			
			if(veloX < 0) {
				veloX *= -1;
			}
			if(veloY < 0) {
				veloY *= -1;
			}
			if (veloX < veloY) {
				MIN_DISTANCE = veloY/3;
			} else {
				MIN_DISTANCE = veloX/3;
			}
			
			float flyingAngle = (float) Math.atan2(box.getLinearVelocity().y, box.getLinearVelocity().x);
			//box.SetAngle(flyingAngle);
			//float forceAngular = flyingAngle * MathUtils.radiansToDegrees;
			//System.out.println(forceAngular);
			box.setTransform(box.getPosition(), flyingAngle);
			//if (distance > MIN_DISTANCE) {
				//box.setLinearVelocity(1.3f, -9.0f);
				//if (force.x < MAX_VELOCITY_STATE && force.y < MAX_VELOCITY_STATE) {
					box.applyLinearImpulse(force, box.getPosition());
				//}
					
					
				box.setAngularVelocity(0);
			//} else {
				//position.sub(velocity.mul(vel));
				//force = new Vector2();
				//setPlayerIdle();
				//box.setLinearVelocity(0,0);
				//box.applyLinearImpulse(0,0, box.getPosition().x,box.getPosition().y);
				//box.setAngularVelocity(0);
			//}
		} else {
			force = new Vector2();
		}
		*/
	}
	
	boolean isAngled = false;
	
	public float getCurrentVelocity() {
		return CURRENT_VELOCITY;
	}
	
	public void setAngleTransform(int angle, int decr) {
		if (angle == 0 && decr < 0) {
			angle = 360;
		}
		if (angle == 360 && decr > 0) {
			angle = 0;
		}
		box.setTransform(box.getPosition(), (angle+decr) * MathUtils.degreesToRadians);
	}
	
	public void changeVelocity(float velocity) {
		
		//System.out.println(velocity);
		if (velocity > 0) {
			//state = State.FLYING;
			if (CURRENT_VELOCITY < MAX_VELOCITY) {
				if (CURRENT_VELOCITY + velocity <= MAX_VELOCITY) {
					CURRENT_VELOCITY = CURRENT_VELOCITY + velocity;
				}
			} else {
				CURRENT_VELOCITY = MAX_VELOCITY;
			}
		}
		if (velocity < 0) {
			if (CURRENT_VELOCITY > 0) {
				if (CURRENT_VELOCITY + velocity >= 0) {
					CURRENT_VELOCITY = CURRENT_VELOCITY + velocity;
				}
			} else {
				CURRENT_VELOCITY = 0;
			}
		}
		
	}
	
	public void setVelocityImpulse(int velo){
		if (velo < MAX_VELOCITY && velo > 0) {
			CURRENT_VELOCITY = velo;
		}
	}
	
	public float getEnergy() {
		return ENERGY;
	}
	
	public void setEnergy(double d) {
		ENERGY += d;
	}
	
	public void resetVelocity(){
		getVelocity().x =0;
		getVelocity().y =0;
	}

	public Vector2 getAcceleration() {
		return acceleration;
	}
}
