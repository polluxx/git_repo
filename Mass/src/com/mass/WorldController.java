package com.mass;

import java.util.HashMap;
import java.util.Map;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.Contact;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.utils.Array;

public class WorldController {   
	
	
	private NewWorld 	world;
	
	//����������� ��������
	  enum Keys {
	    LEFT, RIGHT, UP, DOWN
	  }
	  
	 
	int coordX = 0;
	int coordY = 0;
	  
	private static float ACCELERATION 	= 20f;
	private static float MAX_VEL 		= 4f;
	  
	 //���� ��������...����� ����� ��������� ������������ �� 2-� ������������
	  static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();

	  //������������� �����
	  static {
	    keys.put(Keys.LEFT, false);
	    keys.put(Keys.RIGHT, false);
	    keys.put(Keys.UP, false);
	    keys.put(Keys.DOWN, false);
	  };
	  
	public WorldController(NewWorld world/*, WorldRenderer renderer*/) {

		this.world = world;
		
		
	}
	//���� �������������, ��� �������� �����
	  public void leftPressed() {
	
	    keys.get(keys.put(Keys.LEFT, true));
	  }

	  //���� �������������, ��� �������� ������	
	  public void rightPressed() {
	    keys.get(keys.put(Keys.RIGHT, true));
	  }
	  
	  //����������� �����
	  public void leftReleased() {
	    keys.get(keys.put(Keys.LEFT, false));
	  }	
	  public void rightReleased() {
	    keys.get(keys.put(Keys.RIGHT, false));
	  }
	  //���� �������������, ��� �������� �����	
	  public void upPressed() {
	    keys.get(keys.put(Keys.UP, true));
	  }
	  
	  public void downPressed() {
		    keys.get(keys.put(Keys.DOWN, true));
	  }  
	  
	  public void downReleased() {
		    keys.get(keys.put(Keys.DOWN, false));
	  }
	  
	  public void upReleased() {
		    keys.get(keys.put(Keys.UP, false));
	  }
	  
	  public void coordsSet(int xPoint, int yPoint) {
		  coordX = xPoint;
		  coordY = yPoint;
	  }
	  
	  public void resetWay(){
		    rightReleased();
		    leftReleased();
		    downReleased();
		    upReleased();
		    Body player = world.getPlayer().getBody();
		    //player.applyLinearImpulse(0, 0, player.getPosition().x,  player.getPosition().y);
		  }
	  /*
	 public void resetImpulse(){
		   world.getPlayer().getBody().applyLinearImpulse(0, 0, world.getPlayer().getPosition().x, world.getPlayer().getPosition().y);
			
	 }*/
	public void dispose(){

		//world.dispose();
	}
	
	
	
	
	/**
	 *  �������� ������ 
	 * @param delta
	 * �����, �������� � ������� ����������.
	 */
	public void update(float delta) {
		//grounded = isPlayerGrounded(Gdx.graphics.getDeltaTime());
		//processInput();
		
	
		world.getPlayer().update(delta);
	}
	
	
	public static boolean grounded ;
	 //� ����������� �� ���������� ����������� �������� ���������� ����� ����������� �������� ��� ���������
	/**
	 * � ����������� �� ������� �� ���� ������ ����������� �������� ������.
	 */
	private void processInput() {
		
		Player player = world.getPlayer();
	    
		/*
	    if(!grounded)
			player.setFriction(0F);	
		else{
			if (!keys.get(Keys.LEFT) && !keys.get(Keys.RIGHT)) 
				player.setFriction(200F);
			else
				player.setFriction(0.2F);
		}
	    
	    
		
	    	
			if(grounded) {
				 player.jump();
					this.upReleased();
				 player.getBody().setGravityScale(-3);
				 //world.getWorld().setGravity(new Vector2(0, 20));
			}
	    
	    if ((keys.get(Keys.LEFT) && keys.get(Keys.RIGHT)) ||
				(!keys.get(Keys.LEFT) && (!keys.get(Keys.RIGHT)))) {
		
			player.getVelocity().x = 0;			
		}
	    */
	 }

}
