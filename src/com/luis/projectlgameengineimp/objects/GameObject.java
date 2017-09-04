package com.luis.projectlgameengineimp.objects;

import com.luis.lgameengine.gameutils.gameworld.RigidBody;
import com.luis.lgameengine.gameutils.gameworld.SpriteImage;
import com.luis.lgameengine.gameutils.gameworld.WorldConver;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.projectlgameengineimp.Define;
import com.luis.projectlgameengineimp.GfxManager;
import com.luis.projectlgameengineimp.ModeGame;

/**
 * 
 * @author Luis Valdes Frances
 */
public abstract class GameObject extends RigidBody{
    
    private static int msID = 0;
    private int id;
    
    
    //States:
    protected int newState;
    protected int state;
    protected int lastState;
    
    protected float speed;
    protected float angle;
    protected float cos;
    protected float sin;
    
    protected float rotation;
    protected float rotationSpeed;
    protected int rotatePX;
    protected int rotatePY;
    
    protected boolean flip;
    
    public static final int DESP_SHADOW = ((Define.SIZEX+Define.SIZEY)/2)/16;

    public GameObject(int width, int height, float posX, float posY, float posZ, float speed, float angle) {
        super.init(posX, posY, width, height);
        this.speed = speed;
        this.angle = angle;
        this.id = msID;
        msID++;
        
        
    }

    public void update(float deltaTime, int[][] tilesMatrixID, float tileW, float tileH) {
    	super.runPhysics (deltaTime, tilesMatrixID, tileW, tileH, Define.SIZEX, Define.SIZEY);
    }
    
    public void draw(
    		Graphics _g, Image image, SpriteImage spriteImage, WorldConver worldConver, float cameraX, float cameraY, 
    		int modAnimX, int modAnimY, int modDrawX, int modDrawY, int anchor) {
    	
    	int extraX = 0;
    	int extraY = 0;
		if ((anchor & Graphics.BOTTOM) != 0) {
			extraY = (int) getHeight();
		}
		if ((anchor & Graphics.RIGHT) != 0) {
			extraX = (int) getWidth();
		}
		if ((anchor & Graphics.VCENTER) != 0) {
			extraY = (int) getHeight() / 2;
		}
		if ((anchor & Graphics.HCENTER) != 0) {
			extraX = (int) getWidth() / 2;
		}
		
		int posX = worldConver.getConversionDrawX(cameraX, getPosX())+ modDrawX;
        int posY = worldConver.getConversionDrawY(cameraY, getPosY())+ modDrawY;
        
        if(spriteImage != null)
        	spriteImage.drawFrame(_g, image, posX, posY, flip, anchor);
        else{
        	if(rotation == 0){
	        	_g.setClip(posX - extraX, posY - extraY, (int) getWidth(), (int) getHeight());
	            _g.drawImage(image, posX - modAnimX, posY - modAnimY, anchor);
        	}else{
        		_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
                _g.drawRegion(
    					image, 
    					posX - extraX, posY - extraY, 
    					0, 0, 
    					image.getWidth(), image.getHeight(), 
    					rotation, 
    					posX + rotatePX,
    					posY + rotatePY);
        	}
        }
     }
    
    
    public void move(float deltaTime){
	   //Get speed angle x and 
       float angle = getAngle();
	   float angleToRadiants = (float)(angle * Math.PI) / 180f;
	   cos = (float) Math.cos(angleToRadiants);
	   sin = (float) Math.sin(angleToRadiants);
	   float speedX = (cos * getSpeed());
	   float speedY = (sin * getSpeed());
	   
	   movePosX(getPosX() + (speedX * deltaTime));
	   movePosY(getPosY() - (speedY * deltaTime));
   }
   
   public int getId() {
        return id;
   }
   
   public static int getTotalId() {
        return msID;
   }
   
   public int getState() {
        return state;
   }
   
   public void setNewState(int newState) {
        this.newState = newState;
   }
   
   public void setState(int state) {
	   this.state = state;
   }
   
   public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getAngle(){
		return angle;
	}
	
	public void setAngle(float angle){
		this.angle = angle;
	}

	public float getCos() {
		return cos;
	}

	public float getSin() {
		return sin;
	}

	public boolean isFlip() {
		return flip;
	}

	public void setFlip(boolean flip) {
		this.flip = flip;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}
    
	
    
}
