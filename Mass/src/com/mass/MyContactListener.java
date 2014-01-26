package com.mass;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MyContactListener implements ContactListener{
	World world;
	NewWorld newworld;
	Player player;
	public MyContactListener(World world, NewWorld newW) {
		// TODO Auto-generated constructor stub
		this.world = world;
		this.newworld = newW;
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		//System.out.println(newworld.getPlayer());
		Object bodyUserData = contact.getFixtureA().getBody().getUserData();
		Object targetUserData = contact.getFixtureB().getBody().getUserData();
		
		if (bodyUserData == "bullet" && targetUserData != "ship") {
			contact.getFixtureA().setSensor(false);
			Array<Body> destroyed = newworld.getDestroyedBodies();
			Body destroy = contact.getFixtureB().getBody();
			
			if (destroyed.size > 0) {
				if (destroyed.indexOf(destroy, true) == -1) {
					destroyed.add(destroy);
				}
			} else {
				destroyed.add(destroy);
				
			}
		}
		
		/*if (bodyUserData == "IPBlockFired" && targetUserData != "IPBlock") {
			Array<Body> connected = newworld.getConnectedBodies();
			
			Body contactA = contact.getFixtureA().getBody();
			Body contactB = contact.getFixtureB().getBody();
			if (connected.size == 0) {
				if (connected.indexOf(contactA, true) == -1) {
					connected.add(contactA);
					connected.add(contactB);
				}
			} 
		}*/
		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		/*Object bodyUserData = contact.getFixtureB().getBody().getUserData();
		
		if (bodyUserData == "bullet") {
			contact.getFixtureB().setSensor(false);
			Array<Body> destroyed = newworld.getDestroyedBodies();
			Body destroy = contact.getFixtureB().getBody();
			
			if (destroyed.size > 0) {
				if (destroyed.indexOf(destroy, true) == -1) {
					destroyed.add(destroy);
				}
			} else {
				destroyed.add(destroy);
				
			}
		}*/
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		if (contact.isTouching()) {
			Body bodyA = contact.getFixtureA().getBody();
			Body bodyB = contact.getFixtureB().getBody();
			Object objA = bodyA.getUserData();
			Object objB = bodyB.getUserData();
			
			if (objA == "IPBlockFired" && objB == "asteroid" || objB == "IPBlockFired" && objA == "asteroid") {
				if (objB == "IPBlockFired" && objA == "asteroid") {
					bodyA = contact.getFixtureB().getBody();
					bodyB = contact.getFixtureA().getBody();
				}
				Array<Body> connected = newworld.getConnectedBodies();
				Array<Body> binded = newworld.getBindedBodies();
				Array<Body> chains = newworld.getChainsBodies();
				if (connected.size == 0) {
					if (connected.indexOf(bodyA, true) == -1 && binded.indexOf(bodyA, true) == -1 && binded.indexOf(bodyB, true) == -1) {
						connected.add(bodyA);
						connected.add(bodyB);
						
						chains.add(bodyA);
					}
				} 
			}
		}
	}

}
