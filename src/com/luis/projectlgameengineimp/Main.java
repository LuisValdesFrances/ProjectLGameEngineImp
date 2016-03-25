package com.luis.projectlgameengineimp;


import java.util.List;
import java.util.Random;

/**
 * 
 * @author Luis Valdes Frances
 */

import android.app.Activity;
import android.content.Context;
import android.text.method.MetaKeyKeyListener;
import android.util.Log;
import android.view.MotionEvent;

import com.luis.lgameengine.gameutils.MenuManager;
import com.luis.lgameengine.gameutils.Settings;

import com.luis.lgameengine.implementation.fileio.FileIO;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.lgameengine.implementation.graphics.MyCanvas;
import com.luis.lgameengine.implementation.input.KeyboardHandler;
import com.luis.lgameengine.implementation.input.MultiTouchHandler2;
import com.luis.lgameengine.implementation.sound.SndManager;

public class Main extends MyCanvas implements Runnable {

	public static Main vMain;
	public static boolean isTouchDevice = true;
	
	public static final boolean IS_MOVE_SOFT_BANNER = false;

	public static int iFrame;
	public static long lInitTime;
	private static int iAcumulativeTicks;
	private static long lAcumulativeTime;
	public static int iFramesXSecond;

	private static long lDeltaTime;
	public static int getDeltaMilis(){
		return (int)lDeltaTime;
	}
	public static float getDeltaSec(){
		return (float)lDeltaTime / 1000f;
		//return 0.03f;//Para debug
	}
	public static long lLastTime;

	private static final long MINIM_DURATION_FRAME = 1000 / Define.FPS;
	public static boolean isGameHeart;

	KeyboardHandler vKeyData;

	public static int iState;
	public static int iLastState;

	public static final int COLOR_BLACK = 0x00000000;
	public static final int COLOR_GREEN    = 0xff0ee50e;
	public static final int COLOR_RED      = 0xffA02020;
	public static final int COLOR_ORANGE   = 0xffff7800;
	public static final int COLOR_YELLOW   = 0xfffcff00;
	public static final int COLOR_PURPLE   = 0xffcc00ff;
	public static final int COLOR_PURPLE_GALAXY = 0xff2f0947;
    public static final int COLOR_BLUE     = 0xff202080;
	public static final int COLOR_WHITE    = 0xffffffff;
	
	public static final int COLOR_LILA_BG = 0xffe48bfe;
	public static final int COLOR_BLUE_BG = 0xff88c7fc;
	public static final int COLOR_GREEN_BG = 0xff8bfc88;
	public static final int COLOR_YELOW_BG = 0xfffcf659;

	public static final boolean IS_DEBUG = true;
	public static final boolean IS_INPUT_DEBUG = false;
	public static final boolean IS_GAME_DEBUG = false;

	// Nombre del fichero donde se guardaran y cargaran los datos:
	public static final String DATA_NAME = "angryufodata";
	public static int[] iDataList;
	public static final int INDEX_DATA_LANGUAGE = 0;
	public static final int INDEX_DATA_RECORD = 1;

	public static int iLanguage;

	public Main(Activity _vActivity) {
		super(_vActivity, Define.SIZEX, Define.SIZEY);
		vMain = this;

		// if(Integer.parseInt(VERSION.SDK) < 5)
		// touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
		// else
		// touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
		MultiTouchHandler2.getInstance(this, Settings.getInstance().getScaleX(), Settings.getInstance().getScaleY());
		SndManager.inicialize(_vActivity);
		isGameHeart = true;
	}

	private void initGame() {
		Log.i("INFO", "initMain run");
		// FileIO.loadData(DATA_NAME, Main.Context);
		// if(FileIO.isData()){
		// iDataList = FileIO.getIntData();
		// Log.i("LOGCAT", "Existen "+iDataList.length + " datos");
		// iLanguage = Main.iDataList[0];
		// ModeGame.iRecordPoints = Main.iDataList[1];
		// for(int i = 0; i < iDataList.length; i++){
		// Log.i("LOGCAT", "genLoad: "+ Main.iDataList[i]);
		// }
		// }else{
		// Log.i("LOGCAT", "No existen datos guardados");
		// Main.iDataList = new int[INDEX_DATA_RECORD+1];
		// iLanguage = 0;
		// ModeGame.iRecordPoints = 0;
		// }
		changeState(Define.ST_MENU_LOGO,true);
	}

	
	
	@Override
	public void run() {

		Log.i("INFO", "Game thread execute");
		initGame();

		while (isGameHeart) {
			//if (!isPause) 
			{
				// Log.i("INFO", "FramesXSecond: " + iFramesXSecond);

				lDeltaTime = System.currentTimeMillis() - lLastTime;
				lLastTime = System.currentTimeMillis();

				switch (iState) {
					 case Define.ST_MENU_LOGO:
			         case Define.ST_MENU_ASK_LANGUAGE:
			         case Define.ST_MENU_ASK_SOUND:
			         case Define.ST_MENU_MAIN:
			         case Define.ST_MENU_OPTIONS:
			         case Define.ST_MENU_MORE:
			         case Define.ST_MENU_EXIT:
			         case Define.ST_MENU_HELP:
			         case Define.ST_MENU_ABOUT:
			             if (!isLoading) {
			               ModeMenu.update();
			            }
			            break;
			         
			         case Define.ST_GAME_INIT:
			         case Define.ST_GAME_RUN:
			         case Define.ST_GAME_PERFORMANCE_OPTIONS:
			             if (!isLoading) {
			               ModeGame.update(iState);
			            }
			            break;
		         }

				repaint();

				while (System.currentTimeMillis() - lInitTime < MINIM_DURATION_FRAME)
					Thread.yield();

				// New loop:
				lAcumulativeTime += (System.currentTimeMillis() - lInitTime);
				if (lAcumulativeTime < 1000) {
					iAcumulativeTicks++;
				} else {
					iFramesXSecond = iAcumulativeTicks;
					lAcumulativeTime = 0;
					iAcumulativeTicks = 0;
				}

				lInitTime = System.currentTimeMillis();
				iFrame++;

				/*
				 * if(!vHolder.getSurface().isValid()) continue;
				 * paint(vGraphics); Canvas canvas = vHolder.lockCanvas();
				 * canvas.getClipBounds(vDstRect);//Obtiene la totalidad de la
				 * pantalla. canvas.drawBitmap(vGraphics.mFrameBuffer, null,
				 * vDstRect, null); vHolder.unlockCanvasAndPost(canvas);
				 */
			}
		}
		stop();
	}

	@Override
	protected void paint(Graphics _g) {
		if (!isClock) {
			switch (iState) {
				 case Define.ST_MENU_LOGO:
		         case Define.ST_MENU_ASK_LANGUAGE:
		         case Define.ST_MENU_ASK_SOUND:
		         case Define.ST_MENU_MAIN:
		         case Define.ST_MENU_OPTIONS:
		         case Define.ST_MENU_MORE:
		         case Define.ST_MENU_EXIT:
		         case Define.ST_MENU_HELP:
		         case Define.ST_MENU_ABOUT:
					ModeMenu.draw(_g);
					break;
		         case Define.ST_GAME_INIT:
		         case Define.ST_GAME_RUN:
		         case Define.ST_GAME_PERFORMANCE_OPTIONS:
		        	 ModeGame.draw(_g, iState);
					break;
			}
			
			if (Main.IS_DEBUG) {
				_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
				_g.setTextSize((int) (24 * Settings.getInstance().getScale()));
				_g.setAlpha(160);
				_g.setColor(0x88000000);
				_g.fillRect(0, 0, Define.SIZEX, _g.getTextHeight() * 6);
				_g.setAlpha(255);
				_g.drawText("LGameEngine version: : " + Settings.LGAME_ENGINE_VERSION, 0, _g.getTextHeight(), COLOR_WHITE);
				_g.drawText("FramesXSecond: " + Define.MAX_FPS + "/" + iFramesXSecond, 0, _g.getTextHeight() * 2, COLOR_WHITE);
				_g.drawText("DeltaTime: " + lDeltaTime, Define.SIZEX2, _g.getTextHeight() * 2, COLOR_WHITE);
				_g.drawText("SizeX: " + Define.SIZEX, 0, _g.getTextHeight() * 3,COLOR_WHITE);
				_g.drawText("SizeY: " + Define.SIZEY, Define.SIZEX2, _g.getTextHeight() * 3, COLOR_WHITE);
				_g.drawText("RealW: " + Settings.getInstance().getRealWidth(), 0, _g.getTextHeight() * 4, COLOR_WHITE);
				_g.drawText("RealH: " + Settings.getInstance().getRealHeight(), Define.SIZEX2, _g.getTextHeight() * 4, COLOR_WHITE);
				
				_g.setColor(Main.COLOR_GREEN);
				_g.fillRect(0, 0, Define.SCR_MIDLE/64, Define.SCR_MIDLE/64);
				_g.fillRect(0, Define.SIZEY-Define.SCR_MIDLE/64, Define.SCR_MIDLE/64, Define.SCR_MIDLE/64);
				_g.fillRect(Define.SIZEX-Define.SCR_MIDLE/64, 0, Define.SCR_MIDLE/64, Define.SCR_MIDLE/64);
				_g.fillRect(Define.SIZEX-Define.SCR_MIDLE/64, Define.SIZEY-Define.SCR_MIDLE/64, Define.SCR_MIDLE/64, Define.SCR_MIDLE/64);
				
			}else if (Main.IS_INPUT_DEBUG){
				_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
				_g.setTextSize((int) (24 * Settings.getInstance().getScale()));
				_g.setAlpha(160);
				_g.setColor(0x88000000);
				_g.fillRect(0, 0, Define.SIZEX, _g.getTextHeight() * 10);
				_g.setAlpha(255);
				_g.drawText("TouchAction: " + MultiTouchHandler2.touchAction[0], 0, _g.getTextHeight(),COLOR_WHITE);
				_g.drawText("Orin_X: " + MultiTouchHandler2.touchOriginX[0], 0, _g.getTextHeight()*2,COLOR_WHITE);
				_g.drawText("Orin_Y: " + MultiTouchHandler2.touchOriginY[0], Define.SIZEX2,_g.getTextHeight()*2, COLOR_WHITE);
				_g.drawText("Current_X: " + MultiTouchHandler2.touchX[0], 0, _g.getTextHeight() * 3, COLOR_WHITE);
				_g.drawText("Current_Y: " + MultiTouchHandler2.touchY[0], Define.SIZEX2,_g.getTextHeight() * 3, COLOR_WHITE);
				_g.drawText("Dist_X: " + MultiTouchHandler2.touchDistanceX[0], 0, _g.getTextHeight() * 4, COLOR_WHITE);
				_g.drawText("Dist_Y: " + MultiTouchHandler2.touchDistanceY[0], Define.SIZEX2,_g.getTextHeight() * 4, COLOR_WHITE);
				_g.drawText("Pointer 0: " + MultiTouchHandler2.touchAction[0], 0, _g.getTextHeight() * 5, COLOR_WHITE);
				_g.drawText("Pointer 1: " + MultiTouchHandler2.touchAction[1], Define.SIZEX2,_g.getTextHeight() * 5, COLOR_WHITE);
				_g.drawText("Pointer 2: " + MultiTouchHandler2.touchAction[2], 0, _g.getTextHeight() * 6, COLOR_WHITE);
				_g.drawText("Pointer 3: " + MultiTouchHandler2.touchAction[3], Define.SIZEX2,_g.getTextHeight() * 6, COLOR_WHITE);
				_g.drawText("Pointer 4: " + MultiTouchHandler2.touchAction[4], 0, _g.getTextHeight() * 7, COLOR_WHITE);

			}
			_g.setAlpha(255);
		} else {
			drawClock(_g);
		}
	}
	
	public static final int SOFT_OK = 0;
	public static final int SOFT_BACK = 1;

	public static void drawSoftkey(Graphics g, int softkeys, boolean blink) {
		
		int upBanner = IS_MOVE_SOFT_BANNER ? (GfxManager.vImgSoftkeys.getHeight() >> 1) : 0;

		boolean paint = true;
		if (blink) {
			paint = isIntervalTwo();
		}

		if (paint) {
			switch (softkeys) {
			case SOFT_BACK:
				g.setClip(
						Define.SIZEX - (GfxManager.vImgSoftkeys.getWidth() >> 1),
						Define.SIZEY - (GfxManager.vImgSoftkeys.getHeight() >> 1) - upBanner,
						Define.SIZEX, Define.SIZEY - upBanner);

				if (UserInput.isTouchSoftRight(0,0)) {
					g.drawImage(GfxManager.vImgSoftkeys,
							Define.SIZEX- (GfxManager.vImgSoftkeys.getWidth()),
							Define.SIZEY- (GfxManager.vImgSoftkeys.getHeight() >> 1) - upBanner, 0);
				} else {
					g.drawImage(GfxManager.vImgSoftkeys,Define.SIZEX- (GfxManager.vImgSoftkeys.getWidth() >> 1),
							Define.SIZEY- (GfxManager.vImgSoftkeys.getHeight() >> 1) - upBanner, 0);
				}
				break;

			case SOFT_OK:
				g.setClip(
						0,Define.SIZEY- (GfxManager.vImgSoftkeys.getHeight() >> 1) - upBanner,
						GfxManager.vImgSoftkeys.getWidth() >> 1, Define.SIZEY - upBanner);

				if (UserInput.isTouchSoftLeft(0,0)) {
					g.drawImage(GfxManager.vImgSoftkeys,-(GfxManager.vImgSoftkeys.getWidth() >> 1),
							Define.SIZEY- GfxManager.vImgSoftkeys.getHeight() - upBanner, 0);
				} else {
					g.drawImage(GfxManager.vImgSoftkeys,0,Define.SIZEY- GfxManager.vImgSoftkeys.getHeight() - upBanner, 0);
				}
				break;
			}
		}
		
	}

	 //Resources:
    private static Random vRandom;// = new Random(0);
    //Obtiene un randon entre el primer parametro(Numero menor) y el segundo parametro(numero mayor). 
    //Ambos incluidos.
    public static int getRandom(int _i0, int _i1) {
        if(vRandom == null) vRandom = new Random();
        return _i0 + Math.abs(vRandom.nextInt() % (1 + _i1 - _i0));
    }
    
    public static int getRandom(int _iNumber) {
        if(vRandom == null) vRandom = new Random();
        if (_iNumber < 0) {
            return (vRandom.nextInt() % -_iNumber);
        }
        try {
            return Math.abs(vRandom.nextInt()) % _iNumber;
        } catch (Exception e) {
            e.printStackTrace();
           return 0;
        }
    }
    
    public static boolean isModule(int number) {
        if ((number % 2) == 1) {
            return false;//impar
        } else {
            return true;//par
        }
    }
    
    public static boolean isIntervalTwo() {
        if (iFrame % (50 / Define.FRAME_SPEED_DEC) < 5 || (iFrame % (50 / Define.FRAME_SPEED_DEC) > 10
                && iFrame % (50 / Define.FRAME_SPEED_DEC) < 15)) {
            return false;
        } else {
            return true;
        }
    }
    
    public static boolean isDispareCount(long _lDeltaTime, long _lCurrentCount, long _lTotalCount){
        if((_lCurrentCount%_lTotalCount < _lTotalCount) && ((_lCurrentCount + _lDeltaTime)%_lTotalCount < _lCurrentCount%_lTotalCount) ){
            return true;
        }else{
            return false;
        }
        
    }

	public void pause() {
		//MyCanvas.isPause = true;
//		if (iState >= Define.ST_GAME_INIT)
//			Main.changeState(Define.ST_GAME_PAUSE,false);

		SndManager.pauseMusic();
		Log.i("INFO", "Llamada a pause()");
	}

	public void unPause() {
		//if (MyCanvas.isPause) 
		{
			//MyCanvas.isPause = false;
			SndManager.unpauseMusic();
			Log.i("INFO", "Llamada a unPause()");
		}
	}

	public void stop() {
		SndManager.stopMusic();
		SndManager.flushSndManager();
	}

	public static boolean isLoading;
	public static void changeState(int _iNewState, boolean _isLoadGraphics) {
		isLoading = true;
		
		MultiTouchHandler2.resetTouch();
		UserInput.resetKeys();

		iLastState = iState;
		iState = _iNewState;
		ModeMenu.iOptionSelect = 0;
		
		if(_isLoadGraphics){
			vMain.startClock();
			GfxManager.loadGFX(_iNewState);
		}

		Log.i("INFO", "Estado cambiado a: " + _iNewState);

		if (_iNewState < Define.ST_GAME_INIT)
			ModeMenu.init(iState);
		else
			 ModeGame.init(iState);
		
		vMain.stopClock();
		isLoading = false;

	}

	/*
	 * @Override public boolean onTouch(View _v, MotionEvent _event) { // switch
	 * (_event.getAction()) { // case MotionEvent.ACTION_DOWN: // case
	 * MotionEvent.ACTION_MOVE: // iTouchX = (int)(_event.getX() *
	 * Settings.getScaleX()); // iTouchY = (int)(_event.getX() *
	 * Settings.getScaleY()); // break; // case MotionEvent.ACTION_UP: //
	 * iTouchX = 0; // iTouchY = 0; // break; // } iTouchX =
	 * (int)(_event.getX()); iTouchY = (int)(_event.getX()); return true; }
	 */
	
	
	// Show clock variables:
    private static final int SPEED_TIME_ANIMATION = 200;
	private static final int FRAMES = 4;
	private static long lClCurrentTime;
	private static long lClLastCurrentTime;
	public static int iFrameClock;
	public static boolean isClock = false;
	public static Image imgClock;
	private static Thread tClockThread;
	private static final String TEXT = "Loading...";

	private void startClock() {

		System.out.println("Start clock run");
		isClock = true;
		iFrameClock = 0;
		lClCurrentTime = 0;
		tClockThread = new Thread() {

			public void run() {
				while (isClock) {
					lClLastCurrentTime = System.currentTimeMillis();
					repaint();
					updateClock();
				}
			}
		};
		tClockThread.start();
	}

	private void updateClock() {
		long time = System.currentTimeMillis();
		lClCurrentTime += time - lClLastCurrentTime;
		lClLastCurrentTime = time;

		if (lClCurrentTime > SPEED_TIME_ANIMATION) {
			lClCurrentTime = 0;
			iFrameClock = iFrameClock + 1 == FRAMES ?0:iFrameClock + 1;
			repaint();
		}
	}

	private void stopClock() {
		System.out.println("Stop clock run");
		if (isClock) {
			isClock = false;
			tClockThread = null;
		}
	}

	private void drawClock(Graphics _g) {

		if (isClock) {
			if (imgClock == null){
				try{
				imgClock = Image.createImage("/clock.png");
				}catch(Exception e){
					Log.e("error", "No se encuentra la imagen del reloj de carga");
				}
			}
			if(imgClock != null){
			_g.setColor(COLOR_BLACK);
			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
			_g.fillRect(0, Define.SIZEY2 - imgClock.getHeight(), Define.SIZEX, imgClock.getHeight());
			_g.setClip(
					Define.SIZEX2 - ((imgClock.getWidth() / FRAMES) >> 1),
					Define.SIZEY2 - imgClock.getHeight(),
					imgClock.getWidth() / FRAMES,
					imgClock.getHeight());
			_g.drawImage(
					imgClock,
					Define.SIZEX2 - ((imgClock.getWidth() / FRAMES) >> 1)
							- ((imgClock.getWidth() / FRAMES) * iFrameClock),
					Define.SIZEY2 - imgClock.getHeight(), 0);

			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
		}
		}else{
			Log.e("error", "No se existe la imagen del reloj de carga");
		}
	}
}
