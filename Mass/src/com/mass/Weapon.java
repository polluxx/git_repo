package com.mass;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.mass.Player.State;

public class Weapon {
	public enum WeaponType {
		BLASTER, GRIP, LASER
	}
	
	public enum userDat {
      health,
      stunnedTimeout
	}
	
	WeaponType	weaponType = WeaponType.BLASTER;
	
	NewWorld world;
	World worldPhys;
	Body gripBody;
	
	public Weapon(NewWorld world2, WeaponType weaponType) {
		this.world = world2;
		switch (weaponType) {
			case BLASTER:
				//createBlaster(x,y);
			break;
			case GRIP:
				
			break;
			case LASER:
				
			break;
		}
	}
	
	public void createBlaster(float x, float y) {
		PolygonShape poly = new PolygonShape();
		poly.setAsBox((float) 0.1, (float) 0.2);
		
		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyType.DynamicBody;
		float angle = world.getPlayer().getAngle();
		
		Vector2 angleVector = new Vector2((float)Math.cos(angle)*4, (float)Math.sin(angle)*4);
		
		bodyDef2.position.set(world.getPlayer().getBody().getPosition().x,world.getPlayer().getBody().getPosition().y);
		Body arrowBody = world.getWorld().createBody(bodyDef2);
		arrowBody.setBullet(false);
		arrowBody.setFixedRotation(true);
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = poly;
		fixtureDef2.density = 0.1f; 
		fixtureDef2.friction = 0.1f;
		fixtureDef2.restitution = 0.1f; // Make it bounce a little bit
		fixtureDef2.isSensor = true;
		arrowBody.createFixture(fixtureDef2);
		String userData = "bullet";
		arrowBody.setUserData(userData);
		
		poly.dispose();
		
		  Vector2 wor = new Vector2(1,0);
		  Vector2 pointingDirection = arrowBody.getWorldVector( wor );
		  Vector2 flightDirection = arrowBody.getLinearVelocity();
		  Vector2 flightSpeed = flightDirection.nor();//normalizes and returns length
		  
		  
		  int flyingAngle = (int) (Math.atan2(y, x) * MathUtils.radiansToDegrees);
		  
		  Vector2 direction = new Vector2(x - arrowBody.getPosition().x, y - arrowBody.getPosition().y);
		  //System.out.println(angle - 90);
		
		  int velocityShip = (int) world.getPlayer().getCurrentVelocity();  
		  if (velocityShip < 1) {
			  velocityShip = 1;
		  }
		  
		Vector2 force = new Vector2(direction.x * 100 * velocityShip, direction.y * 100 * velocityShip);
		  
	    arrowBody.applyForce( force, arrowBody.getPosition());
	}
	
	public void createGrip() {
		PolygonShape poly = new PolygonShape();
		poly.setAsBox((float) 0.4, (float) 0.6);
		
		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyType.DynamicBody;
		bodyDef2.position.set((float) (world.getPlayer().getBody().getPosition().x - 2.2),world.getPlayer().getBody().getPosition().y-1);
		Body arrowBody = world.getWorld().createBody(bodyDef2);
		//arrowBody.setBullet(true);
		//arrowBody.setFixedRotation(true);
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = poly;
		fixtureDef2.density = 0.1f; 
		fixtureDef2.friction = 0.1f;
		fixtureDef2.restitution = 0.1f; // Make it bounce a little bit
		arrowBody.createFixture(fixtureDef2);
		arrowBody.setTransform(arrowBody.getPosition(), 0);
		String userData = "gripBlock";
		arrowBody.setUserData(userData);
		gripBody = arrowBody;
		poly.dispose();
		
		WeldJointDef jointDef = new WeldJointDef();
        Vector2 vec = new Vector2(world.getPlayer().getBody().getPosition().x - 2 , world.getPlayer().getBody().getPosition().y-1);
        jointDef.initialize(world.getPlayer().getBody(), arrowBody, vec);
        //jointDef.collideConnected = true;
        //jointDef.enableLimit = true;
        //jointDef.lowerAngle = lowerAngle;
        //jointDef.upperAngle = upperAngle;
        
        world.getWorld().createJoint(jointDef);
        
        

        Body link = arrowBody;
        // rope
        for (int i = 1; i <= 20; i++) {
            // rope segment
        	BodyDef bodyDef = new BodyDef();
        	bodyDef.position.x=(float) arrowBody.getPosition().x+i;
        	bodyDef.position.y=i+arrowBody.getPosition().y;
            PolygonShape boxDef2 = new PolygonShape();
            boxDef2.setAsBox((float) 0.1, (float) 0.5);
            
            Body body2 = createPoly(BodyType.DynamicBody, 0.1, (float) 0.3, 0.1, arrowBody.getPosition().x, (float) (bodyDef.position.y+0.1));
            RevoluteJointDef jointDef2 = new RevoluteJointDef();
            Vector2 vect = new Vector2((float) body2.getPosition().x, body2.getPosition().y);
            jointDef2.initialize(link, body2, vect);
            jointDef2.collideConnected = true;
            jointDef2.maxMotorTorque = 10;
            jointDef2.enableLimit = true;
            jointDef2.maxMotorTorque = 10;
            //jointDef.enableMotor = true;
            
            world.getWorld().createJoint(jointDef2);
            // saving the reference of the last placed link
            link=body2;
        }
		
	}
	
	public void createIP(float x, float y) {
		PolygonShape poly = new PolygonShape();
		poly.setAsBox((float) 0.3, (float) 0.3);
		
		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyType.DynamicBody;
		bodyDef2.position.set(gripBody.getPosition().x, gripBody.getPosition().y);
		Body arrowBody = world.getWorld().createBody(bodyDef2);
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = poly;
		fixtureDef2.density = 0.4f; 
		fixtureDef2.friction = 0.7f;
		fixtureDef2.restitution = 0; 
		arrowBody.setBullet(true);
		arrowBody.createFixture(fixtureDef2);
		arrowBody.setTransform(arrowBody.getPosition(), 0);
		

		String userData = "IPBlockFired";
		
		arrowBody.setUserData(userData);
		
		poly.dispose();
		
		Vector2 direction = new Vector2(x - arrowBody.getPosition().x, y - arrowBody.getPosition().y);
		Vector2 impulse = new Vector2(direction.x * 100 , direction.y * 100 );
		  
	    arrowBody.applyLinearImpulse(impulse, arrowBody.getPosition());
	}
	
	public void destroyGrip() {
		world.getWorld().destroyBody(gripBody);
	}
	
	private Body createPoly(BodyType type, double d, float height, double e, double f, float y) {
		BodyDef def = new BodyDef();
		def.type = type;
		def.position.set((float) f , y);
		Body box = world.getWorld().createBody(def);
 
		PolygonShape poly = new PolygonShape();
		poly.setAsBox((float) d, height);
		
		box.createFixture(poly, (float) e);
		poly.dispose();
 
		return box;
	}
	
	public void createGripBody() {
		PolygonShape poly = new PolygonShape();
		poly.setAsBox((float) 0.6, (float) 0.4);
		
		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyType.DynamicBody;
		bodyDef2.position.set((float) (world.getPlayer().getBody().getPosition().x + 0.5),(float) (world.getPlayer().getBody().getPosition().y-1.4));
		Body arrowBody = world.getWorld().createBody(bodyDef2);
		//arrowBody.setBullet(true);
		//arrowBody.setFixedRotation(true);
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = poly;
		fixtureDef2.density = 0.1f; 
		fixtureDef2.friction = 0.1f;
		fixtureDef2.restitution = 0.1f; // Make it bounce a little bit
		fixtureDef2.isSensor = true;
		arrowBody.createFixture(fixtureDef2);
		arrowBody.setTransform(arrowBody.getPosition(), 0);
		
		String userData = "gripBlock";
		arrowBody.setUserData(userData);
		gripBody = arrowBody;
		poly.dispose();
		
		WeldJointDef jointDef = new WeldJointDef();
        Vector2 vec = new Vector2(world.getPlayer().getBody().getPosition().x , world.getPlayer().getBody().getPosition().y);
        jointDef.initialize(world.getPlayer().getBody(), arrowBody, vec);
        world.getWorld().createJoint(jointDef);
	}
}
