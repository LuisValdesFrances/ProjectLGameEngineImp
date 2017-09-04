package com.luis.projectlgameengineimp.objects;

import com.luis.lgameengine.gameutils.gameworld.SpriteImage;
import com.luis.lgameengine.gameutils.gameworld.WorldConver;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.projectlgameengineimp.Define;
import com.luis.projectlgameengineimp.GfxManager;

public class Rock extends Enemy{
	
	private static Image imgRock;
	
	public static final int STATE_FLIGHT = 0;
	public static final int STATE_DESTROY = 1;

	public Rock(int type, Player player, int width, int height, float posX,
			float posY, float posZ, float speed, float angle) {
		super(type, player, width, height, posX, posY, posZ, speed, angle, -1);
		
		imgRock = GfxManager.imgRock;
		rotatePX = 0;
		rotatePY = -imgRock.getHeight()/2;
		setElasticity(0.65f);
		
	}
	
	public void update(float deltaTime, int[][] _iTilesMatrixID, float _fTileW, float _fTileH) {
    	super.update (deltaTime, _iTilesMatrixID, _fTileW, _fTileH);
    	if(state == STATE_FLIGHT){
    		boolean colPlayer = isColision(player);
    		if(isColisionBotton() || colPlayer){
    			state = STATE_DESTROY;
    			if(colPlayer)
    				player.setDamage(0);
    		}
    	}
    	setRotation(rotation+(rotationSpeed*deltaTime));
	}
	
	public void draw(
			Graphics _g, Image image, SpriteImage spriteImage, WorldConver worldConver, float cameraX, float cameraY,
    		int modAnimX, int modAnimY, int modDrawX, int modDrawY,
    		int anchor){
		switch(state){
		case STATE_FLIGHT:
			super.draw(_g, imgRock, null, worldConver, cameraX, cameraY,
					modAnimX, modAnimY, modDrawX, 12, anchor);
			break;
		}
	}

	@Override
	public boolean createObject() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
