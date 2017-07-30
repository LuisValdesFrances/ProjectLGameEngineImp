package com.luis.projectlgameengineimp.objects;


import java.util.ArrayList;
import java.util.List;

import com.luis.lgameengine.gameutils.controls.GameControl;
import com.luis.lgameengine.gameutils.gameworld.SpriteImage;
import com.luis.lgameengine.gameutils.gameworld.WorldConver;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.projectlgameengineimp.GfxManager;
import com.luis.projectlgameengineimp.Main;
import com.luis.projectlgameengineimp.UserInput;

public class Player extends GameObject{
	
	private int animation;
	private boolean flip;
	private boolean fall;
	public static final int ANIM_IDLE = 0;
	public static final int ANIM_RUN = 1;
	public static final int ANIM_ATACK = 2;
	public static final int ANIM_JUMP = 3;
	public static final int ANIM_JUMP_SUB_1 = 0;
	public static final int ANIM_JUMP_SUB_2 = 1;
	public static final int ANIM_JUMP_SUB_3 = 2;
	
	public static final int IDLE_FRAMES = 30;
	public static final int RUN_FRAMES = 25;
	public static final int ATACK_FRAMES = 38;
	public static final int JUMP_1_FRAMES = 6;
	public static final int JUMP_2_FRAMES = 2;
	public static final int JUMP_3_FRAMES = 6;
	
	public static final int STATE_IDLE = 0;
	public static final int STATE_RUN = 1;
	public static final int STATE_ATACK = 2;
	public static final int STATE_JUMP = 3;
	private int newState;
	private int state;
	
	private List<SpriteImage> spriteImageList;
	
	public float fForceJump;
	
	public Player(int _iWidth, int _iHeight, float _fPosX, float _fPosY, float _fPosZ, float _fSpeed, float _fAngle) {
		 super(_iWidth, _iHeight, _fPosX, _fPosY, _fPosZ, _fSpeed, _fAngle);
		 
		 spriteImageList = new ArrayList<SpriteImage>();
		 
		 spriteImageList.add(new SpriteImage(
				 GfxManager.imgPlayerIdle.getWidth(),
				 GfxManager.imgPlayerIdle.getHeight(),
				 0.1f, IDLE_FRAMES));
		 
		 spriteImageList.add(new SpriteImage(
				 GfxManager.imgPlayerRun.getWidth(),
				 GfxManager.imgPlayerRun.getHeight(),
				 0.020f, RUN_FRAMES));
		 
		 spriteImageList.add(new SpriteImage(
				 GfxManager.imgPlayerAtack.getWidth(),
				 GfxManager.imgPlayerAtack.getHeight(),
				 0.020f, ATACK_FRAMES));
		 
		 spriteImageList.add(new SpriteImage(
				 new int[]{GfxManager.imgPlayerJump.getWidth() / JUMP_1_FRAMES, GfxManager.imgPlayerJump.getWidth() / JUMP_1_FRAMES, GfxManager.imgPlayerJump.getWidth() / JUMP_1_FRAMES}, 
				 new int[]{GfxManager.imgPlayerJump.getHeight()/3, GfxManager.imgPlayerJump.getHeight()/3, GfxManager.imgPlayerJump.getHeight()/3},
				 new float[]{0.060f, 0.6f, 0.060f},
				 new int[]{JUMP_1_FRAMES, JUMP_2_FRAMES, JUMP_3_FRAMES}));
	}
	
	public void update(float _fDeltaTime, int[][] _iTilesMatrixID, float _fTileW, float _fTileH, GameControl _vGameControl) {
		super.update(_fDeltaTime, _iTilesMatrixID, _fTileW, _fTileH);
		listenControls(_vGameControl);
		updateAnimations(_fDeltaTime);
		putNewState();
	}
	
	public void draw(Graphics _g, WorldConver _vWorldConver, float _fCameraX, float _fCameraY, 
		int _iModAnimX, int _iModAnimY, int _iModDrawX, int _iModDrawY, 
		int _iAnchor) {
		
		switch(state){
			case ANIM_IDLE:
				super.draw(_g, GfxManager.imgPlayerIdle, spriteImageList.get(animation), _vWorldConver, _fCameraX, _fCameraY, 
						_iModAnimX, _iModAnimY, _iModDrawX, 12,
						flip, _iAnchor);
				break;
			case ANIM_RUN:
				super.draw(_g, GfxManager.imgPlayerRun, spriteImageList.get(animation), _vWorldConver, _fCameraX, _fCameraY, 
						_iModAnimX, _iModAnimY, _iModDrawX, 12,
						flip, _iAnchor);
				break;
			case ANIM_ATACK:
				super.draw(_g, GfxManager.imgPlayerAtack, spriteImageList.get(animation), _vWorldConver, _fCameraX, _fCameraY, 
						_iModAnimX, _iModAnimY, _iModDrawX, 12,
						flip, _iAnchor);
				break;
			case ANIM_JUMP:
				super.draw(_g, GfxManager.imgPlayerJump, spriteImageList.get(animation), _vWorldConver, _fCameraX, _fCameraY, 
						_iModAnimX, _iModAnimY, _iModDrawX, 12,
						flip, _iAnchor);
				break;
		}
		
	}
	
	private void listenControls(GameControl _vGameControl){
		
		if(state == STATE_IDLE || state == STATE_RUN || state == STATE_JUMP){
			if(_vGameControl.getForce() > 0 || (UserInput.isKeyRight || UserInput.isKeyLeft)){
				if((_vGameControl.getAngle() > 315 || _vGameControl.getAngle() < 45) ||  UserInput.isKeyRight){
					if(state != STATE_JUMP || (state == STATE_JUMP && spriteImageList.get(animation).getFileIndex() == ANIM_JUMP_SUB_2)){
						setAngle(360);
						move(Main.getDeltaSec());
						flip = false;
					}
					if(state != STATE_JUMP)
						newState = STATE_RUN;
				}
				else if(_vGameControl.getAngle() > 45 && _vGameControl.getAngle() < 135){
					 
				}
				else if((_vGameControl.getAngle() > 135 && _vGameControl.getAngle() < 225) || UserInput.isKeyLeft){
					if(state != STATE_JUMP || (state == STATE_JUMP && spriteImageList.get(animation).getFileIndex() == ANIM_JUMP_SUB_2)){
						setAngle(180);
						setSpeed(getSpeed());
						move(Main.getDeltaSec());
						flip = true;
					}
					if(state != STATE_JUMP)
						newState = STATE_RUN;
				}
				else if(_vGameControl.getAngle() > 225 && _vGameControl.getAngle() < 315){
					 
				}
			}
			else{
				if(state != STATE_JUMP){
					newState = STATE_IDLE;
				}
			}
		}
		//Boton salto
		if(state == STATE_IDLE || state == STATE_RUN){
			if(_vGameControl.isButtonPressed(0) || UserInput.isKeyFire){
				if(isColisionBotton()) 
					newState = STATE_JUMP;
			}
			if(_vGameControl.isButtonPressed(1)){
				if(isColisionBotton())
					newState = STATE_ATACK;
			}
		}
	 }
	
	protected void updateAnimations(float deltaTime) {
		spriteImageList.get(animation).updateAnimation(deltaTime);
	}
	
	protected void putNewState() {
		
		//Cae
		if(!isColisionBotton() && state != STATE_JUMP){
			newState = STATE_JUMP;
			fall = true;
			
		}
		if(animation == ANIM_ATACK && spriteImageList.get(animation).isEndAnimation()){
			newState = STATE_IDLE;
		}
		else if(animation == ANIM_JUMP){
			
			switch(spriteImageList.get(animation).getFileIndex()){
			case ANIM_JUMP_SUB_1:
				if(spriteImageList.get(animation).isEndAnimation()){
					spriteImageList.get(animation).setFileIndex(ANIM_JUMP_SUB_2);
					setSpeedY(-fForceJump);
				}
				break;
			case ANIM_JUMP_SUB_2:
				if(isColisionBotton()){
					spriteImageList.get(animation).setFileIndex(ANIM_JUMP_SUB_3);
					//newState = STATE_IDLE;
				}else{
					if(getSpeedY() < 0){
						spriteImageList.get(animation).setFrame(0);
					}else{
						spriteImageList.get(animation).setFrame(1);
					}
				}
				
				break;
			case ANIM_JUMP_SUB_3:
				if(spriteImageList.get(animation).isEndAnimation()){
					newState = STATE_IDLE;
				}
				break;
			}
			
		}
		
		if(newState != state){
			
			state = newState;
			
			switch(state){
			case STATE_IDLE:
				animation = ANIM_IDLE;
				break;
			case STATE_RUN:
				animation = ANIM_RUN;
				break;
			case STATE_ATACK:
				animation = ANIM_ATACK;
				break;
			case STATE_JUMP:
				animation = ANIM_JUMP;
				if(fall){
					fall = false;
					spriteImageList.get(animation).setFileIndex(ANIM_JUMP_SUB_2);
					spriteImageList.get(animation).resetAnimation(ANIM_JUMP_SUB_2);
				}else{
					spriteImageList.get(animation).setFileIndex(ANIM_JUMP_SUB_1);
					//Reseteo las tres fases del salto
					spriteImageList.get(animation).resetAnimation(ANIM_JUMP_SUB_1);
					spriteImageList.get(animation).resetAnimation(ANIM_JUMP_SUB_2);
					spriteImageList.get(animation).resetAnimation(ANIM_JUMP_SUB_3);
				}
				break;
				
			}
			spriteImageList.get(animation).resetAnimation(0);
		}
	}
	
	protected void resolveState() {
		
		
	}
	
	

}
