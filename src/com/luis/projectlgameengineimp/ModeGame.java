package com.luis.projectlgameengineimp;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.luis.lgameengine.gui.Button;
import com.luis.lgameengine.gui.MenuBox;
import com.luis.lgameengine.gui.MenuManager;
import com.luis.lgameengine.gameutils.GamePerformance;
import com.luis.lgameengine.gameutils.Settings;
import com.luis.lgameengine.gameutils.controls.GameControl;
import com.luis.lgameengine.gameutils.controls.TouchPadControl;
import com.luis.lgameengine.gameutils.fonts.Font;
import com.luis.lgameengine.gameutils.gameworld.GameCamera;
import com.luis.lgameengine.gameutils.gameworld.GfxEffects;
import com.luis.lgameengine.gameutils.gameworld.ParticleManager;
import com.luis.lgameengine.gameutils.gameworld.RigidBody;
import com.luis.lgameengine.gameutils.gameworld.TileManager;
import com.luis.lgameengine.gameutils.gameworld.WorldConver;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.lgameengine.implementation.input.TouchData;
import com.luis.projectlgameengineimp.objects.BadRock;
import com.luis.projectlgameengineimp.objects.BadStick;
import com.luis.projectlgameengineimp.objects.BigRock;
import com.luis.projectlgameengineimp.objects.Enemy;
import com.luis.projectlgameengineimp.objects.Player;
import com.luis.projectlgameengineimp.objects.Rock;

/**
 * 
 * @author Luis Valdes Frances
 */
public class ModeGame {
	
	/**
	 * Perfomarce options
	 */
	public static final int PERF_BUTTON_W = Define.SIZEX12;
	public static final int PERF_BUTTON_H = Define.SIZEY12;
	
	public static int systemColision = 0;
	public static boolean isBackBuffer = false;
	
	/**/
	public static boolean isGamePaused;
	private static int gameFrame;
	
	public static final int TILE_SET_SIZE[] = {24,32,48,64};
	public static final String FILE_LEVEL[] = {"ldpi","mdpi","hdpi","hhdpi"};
	public static int tileSize;
	public static float worldWidth;
	public static float worldHeight;
	
	//Levels design:
	private static int bgColor;
	
	static GameCamera gameCamera;
	private static WorldConver worldConver;
	private static TileManager tileManager;
	private static GameControl gameControl;
	private static ParticleManager particleManager;
	private static GfxEffects gfxEffects;
	
	private static BGManager bgManager;
	private static Player player;
	private static List<Enemy> enemyList;
	
	private static Image gameBuffer;
	private static boolean drawBuffer;
	
	private static final String LEVEL_1_PATH = "/level_1";
	private static final int LEVEL_TEST_WIDTH = 40;
	private static final int LEVEL_TEST_HEIGHT = 20;
	
	private static final String LEVEL_ROCK_TEST_PATH = "/level_rock_test";
	private static final int LEVEL_ROCK_WIDTH = 200;
	private static final int LEVEL_ROCK_HEIGHT = 24;
	
	/**
	 * Interface
	 */
	private static Button btnPause;
	private static int optionSelect;
	private static MenuBox confirmationQuit;
	
	/**
	 * Init game zoom
	 */
	private static final float INIT_ZOOM = 4;
	private static float initCurrentZoom;
	
	public static void init(int _iState) {
		
		switch(_iState){
		case Define.ST_GAME_INIT:
			
			//Init world dimensions
			tileSize = TILE_SET_SIZE[Settings.getInstance().getResolution()];
			
			switch(GameState.getInstance().getLevel()){
			case 0:
				worldWidth = tileSize * LEVEL_TEST_WIDTH;
				worldHeight = tileSize * LEVEL_TEST_HEIGHT;
				
				tileManager = new TileManager(
					"/bin/levels/"+ 
							FILE_LEVEL[Settings.getInstance().getResolution()] + LEVEL_1_PATH + "_" + FILE_LEVEL[Settings.getInstance().getResolution()]+ ".map");
				bgColor = 0xffCED8F6;
				
				spawnPlayer();
				gameCamera= new GameCamera(worldConver, player.getPosX(), player.getPosY(), 
						GamePerformance.getInstance().getFrameMult(Main.targetFPS));
				
				bgManager = new BGManager();
				break;
			case 1:
				bgColor = 0xffCED8F6;
				worldWidth = tileSize * LEVEL_ROCK_WIDTH;
				worldHeight = tileSize * LEVEL_ROCK_HEIGHT;
				
				tileManager = new TileManager(
					"/bin/levels/"+ 
					FILE_LEVEL[Settings.getInstance().getResolution()] + LEVEL_ROCK_TEST_PATH+ "_" + FILE_LEVEL[Settings.getInstance().getResolution()]+ ".map");
				
				spawnPlayer();
				
				bgManager = null;
				break;
			}
			
			/**
			 * Game GUI
			 */
			btnPause = new Button(
					GfxManager.imgButtonPauseRelease, 
					GfxManager.imgButtonPauseFocus, 
					Define.SIZEX-GfxManager.imgButtonPauseRelease.getWidth(), 
					GfxManager.imgButtonPauseRelease.getHeight(),
					null, 0){
				@Override
				public void onButtonPressDown(){}
				
				@Override
				public void onButtonPressUp(){
					isGamePaused = true;
					Main.changeState(Define.ST_GAME_PAUSE, false);
				}
			};
			
			tileManager.idConversionData(
					new Image[] {
						GfxManager.vImgGameTilesL1, 
						GfxManager.vImgGameTilesL2,
						GfxManager.vImgGameTilesL3, 
						GfxManager.imgEnemyTile});
			
			worldConver = new WorldConver(Define.SIZEX, Define.SIZEY, 0, 0, 0, 0, worldWidth, worldHeight);
			
			gameCamera= new GameCamera(worldConver, player.getPosX(), player.getPosY(), 
					GamePerformance.getInstance().getFrameMult(Main.targetFPS));
			
			gameControl = new TouchPadControl(
		    		null, null, Define.SIZEX / 6, Define.SIZEY - Define.SIZEY / 4,
		    		new Image[] {null, null}, new Image[] {null, null},
		    		new int[] {Define.SIZEX - Define.SIZEX/4, Define.SIZEX - Define.SIZEX/8},
		    		new int[] {Define.SIZEY - Define.SIZEY/8, Define.SIZEY - Define.SIZEY/4},
		    		TouchData.ACTION_DOWN, TouchData.ACTION_MOVE, TouchData.ACTION_UP);
			
			gameControl.reset();
			
			enemyList = new ArrayList<Enemy>();
			
			particleManager = ParticleManager.getInstance();
			gfxEffects = GfxEffects.getInstance();
			
			gameBuffer = Image.createImage(Define.SIZEX, Define.SIZEY);
			if(isBackBuffer){
				tileManager.enableBuffer(
						worldConver,
						tileSize, 
						Define.SIZEX, 
						Define.SIZEY, 
						gameCamera.getPosX(), 
						gameCamera.getPosY());
				Log.i("Debug", ""+worldConver.getLayoutX() + "x" + worldConver.getLayoutY());
				drawBuffer = true;
			}
			
			Enemy.particleManager = particleManager;
			Enemy.gfxEffects = gfxEffects;
			Enemy.worlConver = worldConver;
			Enemy.gameCamera = gameCamera;
			
			gameFrame = 0;
			
			initCurrentZoom = INIT_ZOOM;
			
			Main.changeState(Define.ST_GAME_RUN, false);
			break;
			
		case Define.ST_GAME_RUN:
			break;
			
		case Define.ST_GAME_PAUSE:
			optionSelect = 0;
			
			confirmationQuit = new MenuBox(
					Define.SIZEX, Define.SIZEY, 
					GfxManager.imgMenuBox, 
					GfxManager.imgButtonRelease, GfxManager.imgButtonFocus,
					Define.SIZEX2, Define.SIZEY2,
					RscManager.sAllTexts[RscManager.TXT_RETURN_MENU],
					new String[]{RscManager.sAllTexts[RscManager.TXT_NO], RscManager.sAllTexts[RscManager.TXT_YES]},
					Font.FONT_MEDIUM, Font.FONT_MEDIUM){
				@Override
				public void onFinish(){
					Log.i("Debug", "Index: " + this.getIndexPressed());
					switch(this.getIndexPressed()){
					case 0:
						UserInput.getInstance().getMultiTouchHandler().resetTouch();
						UserInput.getInstance().getKeyboardHandler().resetKeys();
						optionSelect = 0;
						break;
					case 1:
						Main.changeState(Define.ST_MENU_SELECT_GAME, false);
					break;
					}
				}
			};
			
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
				
				gameFrame++;
				if(!isBackBuffer){
					if(bgManager != null)
						bgManager.update();
				}else{
					drawBuffer = tileManager.updateBuffer(
							gameCamera.getPosX(), 
							gameCamera.getPosY(), 
							tileSize);
				}
				
				if(initCurrentZoom > 0.0f){
					initCurrentZoom -= ((initCurrentZoom*0.05f)*
							GamePerformance.getInstance().getFrameMult(Main.iFramesXSecond) +0.001f);
				}else{
					initCurrentZoom = 0f;
					
					btnPause.update(UserInput.getInstance().getMultiTouchHandler());
					if(!btnPause.isTouching()){
						gameControl.update(
								UserInput.getInstance().getMultiTouchHandler().getTouchOriginX(), 
								UserInput.getInstance().getMultiTouchHandler().getTouchOriginY(), 
								UserInput.getInstance().getMultiTouchHandler().getTouchX(), 
								UserInput.getInstance().getMultiTouchHandler().getTouchY(), 
								UserInput.getInstance().getMultiTouchHandler().getTouchAction());
					}
					
					
					gameCamera.updateCamera(
							player.getPosX()-worldConver.getLayoutX()/2, 
							player.getPosY()-worldConver.getLayoutY()/2);
					//gameCamera.setPosX(player.getPosX());
					//gameCamera.setPosY(player.getPosY());
					particleManager.update(Main.getDeltaSec());
					gfxEffects.update(Main.getDeltaSec());
					
					spawnEnemy();
					updateEnemy();
					
					player.update(Main.getDeltaSec(), tileManager.getLayerID(1), tileSize, tileSize, gameControl);
				}
			}
			break;
			
		case Define.ST_GAME_PERFORMANCE_OPTIONS:
			updatePerformanceMenu();
			break;
			
		case Define.ST_GAME_PAUSE:
			if(!confirmationQuit.update(UserInput.getInstance().getMultiTouchHandler(), Main.getDeltaSec())){
				
				optionSelect = UserInput.getInstance().getOptionMenuTouched_Y(2, optionSelect);
				
				if (UserInput.getInstance().getOkTouched_Y(optionSelect)) {

					switch (optionSelect) {
					case 0:
						Main.changeState(Define.ST_GAME_RUN, true);
						break;
					case 1:
						UserInput.getInstance().getMultiTouchHandler().resetTouch();
						UserInput.getInstance().getKeyboardHandler().resetKeys();
						confirmationQuit.start();
						break;
					}
				}
				break;
				}
			}
		
	}

	public static void draw(Graphics _g, int _iState) {
		_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
		switch(_iState){
		case Define.ST_GAME_INIT:
			break;
			
		case Define.ST_GAME_RUN:
			//Ajuste de la logica
			if(gameFrame<2){
				return;
			}
			
			
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
			
			if(isBackBuffer){
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
							new int[]{modBufferY + (tileSize>>1), modBufferY, modBufferY + (tileSize>>1)},
							Graphics.BOTTOM | Graphics.HCENTER);
				}
				tileManager.drawBuffer(gameBuffer.getGraphics(), Define.SIZEX, Define.SIZEY);
			}
			else{
				//Adornos fondo
				gameBuffer.getGraphics().setClip(0, 0, Define.SIZEX, Define.SIZEY);
				gameBuffer.getGraphics().setColor(bgColor);
				gameBuffer.getGraphics().fillRect(0, 0, Define.SIZEX, Define.SIZEY);
				
				if(bgManager != null)
					bgManager.draw(gameBuffer.getGraphics());
				
				tileManager.drawLayer(gameBuffer.getGraphics(), 
						new Image[] {GfxManager.vImgGameTilesL1, GfxManager.vImgGameTilesL2, GfxManager.vImgGameTilesL3},
						0,
						worldConver, 
						gameCamera.getPosX(), gameCamera.getPosY(), 0, tileSize>>1,
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
			
			
			//Adornos 1º plano
			if(!isBackBuffer){
				tileManager.drawLayer(gameBuffer.getGraphics(), 
						new Image[] {GfxManager.vImgGameTilesL1, GfxManager.vImgGameTilesL2, GfxManager.vImgGameTilesL3},
						2,
						worldConver, 
						gameCamera.getPosX(), gameCamera.getPosY(), 0, tileSize>>1,
						Graphics.BOTTOM | Graphics.HCENTER);
			}
			
			gameBuffer.getGraphics().setClip(0, 0, Define.SIZEX, Define.SIZEY);
			
			particleManager.draw(gameBuffer.getGraphics(), worldConver, gameCamera);
			
			_g.setImageSize(1f+initCurrentZoom, 1f+initCurrentZoom);
			
			_g.drawImage(gameBuffer, 
					Define.SIZEX2+(int)gfxEffects.getTremorX(), 
					Define.SIZEY2+(int)gfxEffects.getTremorY(), 
					Graphics.VCENTER | Graphics.HCENTER);
			
			_g.setImageSize(1f, 1f);
			
			btnPause.draw(_g, 0, 0);
			gameControl.draw(_g);
			drawPerformanceButton(_g);
			
			if (Main.IS_GAME_DEBUG) {
				_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
				_g.setTextSize(32);
				_g.setAlpha(160);
				_g.setColor(0x88000000);
				_g.fillRect(0, 0, Define.SIZEX, _g.getTextHeight() * 3);
				_g.setAlpha(255);
				_g.drawText("FramesXSecond: " + Main.iFramesXSecond, 0, _g.getTextHeight(), Main.COLOR_WHITE);
				_g.drawText("DeltaTime: " + Main.getDeltaSec(), Define.SIZEX2, _g.getTextHeight(), Main.COLOR_WHITE);
				_g.drawText("Player x: " + (int)player.getPosX() + " - Player y: " + (int)player.getPosY(), 
						0, _g.getTextHeight()*2, Main.COLOR_WHITE);
				//Colisiones
				_g.drawText(
						"ColT: " + player.isColisionTop()
						+ " ColB: " + player.isColisionBotton()
						+ " ColL: " + player.isColisionLeft()
						+ " ColR: " + player.isColisionRight()
						, 0, _g.getTextHeight()*3, Main.COLOR_WHITE);
			}
			break;
			
		case Define.ST_GAME_PERFORMANCE_OPTIONS:
			drawPerformanceMenu(_g);
			break;
			
		case Define.ST_GAME_PAUSE:
			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
			//_g.drawImage(GfxManager.vImgBackground, 0, 0, 0);
			_g.setColor(Main.COLOR_RED);
			_g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
			MenuManager.drawButtonsAndTextY(_g, 2, new String[]{"CONTINUAR", "SALIR"},
				    Font.FONT_BIG, optionSelect, null, GfxManager.vImgMenuButtons, Main.iFrame);
			
			confirmationQuit.draw(_g, false);
			break;
		}
	}
	
	private static void spawnPlayer(){
		for(int f = 0; f < tileManager.getLayerID(3).length; f++){
			for(int c = 0; c < tileManager.getLayerID(3)[f].length; c++){
				if(tileManager.getLayerID(3)[f][c] == Define.PLAYER_ID){
					int w = (int) (tileSize);
					int y = (int)(tileSize*2f);
					player = new Player(
							w, y,
							(tileSize * c)+tileSize/2,
                            (tileSize * f)+tileSize,
							0,
							RigidBody.transformUnityValue(0.96f, ModeGame.tileSize, Define.PLAYER_SPEED),
							0);
					
					player.setGravityForce(Define.GRAVITY_FORCE);
					player.setWeight(RigidBody.transformUnityValue(0.96f, tileSize, Define.PLAYER_WEIGHT));
					player.setForceJump(RigidBody.transformUnityValue(0.96f, tileSize, Define.PLAYER_FORCE_JUMP));
					player.setForceJumpShort(RigidBody.transformUnityValue(0.96f, tileSize, Define.PLAYER_FORCE_JUMP_SHORT));
					player.setForceAtack(RigidBody.transformUnityValue(0.96f, tileSize, Define.PLAYER_FORCE_ATACK));
					break;
				}
			}
		}
	}
	
	private static void spawnEnemy(){
		for(int f = 0; f < tileManager.getLayerID(3).length; f++){
			for(int c = 0; c < tileManager.getLayerID(3)[f].length; c++){
				if(tileManager.getLayerID(3)[f][c] != 0){
					Enemy e = null;
					if (worldConver.isObjectInGameLayout(gameCamera.getPosX(), gameCamera.getPosY(),
							(tileSize * c),
                            (tileSize * f),
                            tileSize, tileSize)) {
						
						int w;
						int h;
						switch(tileManager.getLayerID(3)[f][c]){
						case Define.BADROCK_ID:
							w = (int) (tileSize * 0.5f);
							h = (int) (tileSize * 1.5f);
							e = new BadRock(
									Define.BADROCK_ID, player, 
									w, h, 
									tileSize * c+tileSize/2, 
									tileSize * f+tileSize, 
									0, 0, 0, Define.BADROCK_LIVE);
							
							e.setGravityForce(Define.GRAVITY_FORCE);
							e.setWeight(RigidBody.transformUnityValue(0.96f, tileSize, Define.BADROCK_WEIGHT));
							tileManager.getLayerID(3)[f][c] = 0;
							enemyList.add(e);
							break;
							
						case Define.BADSTICK_ID:
							w = (int) (tileSize * 0.5f);
							h = (int) (tileSize * 1.5f);
							e = new BadStick(
									Define.BADSTICK_ID, player, 
									w, h, 
									tileSize * c+tileSize/2, 
									tileSize * f+tileSize, 
									0, 0, 0, Define.BADSTICK_LIVE);
							
							e.setGravityForce(Define.GRAVITY_FORCE);
							e.setWeight(RigidBody.transformUnityValue(0.96f, tileSize, Define.BADSTICK_WEIGHT));
							e.setSpeed(RigidBody.transformUnityValue(0.96f, ModeGame.tileSize, Define.BADSTICK_SPEED));
							tileManager.getLayerID(3)[f][c] = 0;
							enemyList.add(e);
							break;
						
						case Define.BIGROCK_RIGHT_ID:
						case Define.BIGROCK_LEFT_ID:
							
							char d = tileManager.getLayerID(3)[f][c] == Define.BIGROCK_RIGHT_ID ? 'R' : 'L';
							float speed = RigidBody.transformUnityValue(0.96f, ModeGame.tileSize, Define.BIG_ROCK_SPEED);
							w = (int) (tileSize * 4);
							h = (int) (tileSize * 4);
							e = new BigRock(tileManager.getLayerID(3)[f][c], 
									player,
									w, h, 
									tileSize * c + tileSize/2, 
									tileSize * f + tileSize, 0,
									d=='R'?speed:-speed, 0);
							
							e.setGravityForce(Define.GRAVITY_FORCE);
							e.setWeight(RigidBody.transformUnityValue(0.96f, tileSize, Define.BIG_ROCK_WEIGHT));
							e.setRotationSpeed(d=='R'?Define.BIG_ROCK_SPEED_ROTATION:-Define.BIG_ROCK_SPEED_ROTATION);
							tileManager.getLayerID(3)[f][c] = 0;
							enemyList.add(e);
							break;
						}
						if(e != null){
							Log.i("Debug", "Spawn enemy type: " + e.getType() + " x: " + e.getPosX() + " y: " + e.getPosY());
						}
					}
				}
			}
		}
	}
	
	private static void updateEnemy(){
		for(int i = 0; i < enemyList.size(); i++){
			
			enemyList.get(i).update(Main.getDeltaSec(), tileManager.getLayerID(1), tileSize, tileSize);
			
			switch(enemyList.get(i).getType()){
			
				case Define.BADROCK_ID:
				case Define.BADSTICK_ID:
					if(enemyList.get(i).getState() == BadRock.STATE_DEAD){
						enemyList.get(i).createParticles(
								tileSize, 
								enemyList.get(i).getPosX(), enemyList.get(i).getPosY()-enemyList.get(i).getHeight()/2,
								RigidBody.transformUnityValue(0.96f, tileSize, Define.PARTICLE_ENEMY_DESTROY_WEIGHT), 
								RigidBody.transformUnityValue(0.96f, tileSize, Define.PARTICLE_ENEMY_DESTROY_SPEED), 
								Define.PARTICLE_ENEMY_DESTROY_DURATION);
						enemyList.remove(i);
						i--;
					}
					else if(enemyList.get(i).createObject()){
						float x = enemyList.get(i).getPosX();
						float y = enemyList.get(i).getPosY()-enemyList.get(i).getHeight()*0.7f;
						Enemy e = new Rock(
								Define.ROCK_ID, player,
								tileSize, tileSize, 
								x, 
								y, 0, 0, 0);
						e.setGravityForce(Define.GRAVITY_FORCE);
						e.setWeight(RigidBody.transformUnityValue(0.96f, tileSize, Define.ROCK_WEIGHT));
						float speedX = RigidBody.transformUnityValue(0.96f, tileSize, 
								enemyList.get(i).isFlip()?Define.BADROCK_FORCE_LAUNCH:-Define.BADROCK_FORCE_LAUNCH);
						float speedY = RigidBody.transformUnityValue(0.96f, tileSize, 
								Define.BADROCK_FORCE_LAUNCH);
						e.setSpeedX(-speedX);
						e.setSpeedY(-speedY);
						e.setRotationSpeed(enemyList.get(i).isFlip() ? -Define.ROCK_SPEED_ROTATION : Define.ROCK_SPEED_ROTATION);
						
						enemyList.add(e);
					}
				break;
				case Define.ROCK_ID:
					if(enemyList.get(i).getState() == Rock.STATE_DESTROY){
						
						enemyList.get(i).createParticles(
								tileSize,
								enemyList.get(i).getPosX(), enemyList.get(i).getPosY()-enemyList.get(i).getHeight()/2,
								RigidBody.transformUnityValue(0.96f, tileSize, Define.PARTICLE_ROCK_DESTROY_WEIGHT),
								RigidBody.transformUnityValue(0.96f, tileSize, Define.PARTICLE_ROCK_DESTROY_SPEED),
								Define.PARTICLE_ROCK_DESTROY_DURATION);
						
						gfxEffects.createTremor(0.35f, tileSize/8);
						
						enemyList.remove(i);
						i--;
					}
				break;
				case Define.BIGROCK_LEFT_ID:
				case Define.BIGROCK_RIGHT_ID:
					enemyList.get(i).setEnemeyDamage(enemyList);
				default:
					
					break;
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
				isBackBuffer = !isBackBuffer;
				if(isBackBuffer){
					tileManager.enableBuffer(
						worldConver,
						tileSize, Define.SIZEX, Define.SIZEY, gameCamera.getPosX(), gameCamera.getPosY());
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
		
		
		_g.drawText(isBackBuffer ? "TRUE" : "FALSE", Define.SIZEX2, secH * 1 - secH/2, isBackBuffer ? Main.COLOR_GREEN : Main.COLOR_RED);
		
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
			UserInput.getInstance().getMultiTouchHandler().getTouchAction(0) == TouchData.ACTION_DOWN){
			for(int i = 0; i < numOpt; i++){
				if(UserInput.getInstance().compareTouch(0, i * secH, Define.SIZEX, (i + 1) * secH, 0) && UserInput.getInstance().getMultiTouchHandler().getTouchFrames(0) == 1){
					return i;
				}
			}
		}
		return -1;
	}
}
