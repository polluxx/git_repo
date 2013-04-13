<<<<<<< HEAD
package com.mass;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.mass.Player.State;
import com.mass.Weapon.WeaponType;

public class GameScreen implements Screen, InputProcessor {

	
	private static final int DEGTORAD = 1;
	
	public int accelXNor = 0;
	
	private WorldRenderer 	renderer;
	public NewWorld world;
	
	// ***
	private SpriteBatch spriteBatch;
	Texture texture;
	public  Map<String, TextureRegion> textureRegions;// = new HashMap<String, TextureRegion>();
	public  Map<String, TextureRegion> textureRegions2;// = new HashMap<String, TextureRegion>();
	private SpriteBatch batch;
	private Sprite sprite;
	
	public Player player;
	
	Button imgButton;
	Button img2Button;
	Slider slider;
	Skin skin;
	Stage stage;
	// ***
	
	public int width;
	public int height;
    public Vector2 startVelocity = new Vector2();  
    public Vector2 startPoint = new Vector2();  
    
    static final int screenWidth = Gdx.graphics.getWidth();
    static final double screenHeight = Gdx.graphics.getHeight() ;
	
	/** a hit body **/
	Body hitBody = null;
	/** our ground box **/
	Body groundBody;
	private WorldController	controller;
	  
	public void show() {
		
		NewWorld.CAMERA_WIDTH =  NewWorld.CAMERA_HEIGHT* Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
		loadTextures();
		world = new NewWorld();
	
		renderer = new WorldRenderer(world, NewWorld.CAMERA_WIDTH, NewWorld.CAMERA_HEIGHT,true);
		controller = new WorldController(world/*, renderer*/);
		


		spriteBatch = new SpriteBatch();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		stage = new Stage(screenWidth, (float) screenHeight, false, spriteBatch);
		Gdx.input.setInputProcessor(stage);
		
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		//Gdx.input.setInputProcessor(this);
		createFireButton();
		createVelocitySlider();
	}
	
	private void loadTextures() {
		
	}
	
	private void createVelocitySlider() {

		InputListener stopTouchDown = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		};
		
		slider = new Slider(0, world.getPlayer().MAX_VELOCITY, 1, true, skin);
		slider.setValue( 0 );
		slider.setWidth(Gdx.graphics.getWidth()/10);
		slider.setHeight((float) (screenHeight/2 + 100));
		slider.addListener(stopTouchDown);
		slider.setX( 20);
		slider.setY( 30);
		
		//stage.addActor(img2Button);
		stage.addActor(slider);
		
		slider.addListener( new ChangeListener() {
            @Override
            public void changed(
                ChangeEvent event,
                Actor actor )
            {
                float value = ( (Slider) actor ).getValue();
                world.getPlayer().CURRENT_VELOCITY = value;
            }
        } );
	}
	
	public void createFireButton() {
		
		TextureRegion image2 = new TextureRegion(new Texture(Gdx.files.internal("images/target.png")));
		TextureRegion image = new TextureRegion(image2);
		TextureRegion imageFlipped = new TextureRegion(image);
		
		ImageButtonStyle style = new ImageButtonStyle(skin.get(ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(image2);
		style.imageDown = new TextureRegionDrawable(imageFlipped);
		imgButton = new Button(new Image(image), skin);
		imgButton.setX(screenWidth - 80);
		imgButton.setY( (float) (40));
		stage.addActor(imgButton);
		imgButton.addListener( new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				fireReleased();
		    };
	    } );
	}

	
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		return false;
	}
	
	@Override
	public boolean keyTyped(char character) {
		return false; 
	}
	
	/*public void SetCamera(float x, float y){
		this.cam.position.set(x, y,0);	
		this.cam.update();
	}*/
	
	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);
		this.width = width;
		this.height = height;
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {		
		renderer.dispose();
		controller.dispose();
		stage.dispose();
		Gdx.input.setInputProcessor(null);
	}


	@Override
	public boolean keyDown(int keycode) {
		
		return true;
	}
 
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//world.update(delta);
		//world.draw();
		
		stage.act( delta );
		stage.draw();
		
		
		controller.update(delta);
		
		Body playerBody = world.getPlayer().getBody();
		
		
		//Body planetBody = world.getPlanet().getBody();
		
		Array<Planet> planets = world.getPlanets();
		for(Planet planet : planets) {
			
			float planetRadius = 3000f;
			Vector2 planetDistance = new Vector2(0,0);
	        planetDistance.add(playerBody.getPosition());
			planetDistance.sub(planet.getBody().getPosition());
			float finalDistance = planetDistance.len();
			if (finalDistance<=planetRadius*3) {
				planetDistance.x = -planetDistance.x;
				planetDistance.y = -planetDistance.y;
				
		        float vecSum = Math.abs(planetDistance.x)+Math.abs(planetDistance.y);
		        planetDistance.mul((1/vecSum)*planetRadius/finalDistance);
		        playerBody.applyForce(planetDistance, playerBody.getWorldCenter());
		        
			}
			
		}

		

		  
		  
		/*
		// accelerometer
		int accelX = (int) Gdx.input.getAccelerometerX();
		
		accelX = -accelX;
		
		int setter = accelX;
		if (accelX < 0) {
			setter = -accelX;
		} 
		//int accelXNor = 0;
		//Body player = world.getPlayer().getBody();
		//player.setFixedRotation(false);	

		if (setter > 1) {
			world.getPlayer().changeVelocity((float) (accelX * 0.05));
		}*/
		renderer.render(delta);
	}
	@Override
	public boolean keyUp(int keycode) {
		
		return true;
	}
	
	
    public float getX(float t) {  
        return startVelocity.x * t + startPoint.x;  
    }  
  
    public float getY(float t) {  
        return 0.5f * t * t + startVelocity.y * t + startPoint.y;  
    }  

	
	/** we instantiate this vector and the callback here so we don't irritate the GC **/
	Vector3 testPoint = new Vector3();
	
	public void  fireReleased() {
		float angle = world.getPlayer().getAngle();
	    Vector2 vect = new Vector2((float)(Math.cos(angle)), (float)(Math.sin(angle)));
	    // toDo: why this magic to angle the vector???
	    Vector2 newVect = new Vector2(vect.x + 225 , vect.y -90);
		
		testPoint.set(newVect.x, newVect.y, 0);
		
		renderer.getCamera().unproject(testPoint);
		

		int energy = (int) world.getPlayer().getEnergy();
		if (energy > 0) {
			Weapon weapon = world.getWeapon();	
			
			Vector2 vector = new Vector2((float)(Math.cos(angle) * 100), (float)(Math.sin(angle) * 100));
			
			//weapon.createBlaster(testPoint.x, testPoint.y);
			weapon.createIP(testPoint.x, testPoint.y);
			world.getPlayer().setEnergy(-1);
		}
	}
    
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		//controller.resetWay();
		//if (!Gdx.app.getType().equals(ApplicationType.Android))
		//	return false;
		boolean firstFingerTouching = Gdx.input.isTouched(0);
		boolean thirdFingerTouching = Gdx.input.isTouched(2);
		
		if (thirdFingerTouching) {
			world.getWeapon().destroyGrip();
		}
		
		return true;
	} 
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		//if (!Gdx.app.getType().equals(ApplicationType.Android))
		//	return false;
	
		//controller.resetWay();
		hitBody = null;
		return true;
	}
	
	
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		//ChangeNavigation(x,y);
		int min_touch_y = (int) (Gdx.graphics.getHeight() * 0.9);
		boolean firstFingerTouching = Gdx.input.isTouched(0);
		boolean secondFingerTouching = Gdx.input.isTouched(1);
		
		//testPoint.set(x, y, 0);
		
		//renderer.getCamera().unproject(testPoint);
		
		if (secondFingerTouching == true) {
			renderer.SetScreenSize(Gdx.input.getX());
		}
		
		if (firstFingerTouching == true && Gdx.input.getY() < min_touch_y) {
			
			
			//world.getWorld().QueryAABB(callback, testPoint.x - 0.0001f, testPoint.y - 0.0001f, testPoint.x + 0.0001f, testPoint.y + 0.0001f);

			// TODO: Repair;
			// if (hitBody == groundBody) hitBody = null;

			if (hitBody != null && hitBody.getType() == BodyType.KinematicBody) 
				return false;

			if (hitBody != null) {
				/*float worldWidth = Gdx.graphics.getWidth();
				float worldHeight = Gdx.graphics.getHeight();
				float centerY = worldHeight / 2;
				float centerX = worldWidth / 2;
				
				world.getPlayer().setMove(centerX, centerY, testPoint.x, testPoint.y);
				
				world.getPlayer().state = State.FLYING;
				*/
			} else {
				//ChangeNavigation(testPoint.x,testPoint.y);
			}
			
		}
		controller.resetWay();
		//renderer.SetScreenSize(x, y);
		
		//renderer.SetCamera(x/20, y/20);
		
		//
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		//System.out.println(amount);
		return false;
	}
	
}
=======
package com.mass;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.mass.Player.State;
import com.mass.Weapon.WeaponType;

public class GameScreen implements Screen, InputProcessor {

	
	private static final int DEGTORAD = 1;
	
	public int accelXNor = 0;
	
	private WorldRenderer 	renderer;
	public NewWorld world;
	
	// ***
	private SpriteBatch spriteBatch;
	Texture texture;
	public  Map<String, TextureRegion> textureRegions;// = new HashMap<String, TextureRegion>();
	public  Map<String, TextureRegion> textureRegions2;// = new HashMap<String, TextureRegion>();
	private SpriteBatch batch;
	private Sprite sprite;
	
	public Player player;
	
	Button imgButton;
	Button img2Button;
	Slider slider;
	Skin skin;
	Stage stage;
	// ***
	
	public int width;
	public int height;
    public Vector2 startVelocity = new Vector2();  
    public Vector2 startPoint = new Vector2();  
    
    static final int screenWidth = Gdx.graphics.getWidth();
    static final double screenHeight = Gdx.graphics.getHeight() ;
	
	/** a hit body **/
	Body hitBody = null;
	/** our ground box **/
	Body groundBody;
	private WorldController	controller;
	  
	public void show() {
		
		NewWorld.CAMERA_WIDTH =  NewWorld.CAMERA_HEIGHT* Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
		loadTextures();
		world = new NewWorld();
	
		renderer = new WorldRenderer(world, NewWorld.CAMERA_WIDTH, NewWorld.CAMERA_HEIGHT,true);
		controller = new WorldController(world/*, renderer*/);
		


		spriteBatch = new SpriteBatch();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		stage = new Stage(screenWidth, (float) screenHeight, false, spriteBatch);
		Gdx.input.setInputProcessor(stage);
		
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		//Gdx.input.setInputProcessor(this);
		createFireButton();
		createVelocitySlider();
	}
	
	private void loadTextures() {
		
	}
	
	private void createVelocitySlider() {

		InputListener stopTouchDown = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		};
		
		slider = new Slider(0, world.getPlayer().MAX_VELOCITY, 1, true, skin);
		slider.setValue( 0 );
		slider.setWidth(Gdx.graphics.getWidth()/10);
		slider.setHeight((float) (screenHeight/2 + 100));
		slider.addListener(stopTouchDown);
		slider.setX( 20);
		slider.setY( 60);
		
		//stage.addActor(img2Button);
		stage.addActor(slider);
		
		slider.addListener( new ChangeListener() {
            @Override
            public void changed(
                ChangeEvent event,
                Actor actor )
            {
                float value = ( (Slider) actor ).getValue();
                world.getPlayer().CURRENT_VELOCITY = value;
            }
        } );
	}
	
	public void createFireButton() {
		
		TextureRegion image2 = new TextureRegion(new Texture(Gdx.files.internal("images/target.png")));
		TextureRegion image = new TextureRegion(image2);
		TextureRegion imageFlipped = new TextureRegion(image);
		
		ImageButtonStyle style = new ImageButtonStyle(skin.get(ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(image2);
		style.imageDown = new TextureRegionDrawable(imageFlipped);
		imgButton = new Button(new Image(image), skin);
		imgButton.setX(screenWidth - 80);
		imgButton.setY( (float) (40));
		stage.addActor(imgButton);
		imgButton.addListener( new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				fireReleased();
		    };
	    } );
	}

	
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		return false;
	}
	
	@Override
	public boolean keyTyped(char character) {
		return false; 
	}
	
	/*public void SetCamera(float x, float y){
		this.cam.position.set(x, y,0);	
		this.cam.update();
	}*/
	
	@Override
	public void resize(int width, int height) {
		
		this.width = width;
		this.height = height;
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {		
		renderer.dispose();
		controller.dispose();
		stage.dispose();
		Gdx.input.setInputProcessor(null);
	}


	@Override
	public boolean keyDown(int keycode) {
		
		return true;
	}
 
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//world.update(delta);
		//world.draw();
		
		stage.act( delta );
		stage.draw();
		
		
		controller.update(delta);
		
		Body playerBody = world.getPlayer().getBody();
		Body planetBody = world.getPlanet().getBody();
		
		float planetRadius = 2700f;
		Vector2 planetDistance = new Vector2(0,0);
        planetDistance.add(playerBody.getPosition());
		planetDistance.sub(planetBody.getPosition());
		float finalDistance = planetDistance.len();
		if (finalDistance<=planetRadius*3) {
			planetDistance.x = -planetDistance.x;
			planetDistance.y = -planetDistance.y;
			
	        float vecSum = Math.abs(planetDistance.x)+Math.abs(planetDistance.y);
	        planetDistance.mul((1/vecSum)*planetRadius/finalDistance);
	        playerBody.applyForce(planetDistance, playerBody.getWorldCenter());
	        
		}
		

		  
		  
		/*
		// accelerometer
		int accelX = (int) Gdx.input.getAccelerometerX();
		
		accelX = -accelX;
		
		int setter = accelX;
		if (accelX < 0) {
			setter = -accelX;
		} 
		//int accelXNor = 0;
		//Body player = world.getPlayer().getBody();
		//player.setFixedRotation(false);	

		if (setter > 1) {
			world.getPlayer().changeVelocity((float) (accelX * 0.05));
		}*/
		renderer.render(delta);
	}
	@Override
	public boolean keyUp(int keycode) {
		
		return true;
	}
	
	
    public float getX(float t) {  
        return startVelocity.x * t + startPoint.x;  
    }  
  
    public float getY(float t) {  
        return 0.5f * t * t + startVelocity.y * t + startPoint.y;  
    }  

	
	/** we instantiate this vector and the callback here so we don't irritate the GC **/
	Vector3 testPoint = new Vector3();
	
	public void  fireReleased() {
		float angle = world.getPlayer().getAngle();
	    Vector2 vect = new Vector2((float)(Math.cos(angle)), (float)(Math.sin(angle)));
	    // toDo: why this magic to angle the vector???
	    Vector2 newVect = new Vector2(vect.x + 225 , vect.y -90);
		
		testPoint.set(newVect.x, newVect.y, 0);
		
		renderer.getCamera().unproject(testPoint);
		

		int energy = (int) world.getPlayer().getEnergy();
		if (energy > 0) {
			Weapon weapon = world.getWeapon();	
			
			Vector2 vector = new Vector2((float)(Math.cos(angle) * 100), (float)(Math.sin(angle) * 100));
			
			//weapon.createBlaster(testPoint.x, testPoint.y);
			weapon.createIP(testPoint.x, testPoint.y);
			world.getPlayer().setEnergy(-1);
		}
	}
    
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		//controller.resetWay();
		//if (!Gdx.app.getType().equals(ApplicationType.Android))
		//	return false;
		boolean firstFingerTouching = Gdx.input.isTouched(0);
		boolean thirdFingerTouching = Gdx.input.isTouched(2);
		
		if (thirdFingerTouching) {
			world.getWeapon().destroyGrip();
		}
		
		return true;
	} 
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		//if (!Gdx.app.getType().equals(ApplicationType.Android))
		//	return false;
	
		//controller.resetWay();
		hitBody = null;
		return true;
	}
	
	
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		//ChangeNavigation(x,y);
		int min_touch_y = (int) (Gdx.graphics.getHeight() * 0.9);
		boolean firstFingerTouching = Gdx.input.isTouched(0);
		boolean secondFingerTouching = Gdx.input.isTouched(1);
		
		//testPoint.set(x, y, 0);
		
		//renderer.getCamera().unproject(testPoint);
		
		if (secondFingerTouching == true) {
			renderer.SetScreenSize(Gdx.input.getX());
		}
		
		if (firstFingerTouching == true && Gdx.input.getY() < min_touch_y) {
			
			
			//world.getWorld().QueryAABB(callback, testPoint.x - 0.0001f, testPoint.y - 0.0001f, testPoint.x + 0.0001f, testPoint.y + 0.0001f);

			// TODO: Repair;
			// if (hitBody == groundBody) hitBody = null;

			if (hitBody != null && hitBody.getType() == BodyType.KinematicBody) 
				return false;

			if (hitBody != null) {
				/*float worldWidth = Gdx.graphics.getWidth();
				float worldHeight = Gdx.graphics.getHeight();
				float centerY = worldHeight / 2;
				float centerX = worldWidth / 2;
				
				world.getPlayer().setMove(centerX, centerY, testPoint.x, testPoint.y);
				
				world.getPlayer().state = State.FLYING;
				*/
			} else {
				//ChangeNavigation(testPoint.x,testPoint.y);
			}
			
		}
		controller.resetWay();
		//renderer.SetScreenSize(x, y);
		
		//renderer.SetCamera(x/20, y/20);
		
		//
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		//System.out.println(amount);
		return false;
	}
	
}
>>>>>>> 4a0d5e8de561e8a494b2f19267afd59c0bcda5d8
