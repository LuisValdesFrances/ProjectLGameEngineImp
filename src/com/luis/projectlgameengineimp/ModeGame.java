package com.luis.projectlgameengineimp;

import android.util.Log;

import com.luis.lgameengine.gameutils.GameCamera;
import com.luis.lgameengine.gameutils.GfxEffects;
import com.luis.lgameengine.gameutils.Settings;
import com.luis.lgameengine.gameutils.TileManager;
import com.luis.lgameengine.gameutils.WorldConver;
import com.luis.lgameengine.gameutils.controls.GameControl;
import com.luis.lgameengine.gameutils.controls.TouchPadControl;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.lgameengine.implementation.input.MultiTouchHandler2;
import com.luis.projectlgameengineimp.objects.Player;
import com.luis.projectlgameengineimp.objects.RigidBody;

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
	public static boolean isDrawTileFast = false;
	public static boolean isDoubleBuffer = false;
	
	/*
	 * 
	 */
	public static boolean isGamePaused;
	
	public static final int TILE_SET_SIZE[] = {64,96,128,128};
	public static int TILE_SIZE;
	public static float fWorldWidth;
	public static float fWorldHeight;
	//Levels design:
	private static final int[] COLOR_BG = {0xff458071, 0xffbeeee2};
	
	static GameCamera vGameCamera;
	private static WorldConver vWorldConver;
	private static TileManager vTileManager;
	private static GameControl vGameControl;
	
	private static BGManager vBgManager;
	private static Player vPlayer;
	private static final int CAMERA_EXPLORER_SPEED = Define.SCR_MIDLE/2;
	
	private static Image vImgTilesBuffer;
	public static void init(int _iState) {
		
		switch(_iState){
		case Define.ST_GAME_INIT:
			//Init world dimensions
			TILE_SIZE = TILE_SET_SIZE[Settings.getInstance().getResolution()];
			fWorldWidth = TILE_SIZE * 20;
			fWorldHeight = TILE_SIZE * 8;
			
			vWorldConver = new WorldConver(Define.SIZEX, Define.SIZEY, 0, 0, 0, 0, fWorldWidth, fWorldHeight);
			
			vTileManager = new TileManager("/bin/levels/level_1.map");
			vTileManager.idConversionData(
					new Image[] {GfxManager.vImgGameTilesL0, GfxManager.vImgGameTilesL1, GfxManager.vImgGameTilesL2, GfxManager.vImgGameTilesL1});
			
			
			vGameControl = new TouchPadControl(
		    		null, null, Define.SIZEX / 6, Define.SIZEY - Define.SIZEY / 4,
		    		new Image[] {null, null}, new Image[] {null, null},
		    		new int[] {Define.SIZEX - Define.SIZEX/4, Define.SIZEX - Define.SIZEX/8},
		    		new int[] {Define.SIZEY - Define.SIZEY/8, Define.SIZEY - Define.SIZEY/4},
		    		MultiTouchHandler2.ACTION_DOWN, MultiTouchHandler2.ACTION_DRAG, MultiTouchHandler2.ACTION_UP);
			vGameControl.reset();
			vPlayer = new Player(
					GfxManager.vImgPlayer.getWidth(),
					GfxManager.vImgPlayer.getHeight(),
					0, 
					-Define.SIZEY,//fWorldHeight/2, 
					0, RigidBody.transformUnityValue(TILE_SIZE, 2.5f), 0);
			
			RigidBody.gravityForce = 3f;//RigidBody.transformUnityValue(TILE_SIZE, 3f);
			vPlayer.weight = RigidBody.transformUnityValue(TILE_SIZE, 4.8f);
			vPlayer.fForceJump = RigidBody.transformUnityValue(TILE_SIZE, 9f);
			vGameCamera= new GameCamera(vPlayer.getPosX(), vPlayer.getPosY(), fWorldWidth, fWorldHeight, Define.FRAME_SPEED_DEC);
			vBgManager = new BGManager();
			
			vImgTilesBuffer = Image.createImage(Define.SIZEX, Define.SIZEY);
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
				vGameCamera.updateCamera(vPlayer.getPosX(), vPlayer.getPosY());
				vBgManager.update();
				
				vGameControl.update(
						MultiTouchHandler2.touchOriginX, 
						MultiTouchHandler2.touchOriginY, 
						MultiTouchHandler2.touchX, 
						MultiTouchHandler2.touchY, 
						MultiTouchHandler2.touchAction);
				
				vPlayer.update(Main.getDeltaSec(), vTileManager.getLayerID(2), TILE_SIZE, TILE_SIZE, vGameControl);
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
			
			//Adornos fondo
			
			vImgTilesBuffer.getGraphics().setClip(0, 0, Define.SIZEX, Define.SIZEY);
			vImgTilesBuffer.getGraphics().setColor(Main.COLOR_RED);
			vImgTilesBuffer.getGraphics().fillRect(0, 0, Define.SIZEX, Define.SIZEY);
			vBgManager.draw(vImgTilesBuffer.getGraphics());
			vTileManager.drawLayer(vImgTilesBuffer.getGraphics(), 
					new Image[] {GfxManager.vImgGameTilesL0, GfxManager.vImgGameTilesL1, GfxManager.vImgGameTilesL2, GfxManager.vImgGameTilesL1}, 
					0,
					vWorldConver, 
					vGameCamera.getPosX(), vGameCamera.getPosY(), 0, TILE_SIZE>>1, isDrawTileFast,
					Graphics.BOTTOM | Graphics.HCENTER);
			
			vTileManager.drawLayer(vImgTilesBuffer.getGraphics(), 
					new Image[] {GfxManager.vImgGameTilesL0, GfxManager.vImgGameTilesL1, GfxManager.vImgGameTilesL2, GfxManager.vImgGameTilesL1}, 
					2,
					vWorldConver, 
					vGameCamera.getPosX(), vGameCamera.getPosY(), 0, 0, isDrawTileFast,
					Graphics.BOTTOM | Graphics.HCENTER);
			
			
			vPlayer.draw(vImgTilesBuffer.getGraphics(), vWorldConver, vGameCamera.getPosX(), vGameCamera.getPosY(), 0, 0, 0, 0, Graphics.BOTTOM | Graphics.HCENTER);
			
			//Adornos 1º plano
			vTileManager.drawLayer(vImgTilesBuffer.getGraphics(), 
					new Image[] {GfxManager.vImgGameTilesL0, GfxManager.vImgGameTilesL1, GfxManager.vImgGameTilesL2, GfxManager.vImgGameTilesL1}, 
					1,
					vWorldConver, 
					vGameCamera.getPosX(), vGameCamera.getPosY(), 0, TILE_SIZE>>1, isDrawTileFast,
					Graphics.BOTTOM | Graphics.HCENTER);
			
			_g.drawImage(vImgTilesBuffer, Define.SIZEX2, Define.SIZEY2, Graphics.VCENTER | Graphics.HCENTER);
			
			vGameControl.draw(_g);
			
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
						"ColT: " + vPlayer.isColisionTop()
						+ " ColB: " + vPlayer.isColisionBotton()
						+ " ColL: " + vPlayer.isColisionLeft()
						+ " ColR: " + vPlayer.isColisionRight()
						, 0, _g.getTextHeight()*3, Main.COLOR_RED);
				
				
			}
			
			/*
			try {
				Thread.sleep(120);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			break;
			
		case Define.ST_GAME_PERFORMANCE_OPTIONS:
			drawPerformanceMenu(_g);
			break;
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
		if(
				(MultiTouchHandler2.touchAction[0] == MultiTouchHandler2.ACTION_DOWN || MultiTouchHandler2.touchAction[0] == MultiTouchHandler2.ACTION_DRAG) && 
				UserInput.compareTouch(0, Define.SIZEY - PERF_BUTTON_H, PERF_BUTTON_W, Define.SIZEY, 0)){
			return true;
		}
		return false;
	}
	private static boolean isGoToPerformanceMenu(){
		if(
				MultiTouchHandler2.touchAction[0] == MultiTouchHandler2.ACTION_UP && 
				UserInput.compareTouch(0, Define.SIZEY - PERF_BUTTON_H, PERF_BUTTON_W, Define.SIZEY, 0)){
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
				isDrawTileFast = !isDrawTileFast;
				break;
			case 1:
				RigidBody.isBoundColToScreen = !RigidBody.isBoundColToScreen;
				break;
			case 2:
				RigidBody.isFastColision = !RigidBody.isFastColision;
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
		
		String[] nameOpt = {"DrawTileFast : ", "isBoundColtoScreen : ", "isFastColision : "};
		for(int i = 0; i < numOpt; i++){
			if(Main.isModule(i))
				_g.setColor(0xff151515);
			else
				_g.setColor(0xff424242);
		
			_g.fillRect(0, secH * i, Define.SIZEX, secH * (i + 1));
			_g.drawText(nameOpt[i], Define.SIZEX64, secH * (i + 1) - secH/2, Main.COLOR_WHITE);
			
		}
		
		
		_g.drawText(isDrawTileFast ? "TRUE" : "FALSE", Define.SIZEX2, secH * 1 - secH/2, isDrawTileFast ? Main.COLOR_GREEN : Main.COLOR_RED);
		
		_g.drawText(RigidBody.isBoundColToScreen ? "TRUE" : "FALSE", Define.SIZEX2, secH * 2 - secH/2, RigidBody.isBoundColToScreen ?Main.COLOR_GREEN : Main.COLOR_RED);
		
		_g.drawText(RigidBody.isFastColision ? "TRUE" : "FALSE", Define.SIZEX2, secH * 3 - secH/2, RigidBody.isFastColision ?Main.COLOR_GREEN : Main.COLOR_RED);
		
		_g.setColor(0xff000000);
		_g.fillRect(0, secH * 3, Define.SIZEX, secH * 4);
		_g.drawText("OK", 0, Define.SIZEY, Main.COLOR_WHITE);
	}
	
	private static int getPerformanceOpt(){
		int numOpt = 3;
		int spaceY = Define.SIZEY - PERF_BUTTON_H;
		int secH = spaceY / numOpt;
		if(MultiTouchHandler2.touchAction[0] == MultiTouchHandler2.ACTION_DOWN && MultiTouchHandler2.touchFrames[0] == 1){
			for(int i = 0; i < numOpt; i++){
				if(UserInput.compareTouch(0, i * secH, Define.SIZEX, (i + 1) * secH, 0)){
					return i;
				}
			}
		}
		return -1;
	}
	
	
}
