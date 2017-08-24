package com.luis.projectlgameengineimp.objects;

import java.util.List;

import com.luis.lgameengine.gameutils.gameworld.SpriteImage;
import com.luis.projectlgameengineimp.Define;

public abstract class Enemy extends GameObject{
	
	protected int type;
	protected int animation;
	protected int live;
	
	protected List<SpriteImage> spriteImageList;
	
	protected Player player;

	public Enemy(int type, Player player, int width, int height, float posX, float posY, float posZ,
			float speed, float angle, int live) {
		super(width, height, posX, posY, posZ, speed, angle);
		this.type = type;
		this.player = player;
		this.live = live;
	}
	
	public void update(float deltaTime, int[][] tilesMatrixID, float tileW, float tileH) {
    	super.update (deltaTime, tilesMatrixID, tileW, tileH);
    }
    
    public abstract boolean createObject();
    
    public boolean isDead(){
    	return live < 1; 
    }
	
	protected boolean checkDamageFromPlayer(Player player){
		
		boolean damageFrame = player.getState() == Player.STATE_ATACK && player.getSpriteImageList().get(player.getAnimation()).getFrame() == 17;
    	float offsetX = player.getWidth()*0.65f;
    	boolean colisionY = isColisionY(player);
    	boolean colisionX = false;
    	float atackW = player.getWidth()*2f;
    	if(!player.isFlip()){
    		colisionX = player.getPosX() + offsetX + atackW > getPosX() - getWidth()/2 && player.getPosX() + offsetX < getPosX() - getWidth()/2;
    	}else{
    		colisionX = player.getPosX() - offsetX - atackW < getPosX() + getWidth()/2 && player.getPosX() - offsetX > getPosX() + getWidth()/2;
    	}
    	
    	if(colisionY && colisionX && damageFrame){
    		live -= Define.PLAYER_DAMAGE;
    		try{
    			Thread.sleep(Define.HIT_PAUSE);
    			return true;
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    	return false;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAnimation() {
		return animation;
	}

	public void setAnimation(int animation) {
		this.animation = animation;
	}

	public boolean isFlip() {
		return flip;
	}

	public void setFlip(boolean flip) {
		this.flip = flip;
	}

	public int getLive() {
		return live;
	}

	public void setLive(int live) {
		this.live = live;
	}
	
	
}
