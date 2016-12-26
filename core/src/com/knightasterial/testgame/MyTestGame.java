package com.knightasterial.testgame;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class MyTestGame extends ApplicationAdapter {
	Texture dropImage;
	Texture bucketImage;
	Sound dropSound;
	Music rainMusic;
	
	SpriteBatch batch;
	OrthographicCamera camera;
	
	Rectangle bucket;
	
	Array<Rectangle> raindrops;
	long lastDropTime;
	
	@Override
	public void create () {
		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		
		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		
		 // start the playback of the background music immediately
		rainMusic.setLooping(true);
		rainMusic.play();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);
		
		batch = new SpriteBatch();
		
		//initialize bucket
		bucket = new Rectangle();
		bucket.x = 800/2 - 64/2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;
		
		//initialize raindrops
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}

	@Override
	public void render () {
						  //r  g   b    a
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen
		
		camera.update();
		
		//sets batch units to the units of the camera
		batch.setProjectionMatrix(camera.combined);
		
		//does the drawing
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for (Rectangle raindrop : raindrops){
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		
		batch.end();
		
		//processes user input to move bucket
		if(Gdx.input.isTouched()) {
		      Vector3 touchPos = new Vector3();
		      touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		      
		      //To transform these coordinates to our camera's coordinate system,
		      //we need to call the camera.unproject() method, 
		      //which requests a Vector3, a three dimensional vector
		      camera.unproject(touchPos);
		      bucket.x = touchPos.x - 64 / 2;
		}
		
		//moves bucket based on arrow keys
		//We want the bucket to move without acceleration, at two hundred pixels/units per second
		
		//getDeltaTime() returns the time passed between the last and the current frame in seconds
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			bucket.x += 200 * Gdx.graphics.getDeltaTime();
		}
		
		//makes sure bucket doesn't go off screen
		if (bucket.x < 0){
			bucket.x = 0;
		}
		if (bucket.x > 800-64){
			bucket.x = 800-64;
		}
		
		//check time to add next raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000){
			spawnRaindrop();
		}
		
		//makes an Iterator to go through Array, moves every raindrop at 200 px/sec
		Iterator<Rectangle> iter = raindrops.iterator();
		while(iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0){
				iter.remove();
			}
			if (raindrop.overlaps(bucket)){
				dropSound.play();
				iter.remove();
			}
			
		}
		
		
	}
	
	@Override
	public void dispose () {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}
	
	private void spawnRaindrop() {
	      Rectangle raindrop = new Rectangle();
	      raindrop.x = MathUtils.random(0, 800-64);
	      raindrop.y = 480;
	      raindrop.width = 64;
	      raindrop.height = 64;
	      raindrops.add(raindrop);
	      lastDropTime = TimeUtils.nanoTime();
	  }
}
