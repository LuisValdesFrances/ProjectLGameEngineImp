package com.luis.projectlgameengineimp;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.luis.lgameengine.gameutils.Settings;
import com.luis.lgameengine.gameutils.controls.GameControl;
import com.luis.lgameengine.gameutils.controls.TouchPadControl;
import com.luis.lgameengine.gameutils.gameworld.GameCamera;
import com.luis.lgameengine.gameutils.gameworld.ParticlesManager;
import com.luis.lgameengine.gameutils.gameworld.RigidBody;
import com.luis.lgameengine.gameutils.gameworld.TileManager;
import com.luis.lgameengine.gameutils.gameworld.WorldConver;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.lgameengine.implementation.input.TouchData;
import com.luis.projectlgameengineimp.objects.BadRock;
import com.luis.projectlgameengineimp.objects.Enemy;
import com.luis.projectlgameengineimp.objects.Player;
import com.luis.projectlgameengineimp.objects.Rock;

/**
 * 
 * @author Luis Valdes Frances
 */
public class ModeGame {
	/*
	 * Perfomarce options
	 */
	public static final int PERF_BUTTON_W = Define.SIZEX12;
	public static final int PERF_BUTTON_H = Define.SIZEY12;
	public static int systemColision = 0;
	public static boolean isDoubleBuffer = false;
	
	/*
	 * 
	 */
	public static boolean isGamePaused;
	
	public static final int TILE_SET_SIZE[] = {24,32,48,64};
	public static int TILE_SIZE;
	public static float worldWidth;
	public static float worldHeight;
	//Levels design:
	private static final int[] COLOR_BG = {0xff458071, 0xffbeeee2};
	
	static GameCamera gameCamera;
	private static WorldConver worldConver;
	private static TileManager tileManager;
	private static GameControl gameControl;
	private static ParticlesManager particlesManager;
	
	private static BGManager bgManager;
	private static Player player;
	private static List<Enemy> enemyList;
	private static final int CAMERA_EXPLORER_SPEED = Define.SCR_MIDLE/2;
	
	private static Image gameBuffer;
	private static boolean drawBuffer;
	public static void init(int _iState) {
		
		switch(_iState){
		case Define.ST_GAME_INIT:
			//Init world dimensions
			TILE_SIZE = TILE_SET_SIZE[Settings.getInstance().getResolution()];
			worldWidth = TILE_SIZE * 40;
			worldHeight = TILE_SIZE * 20;
			
			worldConver = new WorldConver(Define.SIZEX, Define.SIZEY, 0, 0, 0, 0, worldWidth, worldHeight);
			
			tileManager = new TileManager(
					"/bin/levels/" + Settings.BITMAP_FOLDER[Settings.getInstance().getResolution()]  + "/level_1.map");
			tileManager.idConversionData(
					new Image[] {GfxManager.vImgGameTilesL1, GfxManager.vImgGameTilesL2, GfxManager.vImgGameTilesL3, GfxManager.imgEnemyTile});
			
			
			gameControl = new TouchPadControl(
		    		null, null, Define.SIZEX / 6, Define.SIZEY - Define.SIZEY / 4,
		    		new Image[] {null, null}, new Image[] {null, null},
		    		new int[] {Define.SIZEX - Define.SIZEX/4, Define.SIZEX - Define.SIZEX/8},
		    		new int[] {Define.SIZEY - Define.SIZEY/8, Define.SIZEY - Define.SIZEY/4},
		    		TouchData.ACTION_DOWN, TouchData.ACTION_MOVE, TouchData.ACTION_UP);
			
			gameControl.reset();
			
			player = new Player(
					(int) (TILE_SIZE),//(int)((GfxManager.imgPlayerIdle.getWidth()/Player.IDLE_FRAMES) * 0.30f),
					(int)(TILE_SIZE*2f),
					Define.SIZEX8, 
					-TILE_SIZE,//fWorldHeight/2,
					0, 
					0);
			
			player.setGravityForce(Define.GRAVITY_FORCE);
			player.setWeight(RigidBody.transformUnityValue(0.96f, TILE_SIZE, Define.PLAYER_WEIGHT));
			player.setForceJump(RigidBody.transformUnityValue(0.96f, TILE_SIZE, Define.PLAYER_FORCE_JUMP));
			player.setForceJumpShort(RigidBody.transformUnityValue(0.96f, TILE_SIZE, Define.PLAYER_FORCE_JUMP_SHORT));
			player.setForceAtack(RigidBody.transformUnityValue(0.96f, TILE_SIZE, Define.PLAYER_FORCE_ATACK));
			
			enemyList = new ArrayList<Enemy>();
			
			gameCamera= new GameCamera(player.getPosX(), player.getPosY(), worldWidth, worldHeight, Define.FRAME_SPEED_DEC);
			bgManager = new BGManager();
			
			gameBuffer = Image.createImage(Define.SIZEX, Define.SIZEY);
			
			
			
			if(isDoubleBuffer){
				tileManager.enableBuffer(
						worldConver,
						TILE_SIZE, Define.SIZEX, Define.SIZEY, gameCamera.getPosX(), gameCamera.getPosY());
				Log.i("Debug", ""+worldConver.getLayoutX() + "x" + worldConver.getLayoutY());
				drawBuffer = true;
			}
			
			particlesManager = ParticlesManager.getInstance();
			Main.changeState(Define.ST_GAME_RUN, false);
			break;
			
		case Define.ST_GAME_RUN:
			break;
		}
	}

	public static void update(int _iState) {
		switch(_iState){
		case Define.ST_GAME_INIT:
			break;
			
		case Define.ST_GAME_RUN:
			if(isGoToPerformanceMenu() || isFocusPerformanceMenu()){
				if(isGoToPerformanceMenu())
					Main.changeState(Define.ST_GAME_PERFORMANCE_OPTIONS, false);
			}else{
				//Obtengo el dt en segundos (Esta en milisegundos)
				gameCamera.updateCamera(player.getPosX(), player.getPosY());
				if(!isDoubleBuffer)
					bgManager.update();
				else{
					drawBuffer = tileManager.updateBuffer(gameCamera.getPosX(), gameCamera.getPosY(), TILE_SIZE);
				}
				
				gameControl.update(
						UserInput.getInstance().getMultiTouchHandler().getTouchOriginX(), 
						UserInput.getInstance().getMultiTouchHandler().getTouchOriginY(), 
						UserInput.getInstance().getMultiTouchHandler().getTouchX(), 
						UserInput.getInstance().getMultiTouchHandler().getTouchY(), 
						UserInput.getInstance().getMultiTouchHandler().getTouchAction());
				
				checkEnemy();
				updateEnemy();
				
				player.update(Main.getDeltaSec(), tileManager.getLayerID(1), enemyList, TILE_SIZE, TILE_SIZE, gameControl);
				
				particlesManager.runParticles(Main.getDeltaSec());
			}
			
			break;
			
		case Define.ST_GAME_PERFORMANCE_OPTIONS:
			updatePerformanceMenu();
			break;
		}
	}

	public static void draw(Graphics _g, int _iState) {
		_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
		switch(_iState){
		case Define.ST_GAME_INIT:
			break;
			
		case Define.ST_GAME_RUN:
			
			
			
			
//			vTileManager.drawLayer(_g, GfxManager.vImgGameTilesL0, 8, 0, vWorldConver, 
//					vGameCamera.getPosX(), vGameCamera.getPosY(), 0, TILE_SIZE >> 1, false);
			
			/*
			 //Costoso
			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
			_g.setShader(Define.SIZEX2, 0, Define.SIZEX2, Define.SIZEY, COLOR_BG, null, Graphics.CLAMP);
			_g.setAlpha(200);
			_g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
			_g.cleanShader();
			_g.setAlpha(255);
			*/
			
			//Menos costoso
//			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
//			_g.setAlpha(120);
//			_g.setColor(COLOR_BG[1]);
//			_g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
//			_g.setAlpha(255);
			
//			vTileManager.drawLayers(_g, 
//					new Image[] { 
//					GfxManager.vImgGameTilesL0, GfxManager.vImgGameTilesL1, GfxManager.vImgGameTilesL2, GfxManager.vImgGameTilesL1}, 
//					vWorldConver, 
//					vGameCamera.getPosX(), vGameCamera.getPosY(), 0, 0, false,
//					Graphics.BOTTOM | Graphics.HCENTER);
			
			/*
			drawLayer
			(Graphics _g, Image[] _vImgLayers, int _iLayer,
			WorldConver _vWorldConver, 
			float _fCameraX, float _fCameraY,
			int _iModDrawX, int _iModDrawY, boolean _isComplex,
			int _iAnchor)
			 */
			
			if(isDoubleBuffer){
				if(drawBuffer){
					
					//Adornos fondo
					tileManager.getBuffer().getGraphics().setClip(0, 0, tileManager.getBuffer().getWidth(), tileManager.getBuffer().getHeight());
					tileManager.getBuffer().getGraphics().setColor(0xffCED8F6);
					tileManager.getBuffer().getGraphics().fillRect(0, 0, tileManager.getBuffer().getWidth(), tileManager.getBuffer().getHeight());
					
					int modBufferX = tileManager.getExtraWidth()>>1;
					int modBufferY = tileManager.getExtraHeight()>>1;
					
					tileManager.drawLayers(tileManager.getBuffer().getGraphics(),
							new Image[] {GfxManager.vImgGameTilesL1, GfxManager.vImgGameTilesL2, GfxManager.vImgGameTilesL3},
							worldConver, 
							gameCamera.getPosX(), gameCamera.getPosY(), 
							new int[]{modBufferX, modBufferX, modBufferX}, 
							new int[]{modBufferY + (TILE_SIZE>>1), modBufferY, modBufferY + (TILE_SIZE>>1)},
							Graphics.BOTTOM | Graphics.HCENTER);
				}
				tileManager.drawBuffer(gameBuffer.getGraphics(), Define.SIZEX, Define.SIZEY);
			}
			else{
				//Adornos fondo
				gameBuffer.getGraphics().setClip(0, 0, Define.SIZEX, Define.SIZEY);
				gameBuffer.getGraphics().setColor(0xffCED8F6);
				gameBuffer.getGraphics().fillRect(0, 0, Define.SIZEX, Define.SIZEY);
				bgManager.draw(gameBuffer.getGraphics());
				
				tileManager.drawLayer(gameBuffer.getGraphics(), 
						new Image[] {GfxManager.vImgGameTilesL1, GfxManager.vImgGameTilesL2, GfxManager.vImgGameTilesL3},
						0,
						worldConver, 
						gameCamera.getPosX(), gameCamera.getPosY(), 0, TILE_SIZE>>1,
						Graphics.BOTTOM | Graphics.HCENTER);
				
				tileManager.drawLayer(gameBuffer.getGraphics(), 
						new Image[] {GfxManager.vImgGameTilesL1, GfxManager.vImgGameTilesL2, GfxManager.vImgGameTilesL3},
						1,
						worldConver, 
						gameCamera.getPosX(), gameCamera.getPosY(), 0, 0,
						Graphics.BOTTOM | Graphics.HCENTER);
			}
			
			drawEnemy(gameBuffer.getGraphics());
			
			player.draw(gameBuffer.getGraphics(), worldConver, gameCamera.getPosX(), gameCamera.getPosY(), 
					0, 0, 0, 0, 
					Graphics.BOTTOM | Graphics.HCENTER);
			
			
			//Adornos 1� plano
			if(!isDoubleBuffer){
				tileManager.drawLayer(gameBuffer.getGraphics(), 
						new Image[] {GfxManager.vImgGameTilesL1, GfxManager.vImgGameTilesL2, GfxManager.vImgGameTilesL3},
						2,
						worldConver, 
						gameCamera.getPosX(), gameCamera.getPosY(), 0, TILE_SIZE>>1,
						Graphics.BOTTOM | Graphics.HCENTER);
			}
			
			particlesManager.drawParticles(gameBuffer.getGraphics(), worldConver, gameCamera);
			
			_g.drawImage(gameBuffer, Define.SIZEX2, Define.SIZEY2, Graphics.VCENTER | Graphics.HCENTER);
			
			gameControl.draw(_g);
			
			drawPerformanceButton(_g);
			
			
			
			
			if (Main.IS_GAME_DEBUG) {
				_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
				_g.setTextSize((int) (24 * Settings.getInstance().getScale()));
				_g.setAlpha(160);
				_g.setColor(Main.COLOR_BLACK);
				_g.fillRect(0, 0, Define.SIZEX, _g.getTextHeight() * 6);
				_g.setAlpha(255);
				_g.drawText("FramesXSecond: " + Main.iFramesXSecond, 0, _g.getTextHeight(), Main.COLOR_RED);
				_g.drawText("DeltaTime: " + Main.getDeltaSec(), Define.SIZEX2, _g.getTextHeight(), Main.COLOR_RED);
				//Colisiones
				_g.drawText(
						"ColT: " + player.isColisionTop()
						+ " ColB: " + player.isColisionBotton()
						+ " ColL: " + player.isColisionLeft()
						+ " ColR: " + player.isColisionRight()
						, 0, _g.getTextHeight()*3, Main.COLOR_RED);
				
				
			}
			
			break;
			
		case Define.ST_GAME_PERFORMANCE_OPTIONS:
			drawPerformanceMenu(_g);
			break;
		}
	}
	
	private static void checkEnemy(){
		for(int f = 0; f < tileManager.getLayerID(3).length; f++){
			for(int c = 0; c < tileManager.getLayerID(3)[f].length; c++){
				if(tileManager.getLayerID(3)[f][c] != 0){
					Enemy e = null;
					if (worldConver.isObjectInGameLayout(gameCamera.getPosX(), gameCamera.getPosY(),
                            (TILE_SIZE * c),
                            (TILE_SIZE * f),
                            TILE_SIZE, TILE_SIZE)) {
						
						int w;
						int h;
						switch(tileManager.getLayerID(3)[f][c]){
						case Define.BADROCK_ID:
							w = (int) (TILE_SIZE * 0.5f);
							h = (int) (TILE_SIZE * 1.5f);
							e = new BadRock(Define.BADROCK_ID, player, w, h, 
									TILE_SIZE * c + w/2, TILE_SIZE * f+TILE_SIZE, 0, 0, 0, Define.BADROCK_LIVE);
							enemyList.add(e);
							
							e.setGravityForce(Define.GRAVITY_FORCE);
							e.setWeight(RigidBody.transformUnityValue(0.96f, TILE_SIZE, Define.PLAYER_WEIGHT));
							
							tileManager.getLayerID(3)[f][c] = 0;
							break;
							
						}
						Log.i("Debug", "Spawn enemy type: " + e.getType() + " x: " + e.getPosX() + " y: " + e.getPosY());
						
					}
				}
			}
		}
	}
	
	private static void updateEnemy(){
		for(int i = 0; i < enemyList.size(); i++){
			
			enemyList.get(i).update(Main.getDeltaSec(), tileManager.getLayerID(1), TILE_SIZE, TILE_SIZE);
			
			if(enemyList.get(i).getType() == Define.BADROCK_ID){
				if(enemyList.get(i).getState() == BadRock.STATE_DEAD){
					
					particlesManager.createParticles(
							3,
							enemyList.get(i).getPosX(), enemyList.get(i).getPosY()-enemyList.get(i).getHeight()/2, 
							350, 
							(int)(enemyList.get(i).getWidth()*0.75f), (int)(enemyList.get(i).getWidth()*0.85f),
							ParticlesManager.COL_CENTER, 
							new int[]{0xffE2C683, 0xff724611, 0xffD2872C, 0xffD5FCB5, 0Xff4E4032, 0xff426129},
							0.95f, false, false);
					enemyList.remove(i);
					i--;
				}
				else if(enemyList.get(i).createObject()){
					float x = enemyList.get(i).getPosX();
					float y = enemyList.get(i).getPosY()-enemyList.get(i).getHeight()*0.7f;
					Enemy e = new Rock(Define.ROCK_ID, player, 
							TILE_SIZE, TILE_SIZE, 
							x, 
							y, 0, 0, 0);
					enemyList.add(e);
					
					e.setGravityForce(Define.GRAVITY_FORCE);
					e.setWeight(RigidBody.transformUnityValue(0.96f, TILE_SIZE, Define.ROCK_WEIGHT));
					float speedX = RigidBody.transformUnityValue(0.96f, TILE_SIZE, 
							enemyList.get(i).isFlip()?Define.BADROCK_FORCE_LAUNCH:-Define.BADROCK_FORCE_LAUNCH);
					float speedY = RigidBody.transformUnityValue(0.96f, TILE_SIZE, 
							Define.BADROCK_FORCE_LAUNCH);
					e.setSpeedX(-speedX);
					e.setSpeedY(-speedY);
				}
			}else if(enemyList.get(i).getType() == Define.ROCK_ID){
				if(enemyList.get(i).getState() == Rock.STATE_DESTROY){
					
					particlesManager.createParticles(
							2,
							enemyList.get(i).getPosX(), enemyList.get(i).getPosY(), 
							650, 
							(int)(enemyList.get(i).getWidth()*0.45f), (int)(enemyList.get(i).getWidth()*0.45f),
							ParticlesManager.COL_CENTER, 
							new int[]{0xff35302A, 0Xff4E4032, 0xff918274},
							0.45f, false, false);
					
					enemyList.remove(i);
					i--;
				}
			}
		}
	}
	
	private static void drawEnemy(Graphics _g){
		if(enemyList.size() > 0){
			for(Enemy e : enemyList){
				e.draw(_g, null, null, worldConver, gameCamera.getPosX(), gameCamera.getPosY(), 
						0, 0, 0, 0, 
						Graphics.BOTTOM | Graphics.HCENTER);
			}
		}
	}
	
	private static void drawPerformanceButton(Graphics _g){
		_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
		if(isFocusPerformanceMenu()){
			_g.setColor(Main.COLOR_RED);
		}else{
			_g.setColor(Main.COLOR_BLACK);
		}
		_g.fillRect(0, Define.SIZEY - PERF_BUTTON_H, PERF_BUTTON_W, PERF_BUTTON_H);
		_g.drawText("P", 0, Define.SIZEY, Main.COLOR_WHITE);
	}
	
	private static boolean isFocusPerformanceMenu(){
		if((
			UserInput.getInstance().getMultiTouchHandler().getTouchAction(0) == TouchData.ACTION_DOWN || 
			UserInput.getInstance().getMultiTouchHandler().getTouchAction(0) == TouchData.ACTION_MOVE) && 
			UserInput.getInstance().compareTouch(0, Define.SIZEY - PERF_BUTTON_H, PERF_BUTTON_W, Define.SIZEY, 0)){
			return true;
		}
		return false;
	}
	
	private static boolean isGoToPerformanceMenu(){
		if(
				UserInput.getInstance().getMultiTouchHandler().getTouchAction(0) == TouchData.ACTION_UP && 
				UserInput.getInstance().compareTouch(0, Define.SIZEY - PERF_BUTTON_H, PERF_BUTTON_W, Define.SIZEY, 0)){
			return true;
		}
		return false;
	}
	
	private static void updatePerformanceMenu(){
		if(isGoToPerformanceMenu()){
			Main.changeState(Define.ST_GAME_RUN, false);
		}else{
			switch(getPerformanceOpt()){
			case 0:
				isDoubleBuffer = !isDoubleBuffer;
				if(isDoubleBuffer){
					tileManager.enableBuffer(
						worldConver,
						TILE_SIZE, Define.SIZEX, Define.SIZEY, gameCamera.getPosX(), gameCamera.getPosY());
					drawBuffer = true;
					//Reinicio worldConver, ahora las dismensiones de la pantalla deben de ser la de la imagen del buffer
					worldConver = new WorldConver(tileManager.getBuffer().getWidth(), tileManager.getBuffer().getHeight(), 
							-tileManager.getExtraHeight()/2, tileManager.getExtraHeight()/2, tileManager.getExtraWidth()/2, -tileManager.getExtraWidth()/2, 
							worldConver.getWorldWidth(), worldConver.getWorldHeight());
					Log.i("Debug", ""+worldConver.getLayoutX() + "x" + worldConver.getLayoutY());
				}else{
					//Vuelvo a poner world convert con las dimensiones normales
					worldConver = new WorldConver(Define.SIZEX, Define.SIZEY, 0, 0, 0, 0, worldWidth, worldHeight);
					Log.i("Debug", ""+worldConver.getLayoutX() + "x" + worldConver.getLayoutY());
				}
				break;
			case 1:
				if(systemColision == 0)systemColision = 1;
				else systemColision = 0;
				break;
			case 2:
				
				break;
				
			}
		}
	}
	private static void drawPerformanceMenu(Graphics _g){
		_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
		_g.setColor(Main.COLOR_BLACK);
		_g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
		int numOpt = 3;
		int spaceY = Define.SIZEY - PERF_BUTTON_H;
		int secH = spaceY / numOpt;
		
		String[] nameOpt = {"DoubleBuffer : ", "colisionType : ", "-"};
		for(int i = 0; i < numOpt; i++){
			if(Main.isModule(i))
				_g.setColor(0xff151515);
			else
				_g.setColor(0xff424242);
		
			_g.fillRect(0, secH * i, Define.SIZEX, secH * (i + 1));
			_g.drawText(nameOpt[i], Define.SIZEX64, secH * (i + 1) - secH/2, Main.COLOR_WHITE);
			
		}
		
		
		_g.drawText(isDoubleBuffer ? "TRUE" : "FALSE", Define.SIZEX2, secH * 1 - secH/2, isDoubleBuffer ? Main.COLOR_GREEN : Main.COLOR_RED);
		
		_g.drawText(systemColision == 0? "0" : "1", Define.SIZEX2, secH * 2 - secH/2, systemColision == 0 ?Main.COLOR_GREEN : Main.COLOR_RED);
		
		_g.drawText("FREE", Define.SIZEX2, secH * 3 - secH/2, Main.COLOR_RED);
		
		_g.setColor(0xff000000);
		_g.fillRect(0, secH * 3, Define.SIZEX, secH * 4);
		_g.drawText("OK", 0, Define.SIZEY, Main.COLOR_WHITE);
	}
	
	private static int getPerformanceOpt(){
		int numOpt = 3;
		int spaceY = Define.SIZEY - PERF_BUTTON_H;
		int secH = spaceY / numOpt;
		if(
			UserInput.getInstance().getMultiTouchHandler().getTouchAction(0) == TouchData.ACTION_DOWN && 
			UserInput.getInstance().getMultiTouchHandler().getTouchFrames(0) == 1){
			for(int i = 0; i < numOpt; i++){
				if(UserInput.getInstance().compareTouch(0, i * secH, Define.SIZEX, (i + 1) * secH, 0)){
					return i;
				}
			}
		}
		return -1;
	}
	
	
}
