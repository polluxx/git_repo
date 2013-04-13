package com.mass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
<<<<<<< HEAD
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
=======
>>>>>>> 4a0d5e8de561e8a494b2f19267afd59c0bcda5d8
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class WorldMap {
	
	public Element coords;
	
	private Element coord;
	
	public String size;
	
	private NewWorld world;
	
<<<<<<< HEAD
	Asteroid asteroid;
	Sprite asteroidSprite = new Sprite();
	
=======
>>>>>>> 4a0d5e8de561e8a494b2f19267afd59c0bcda5d8
	// create array of planets coords
	Array<Element> planetsCoords = new Array<Element>();
	Vector2 currentCoordsPlanet;
	Vector2 iterateCoordsPlanet;
	
	Array<Element> topPoints = new Array<Element>();
	Array<Element> asterPoints = new Array<Element>();
	Array<Element> trashPoints = new Array<Element>();
	
<<<<<<< HEAD
	Array<Planet> planets;
	
	Array<Asteroid> asteroids;
	TextureRegion ast2;
	TextureRegion ast3;
	TextureRegion ast4;
	TextureRegion ast5;
	TextureRegion ast6;
=======
	Array<Body> planets;
>>>>>>> 4a0d5e8de561e8a494b2f19267afd59c0bcda5d8
	
	public WorldMap(NewWorld world2) {
		this.world = world2;
        FileHandle xmlmap = Gdx.files.internal( "data/maps/map_perlin2.xml" );
        
        //if( xmlmap.exists() ) {
	        String data_file = xmlmap.readString();
	        XmlReader reader = new XmlReader();
	        Element data_formated = reader.parse(data_file);
	        coords = data_formated.getChildByName("centers");
	        size = data_formated.getAttribute("size");
        //}
	    for (int i = 0; i < coords.getChildCount(); i++) {
	    	coord = coords.getChild(i);
	    	float elevation = Float.parseFloat(coord.getAttribute("elevation"));
	    	if (elevation > 0.8) {
	    		topPoints.add(coord);
	    	} else if (elevation > 0.3 && elevation < 0.8) {
	    		asterPoints.add(coord);
	    	} else {
	    		trashPoints.add(coord);
	    	}
	    	
	    }
	        
		createPlanets();
		createAsteroids();
<<<<<<< HEAD
		//createTrash();
=======
		createTrash();
>>>>>>> 4a0d5e8de561e8a494b2f19267afd59c0bcda5d8
	}
	
	public void createPlanets() {
		BodyDef defPlanet = new BodyDef();
		
        if (topPoints.size > 2) {
        	for(Element topPoint : topPoints) {
    	    	float x = Float.parseFloat(topPoint.getAttribute("x"));
    	    	float y = Float.parseFloat(topPoint.getAttribute("y"));
    	    	
        		currentCoordsPlanet = new Vector2(x, y);
        		
        		for(Element nextPoint : topPoints) {
        	    	float iterX = Float.parseFloat(nextPoint.getAttribute("x"));
        	    	float iterY = Float.parseFloat(nextPoint.getAttribute("y"));
        	    	iterateCoordsPlanet = new Vector2(iterX, iterY);
        			float length = currentCoordsPlanet.dst(iterateCoordsPlanet);
        			
<<<<<<< HEAD
        			if ((int)length > 150 && planetsCoords.size < 2) {
        				
        				planetsCoords.add(topPoint);
        				planetsCoords.add(nextPoint);
        				
        			}
        			
=======
        			if (length > 150 && planetsCoords.size < 4) {
        				planetsCoords.add(topPoint);
        				planetsCoords.add(nextPoint);
        			}
>>>>>>> 4a0d5e8de561e8a494b2f19267afd59c0bcda5d8
        		}
        	}
		
			if (planetsCoords.size > 1) {
				for(Element planetElement : planetsCoords) { 
	    	    	float coordX = Float.parseFloat(planetElement.getAttribute("x"));
	    	    	float coordY = Float.parseFloat(planetElement.getAttribute("y"));
	    	    	float size  = Float.parseFloat(planetElement.getAttribute("elevation"));
					defPlanet.type = BodyType.StaticBody;
					Body boxPlanet = world.getWorld().createBody(defPlanet);
					Planet planet = new Planet(boxPlanet, 30f * size);
<<<<<<< HEAD
					planet.radius = 30f * size;
=======
>>>>>>> 4a0d5e8de561e8a494b2f19267afd59c0bcda5d8
					planet.getBody().setTransform(coordX, coordY, 0);
					
					// adding planets to array
					planets = world.getPlanets();
<<<<<<< HEAD
					planets.add(planet);
=======
					planets.add(planet.getBody());
>>>>>>> 4a0d5e8de561e8a494b2f19267afd59c0bcda5d8
				}
			}
        }
	}
	
	public void createAsteroids() {
		if (asterPoints.size > 1) {
<<<<<<< HEAD
			TextureRegion[] textures = new TextureRegion[5];
			textures[0] = new TextureRegion(new Texture(Gdx.files.internal("images/trash/ast2.png")));
			textures[1] = new TextureRegion(new Texture(Gdx.files.internal("images/trash/ast3.png")));
			textures[2] = new TextureRegion(new Texture(Gdx.files.internal("images/trash/ast4.png")));
			textures[3] = new TextureRegion(new Texture(Gdx.files.internal("images/trash/ast5.png")));
			textures[4] = new TextureRegion(new Texture(Gdx.files.internal("images/trash/ast6.png")));
			
			
			for(Element asteroid : asterPoints) { 
				double rand = Math.random() * 10 / 2;
				
    	    	float coordX = Float.parseFloat(asteroid.getAttribute("x"));
    	    	float coordY = Float.parseFloat(asteroid.getAttribute("y"));
    	    	float size  = Float.parseFloat(asteroid.getAttribute("elevation"));
    	    	createAsteroid(coordX*2, coordY*2, size + 1, textures[(int) rand]);
    	    	
=======
			for(Element asteroid : asterPoints) { 
    	    	float coordX = Float.parseFloat(asteroid.getAttribute("x"));
    	    	float coordY = Float.parseFloat(asteroid.getAttribute("y"));
    	    	float size  = Float.parseFloat(asteroid.getAttribute("elevation"));
    	    	createAsteroid(coordX, coordY, size + 1);
>>>>>>> 4a0d5e8de561e8a494b2f19267afd59c0bcda5d8
			}
		}
	}
	
	public void createTrash() {
		if (trashPoints.size > 1) {
			for(Element trash : trashPoints) { 
    	    	float coordX = Float.parseFloat(trash.getAttribute("x"));
    	    	float coordY = Float.parseFloat(trash.getAttribute("y"));
    	    	float size  = Float.parseFloat(trash.getAttribute("elevation"));
<<<<<<< HEAD
    	    	createAsteroid(coordX, coordY, size, asteroidSprite);
=======
    	    	createAsteroid(coordX, coordY, size);
>>>>>>> 4a0d5e8de561e8a494b2f19267afd59c0bcda5d8
			}
		}
	}
	
<<<<<<< HEAD
	public void createAsteroid(float x, float y, float size, TextureRegion region) {
		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyType.DynamicBody;
		bodyDef2.position.set(x , y);
		Body asteroidBody = world.getWorld().createBody(bodyDef2);
		asteroid = new Asteroid(asteroidBody, size, region);
		
		// adding trash to array
		asteroids = world.getAsteroids();
		asteroids.add(asteroid);
		
=======
	public void createAsteroid(float x, float y, float size) {
		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyType.DynamicBody;
		bodyDef2.position.set(x , y);
		Body body2 = world.getWorld().createBody(bodyDef2);
		
		CircleShape circle2 = new CircleShape();
		circle2.setRadius((float) (size*1f));
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = circle2;
		fixtureDef2.density = 0.4f; 
		fixtureDef2.friction = 0.2f;
		fixtureDef2.restitution = 0.5f; // Make it bounce a little bit
		
		body2.createFixture(fixtureDef2);
		body2.resetMassData();
		String userData = "asteroid";
		body2.setUserData(userData);
		circle2.dispose();
>>>>>>> 4a0d5e8de561e8a494b2f19267afd59c0bcda5d8
	}
}
