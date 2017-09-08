package com.luis.projectlgameengineimp.objects;


import java.util.List;

import com.luis.lgameengine.gameutils.gameworld.ParticleManager;
import com.luis.lgameengine.gameutils.gameworld.RigidBody;
import com.luis.lgameengine.gameutils.gameworld.SpriteImage;
import com.luis.lgameengine.gameutils.gameworld.WorldConver;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.projectlgameengineimp.Define;
import com.luis.projectlgameengineimp.GfxManager;

public class BigRock extends Enemy{
	
	private static Image imgBigRock;
	private boolean isActive;
	private boolean isNoDamage;
	
	public BigRock(int type, Player player, int width, int height, float posX,
			float posY, float posZ, 
			float speed, float angle) {
		super(type, player, width, height, posX, posY, posZ, speed, angle, -1);
		
		imgBigRock = GfxManager.imgBigRock;
		rotatePX = 0;
		rotatePY = -height/2;
		isActive = false;
		setElasticity(0.15f);
	}
	
	public void update(float deltaTime, int[][] tilesMatrixID, float tileW, float tileH) {
		if(isActive){
			float lastX = getPosX();
			move(deltaTime);
			
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
					createParticles(
						(int)tileW, 
						colDown[4]-tileW/2, 
						colDown[3]-tileH/2,
						RigidBody.transformUnityValue(0.96f, tileW, Define.PARTICLE_BIG_ROCK_DESTROY_WEIGHT),
						RigidBody.transformUnityValue(0.96f, tileW, Define.PARTICLE_BIG_ROCK_DESTROY_SPEED),
						Define.PARTICLE_BIG_ROCK_DESTROY_DURATION);
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
					createParticles(
						(int)tileW, 
						colRight[4]-tileW/2, 
						colRight[3]-tileH/2, 
						RigidBody.transformUnityValue(0.96f, tileW, Define.PARTICLE_BIG_ROCK_DESTROY_WEIGHT),
						RigidBody.transformUnityValue(0.96f, tileW, Define.PARTICLE_BIG_ROCK_DESTROY_SPEED),
						Define.PARTICLE_BIG_ROCK_DESTROY_DURATION);
					gfxEffects.createTremor(0.35f, tileW/8);
				}
				
				colLeft = checkTileColisionX(
						tilesMatrixID, 
						Define.BIG_ROCK_TILE_DESTROY[i], 
						getPosX()-getWidth()/2-1,
						getPosY()-getHeight(),
						getWidth(), getHeight(),
						tileW, tileH);
				
				if(colLeft != null){
					tilesMatrixID[colLeft[1]][colLeft[2]] = 0;
					createParticles(
						(int)tileW,
						colLeft[4]-tileW/2, 
						colLeft[3]-tileH/2, 
						RigidBody.transformUnityValue(0.96f, tileW, Define.PARTICLE_BIG_ROCK_DESTROY_WEIGHT),
						RigidBody.transformUnityValue(0.96f, tileW, Define.PARTICLE_BIG_ROCK_DESTROY_SPEED),
						Define.PARTICLE_BIG_ROCK_DESTROY_DURATION);
					gfxEffects.createTremor(0.35f, tileW/8);
				}
				
			}
			
			super.update(deltaTime, tilesMatrixID, tileW, tileH);
	    	
	    	if(!isNoDamage && isColision(player)){
				player.setDamage(0);
			}
	    	
	    	if(lastX == getPosX() &&
	    		((getSpeed() > 0 && isColisionRight()) || getSpeed() < 0 && isColisionLeft())){
	    		setSpeed(0);
	    		setRotationSpeed(0);
	    		isNoDamage = true;
	    	}
	    	setRotation(rotation+(rotationSpeed*deltaTime));
		}else{
			boolean playerInCorrectPosition = speed>0?player.getPosX() > getPosX()+worlConver.getLayoutX():player.getPosX() < getPosX()-worlConver.getLayoutX();
			if(playerInCorrectPosition
				&& !worlConver.isObjectInGameLayout(gameCamera.getPosX(), gameCamera.getPosY(), 
							getPosX(), getPosY(), getWidth(), getHeight())){
				isActive = true;
			}
		}
    	
	}
	
	public void draw(
			Graphics _g, Image image, SpriteImage spriteImage, WorldConver worldConver, float cameraX, float cameraY,
			int modAnimX, int modAnimY, int modDrawX, int modDrawY,
			int anchor){
		if(isActive){
			super.draw(_g, imgBigRock, null, worldConver, cameraX, cameraY,
					modAnimX, modAnimY, modDrawX, 12, anchor);
		}
	}
	
	@Override
	public void setEnemeyDamage(List<Enemy> enemyList){
		if(isActive){
			for(int i = 0;i < enemyList.size(); i++){
				if(enemyList.get(i).getType() != getType()){
					if(isColision(enemyList.get(i)))
						enemyList.get(i).setState(Enemy.STATE_DEAD);
				}
			}
		}
	}
	

	@Override
	public boolean createObject() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void createParticles(int tileSize, float x, float y, float weight, float speed, float duration){
		particleManager.createParticles(
				4,
				Define.GRAVITY_FORCE,
				x, y, 
				weight,
				speed,
				(int)(tileSize*0.45f), (int)(tileSize*0.45f),
				ParticleManager.COL_CENTER, 
				new int[]{0xff35302A, 0Xff4E4032, 0xffCBBBAC},
				duration, false, false);
	}
	
	

}
