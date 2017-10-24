package com.luis.projectlgameengineimp.objects;

import java.util.ArrayList;

import com.luis.lgameengine.gameutils.gameworld.SpriteImage;
import com.luis.lgameengine.gameutils.gameworld.WorldConver;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.projectlgameengineimp.Define;
import com.luis.projectlgameengineimp.GfxManager;
import com.luis.projectlgameengineimp.Main;

public class BadStick extends Enemy{
	
	public static final int ANIM_IDLE = 0;
	public static final int ANIM_ATACK = 1;
	public static final int ANIM_RUN = 2;
	
	public static final int IDLE_FRAMES = 9;
	public static final int ATACK_FRAMES = 15;
	public static final int RUN_FRAMES = 17;
	
	private float idleTime;
	private float runTime;
	private float animCount;

	public BadStick(int type, Player player, int width, int height, float posX,
			float posY, float posZ, float speed, float angle, int live) {
		super(type, player, width, height, posX, posY, posZ, speed, angle, live);
		
		spriteImageList = new ArrayList<SpriteImage>();
		
		spriteImageList.add(new SpriteImage(
				GfxManager.imgBadStickIdle.getWidth(),
				GfxManager.imgBadStickIdle.getHeight(),
				Define.BADSTICK_DUR_ANIM_IDLE, IDLE_FRAMES));
		
		spriteImageList.add(new SpriteImage(
				GfxManager.imgBadStickAtack.getWidth(),
				GfxManager.imgBadStickAtack.getHeight(),
				Define.BADSTICK_DUR_ANIM_ATACK, ATACK_FRAMES));
		
		spriteImageList.add(new SpriteImage(
				GfxManager.imgBadStickRun.getWidth(),
				GfxManager.imgBadStickRun.getHeight(),
				Define.BADSTICK_DUR_ANIM_RUN, RUN_FRAMES));
		
		idleTime = Define.BADROCK_IDLE_TIME + (float)Main.getRandom(0, 20)*0.1f;
	}
	
	public void update(float deltaTime, int[][] tilesMatrixID, float tileW, float tileH) {
    	super.update (deltaTime, tilesMatrixID, tileW, tileH);
    	
    	if(state == STATE_DEAD)
    		return;
    	
    	if(worlConver.isObjectInGameLayout(gameCamera.getPosX(), gameCamera.getPosY(), 
    			getPosX(), gameCamera.getPosY(), getWidth(), getHeight())){
	        if(isDead()){
	        	state = STATE_DEAD;
	        	return;
	        }
    	
    	
	    	switch(state){
			case STATE_IDLE:
				if(isColisionBotton()){
					if(animCount < idleTime){
						animCount+= deltaTime;
					}else{
						newState = STATE_RUN;
						animCount = 0;
						setFlip(!isFlip());
						setAngle(isFlip()?180:360);
					}
				}
				break;
				
			case STATE_ATACK:
				if(isColisionBotton()){
					if(spriteImageList.get(animation).isEndAnimation()){
						newState = STATE_IDLE;
					}
				}
				break;
			case STATE_RUN:
				if(isColisionBotton()){
					if(animCount < runTime){
						animCount+= deltaTime;
						move(Main.getDeltaSec());
						
						//Chequeo paredes
						if((isFlip() && isColisionLeft()) || (!isFlip() && isColisionRight())){
							newState = STATE_IDLE;
							animCount = 0;
						}
						
						//Chequeo suelo. Desplazo su anchura para el chequeo
						float pX = isFlip()?getPosX()-getWidth()*1.5f:getPosX()+getWidth()*1.5f;
						if(!checkColision(tilesMatrixID, pX, getPosY(), getWidth(), getHeight(), tileW, tileH)){
							newState = STATE_IDLE;
							animCount = 0;
						}
					}else{
						newState = STATE_IDLE;
						animCount = 0;
					}
				}
				break;
			
			case STATE_SUFF:
				if(spriteImageList.get(animation).isEndAnimation()){
					newState = STATE_IDLE;
				}
				break;
	    	}
	    	
	    	
	    	if(newState != state){
	    		lastState = state;
	    		state = newState;
				switch(state){
					case STATE_IDLE:
						animation = ANIM_IDLE;
						idleTime = Define.BADROCK_IDLE_TIME + (float)Main.getRandom(0, 30)*0.1f;
						break;
					case STATE_ATACK:
						animation = ANIM_ATACK;
						break;
					case STATE_RUN:
						animation = ANIM_RUN;
						runTime = Define.BADROCK_RUN_TIME + (float)Main.getRandom(0, 30)*0.1f;
						break;
				}
				spriteImageList.get(animation).resetAnimation(0);
			}
	    	updateAnimations(deltaTime);
    	}
    }
	
	public void draw(
			Graphics _g, Image image, SpriteImage spriteImage, WorldConver worldConver, float cameraX, float cameraY,
    		int modAnimX, int modAnimY, int modDrawX, int modDrawY,
    		int anchor){
		switch(state){
		case STATE_IDLE:
			super.draw(_g, GfxManager.imgBadStickIdle, spriteImageList.get(animation), worldConver, cameraX, cameraY,
					modAnimX, modAnimY, modDrawX, 12, anchor);
			break;
			
		case STATE_ATACK:
			super.draw(_g, GfxManager.imgBadStickAtack, spriteImageList.get(animation), worldConver, cameraX, cameraY,
					modAnimX, modAnimY, modDrawX, 12, anchor);
			break;
			
		case STATE_RUN:
			super.draw(_g, GfxManager.imgBadStickRun, spriteImageList.get(animation), worldConver, cameraX, cameraY,
					modAnimX, modAnimY, modDrawX, 12, anchor);
			break;
			
		case STATE_SUFF:
			super.draw(_g, GfxManager.imgBadStickSuff, spriteImageList.get(animation), worldConver, cameraX, cameraY,
					modAnimX, modAnimY, modDrawX, 12, anchor);
			break;
		}
	}

	protected void updateAnimations(float deltaTime) {
		if(state != STATE_TREMOR){
			spriteImageList.get(animation).updateAnimation(deltaTime);
		}
	}

	@Override
	public boolean createObject() {
		// TODO Auto-generated method stub
		return false;
	}

}
