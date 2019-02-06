package com.abhishekvenunathan.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture gameover;
	//ShapeRenderer shapeRenderer;

	Texture[] birds;
	Circle birdCircle;

	Rectangle[] toptubeRectangles;
	Rectangle[] bottomtubeRectangles;


	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;
	float gravity = 1.5f;

	Texture bottomTube;
	Texture topTube;

	float gap = 400;
	float maxtubeoffset = 0;
	Random randomGenerator;
	int numberofTubes = 4;

	float[] tubeoffset = new float[numberofTubes];
	float tubeVelocity = 4;
	float[] tubeX = new float[numberofTubes];

	float distancebetweenTubes;

	int score=0;
	int scoringTube=0;

	BitmapFont font;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		bottomTube = new Texture("bottomtube.png");
		topTube = new Texture("toptube.png");
		gameover = new Texture("gameover.png");

		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();

		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		toptubeRectangles = new Rectangle[numberofTubes];
		bottomtubeRectangles = new Rectangle[numberofTubes];

		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
		maxtubeoffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;

		randomGenerator = new Random();
		distancebetweenTubes = Gdx.graphics.getWidth() * 3/4;

		for(int i=0;i<numberofTubes;i++){
			tubeoffset[i] =(randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()- gap - 200);
			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + + Gdx.graphics.getWidth() + i*distancebetweenTubes;

			toptubeRectangles[i] = new Rectangle();
			bottomtubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState==1){

			if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2){
				score++;
				if(scoringTube<numberofTubes-1){
					scoringTube++;
				}else{
					scoringTube=0;
				}
			}

			if(Gdx.input.isTouched()){
				velocity=-20;
			}

			for(int i =0;i<numberofTubes;i++) {

				if(tubeX[i]< -topTube.getWidth()){
					tubeX[i]+=numberofTubes*distancebetweenTubes;
					tubeoffset[i] =(randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()- gap - 200);
				} else {
					tubeX[i] -= tubeVelocity;
				}
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeoffset[i]);

				toptubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],topTube.getWidth(),topTube.getHeight());
				bottomtubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight(),bottomTube.getWidth(),bottomTube.getHeight());

			}

			if(birdY>0){
				velocity += gravity;
				birdY -= velocity;
			}else {
				gameState=2;
			}

		} else if (gameState==0) {
			if(Gdx.input.isTouched()){
				gameState = 1;
			}
		}else if(gameState == 2){
			batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
			if(Gdx.input.isTouched()){
				gameState = 1;
			}

		}

		if(flapState == 0){
			flapState=1;
		}else{
			flapState=0;
		}

		font.draw(batch,String.valueOf(score),100,200);

		batch.draw(birds[flapState],Gdx.graphics.getWidth()/2 - birds[0].getWidth()/2,birdY);
		batch.end();

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.FIREBRICK);
		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for(int i=0;i<numberofTubes;i++){
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],topTube.getWidth(),topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight(),bottomTube.getWidth(),bottomTube.getHeight());

			if(Intersector.overlaps(birdCircle,toptubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomtubeRectangles[i])){
				gameState=2;
			}
		}

		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
