package com.luis.projectlgameengineimp.objects;


import com.luis.lgameengine.gameutils.gameworld.ParticleManager;
import com.luis.lgameengine.gameutils.gameworld.RigidBody;
import com.luis.lgameengine.gameutils.gameworld.SpriteImage;
import com.luis.lgameengine.gameutils.gameworld.WorldConver;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.projectlgameengineimp.Define;
import com.luis.projectlgameengineimp.GfxManager;

public class BigRock extends Enemy{
	
	private static final float HIGHT_FALL = 1200f;
	private static Image imgBigRock;
	private float fallForce;
	
	public BigRock(int type, Player player, int width, int height, float posX,
			float posY, float posZ, 
			float speed, float angle) {
		super(type, player, width, height, posX, posY, posZ, speed, angle, -1);
		
		imgBigRock = GfxManager.imgBigRock;
		rotatePX = 0;
		rotatePY = -height/2;
		setElasticity(0.25f);
	}
	
	public void update(float deltaTime, int[][] tilesMatrixID, float tileW, float tileH) {
		//Guardo la fuerza de caida
		fallForce = getSpeedY();
		
		///*
		move(deltaTime);
		if(getPosX()+width/2 > worlConver.getWorldWidth()-tileW){
			setPosX(worlConver.getWorldWidth()-tileW);
			setSpeed(getSpeed()*-1);
			setRotationSpeed(getRotationSpeed()*-1);
		}else if(getPosX()-width/2 < tileW){
			setPosX(tileW);
			setSpeed(getSpeed()*-1);
			setRotationSpeed(getRotationSpeed()*-1);
		}
		
		int[] colDown = null;
		int[] colUp = null;
		int[] colRight = null;
		int[] colLeft = null;
		for(int i = 0; i < Define.BIG_ROCK_TILE_DESTROY.length; i++){
			
			colDown = checkTileColisionY(
					tilesMatrixID, 
					Define.BIG_ROCK_TILE_DESTROY[i], 
					getPosX()-getWidth()/2,
					getPosY(),
					getWidth(), getHeight(),
					tileW, tileH);
			if(colDown != null){
				tilesMatrixID[colDown[1]][colDown[2]] = 0;
				createParticles(colDown[4]-tileW/2, colDown[3]-tileH/2, (int)tileW);
				gfxEffects.createTremor(0.55f, tileW/6);
			}
			
			
			colRight = checkTileColisionX(
					tilesMatrixID, 
					Define.BIG_ROCK_TILE_DESTROY[i], 
					getPosX()+getWidth()/2,
					getPosY()-getHeight(),
					getWidth(), getHeight(),
					tileW, tileH);
			
			if(colRight != null){
				tilesMatrixID[colRight[1]][colRight[2]] = 0;
				createParticles(colRight[4]-tileW/2, colRight[3]-tileH/2, (int)tileW);
				gfxEffects.createTremor(0.35f, tileW/8);
			}
			
			colLeft = checkTileColisionX(
					tilesMatrixID, 
					Define.BIG_ROCK_TILE_DESTROY[i], 
					getPosX()-getWidth()/2,
					getPosY()-getHeight(),
					getWidth(), getHeight(),
					tileW, tileH);
			
			if(colLeft != null){
				tilesMatrixID[colLeft[1]][colLeft[2]] = 0;
				createParticles(colLeft[4]-tileW/2, colLeft[3]-tileH/2, (int)tileW);
				gfxEffects.createTremor(0.35f, tileW/8);
			}
			
		}
		//*/
		super.update(deltaTime, tilesMatrixID, tileW, tileH);
    	
    	setRotation(rotation+(rotationSpeed*deltaTime));
    	if(isColision(player)){
			player.setDamage(0);
		}
	}
	
	public void draw(
			Graphics _g, Image image, SpriteImage spriteImage, WorldConver worldConver, float cameraX, float cameraY,
    		int modAnimX, int modAnimY, int modDrawX, int modDrawY,
    		int anchor){
		super.draw(_g, imgBigRock, null, worldConver, cameraX, cameraY,
				modAnimX, modAnimY, modDrawX, 12, anchor);
	}

	@Override
	public boolean createObject() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void createParticles(float x, float y, int tileSize){
		particleManager.createParticles(
				4,
				Define.GRAVITY_FORCE,
				RigidBody.transformUnityValue(0.96f, tileSize, Define.PARTICLE_BIG_ROCK_DESTROY_WEIGHT),
				x, y, 
				RigidBody.transformUnityValue(0.96f, tileSize, Define.PARTICLE_BIG_ROCK_DESTROY_SPEED),
				(int)(tileSize*0.45f), (int)(tileSize*0.45f),
				ParticleManager.COL_CENTER, 
				new int[]{0xff35302A, 0Xff4E4032, 0xffCBBBAC},
				Define.PARTICLE_BIG_ROCK_DESTROY_DURATION, false, false);
	}
	
	

}
