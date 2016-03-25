package com.luis.projectlgameengineimp.objects;


import com.luis.lgameengine.gameutils.controls.GameControl;
import com.luis.lgameengine.gameutils.gameworld.WorldConver;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.projectlgameengineimp.GfxManager;
import com.luis.projectlgameengineimp.Main;
import com.luis.projectlgameengineimp.UserInput;

public class Player extends GameObject{
	
	public float fForceJump;
	
	public Player(int _iWidth, int _iHeight, float _fPosX, float _fPosY, float _fPosZ, float _fSpeed, float _fAngle) {
		 super(_iWidth, _iHeight, _fPosX, _fPosY, _fPosZ, _fSpeed, _fAngle);
	}
	
	public void update(float _fDeltaTime, int[][] _iTilesMatrixID, float _fTileW, float _fTileH, GameControl _vGameControl) {
		super.update(_fDeltaTime, _iTilesMatrixID, _fTileW, _fTileH);
		listenControls(_vGameControl);
		putNewState();
	    resolveState();
	    //move(_lDeltaTime);
	    updateAnimations();
	}
	
	public void draw(Graphics _g, WorldConver _vWorldConver, float _fCameraX, float _fCameraY, 
			int _iModAnimX, int _iModAnimY, int _iModDrawX, int _iModDrawY, 
			int _iAnchor) {
		
		/*
		int extraX = 0;
    	int extraY = 0;
    	if ((_iAnchor & Graphics.BOTTOM) != 0) {
    		extraY = (int) getHeight();
        }
    	if ((_iAnchor & Graphics.RIGHT) != 0) {
    		extraX = (int) getWidth();
        }
    	if ((_iAnchor & Graphics.VCENTER) != 0) {
    		extraY = (int) getHeight() / 2;
        }
    	if ((_iAnchor & Graphics.HCENTER) != 0) {
    		extraX = (int) getWidth() / 2;
        }
    	
		 _g.setColor(0xfffd0101);
		 _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
		 _g.fillRect(
				 (int) _vWorldConver.getConversionDrawX(_fCameraX -extraX, posX) + (int)_fModDrawX, 
				 (int) _vWorldConver.getConversionDrawY(_fCameraY -extraY, posY) + (int)_fModDrawY,
				 (int)width, (int)height);
				 */
//		Graphics, Image, WorldConver, _fCameraX, _fCameraY, _iModAnimX, _iModAnimY, iAnchor
		super.draw(_g, GfxManager.vImgPlayer, _vWorldConver, _fCameraX, _fCameraY, 
				_iModAnimX, _iModAnimY, _iModDrawX, _iModDrawY,
				_iAnchor);
	}
	
	private void listenControls(GameControl _vGameControl){
		 if(_vGameControl.getForce() > 0 || (UserInput.isKeyRight || UserInput.isKeyLeft)){
			 if((_vGameControl.getAngle() > 315 || _vGameControl.getAngle() < 45) ||  UserInput.isKeyRight){
				 setAngle(360);
				 move(Main.getDeltaSec());
			 }
			 else if(_vGameControl.getAngle() > 45 && _vGameControl.getAngle() < 135){
				 
			 }
			 else if((_vGameControl.getAngle() > 135 && _vGameControl.getAngle() < 225) || UserInput.isKeyLeft){
				 setAngle(180);
				 setSpeed(getSpeed());
				 move(Main.getDeltaSec());
			 }
			 else if(_vGameControl.getAngle() > 225 && _vGameControl.getAngle() < 315){
				 
			 }
		 }
		 //Botones
		 if(_vGameControl.isButtonPressed(0) || UserInput.isKeyFire){
			 if(isColisionBotton()) 
				 setSpeedY(-fForceJump);
		 }
		 if(_vGameControl.isButtonPressed(1)){
			 //setSpeedX(-fForceJump);
				 
		 }
	 }
	
	protected void putNewState() {
		
		
	}
	
	protected void resolveState() {
		
		
	}protected void updateAnimations() {
		
		
	}

}
