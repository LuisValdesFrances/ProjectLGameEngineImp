package com.luis.projectlgameengineimp.objects;


import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.luis.lgameengine.gameutils.controls.GameControl;
import com.luis.lgameengine.gameutils.gameworld.RigidBody;
import com.luis.lgameengine.gameutils.gameworld.SpriteImage;
import com.luis.lgameengine.gameutils.gameworld.WorldConver;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.input.KeyData;
import com.luis.projectlgameengineimp.Define;
import com.luis.projectlgameengineimp.GfxManager;
import com.luis.projectlgameengineimp.Main;
import com.luis.projectlgameengineimp.ModeGame;
import com.luis.projectlgameengineimp.UserInput;

public class Player extends GameObject{
	
	private int animation;
	private boolean fall;
	private static final float HIGHT_FALL = 650; 
	private float fallForce;
	private boolean saveAtack;
	private boolean saveJump;
	
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
	
	private List<SpriteImage> spriteImageList;
	
	private float forceJump;
	private float forceJumpShort;
	private float forceAtack;
	
	private boolean jumpCancel;
	
	private boolean isSuff;
	private float blinkCount;
	private boolean blink;
	
	public Player(int width, int height, float posX, float posY, float posZ, float speed, float angle) {
		 super(width, height, posX, posY, posZ, speed, angle);
		 
		 spriteImageList = new ArrayList<SpriteImage>();
		 
		 spriteImageList.add(new SpriteImage(
				 GfxManager.imgPlayerIdle.getWidth(),
				 GfxManager.imgPlayerIdle.getHeight(),
				 Define.PLAYER_DUR_ANIM_IDLE, IDLE_FRAMES));
		 
		 spriteImageList.add(new SpriteImage(
				 GfxManager.imgPlayerRun.getWidth(),
				 GfxManager.imgPlayerRun.getHeight(),
				 Define.PLAYER_DUR_ANIM_RUN, RUN_FRAMES));
		 
		 spriteImageList.add(new SpriteImage(
				 GfxManager.imgPlayerAtack.getWidth(),
				 GfxManager.imgPlayerAtack.getHeight(),
				 Define.PLAYER_DUR_ANIM_ATACK, ATACK_FRAMES));
		 
		 spriteImageList.add(new SpriteImage(
				 new int[]{GfxManager.imgPlayerJump.getWidth() / JUMP_1_FRAMES, GfxManager.imgPlayerJump.getWidth() / JUMP_1_FRAMES, GfxManager.imgPlayerJump.getWidth() / JUMP_1_FRAMES}, 
				 new int[]{GfxManager.imgPlayerJump.getHeight()/3, GfxManager.imgPlayerJump.getHeight()/3, GfxManager.imgPlayerJump.getHeight()/3},
				 new float[]{Define.PLAYER_DUR_ANIM_JUMP_1, Define.PLAYER_DUR_ANIM_JUMP_2, Define.PLAYER_DUR_ANIM_JUMP_3},
				 new int[]{JUMP_1_FRAMES, JUMP_2_FRAMES, JUMP_3_FRAMES}));
	}
	
	public void update(float deltaTime, int[][] tilesMatrixID, float tileW, float tileH, GameControl gameControl) {
		//Guardo la fuerza de caida
		fallForce = getSpeedY();
		super.update(deltaTime, tilesMatrixID, tileW, tileH);
		listenControls(gameControl);
		updateAnimations(deltaTime);
		putNewState();
		
		if(isSuff){
			blinkCount +=deltaTime;
			if(blinkCount >= Define.PLAYER_SUFF_DURATION){
				isSuff = false;
				blink = false;
			}else{
				if(Main.isDispareCount(deltaTime, blinkCount, Define.SPEED_BLICK)){
					blink = !blink;
				}
			}
		}
	}
	
	public void draw(Graphics _g, WorldConver _vWorldConver, float _fCameraX, float _fCameraY, 
		int _iModAnimX, int _iModAnimY, int _iModDrawX, int _iModDrawY, 
		int _iAnchor) {
		if(!blink){
		switch(state){
			case STATE_IDLE:
				super.draw(_g, GfxManager.imgPlayerIdle, spriteImageList.get(animation), _vWorldConver, _fCameraX, _fCameraY, 
						_iModAnimX, _iModAnimY, _iModDrawX, 12, _iAnchor);
				break;
			case STATE_RUN:
				super.draw(_g, GfxManager.imgPlayerRun, spriteImageList.get(animation), _vWorldConver, _fCameraX, _fCameraY, 
						_iModAnimX, _iModAnimY, _iModDrawX, 12, _iAnchor);
				break;
			case STATE_ATACK:
				super.draw(_g, GfxManager.imgPlayerAtack, spriteImageList.get(animation), _vWorldConver, _fCameraX, _fCameraY, 
						_iModAnimX, _iModAnimY, _iModDrawX, 12, _iAnchor);
				break;
			case STATE_JUMP:
				super.draw(_g, GfxManager.imgPlayerJump, spriteImageList.get(animation), _vWorldConver, _fCameraX, _fCameraY, 
						_iModAnimX, _iModAnimY, _iModDrawX, 12, _iAnchor);
				break;
			}
		}
	}
	
	public void setDamage(int damage){
		if(!isSuff){
			isSuff = true;
			blink = true;
			blinkCount = 0;
		}
	}
	
	private void listenControls(GameControl _vGameControl){
		
		int keyUp = (UserInput.getInstance().getKeyboardHandler().getPressedKeys(UserInput.KEYCODE_UP).getAction());
		int keyDown = (UserInput.getInstance().getKeyboardHandler().getPressedKeys(UserInput.KEYCODE_DOWN).getAction());
		boolean keyLeft = (
				UserInput.getInstance().getKeyboardHandler().getPressedKeys(UserInput.KEYCODE_LEFT).getAction() == KeyData.KEY_DOWN
				||
				UserInput.getInstance().getKeyboardHandler().getPressedKeys(UserInput.KEYCODE_LEFT).getAction() == KeyData.KEY_PRESS);
		boolean keyRight = (
				UserInput.getInstance().getKeyboardHandler().getPressedKeys(UserInput.KEYCODE_RIGHT).getAction() == KeyData.KEY_DOWN
				||
				UserInput.getInstance().getKeyboardHandler().getPressedKeys(UserInput.KEYCODE_RIGHT).getAction() == KeyData.KEY_PRESS);
		
		boolean keyAtack =(
				UserInput.getInstance().getKeyboardHandler().getPressedKeys(UserInput.KEYCODE_SHIELD_B).getAction() == KeyData.KEY_DOWN);
		boolean keyJump =(
				UserInput.getInstance().getKeyboardHandler().getPressedKeys(UserInput.KEYCODE_SHIELD_A).getAction() == KeyData.KEY_DOWN);
		boolean keyJumpCancel =(
				UserInput.getInstance().getKeyboardHandler().getPressedKeys(UserInput.KEYCODE_SHIELD_A).getAction() == KeyData.KEY_UP);
		
		if(state == STATE_IDLE || state == STATE_RUN || state == STATE_JUMP){
			if(_vGameControl.getForce() > 0 || 
					(keyLeft || keyRight)){
				if((_vGameControl.getAngle() != -1 && (_vGameControl.getAngle() > 315 || _vGameControl.getAngle() < 45)) 
						||  keyRight){
					if(state != STATE_JUMP || (state == STATE_JUMP && spriteImageList.get(animation).getFileIndex() == ANIM_JUMP_SUB_2)){
						setAngle(360);
						move(Main.getDeltaSec());
						flip = false;
					}
					if(state != STATE_JUMP)
						newState = STATE_RUN;
				}
				else if((_vGameControl.getAngle() != -1 && (_vGameControl.getAngle() > 45 && _vGameControl.getAngle() < 135))){
					 
				}
				else if((_vGameControl.getAngle() != -1 && (_vGameControl.getAngle() > 135 && _vGameControl.getAngle() < 225)) 
						|| keyLeft){
					if(state != STATE_JUMP || (state == STATE_JUMP && spriteImageList.get(animation).getFileIndex() == ANIM_JUMP_SUB_2)){
						setAngle(180);
						setSpeed(getSpeed());
						move(Main.getDeltaSec());
						flip = true;
					}
					if(state != STATE_JUMP)
						newState = STATE_RUN;
				}
				else if((_vGameControl.getAngle() != -1 && (_vGameControl.getAngle() > 225 && _vGameControl.getAngle() < 315))){
					 
				}
			}
			else{
				if(state != STATE_JUMP){
					newState = STATE_IDLE;
				}
			}
		}
		if(isColisionBotton() && (state == STATE_IDLE || state == STATE_RUN  || state == STATE_ATACK)){
			//Input ataque
			if((_vGameControl.getButtonCounter(1) == 1 &&_vGameControl.isButtonPressed(1)) || keyAtack){
				if(state != STATE_ATACK){
					newState = STATE_ATACK;
				}else if(state == STATE_ATACK){
					
					if(spriteImageList.get(animation).getFrame() >= 22 && 
							spriteImageList.get(animation).getFrame() <= 33){
						spriteImageList.get(animation).setFrame(8);
					}
					//Si pulsa antes de acabar la animacion, le doy un tiempo de cortesia (Gracias Javi Cepa)
					else if(spriteImageList.get(animation).getFrame() > 33){
						saveAtack = true;
					}
				}
			}
			//Input salto
			else if((_vGameControl.getButtonCounter(0) == 1 &&_vGameControl.isButtonPressed(0)) || keyJump){
				if(state != STATE_ATACK){
					newState = STATE_JUMP;
				}
			}
		}
		
		/*Perfecccion del salto*/
		
		//Si esta terminando el salto y se pulsa vover a saltar, guardo el salto para efectuar otra cuando acabe la animación
		else if(isColisionBotton() && (state == STATE_JUMP) && spriteImageList.get(animation).getFileIndex()==ANIM_JUMP_SUB_3){
			if((_vGameControl.getButtonCounter(0) == 1 &&_vGameControl.isButtonPressed(0)) || keyJump){
				saveJump = true;
			}
		}
		
		//Jump canceled
		if(!isColisionBotton() && state == STATE_JUMP && !_vGameControl.isButtonPressed(0) && keyJumpCancel){
			jumpCancel = true;
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
		if(state == STATE_ATACK && spriteImageList.get(animation).isEndAnimation()){
			if(saveAtack){
				saveAtack = false;
				spriteImageList.get(animation).setFrame(0);
			}else{
				newState = STATE_IDLE;
			}
		}
		else if(state == STATE_JUMP){
			
			switch(spriteImageList.get(animation).getFileIndex()){
			case ANIM_JUMP_SUB_1:
				if(spriteImageList.get(animation).isEndAnimation()){
					spriteImageList.get(animation).setFileIndex(ANIM_JUMP_SUB_2);
					setSpeedY(-forceJump);
					jumpCancel = false;
				}
				break;
			case ANIM_JUMP_SUB_2:
				if(isColisionBotton()){
					//Dependendido de la fuerza de caida, empieza en un frame u otro
					spriteImageList.get(animation).setFileIndex(ANIM_JUMP_SUB_3);
					if(fallForce < HIGHT_FALL){
						spriteImageList.get(animation).setFrame(3);
					}
				}else{
					//Subiendo
					if(getSpeedY() < 0){
						spriteImageList.get(animation).setFrame(0);
						if(jumpCancel){
							if(Math.abs(getSpeedY()) > forceJumpShort){
								setSpeedY(-forceJumpShort);
								jumpCancel = false;
							}
						}
					}
					//Bajando
					else{
						spriteImageList.get(animation).setFrame(1);
					}
				}
				break;
			case ANIM_JUMP_SUB_3:
				if(spriteImageList.get(animation).isEndAnimation()){
					if(saveJump){
						saveJump = false;
						spriteImageList.get(animation).setFileIndex(ANIM_JUMP_SUB_1);
						//Reseteo las tres fases del salto
						spriteImageList.get(animation).resetAnimation(ANIM_JUMP_SUB_1);
						spriteImageList.get(animation).resetAnimation(ANIM_JUMP_SUB_2);
						spriteImageList.get(animation).resetAnimation(ANIM_JUMP_SUB_3);
					}else{
						newState = STATE_IDLE;
					}
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

	public int getAnimation() {
		return animation;
	}

	public void setAnimation(int animation) {
		this.animation = animation;
	}

	public List<SpriteImage> getSpriteImageList() {
		return spriteImageList;
	}

	public void setSpriteImageList(List<SpriteImage> spriteImageList) {
		this.spriteImageList = spriteImageList;
	}

	public float getForceJump() {
		return forceJump;
	}

	public void setForceJump(float forceJump) {
		this.forceJump = forceJump;
	}
	
	public float getForceJumpShort() {
		return forceJumpShort;
	}

	public void setForceJumpShort(float forceJumpShort) {
		this.forceJumpShort = forceJumpShort;
	}

	public float getForceAtack() {
		return forceAtack;
	}

	public void setForceAtack(float forceAtack) {
		this.forceAtack = forceAtack;
	}
	
	
	

}
