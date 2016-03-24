package com.luis.projectlgameengineimp.objects;

import com.luis.lgameengine.gameutils.GfxEffects;
import com.luis.lgameengine.gameutils.Math2D;
import com.luis.lgameengine.gameutils.WorldConver;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.projectlgameengineimp.Define;
import com.luis.projectlgameengineimp.ModeGame;

/**
 * 
 * @author Luis Valdes Frances
 */
public abstract class GameObject extends RigidBody{
    
    private static int ms_iID = 0;
    private int iID;
    
    
    //States:
    protected int iNewState;
    protected int iState;
    protected int iLastState;
    
    protected float fSpeed;
    protected float fAngle;
    protected float fCos;
    protected float fSin;
    
    public static final int DESP_SHADOW = ((Define.SIZEX+Define.SIZEY)/2)/16;

    public GameObject(int _fWidth, int _fHeight, float _fPosX, float _fPosY, float _fPosZ, float _fSpeed, float _fAngle) {
        super.init(_fPosX, _fPosY, _fWidth, _fHeight);
        this.fSpeed = _fSpeed;
        this.fAngle = _fAngle;
        this.iID = ms_iID;
        ms_iID++;
        
        
    }

    public void update(float _fDeltaTime, int[][] _iTilesMatrixID, float _fTileW, float _fTileH) {
    	super.runPhysics (_fDeltaTime, _iTilesMatrixID, _fTileW, _fTileH, Define.SIZEX, Define.SIZEY);
    }
    
    public void draw(
    		Graphics _g, Image _vImage, WorldConver _vWorldConver, float _fCameraX, float _fCameraY, 
    		int _iModAnimX, int _iModAnimY, int _iModDrawX, int _iModDrawY, 
    		int _iAnchor) {
    	
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
		
		/*
    	 * Como los tiles se pintan con anclaje arriba-izquierda, pero se procesan en la logica abajo centro
    	 * es necesario desplazar todos los objetos para que coincidan en la posicion real de los tiles 
    	 */
    	int adjustTileX = ModeGame.TILE_SIZE/2;
    	int adjustTileY = ModeGame.TILE_SIZE;
    	
    	int posX = _vWorldConver.getConversionDrawX(_fCameraX, getPosX()) + adjustTileX;
        int posY = _vWorldConver.getConversionDrawY(_fCameraY, getPosY()) + adjustTileY;
        
        _g.setClip(posX - extraX + _iModDrawX, posY - extraY + _iModDrawY, (int) getWidth(), (int) getHeight());
        //_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
        
        _g.drawImage(_vImage, posX - _iModAnimX + _iModDrawX, posY - _iModAnimY + _iModDrawY, _iAnchor);
     }
    
    
    public void move(float _fDeltaTime){
	   //Get speed angle x and 
       float angle = getAngle();
	   float angleToRadiants = (float)(angle * Math.PI) / 180f;
	   fCos = (float) Math.cos(angleToRadiants);
	   fSin = (float) Math.sin(angleToRadiants);
	   float speedX = (fCos * getSpeed());
	   float speedY = (fSin * getSpeed());
	   
	   movePosX(getPosX() + (speedX * _fDeltaTime));
	   movePosY(getPosY() - (speedY * _fDeltaTime));
   }
   
   public int getID() {
        return iID;
   }
   
   public static int getTotalID() {
        return ms_iID;
   }
   
   public int getState() {
        return iState;
   }
   
   public void setNewState(int _iNewState) {
        iNewState=_iNewState;
   }
   
   public void setState(int _iState) {
        iState=_iState;
   }
   
   public float getSpeed() {
		return fSpeed;
	}

	public void setSpeed(float _fSpeed) {
		this.fSpeed = _fSpeed;
	}
	
	public float getAngle(){
		return fAngle;
	}
	
	public void setAngle(float _fAngle){
		this.fAngle = _fAngle;
	}

	public float getCos() {
		return fCos;
	}

	public float getSin() {
		return fSin;
	}
    
    
}
