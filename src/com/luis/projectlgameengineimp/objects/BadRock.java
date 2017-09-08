package com.luis.projectlgameengineimp.objects;

import java.util.ArrayList;

import com.luis.lgameengine.gameutils.gameworld.GameCamera;
import com.luis.lgameengine.gameutils.gameworld.ParticleManager;
import com.luis.lgameengine.gameutils.gameworld.SpriteImage;
import com.luis.lgameengine.gameutils.gameworld.WorldConver;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.projectlgameengineimp.Define;
import com.luis.projectlgameengineimp.GfxManager;
import com.luis.projectlgameengineimp.Main;

public class BadRock extends Enemy{
	
	public static final int ANIM_IDLE = 0;
	public static final int ANIM_ATACK = 1;
	public static final int ANIM_SUFF = 2;
	
	public static final int IDLE_FRAMES = 10;
	public static final int ATACK_FRAMES = 20;
	public static final int SUFF_FRAMES = 5;
	
	private boolean launchRock;
	
	//Tembleque
	private float idleTime;
	private float idleCount;
	private float tremorCount;
	private float tremorFrameCount;

	public BadRock(int id, Player player, int width, int height, float posX, float posY, float posZ,
			float speed, float angle, int live) {
		super(id, player, width, height, posX, posY, posZ, speed, angle, live);
		
		spriteImageList = new ArrayList<SpriteImage>();
		 
		spriteImageList.add(new SpriteImage(
				GfxManager.imgBadRockIdle.getWidth(),
				GfxManager.imgBadRockIdle.getHeight(),
				Define.BADROCK_DUR_ANIM_IDLE, IDLE_FRAMES));
		
		spriteImageList.add(new SpriteImage(
				GfxManager.imgBadRockAtack.getWidth(),
				GfxManager.imgBadRockAtack.getHeight(),
				Define.BADROCK_DUR_ANIM_ATACK, ATACK_FRAMES));
		
		spriteImageList.add(new SpriteImage(
				GfxManager.imgBadRockSuff.getWidth(),
				GfxManager.imgBadRockSuff.getHeight(),
				Define.BADROCK_DUR_ANIM_SUFF, SUFF_FRAMES));
		
		idleTime = Define.BADROCK_IDLE_TIME + (float)Main.getRandom(0, 20)*0.1f;
	}
	
	public boolean createObject(){
		if(launchRock && state == STATE_ATACK){
			
			if(launchRock && spriteImageList.get(animation).getFrame()==13){
				launchRock = false;
				return true;
			}
		}
		return false;
	}
	
	public void update(float deltaTime, int[][] _iTilesMatrixID, float _fTileW, float _fTileH) {
    	super.update (deltaTime, _iTilesMatrixID, _fTileW, _fTileH);
    	
    	if(state == STATE_DEAD)
    		return;
    	
    	if(worlConver.isObjectInGameLayout(gameCamera.getPosX(), gameCamera.getPosY(), 
			getPosX(), gameCamera.getPosY(), getWidth(), getHeight())){
    	if(isDead()){
    		state = STATE_DEAD;
    		return;
    	}
    	
    	//if(state != STATE_SUFF){
    		if(checkDamageFromPlayer(player)){
    			newState = STATE_SUFF;
    			int forceMod = Main.getRandom(0, (int)(player.getForceAtack()*0.5f));
    			setSpeedX(player.isFlip() ? -player.getForceAtack()-forceMod : player.getForceAtack()-forceMod);
    			forceMod = Main.getRandom(0, (int)(player.getForceAtack()*0.5f));
    			setSpeedY(-player.getForceAtack()-forceMod);
    			//Si ya esta sufriendo, lo reinico
    			spriteImageList.get(animation).setFrame(0);
    		}else{
    			if(isColision(player)){
    				player.setDamage(0);
    			}
    		}
    	//}
    		
    	switch(state){
		case STATE_IDLE:
			if(idleCount < idleTime){
				idleCount+= deltaTime;
				if(spriteImageList.get(animation).getFrame() == 0){
					flip = player.getPosX() < getPosX();
				}
			}else{
				newState = STATE_ATACK;
				idleCount = 0;
			}
			break;
			
		case STATE_ATACK:
			if(lastState != STATE_TREMOR && spriteImageList.get(animation).getFrame() == 10){
				lastState = state;
				state = STATE_TREMOR;
				newState = state;
				tremorCount = 0;
				tremorFrameCount = 0;
			}
			if(spriteImageList.get(animation).isEndAnimation()){
				newState = STATE_IDLE;
			}
			break;
    	
		case STATE_TREMOR:
			if(tremorCount < 1){
				tremorCount += deltaTime;
				
				float tremor = 0.0015f;
				float modTremor = tremor + tremorCount*0.25f;
				if(tremorFrameCount < modTremor){
					tremorFrameCount+= deltaTime;
				}else{
					tremorFrameCount = 0;
					if(spriteImageList.get(animation).getFrame() == 10){
						spriteImageList.get(animation).setFrame(11);
					}else{
						spriteImageList.get(animation).setFrame(10);
					}
				}
				
			}else{
				tremorCount = 0;
				lastState = state;
				state = STATE_ATACK;
				newState = state;
				spriteImageList.get(animation).setTimeUpdate((spriteImageList.get(animation).getTimeUpdame()*1.75f));
				spriteImageList.get(animation).setFrame(11);
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
					spriteImageList.get(animation).setTimeUpdate(0.08f);
					launchRock = true;
					break;
				case STATE_SUFF:
					animation = ANIM_SUFF;
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
			super.draw(_g, GfxManager.imgBadRockIdle, spriteImageList.get(animation), worldConver, cameraX, cameraY,
					modAnimX, modAnimY, modDrawX, 12, anchor);
			break;
			
		case STATE_ATACK:
		case STATE_TREMOR:
			super.draw(_g, GfxManager.imgBadRockAtack, spriteImageList.get(animation), worldConver, cameraX, cameraY,
					modAnimX, modAnimY, modDrawX, 12, anchor);
			break;
			
		case STATE_SUFF:
			super.draw(_g, GfxManager.imgBadRockSuff, spriteImageList.get(animation), worldConver, cameraX, cameraY,
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
	public void createParticles(int tileSize, float x, float y, float weight, float speed, float duration) {
		particleManager.createParticles(
				3,
				Define.GRAVITY_FORCE,
				x, y,
				weight,
				speed, 
				(int)(getWidth()*0.75f), (int)(getWidth()*0.85f),
				ParticleManager.COL_CENTER, 
				new int[]{0xffE2C683, 0xff724611, 0xffD2872C, 0xffD5FCB5, 0Xff4E4032, 0xff426129},
				duration, false, false);
		
	}
}
