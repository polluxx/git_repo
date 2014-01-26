package com.mass;

import java.io.InputStream;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mass.Weapon.WeaponType;

public class NewWorld {

	World world;
	Player player;
	Planet planet;
	Weapon weapon;
	
	Body shipModel;
	Body boxGround;
	Body groundBody;
	Vector2 shipModelOrigin;
	private WorldRenderer 	renderer;
	
    private int worldScale = 30;
    private Vector2 planetVector = new Vector2();
    private Sprite orbitCanvas = new Sprite();
    
    //private static mapWorld;
    private WorldMap map;
	
	public static float CAMERA_WIDTH = 8f;
	public static  float CAMERA_HEIGHT = 12f;
	
	public int width;
	public int height;
	
	// ** array for destroyed objects
	Array<Body> destroyed = new Array<Body>();
	
	Array<Body> connected = new Array<Body>();
	
	Array<Body> binded = new Array<Body>();

	Array<Body> chains = new Array<Body>();
	
	// array of planets
	Array<Planet> planets = new Array<Planet>();
	
	// -- asteroids create --
	Array<Asteroid> asteroids = new Array<Asteroid>();
	
	// texture regions
	java.util.Map<String, TextureRegion> textureRegions;
	
	/** a hit body **/
	Body hitBody = null;
	
	public void update(float delta){
		world.clearForces();
		
	}
	
	public WorldRenderer getRenderer() {
		return renderer;
	}
	
	public Array<Body> getDestroyedBodies() {
		return destroyed;
	}
	
	public Array<Body> getConnectedBodies() {
		return connected;
	}
	
	public Array<Body> getBindedBodies() {
		return binded;
	}
	
	public Array<Body> getChainsBodies() {
		return chains;
	}
	
	public void setDestroyedBodies(Array<Body> destroyed) {
		this.destroyed = destroyed;
	}
	
	public void setConnectedBodies(Array<Body> connect) {
		this.connected = connect;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}

	public Planet getPlanet(){
		return planet;
	}
	
	public Array<Planet> getPlanets() {
		return planets;
	}
	
	public Array<Asteroid> getAsteroids() {
		return asteroids;
	}
	
	public void setAsteroids(Array<Asteroid> asteroids) {
		this.asteroids = asteroids;
	}
	
	
	public World getWorld(){
		return world;
	}
	
	public java.util.Map<String, TextureRegion> getTextures() {
		return textureRegions;
	};
	
	public NewWorld(java.util.Map<String, TextureRegion> textures){
		textureRegions = textures;
		width = 8;
		height = 12;
		world = new World(new Vector2(0, 0), true);	
		world.setContactListener(new MyContactListener(world, this));
		//world.setAutoClearForces(true);
        BodyDef bodyDef = new BodyDef();
        groundBody = world.createBody(bodyDef);
        
        
        map = new WorldMap(this);

		createWorld();
	}
	
	public WorldMap getWorldMap() {
		return map;
	}
	
	
	public void reloadMap(Vector2 coordsPlayer) {
		//System.out.println(map.size);
		calculateBodies(coordsPlayer.x, coordsPlayer.y);
	}
	
	QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture (Fixture fixture) {
				Object bodyUserData = fixture.getBody().getUserData();
				
				if (bodyUserData == "asteroid") {
					hitBody = fixture.getBody();
					Vector2 planetDistance = new Vector2(0,0);
			        planetDistance.add(hitBody.getPosition());
					planetDistance.sub(player.getBody().getPosition());
					float finalDistance = planetDistance.len();
					//planetDistance.x = -planetDistance.x;
					//planetDistance.y = -planetDistance.y;
					
			        float vecSum = Math.abs(planetDistance.x)+Math.abs(planetDistance.y);
			        planetDistance.mul((1/vecSum)*300f/finalDistance);
			        hitBody.applyForce(planetDistance, hitBody.getWorldCenter());
					
					hitBody.setActive(true);
					
				}
				return true;
		}
	};
	
	private void calculateBodies(float x, float y) {
		Vector3 testPoint = new Vector3();
		testPoint.set(x, y, 0);

		world.QueryAABB(callback, testPoint.x-30f, testPoint.y-30f, testPoint.x+30f, testPoint.y+30f);
		
	}
	
	public void killBodies() {
		if (destroyed.size > 0) {
			for(Body kill : destroyed) {
				world.destroyBody(kill);
				destroyed.removeValue(kill, true);
			}
			createAsteroids();
		}
	}

	
	public void setPP(float x, float y){
		//ppuX = x;
		//ppuY = y;
	}
	
	private void createWorld(){
		
		// ---- PLAYER ----
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		shipModel = world.createBody(def);
		player = new Player(shipModel);		
		player.getBody().setTransform((float) 360, 360, 0);
		
		weapon = new Weapon(this, WeaponType.GRIP);	
		weapon.createGripBody();
		
	    BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/shipIst.json"));
	    shipModelOrigin = loader.getOrigin("ShipIstr", 5).cpy();
	 
	    // 4. Create the body fixture automatically by using the loader.
	    loader.attachFixture(shipModel, "ShipIstr", player.playerPhysicsFixture, 5);
	    shipModel.resetMassData();
	    
		//  ------ END PLAYER ------
		
	}
	
	public void createAsteroids() {
		int numberAsteroids = asteroids.size;
		int astToCreate = 300 - numberAsteroids;
		if (astToCreate > 0){
			for(int i=0;i<astToCreate; ++i){
				double randomInt = Math.random();
				Body circleGround = createCircles((float) randomInt * 10);
				//asteroids.add(circleGround);
			}
		}
	}
	
	public Vector2 getShipOrigin() {
		return shipModelOrigin;
	}
	
	
	private void createBox() {
 
		
    	BodyDef bodyDef = new BodyDef();
    	bodyDef.position.x=player.getBody().getPosition().x+1;
    	bodyDef.position.y=player.getBody().getPosition().y+1;
        PolygonShape boxDef = new PolygonShape();
        boxDef.setAsBox((float) 0.1, (float) 0.5);
        
        Body body = createPoly(BodyType.DynamicBody, 0.1, (float) 0.3, 0.1, 0.2, 1);
        
        RevoluteJointDef jointDef = new RevoluteJointDef();
        Vector2 vec = new Vector2(player.getBody().getPosition().x, player.getBody().getPosition().y);
        jointDef.initialize(player.getBody(), body, vec);
        //jointDef.collideConnected = true;
        //jointDef.enableLimit = true;
        //jointDef.lowerAngle = lowerAngle;
        //jointDef.upperAngle = upperAngle;
        
        //world.createJoint(jointDef);
        
        
        Body link = body;
        // rope
        for (int i = 1; i <= 10; i++) {
            // rope segment
        	BodyDef bodyDef2 = new BodyDef();
        	bodyDef2.position.x=(float) player.getBody().getPosition().x+i;
        	bodyDef2.position.y=i+player.getBody().getPosition().y;
            PolygonShape boxDef2 = new PolygonShape();
            boxDef2.setAsBox((float) 0.1, (float) 0.5);
            
            Body body2 = createPoly(BodyType.DynamicBody, 0.1, (float) 0.3, 0.1, 0.1, (float) (i));
            
            //Body body2 = world.createBody(bodyDef);
            //body2.createFixture(boxDef2, 0);
            // joint
            
            Vector2 vect = new Vector2((float) 0.1, (float) (i));
            jointDef.initialize(link, body2, vect);
            jointDef.enableMotor = true;
            //jointDef.enableLimit = true;
            //jointDef.collideConnected = true;
            //jointDef.lowerAngle = 10;
            //jointDef.enableLimit = true;
            //jointDef.lowerAngle = lowerAngle;
            //jointDef.upperAngle = upperAngle;
            
            world.createJoint(jointDef);
            // saving the reference of the last placed link
            link=body2;
        }
		
		
		//return box;
	}
	
	private Body createPoly(BodyType type, double d, float height, double e, double f, float y) {
		BodyDef def = new BodyDef();
		def.type = type;
		def.position.set((float) f , y);
		Body box = world.createBody(def);
 
		PolygonShape poly = new PolygonShape();
		poly.setAsBox((float) d, height);
		
		box.createFixture(poly, (float) e);
		poly.dispose();
 
		return box;
	}
	
	private Body createCircles(float randomInt) {
		// First we create a body definition
		BodyDef bodyDef2 = new BodyDef();
		//System.out.println(randomInt);
		bodyDef2.type = BodyType.DynamicBody;
		double randomSecond = Math.random();
		double randomSize = Math.random() * 3;
		if (randomInt > 0.5) {
			randomInt = -randomInt;
		}
		if (randomSecond < 0.5) {
			randomSecond = -randomSecond;
		}
		
		randomSecond = randomSecond*100;
		randomInt = randomInt*100;
		bodyDef2.position.set((float) (randomSecond) , (float)(randomInt));
		Body body2 = world.createBody(bodyDef2);
		//body2.applyForce(0, 10000, body2.getPosition().x, body2.getPosition().y);
		CircleShape circle2 = new CircleShape();
		circle2.setRadius((float) (randomSize*1f + 1));
		
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
	    
	    return body2;
	}

	public void dispose(){
		world.dispose();
		
	}
	
	/*
	public void createDebris(mouseX, mouseY) {
        addBox(mouseX,mouseY,20,20);
    }*/
	
	public void addPlanet(int pX, int pY, int r) {
		FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0;
        fixtureDef.density = 1;
        CircleShape circleShape = new CircleShape();
        fixtureDef.shape = circleShape;
        circleShape.setRadius(r/worldScale);
        
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        
        //bodyDef.userData = new Sprite();
        bodyDef.position.set(pX/worldScale,pY/worldScale);
        Body thePlanet = world.createBody(bodyDef);
        thePlanet.setUserData(new Sprite());
        //planetVector.push(thePlanet);
        thePlanet.createFixture(fixtureDef);
        
        //orbitCanvas.draw(pX,pY,r*3);
        
        circleShape.dispose();
    }
}
