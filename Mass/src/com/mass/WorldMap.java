package com.mass;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
	public Element corners;
	public Element trashes;
	
	private Element coord;
	
	public int size;
	public int gridSize = 200;
	
	public int currentGridCell = 0;
	
	private NewWorld world;
	
	Sprite[] textures = new Sprite[5];
	
	Asteroid asteroid;
	Sprite asteroidSprite = new Sprite();
	// create array of planets coords
	Array<Element> planetsCoords = new Array<Element>();
	Vector2 currentCoordsPlanet;
	Vector2 iterateCoordsPlanet;
	
	Array<Element> topPoints = new Array<Element>();
	Array<Element> asterPoints = new Array<Element>();
	Array<Element> trashPoints = new Array<Element>();
	
	Array<Planet> planets;
	
	Array<Asteroid> asteroids;
	public  Map<String, TextureRegion> textureRegions;
	
	//set MAP
	Element MAP;
	
	public WorldMap(NewWorld world2) {
		this.world = world2;
        FileHandle xmlmap = Gdx.files.internal( "data/maps/map3.xml" );
        
        //if( xmlmap.exists() ) {
	        String data_file = xmlmap.readString();
	        XmlReader reader = new XmlReader();
	        MAP = reader.parse(data_file);
	        coords = MAP.getChildByName("centers");
	        corners = MAP.getChildByName("corners");
	        trashes = MAP.getChildByName("trashes");
	        size = (int) Float.parseFloat(MAP.getAttribute("size"));
        //}
	       
        setWorldGrid();
	        
	    for (int i = 0; i < coords.getChildCount(); i++) {
	    	coord = coords.getChild(i);
	    	
	    	//float elevation = Float.parseFloat(coord.getAttribute("elevation"));
	    	topPoints.add(coord);
	    	
	    	/*if (elevation > 0.8) {
	    		topPoints.add(coord);
	    	} else if (elevation > 0.3 && elevation < 0.8) {
	    		asterPoints.add(coord);
	    	} else {
	    		trashPoints.add(coord);
	    	}*/
	    	
	    }
	    if (trashes != null) {
		    for (int i = 0; i < trashes.getChildCount(); i++) {
		    	Element trash = trashes.getChild(i);
		    	asterPoints.add(trash);
		    }
	    }
		createPlanets();
		createAsteroids();
		//createTrash();
	}
	
	private void setWorldGrid() {
		int startKey = 4;
		int squaredX = 0;
		int squaredY = 0;
		int gridNumber = size / gridSize;
		int currentSize = size;
		
		int i = 0;
		do {
			
			i++;
			if (squaredX < size/2) {
				squaredX += gridSize;
			} else {
				if (squaredY+gridSize < size/2) {
					squaredX = 0;
					squaredY += gridSize;
				} else {
					break;
				}
			}
			
			Element cornertl = new Element("corner", MAP.getChildByName("corners"));
			cornertl.setAttribute("lx", ""+squaredX);
			cornertl.setAttribute("ly", ""+squaredY);
			cornertl.setAttribute("id", ""+i);
			cornertl.setAttribute("rx", ""+(squaredX+gridSize));
			cornertl.setAttribute("ry", ""+(squaredY+gridSize));
			cornertl.setAttribute("elevation", "1");
			
			MAP.getChildByName("corners").addChild(cornertl);
			
		} while (squaredY < size/2);
		
		
	}
	
	public int getGridId(Vector2 coord) {
		int id = 0;
		for (int i = 0; i < corners.getChildCount(); i++) {
	    	Element corner = corners.getChild(i);
	    	float lx = Float.parseFloat(corner.getAttribute("lx"));
	    	float ly = Float.parseFloat(corner.getAttribute("ly"));
	    	float rx = Float.parseFloat(corner.getAttribute("rx"));
	    	float ry = Float.parseFloat(corner.getAttribute("ry"));
	    	
	    	if (lx < coord.x && rx > coord.x && ly < coord.y && ry > coord.y) {
	    		id = (int)Float.parseFloat(corner.getAttribute("id"));
	    		break;
	    	}
	    	
	    }
		return id;
	}
	
	public void createPlanets() {
		BodyDef defPlanet = new BodyDef();
		
        if (topPoints.size > 2) {
        	for(Element topPoint : topPoints) {
    	    	//float x = Float.parseFloat(topPoint.getAttribute("x"));
    	    	//float y = Float.parseFloat(topPoint.getAttribute("y"));
    	    	
        		//currentCoordsPlanet = new Vector2(x, y);
        		
        		for(Element nextPoint : topPoints) {
        	    	//float iterX = Float.parseFloat(nextPoint.getAttribute("x"));
        	    	//float iterY = Float.parseFloat(nextPoint.getAttribute("y"));
        	    	//iterateCoordsPlanet = new Vector2(iterX, iterY);
        			//float length = currentCoordsPlanet.dst(iterateCoordsPlanet);
        			
        			//if ((int)length > 150 && planetsCoords.size < 2) {
        				
        				planetsCoords.add(topPoint);
        				planetsCoords.add(nextPoint);
        				
        			//}
        			
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
					planet.radius = 30f * size;
					planet.getBody().setTransform(coordX, coordY, 0);
					
					// adding planets to array
					planets = world.getPlanets();
					planets.add(planet);
				}
			}
        }
	}
	
	public void createAsteroids() {
		if (asterPoints.size > 1) {
			
			textureRegions = world.getTextures();
			
			for(int i = 0; i<5; i++) {
				textures[i] = new Sprite(textureRegions.get("ast"+i));
			}
			
			int counter = 0;
			for(Element asteroid : asterPoints) { 
				double rand = Math.random() * 10 / 2;
				counter++;
				if (counter < 300) {
	    	    	float coordX = Float.parseFloat(asteroid.getAttribute("x"));
	    	    	float coordY = Float.parseFloat(asteroid.getAttribute("y"));
	    	    	float size  = Float.parseFloat(asteroid.getAttribute("elevation"));
	    	    	createAsteroid(coordX, coordY, size + 1, textures[(int)rand], "asteroid");
				}
    	    	//float coordrX = Float.parseFloat(asteroid.getAttribute("rx"));
    	    	//float coordrY = Float.parseFloat(asteroid.getAttribute("ry"));
    	    	
    	    	//createAsteroid(coordrX, coordrY, size + 1, textures[(int)rand], "asteroid");
    	    	
    	    	
			}
		}
	}
	
	public void createTrash() {
		if (trashPoints.size > 1) {
			for(Element trash : trashPoints) { 
				double rand = Math.random() * 10 / 2;
    	    	float coordX = Float.parseFloat(trash.getAttribute("x"));
    	    	float coordY = Float.parseFloat(trash.getAttribute("y"));
    	    	float size  = Float.parseFloat(trash.getAttribute("elevation"));
    	    	createAsteroid(coordX, coordY, size, asteroidSprite, "trash");
			}
		}
	}
	
	public void createAsteroid(float x, float y, float size, Sprite region, String type) {
		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyType.DynamicBody;
		bodyDef2.position.set(x , y);
		Body asteroidBody = world.getWorld().createBody(bodyDef2);
		asteroid = new Asteroid(asteroidBody, size, region);
		
		if (type == "asteroid") {
			// adding trash to array
			asteroids = world.getAsteroids();
			asteroids.add(asteroid);
		}
	}
}
