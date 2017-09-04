package com.luis.projectlgameengineimp;

import com.luis.lgameengine.gameutils.Settings;
import com.luis.lgameengine.gameutils.gameworld.RigidBody;

import android.annotation.SuppressLint;
import android.util.Log;

/**
*
* @author Luis Valdes Frances
*/
@SuppressLint("NewApi")
public class Define {
	
	public static int iFrames;
    public static int setFrames() {
    	if (((Settings.getInstance().ms_iCPUMaxMhzs >= 1190 || Settings.ms_iCPUMaxMhzs == -1)
                && android.os.Build.VERSION.SDK_INT > 16)
                //&& Settings.ms_iDeviceOS == Settings.OS_ANDROID
                //&& Settings.ms_iSize > Settings.HVGA_320X480
                //&& Settings.ms_iSize < Settings.WXGA_720X1280
                ) {
        	iFrames=60;
        } else {
        	iFrames=30;
        }
        Log.i("INFO", "Frames: " + iFrames);
        return iFrames;
     }
	
	public static int FPS = setFrames();
	public static final int MAX_FPS = 60;
    public static final int MIN_FPS = 10;
    
    public static final int FRAME_SPEED_DEC = MAX_FPS / FPS;//60 fps=1,30 fps=2,15 fps=4,10 fps=6
    public static final int FRAME_SPEED_INC = ((FPS * (MAX_FPS/FPS)) / MIN_FPS);//60 fps=6,30 fps=4,15 fps=2,10 fps=1
    
    public static final int getFPSset(){
    	switch(FPS){
    	case 60:
    		return 3;
    	case 30:
    		return 2;
    	case 15:
    		return 1;
    	default:
    		return 0;
    	}
    	
    }
	
	public static final int SIZEX = (int) Settings.getInstance().getScreenWidth();
	public static final int SIZEY = (int) Settings.getInstance().getScreenHeight();

	public static final int SIZEX2 = SIZEX >> 1;
	public static final int SIZEY2 = SIZEY >> 1;
	public static final int SIZEX4 = SIZEX >> 2;
	public static final int SIZEY4 = SIZEY >> 2;
	public static final int SIZEX8 = SIZEX >> 3;
	public static final int SIZEY8 = SIZEY >> 3;
	public static final int SIZEX12 = SIZEX / 12;
	public static final int SIZEY12 = SIZEY / 12;
	public static final int SIZEX16 = SIZEX >> 4;
	public static final int SIZEY16 = SIZEY >> 4;
	public static final int SIZEX24 = SIZEX / 24;
	public static final int SIZEY24 = SIZEY / 24;
	public static final int SIZEX32 = SIZEX >> 5;
	public static final int SIZEY32 = SIZEY >> 5;
	public static final int SIZEX64 = SIZEX >> 6;
	public static final int SIZEY64 = SIZEY >> 6;

	public static final int SCR_MIDLE = (SIZEX + SIZEY) / 2;

	public static final int FX_POINT = 8;

	// Menu States:
	public static final int ST_MENU_LOGO = 0;
	public static final int ST_MENU_ASK_LANGUAGE = 1;
	public static final int ST_MENU_ASK_SOUND = 2;

	public static final int ST_MENU_MAIN = 3;
	public static final int ST_MENU_OPTIONS = 4;
	public static final int ST_MENU_MORE = 5;
	public static final int ST_MENU_EXIT = 6;
	public static final int ST_MENU_HELP = 7;
	public static final int ST_MENU_ABOUT = 8;

	// Game states:
	public static final int ST_GAME_INIT = 100;
	public static final int ST_GAME_RUN = 101;
	public static final int ST_GAME_PAUSE = 102;
	public static final int ST_GAME_PERFORMANCE_OPTIONS = 200;

	// Game constans
	public static final int ROCK_ID = 1;
	public static final int PLAYER_ID = 191;
	public static final int BADROCK_ID = 192;
	public static final int BIGROCK_RIGHT_ID = 193;
	public static final int BIGROCK_LEFT_ID = 194;

	public static final long HIT_PAUSE = 85;
	public static final float GRAVITY_FORCE = 3f;
	public static final float SPEED_BLICK = 0.08f;

	public static final float PLAYER_SPEED = 4f;
	public static final float PLAYER_WEIGHT = 4.8f;
	public static final float PLAYER_FORCE_JUMP = 9f;
	public static final float PLAYER_FORCE_JUMP_SHORT = 4.5f;
	public static final float PLAYER_FORCE_ATACK = 2f;
	public static final float PLAYER_DAMAGE = 100;
	public static final float PLAYER_SUFF_DURATION = 2.5f;

	// ANIMATION DURATION FRAME
	public static final float PLAYER_DUR_ANIM_IDLE = 0.1f;
	public static final float PLAYER_DUR_ANIM_RUN = 0.015f;
	public static final float PLAYER_DUR_ANIM_ATACK = 0.01f;
	public static final float PLAYER_DUR_ANIM_JUMP_1 = 0.04f;
	public static final float PLAYER_DUR_ANIM_JUMP_2 = 0.4f;
	public static final float PLAYER_DUR_ANIM_JUMP_3 = 0.04f;

	public static final float BADROCK_WEIGHT = 4.8f;
	public static final float BADROCK_IDLE_TIME = 2f;
	public static final float BADROCK_FORCE_LAUNCH = 10f;
	public static final int BADROCK_LIVE = 400;

	// ANIMATION DURATION FRAME
	public static final float BADROCK_DUR_ANIM_IDLE = 0.1f;
	public static final float BADROCK_DUR_ANIM_ATACK = 0.06f;
	public static final float BADROCK_DUR_ANIM_SUFF = 0.14f;

	public static final float ROCK_WEIGHT = 8f;
	public static final float ROCK_SPEED_ROTATION = 120f;
	
	public static final float BIG_ROCK_SPEED = PLAYER_SPEED*0.55f;
	public static final float BIG_ROCK_WEIGHT = 10f;
	public static final float BIG_ROCK_SPEED_ROTATION = 110f;
	public static final int[] BIG_ROCK_TILE_DESTROY = {93, 94, 95, 96, 97};
	
	//PARTICLES
	public static final float PARTICLE_ROCK_DESTROY_DURATION = 0.7f;
	public static final float PARTICLE_ROCK_DESTROY_SPEED = 8f;
	public static final float PARTICLE_ROCK_DESTROY_WEIGHT = 4f;
	
	public static final float PARTICLE_BIG_ROCK_DESTROY_DURATION = 1f;
	public static final float PARTICLE_BIG_ROCK_DESTROY_SPEED = 6f;
	public static final float PARTICLE_BIG_ROCK_DESTROY_WEIGHT = 5f;
	
	public static final float PARTICLE_ENEMY_DESTROY_DURATION = 0.75f;
	public static final float PARTICLE_ENEMY_DESTROY_SPEED = 6f;
	public static final float PARTICLE_ENEMY_DESTROY_WEIGHT = 2f;
	   
	  
	 
	

}
