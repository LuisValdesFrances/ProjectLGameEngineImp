package com.luis.projectlgameengineimp;

import java.io.IOException;

import com.luis.lgameengine.implementation.graphics.Image;

public class GfxManager {
	
	   public static Image vImgFontSmall;
	   public static Image vImgFontMedium;
	   public static Image vImgFontBig;
	   
	   //public static Image vImgLogo;
	   //public static Image vImgBackground;
	   public static Image vImgMenuButtons;
	   public static Image vImgMenuArrows;
	   public static Image vImgSoftkeys;
	   public static Image vImgPresent;
	   public static Image vImageArrowsLR;
	   public static Image vImageArrowsUD;
	   public static Image vImagePatch;
	   public static Image vImagePatch2;
	   public static Image vImagePlanetLocked;
	   //Game images:
	   public static Image vImgPause;
	   public static Image vImgGameTilesL1;
	   public static Image vImgGameTilesL2;
	   public static Image vImgGameTilesL3;
	   public static Image vImgGameBG;
	   
	   public static Image imgPlayerIdle;
	   public static Image imgPlayerRun;
	   public static Image imgPlayerAtack;
	   public static Image imgPlayerJump;
	   
	   public static Image imgEnemyTile;
	   public static Image imgBadRockIdle;
	   public static Image imgBadRockAtack;
	   public static Image imgBadRockSuff;
	   
	   public static Image imgRock;
	   public static Image imgBigRock;
	   //bg
	   public static Image[] vImgBGs;

	   public static void loadGFX(int _iNewState) {

	      switch (_iNewState) {
	          case Define.ST_MENU_LOGO:
	            try {
	            	//Load fonts
		            vImgFontSmall = Image.createImage("/font_small.png");
		            vImgFontMedium = Image.createImage("/font_medium.png");
		            vImgFontBig = Image.createImage("/font_big.png");
		               
		            //vImgLogo = Image.createImage("/4away.png");
	            } catch (IOException ex) {
	               ex.printStackTrace();
	            }
	            break;
	         case Define.ST_MENU_ASK_SOUND:
	         case Define.ST_MENU_ASK_LANGUAGE:
		        try {
		        	vImgMenuButtons = Image.createImage("/menu_buttons.png");
		            vImgMenuArrows = Image.createImage("/menu_arrows.png");
		            vImgSoftkeys = Image.createImage("/softkeys.png");
		            try{
		                //vImgBackground = Image.createImage("/bg_generic.png");
		            }catch(Exception e){}
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
		        
		        break;
		        
	         case Define.ST_GAME_INIT:
	            try {
	               vImgPause = Image.createImage("/pause.png");
	               vImgGameTilesL1 = Image.createImage("/tile_matrix_layer_1.png");
	               vImgGameTilesL2 = Image.createImage("/tile_matrix_layer_2.png");
	               vImgGameTilesL3 = Image.createImage("/tile_matrix_layer_3.png");
	               imgPlayerIdle = Image.createImage("/player_idle.png");
	               imgPlayerRun = Image.createImage("/player_run.png");
	               imgPlayerAtack = Image.createImage("/player_atack.png");
	               imgPlayerJump = Image.createImage("/player_jump.png");
	               
	               imgEnemyTile = Image.createImage("/enemy_tile.png");
	               imgBadRockIdle = Image.createImage("/badrock_idle.png");
	               imgBadRockAtack = Image.createImage("/badrock_atack.png");
	               imgBadRockSuff = Image.createImage("/badrock_suff.png");
	               
	               imgRock = Image.createImage("/rock.png");
	               imgBigRock = Image.createImage("/big_rock.png");
	               //bg
	               vImgBGs = new Image[5];
	               for(int i = 0; i < vImgBGs.length; i++){
	            	   vImgBGs[i] = Image.createImage("/bg/layer_" + (i + 1) + ".png");
	               }
	            } catch (IOException ex) {
	               ex.printStackTrace();
	            }
	            break;
	        }
	   }

	    public static void deleteGFX() {
	    	//vImgLogo = null;
	        //vImgBackground = null;
	        //Game images:
	        vImgPause = null;
	        System.gc();
	    }
    
   
    
    

}
