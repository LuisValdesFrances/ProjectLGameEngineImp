package com.luis.projectlgameengineimp;

import android.util.Log;

import com.luis.lgameengine.gameutils.MenuManager;
import com.luis.lgameengine.gameutils.Settings;
import com.luis.lgameengine.gameutils.fonts.Font;
import com.luis.lgameengine.gameutils.fonts.TextManager;
import com.luis.lgameengine.implementation.fileio.FileIO;
import com.luis.lgameengine.implementation.graphics.Graphics;
import com.luis.lgameengine.implementation.graphics.Image;
import com.luis.lgameengine.implementation.sound.SndManager;

public class ModeMenu {
	
	public static final int NUMBER_OPTS_LANGUAGE = 3;
	public static final int NUMBER_OPTS_SOUND = 2;
	public static final int NUMBER_OPTS_MAIN_MENU = 4;
	public static final int NUMBER_OPTS_MORE_MENU = 2;
	
	public static int iOptionSelect;
	public static int iLanguageSelect;
	public static int iSoundSelect;
	
	public static Image vImgPlanet;
	public static Image vImgAstheroid;
	public static Image vImgAstheroid2;
	
	
	public static void init(int _iMenuState){
		Log.i("Info", "Init State: "+ _iMenuState);
		switch (_iMenuState) {
        case Define.ST_MENU_LOGO:
			iStateLogo = ST_LOGO_1;
			iLevelAlpha = 255;
			lInitialLogoTime = System.currentTimeMillis();
			Font.init(GfxManager.vImgFontSmall, GfxManager.vImgFontMedium, GfxManager.vImgFontBig);
//			if(FileIO.isData()){
//				
//			}else{
//				
//			}
			RscManager.loadLanguage(Main.iLanguage);
			break;
		case Define.ST_MENU_ASK_SOUND:
		case Define.ST_MENU_ASK_LANGUAGE:
			MenuManager.init(
					GfxManager.vImgMenuButtons.getWidth(), GfxManager.vImgMenuButtons.getHeight()/2,
					GfxManager.vImgSoftkeys.getWidth()/2, GfxManager.vImgSoftkeys.getHeight()/2,
					GfxManager.vImgMenuArrows.getWidth()/2, GfxManager.vImgMenuArrows.getHeight());
			break;
		case Define.ST_MENU_MAIN:
			break;
		case Define. ST_MENU_OPTIONS:
			iLanguageSelect = Main.iLanguage;
			iSoundSelect = SndManager.isSound?0:1;
			break;
		case Define. ST_MENU_MORE:
		case Define. ST_MENU_EXIT:
		case Define. ST_MENU_HELP:
		case Define. ST_MENU_ABOUT:
			break;
		}
	}
	
	public static void update(){
		switch (Main.iState) {
		case Define.ST_MENU_LOGO:
			runLogo();
			break;
		case Define.ST_MENU_ASK_LANGUAGE:
			if (UserInput.getInstance().getOptionMenuTouched_X(MenuManager.BUTTON_CENTER,0)) {
				if (iOptionSelect <= 0) 
					iOptionSelect = 1;
				else 
					iOptionSelect--;
				
				//UserInput.getInstance().isKeyLeft = false;
				
			} else if (UserInput.getInstance().getOptionMenuTouched_X(MenuManager.BUTTON_CENTER,1)) {
				if (iOptionSelect >= NUMBER_OPTS_LANGUAGE-1) 
					iOptionSelect = 0;
				 else 
					 iOptionSelect++;
				
				//UserInput.getInstance().isKeyRight = false;
			} else if (UserInput.getInstance().goToSoftLeft(0,0)){
				
				/*
				if (iOptionSelect == Resources.ENGLISH){
					Log.i("LOGCAT", "Se van a salvar los datos:");
					Main.iDataList[Main.INDEX_DATA_LANGUAGE] = Resources.ENGLISH;
				}else if (iOptionSelect == Resources.SPANISH){
					Log.i("LOGCAT", "Se van a salvar los datos:");
					Main.iDataList[Main.INDEX_DATA_LANGUAGE] = Resources.SPANISH;
				}else if (iOptionSelect == Resources.CATALA){
					Log.i("LOGCAT", "Se van a salvar los datos:");
					Main.iDataList[Main.INDEX_DATA_LANGUAGE] = Resources.CATALA;
				}
				
				Main.iLanguage = Main.iDataList[Main.INDEX_DATA_LANGUAGE];
				Resources.loadLanguage(Main.iLanguage);
				Log.i("LOGCAT", "genSave: "+ Main.iDataList[Main.INDEX_DATA_LANGUAGE]);
				FileIO.saveData(Main.iDataList, Main.DATA_NAME, Main.Context);
	    		Log.i("LOGCAT", "Datos salvados");
				Main.changeState(Define.ST_MENU_ASK_SOUND);
				*/
				
				
				Main.iLanguage = iOptionSelect;
//				Resources.loadLanguage(Main.iLanguage);
//				Main.iDataList[Main.INDEX_DATA_LANGUAGE]=iOptionSelect;
//				Log.i("LOGCAT", "Save language: "+ iOptionSelect);
//				FileIO.saveData(Main.iDataList, Main.DATA_NAME, Main.Context);
//	    		Log.i("LOGCAT", "Datos salvados");
				Main.changeState(Define.ST_MENU_ASK_SOUND,false);
				
			}
			break;
		
		case Define.ST_MENU_ASK_SOUND:
			if (UserInput.getInstance().getOptionMenuTouched_X(
					MenuManager.BUTTON_CENTER,0)) {
				if (iOptionSelect <= 0) 
					iOptionSelect = 1;
				else 
					iOptionSelect--;
				
				//UserInput.getInstance().isKeyLeft = false;
				
			} else if (UserInput.getInstance().getOptionMenuTouched_X(
					MenuManager.BUTTON_CENTER,1)) {
				if (iOptionSelect >= NUMBER_OPTS_SOUND-1) 
					iOptionSelect = 0;
				 else 
					 iOptionSelect++;
				
				//UserInput.getInstance().isKeyRight = false;
			} else if (UserInput.getInstance().goToSoftLeft(0,0)) {
				
				if (iOptionSelect == 0){
					Log.i("LOGCAT", "Sonido ON");
					SndManager.isSound=true;
				}else{
					Log.i("LOGCAT", "Sonido OFF");
					SndManager.isSound=false;
				}
				
				Main.changeState(Define.ST_MENU_MAIN,false);
				SndManager.playMusic(SndManager.MUSIC_MENU, true);
				
			}
			break;
		
		case Define.ST_MENU_MAIN:
			/*
			if (UserInput.getInstance().isKeyUp) {
				if (iOptionSelect <= 0) {
					iOptionSelect = NUMBER_OPTS_MAIN_MENU - 1;
				} else {
					iOptionSelect--;
				}
				UserInput.getInstance().isKeyUp = false;
				
			} else if (UserInput.getInstance().isKeyDown) {
				if (iOptionSelect >= NUMBER_OPTS_MAIN_MENU - 1) {
					iOptionSelect = 0;
				} else {
					iOptionSelect++;
				}
				UserInput.getInstance().isKeyDown = false;
				
			}else {
			*/
				iOptionSelect = UserInput.getInstance().getOptionMenuTouched_Y(
						NUMBER_OPTS_MAIN_MENU,
						iOptionSelect);
			//}
			if (UserInput.getInstance().getOkTouched_Y(iOptionSelect)) {

				switch (iOptionSelect) {
				case 0:// Jugar
					Main.changeState(Define.ST_GAME_INIT,true);
					break;
				case 1:// Opciones
					Main.changeState(Define.ST_MENU_OPTIONS,false);
					break;
				case 2:// More
					Main.changeState(Define.ST_MENU_MORE,false);
					break;
				case 3://Exit
					Main.changeState(Define.ST_MENU_EXIT,false);
					break;
				}
			}
			break;
			
		case Define.ST_MENU_MORE:
			/*
			if (UserInput.getInstance().isKeyUp) {
				if (iOptionSelect <= 0) {
					iOptionSelect = NUMBER_OPTS_MORE_MENU - 1;
				} else {
					iOptionSelect--;
				}
				//UserInput.getInstance().isKeyUp = false;
				
			} else if (UserInput.getInstance().isKeyDown) {
				if (iOptionSelect >= NUMBER_OPTS_MORE_MENU - 1) {
					iOptionSelect = 0;
				} else {
					iOptionSelect++;
				}
				//UserInput.getInstance().isKeyDown = false;
				
			}else {
			*/
				iOptionSelect = UserInput.getInstance().getOptionMenuTouched_Y(
						NUMBER_OPTS_MORE_MENU, iOptionSelect);
			//}
			if (UserInput.getInstance().getOkTouched_Y(iOptionSelect)) {

				switch (iOptionSelect) {
				case 0:
					Main.changeState(Define.ST_MENU_HELP,false);
					break;
				case 1:
					Main.changeState(Define.ST_MENU_ABOUT,false);
					break;
				}
			}else if (UserInput.getInstance().goToSoftRight(0,0)) {
				Main.changeState(Define.ST_MENU_MAIN,false);
			}
			break;
			
		case Define.ST_MENU_OPTIONS:
			//Language
			if (UserInput.getInstance().getOptionMenuTouched_X(
							MenuManager.BUTTON_UP,0)) {
				if (iLanguageSelect <= 0) 
					iLanguageSelect = NUMBER_OPTS_LANGUAGE -1;
				else 
					iLanguageSelect--;
				
				RscManager.loadLanguage(iLanguageSelect);
				
				//UserInput.getInstance().isKeyLeft = false;
				
			} else if (UserInput.getInstance().getOptionMenuTouched_X(
							MenuManager.BUTTON_UP,1)) {
				if (iLanguageSelect >= NUMBER_OPTS_LANGUAGE -1) 
					iLanguageSelect = 0;
				 else 
					iLanguageSelect++;
				
				RscManager.loadLanguage(iLanguageSelect);
				
				//UserInput.getInstance().isKeyRight = false;
				}
			
			//Sound
			if (UserInput.getInstance().getOptionMenuTouched_X(
							MenuManager.BUTTON_CENTER,0)) {
				if (iSoundSelect <= 0) 
					iSoundSelect = NUMBER_OPTS_SOUND -1;
				else 
					iSoundSelect--;
				
				if (iSoundSelect == 0){
					SndManager.isSound = true;
					SndManager.playMusic(SndManager.MUSIC_MENU, true);
				}else{
					SndManager.stopMusic();
					SndManager.isSound = false;
				}
				
				//UserInput.getInstance().isKeyLeft = false;
				
			} else if (UserInput.getInstance().getOptionMenuTouched_X(
							MenuManager.BUTTON_CENTER,1)) {
				if (iSoundSelect >= NUMBER_OPTS_SOUND -1) 
					iSoundSelect = 0;
				 else 
					 iSoundSelect++;
				
				if (iSoundSelect == 0){
					SndManager.isSound = true;
					SndManager.playMusic(SndManager.MUSIC_MENU, true);
				}else{
					SndManager.stopMusic();
					SndManager.isSound = false;
				}
				//UserInput.getInstance().isKeyRight = false;
			}
			if (UserInput.getInstance().goToSoftLeft(0,0)) {
				Main.iLanguage = iLanguageSelect;
//				//saveSystemData();
				Main.changeState(Define.ST_MENU_MAIN,false);
			}
			break;
		case Define.ST_MENU_HELP:
		case Define.ST_MENU_ABOUT:
			if (UserInput.getInstance().goToSoftRight(0,0)) {
				Main.changeState(Define.ST_MENU_MORE,false);
			}
			break;
			
		case Define.ST_MENU_EXIT:
        	
        	if (UserInput.getInstance().getOptionMenuTouched_X(
					MenuManager.BUTTON_CENTER,0)) {
				if (iOptionSelect <= 0) 
					iOptionSelect = 1;
				else 
					iOptionSelect--;
				
				//UserInput.getInstance().isKeyLeft = false;
				
			} else if (UserInput.getInstance().getOptionMenuTouched_X(
					MenuManager.BUTTON_CENTER,1)) {
				if (iOptionSelect >= 1) 
					iOptionSelect = 0;
				 else 
					 iOptionSelect++;
				
				//UserInput.getInstance().isKeyRight = false;
			} else if (UserInput.getInstance().goToSoftLeft(0,0)
					) {
				
			if (iOptionSelect == 0){
				Main.changeState(Define.ST_MENU_MAIN,false);
			}else{
				Main.isGameHeart=false;
			}
			}
        	break;
		}
	}
	
	public static void draw(Graphics _g){
		switch (Main.iState) {
		case Define.ST_MENU_LOGO:
			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
			_g.setColor(Main.COLOR_GREEN);
			_g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
			//_g.drawImage(GfxManager.vImgLogo, Define.SIZEX2, Define.SIZEY2, Graphics.VCENTER|Graphics.HCENTER);
			//_g.setAlpha(255);
             
			break;
		case Define.ST_MENU_ASK_LANGUAGE:
			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
			//_g.drawImage(GfxManager.vImgBackground, 0, 0, 0);
			_g.setColor(Main.COLOR_BLACK);
			_g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
			MenuManager.drawButtonsAndTextX(_g, MenuManager.BUTTON_CENTER,NUMBER_OPTS_LANGUAGE, RscManager.TXT_ENGLISH, 
					RscManager.sAllTexts, Font.FONT_BIG,
					iOptionSelect, GfxManager.vImgSoftkeys,GfxManager.vImgMenuButtons, GfxManager.vImgMenuArrows, Main.iFrame);
			Main.drawSoftkey(_g, Main.SOFT_OK, false);
			break;
			
		case Define.ST_MENU_ASK_SOUND:
			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
			//_g.drawImage(GfxManager.vImgBackground, 0, 0, 0);
			_g.setColor(Main.COLOR_BLACK);
			_g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
			MenuManager.drawButtonsAndTextX(_g, MenuManager.BUTTON_CENTER,NUMBER_OPTS_SOUND, RscManager.TXT_SOUND_ON, 
					RscManager.sAllTexts, Font.FONT_BIG,iOptionSelect, 
					GfxManager.vImgSoftkeys,GfxManager.vImgMenuButtons, GfxManager.vImgMenuArrows, Main.iFrame);
			Main.drawSoftkey(_g, Main.SOFT_OK, false);
			break;
		
		case Define.ST_MENU_MAIN:
			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
			//_g.drawImage(GfxManager.vImgBackground, 0, 0, 0);
			_g.setColor(Main.COLOR_RED);
			_g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
			MenuManager.drawButtonsAndTextY(_g,NUMBER_OPTS_MAIN_MENU, RscManager.TXT_PLAY, RscManager.sAllTexts,
				    Font.FONT_BIG, iOptionSelect, null, GfxManager.vImgMenuButtons, Main.iFrame);
			break;
			
		case Define.ST_MENU_OPTIONS:
			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
			//_g.drawImage(GfxManager.vImgBackground, 0, 0, 0);
			_g.setColor(Main.COLOR_GREEN);
			_g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
			
			MenuManager.drawButtonsAndTextX(_g, MenuManager.BUTTON_UP,NUMBER_OPTS_LANGUAGE, RscManager.TXT_ENGLISH, 
					RscManager.sAllTexts, Font.FONT_BIG, 
					iLanguageSelect, 
					GfxManager.vImgSoftkeys,GfxManager.vImgMenuButtons, GfxManager.vImgMenuArrows, Main.iFrame);
			MenuManager.drawButtonsAndTextX(_g, MenuManager.BUTTON_CENTER,NUMBER_OPTS_SOUND, RscManager.TXT_SOUND_ON, 
					RscManager.sAllTexts, Font.FONT_BIG,
					iSoundSelect, 
					GfxManager.vImgSoftkeys,GfxManager.vImgMenuButtons, GfxManager.vImgMenuArrows, Main.iFrame);
			Main.drawSoftkey(_g, Main.SOFT_OK, false);
			break;
			
		case Define.ST_MENU_MORE:
			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
			//_g.drawImage(GfxManager.vImgBackground, 0, 0, 0);
			_g.setColor(Main.COLOR_BLACK);
			_g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
			MenuManager.drawButtonsAndTextY(_g,NUMBER_OPTS_MORE_MENU, RscManager.TXT_HELP, RscManager.sAllTexts, Font.FONT_BIG, 
					iOptionSelect, null, GfxManager.vImgMenuButtons, Main.iFrame);
			
			Main.drawSoftkey(_g, Main.SOFT_BACK, false);
			break;
			
		case Define.ST_MENU_HELP:
		case Define.ST_MENU_ABOUT:
			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
			//_g.drawImage(GfxManager.vImgBackground, 0, 0, 0);
			_g.setColor(Main.COLOR_BLACK);
			_g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
			
			_g.setAlpha(120);
			_g.setColor(Main.COLOR_LILA_BG);
			_g.fillRect(Define.SCR_MIDLE/64, Define.SCR_MIDLE/64, 
					Define.SIZEX - Define.SCR_MIDLE/32, Define.SIZEY - Define.SCR_MIDLE/32);
			_g.setAlpha(255);
       	 
			TextManager.draw(_g, Font.FONT_MEDIUM, 
					RscManager.sAllTexts[Main.iState==Define.ST_MENU_HELP? RscManager.TXT_HELP_DESCRIP:RscManager.TXT_ABOUT_DESCRIP], 
        			Define.SIZEX2, Define.SIZEY2, Define.SIZEX - Define.SIZEX32, TextManager.ALING_CENTER, -1);
			
			Main.drawSoftkey(_g, Main.SOFT_BACK, false);
			
			break;
			
		case Define.ST_MENU_EXIT:
			_g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
			//_g.drawImage(GfxManager.vImgBackground, 0, 0, 0);
			_g.setColor(Main.COLOR_BLACK);
			_g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
        	
        	MenuManager.drawButtonsAndTextX(_g, MenuManager.BUTTON_CENTER,2, RscManager.TXT_NO, 
        			RscManager.sAllTexts, Font.FONT_BIG,
					iOptionSelect, 
					GfxManager.vImgSoftkeys,GfxManager.vImgMenuButtons, GfxManager.vImgMenuArrows, Main.iFrame);
        	
        	_g.setAlpha(120);
        	_g.setColor(Main.COLOR_LILA_BG);
        	_g.fillRect(Define.SIZEX2 - (((RscManager.sAllTexts[RscManager.TXT_RETURN_MENU]).length()*Font.getFontWidth(Font.FONT_BIG))>>1), 
        			    (Font.getFontHeight(Font.FONT_BIG)<<2) - (Font.getFontHeight(Font.FONT_MEDIUM)),
                 	    (((RscManager.sAllTexts[RscManager.TXT_RETURN_MENU]).length()*Font.getFontWidth(Font.FONT_BIG))>>1),
                 	     (Font.getFontHeight(Font.FONT_MEDIUM)));
        	_g.setAlpha(255);
        	
        	TextManager.draw(_g, Font.FONT_MEDIUM, RscManager.sAllTexts[RscManager.TXT_WANT_EXIT_GAME], 
        			Define.SIZEX2,(Font.getFontHeight(Font.FONT_BIG)<<2),Define.SIZEX, TextManager.ALING_CENTER, -1);
        	Main.drawSoftkey(_g, Main.SOFT_OK, false);
        	break;
		}
	}
	
	public static void drawBackground(Graphics _g, Image _vGB) {
        if (_vGB != null) {
            _g.drawImage(_vGB, 0, 0, 0);
        } else {
            gradientBackground(_g, Main.COLOR_BLUE_BG, Main.COLOR_BLACK, 0, 0, Define.SIZEX, Define.SIZEY, 32);
        }
    }
	
	public static int[] iColor = new int[3];
    public static boolean isVibrate;

    public static void gradientBackground(Graphics g, int color1, int color2, int x, int y, int width, int height, int steps) {

        int stepSize = height / steps;

        int color1RGB[] = new int[]{(color1 >> 16) & 0xff, (color1 >> 8) & 0xff, color1 & 0xff};
        int color2RGB[] = new int[]{(color2 >> 16) & 0xff, (color2 >> 8) & 0xff, color2 & 0xff};

        int colorCalc[] = new int[]{
            ((color2RGB[0] - color1RGB[0]) << 16) / steps,
            ((color2RGB[1] - color1RGB[1]) << 16) / steps,
            ((color2RGB[2] - color1RGB[2]) << 16) / steps
        };

        for (int i = 0; i < steps; i++) {
            g.setColor(color1RGB[0] + ((i * colorCalc[0] >> 16)) << 16
                    | color1RGB[1] + ((i * colorCalc[1] >> 16)) << 8
                    | color1RGB[2] + ((i * colorCalc[2] >> 16)));
            if (i != steps - 1) {
                g.fillRect(x, y + i * stepSize, width, stepSize);
            } else {
                g.fillRect(x, y + i * stepSize, width, stepSize + 30); //+20 corrects presicion los due to divisions
            }
        }
    }
	
	public static long lInitialLogoTime;
	public static final long ST_TIME_LOGO_1 = 10;//2000;
	public static final long ST_TIME_LOGO_2 = 10;//1000;
	public static final long ST_TIME_LOGO_3 = 10;//2000;
	public static int iStateLogo;
	public static final int ST_LOGO_1 = 0;
	public static final int ST_LOGO_2 = 2;
	public static final int ST_LOGO_3 = 3;
	public static int iLevelAlpha;
	
	public static void runLogo(){
		switch(iStateLogo){
		case ST_LOGO_1:
			iLevelAlpha = (int)(((System.currentTimeMillis() - lInitialLogoTime)*255)/ST_TIME_LOGO_1);
			if(iLevelAlpha >= 255){
				iLevelAlpha = 255;
				iStateLogo = ST_LOGO_2;
				lInitialLogoTime = System.currentTimeMillis();
			}
			break;
		case ST_LOGO_2:
			if(System.currentTimeMillis()>lInitialLogoTime+ST_TIME_LOGO_2){
				iStateLogo = ST_LOGO_3;
				lInitialLogoTime = System.currentTimeMillis();
				
			}
			break;
		case ST_LOGO_3:
			iLevelAlpha = 255- (int)(((System.currentTimeMillis() - lInitialLogoTime)*255)/ST_TIME_LOGO_3);
			if(iLevelAlpha <= 0){
				iLevelAlpha = 255;
				if(FileIO.isData()) Main.changeState(Define.ST_MENU_ASK_SOUND,true);
				else Main.changeState(Define.ST_MENU_ASK_LANGUAGE,true);
			}
			
			break;
		}
		//Log.i("Info", "iLevelAlpha: "+iLevelAlpha);
	}
	
	public static void saveSystemData(){
		Main.iDataList[Main.INDEX_DATA_LANGUAGE] = Main.iLanguage;
		Log.i("LOGCAT", "Save: "+ Main.iDataList[Main.INDEX_DATA_LANGUAGE]);
		FileIO.saveData(Main.iDataList, Main.DATA_NAME, Settings.getInstance().getActiviy());
		Log.i("LOGCAT", "Datos salvados");
	}
	}
