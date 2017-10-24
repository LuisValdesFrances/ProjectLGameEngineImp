package com.luis.projectlgameengineimp;

import java.io.IOException;

import com.luis.lgameengine.implementation.graphics.Image;

public class GfxManager {
	
	   public static Image vImgFontSmall;
	   public static Image vImgFontMedium;
	   public static Image vImgFontBig;
	   
	   public static Image vImgLogo;
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
	   
	   public static Image imgMenuBox;
	   public static Image imgButtonRelease;
	   public static Image imgButtonFocus;
	   
	   //Game images:
	   public static Image imgButtonPauseRelease;
	   public static Image imgButtonPauseFocus;
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
	   
	   public static Image imgBadStickIdle;
	   public static Image imgBadStickRun;
	   public static Image imgBadStickAtack;
	   public static Image imgBadStickSuff;
	   
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
		               
		            vImgLogo = Image.createImage("/4away.png");
		            
		            imgMenuBox = Image.createImage("/menu_box.png");
		            imgButtonRelease = Image.createImage("/button_release.png");
		            imgButtonFocus = Image.createImage("/button_focus.png");
		            
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
		       
		     //Test
	         case Define.ST_MENU_SELECT_GAME:
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
	            	
	            	switch(GameState.getInstance().getLevel()){
	            	case 0:
	            		vImgGameTilesL1 = Image.createImage("/tile_matrix_layer_1.png");
						vImgGameTilesL2 = Image.createImage("/tile_matrix_layer_2.png");
						vImgGameTilesL3 = Image.createImage("/tile_matrix_layer_3.png");
						
						//bg
			            vImgBGs = new Image[5];
			            for(int i = 0; i < vImgBGs.length; i++){
			            	vImgBGs[i] = Image.createImage("/bg/layer_" + (i + 1) + ".png");
			        	}
			            
	            		break;
	            	case 1:
	            		vImgGameTilesL1 = Image.createImage("/tile_matrix_layer_1_2.png");
						vImgGameTilesL2 = Image.createImage("/tile_matrix_layer_2.png");
						vImgGameTilesL3 = Image.createImage("/tile_matrix_layer_3.png");
	            		break;
	            		
	            	}
	            	
	            	imgButtonPauseRelease = Image.createImage("/pause_release.png");
	            	imgButtonPauseFocus = Image.createImage("/pause_focus.png");
	            	imgPlayerIdle = Image.createImage("/player_idle.png");
	            	imgPlayerRun = Image.createImage("/player_run.png");
	            	imgPlayerAtack = Image.createImage("/player_atack.png");
	            	imgPlayerJump = Image.createImage("/player_jump.png");
	               
	               imgEnemyTile = Image.createImage("/enemy_tile.png");
	               imgBadRockIdle = Image.createImage("/badrock_idle.png");
	               imgBadRockAtack = Image.createImage("/badrock_atack.png");
	               imgBadRockSuff = Image.createImage("/badrock_suff.png");
	               
	               imgBadStickIdle = Image.createImage("/badstick_idle.png");
	               imgBadStickAtack = Image.createImage("/badstick_atack.png");
	               imgBadStickRun = Image.createImage("/badstick_run.png");
	               //imgBadStickSuff = Image.createImage("/badrock_suff.png");
	               
	               imgRock = Image.createImage("/rock.png");
	               imgBigRock = Image.createImage("/big_rock.png");
	               
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
	        System.gc();
	    }
    
   
    
    

}
