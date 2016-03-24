package com.luis.projectlgameengineimp;


import com.luis.lgameengine.gameutils.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * 
 * @author Luis Valdes Frances
 */

public class MainActivity extends Activity{
	
	Thread vGameThread;
	public static AssetManager vAssets;

	public static Main vMain;
	
	Thread ms_RenderView;
	
	
	
	//Screen do not sleep:
	public static PowerManager ms_PowerManager;
    public static PowerManager.WakeLock ms_WakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Log.i("INFO", "BETA_ENGINE START");
		Settings.getInstance().init(this);
		vAssets = getAssets();
		vMain = new Main(this);
		
		
		RelativeLayout layout = new RelativeLayout(this);

			// Rescale surface view to layout size:
			DisplayMetrics dm = new DisplayMetrics();

			getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			int real_width = dm.widthPixels;
			int real_height = dm.heightPixels;

			if (Settings.getInstance().getScreenOrientation() == Settings.ORIENTATION_LANDSCAPE) {
				real_width = dm.widthPixels > dm.heightPixels ? dm.widthPixels:dm.heightPixels;
				real_height = dm.heightPixels > dm.widthPixels ? dm.widthPixels:dm.heightPixels;
			} else if (Settings.getInstance().getScreenOrientation() == Settings.ORIENTATION_PORTRAIT) {
				real_width = dm.widthPixels > dm.heightPixels ? dm.heightPixels:dm.widthPixels;
				real_height = dm.heightPixels > dm.widthPixels ? dm.heightPixels:dm.widthPixels;
			} else {
				real_width = dm.widthPixels;
				real_height = dm.heightPixels;
			}

			layout.addView(vMain, real_width, real_height);
		
		//setContentView(vMain);
		setContentView(layout);
		ms_RenderView = new Thread(new Main(this));
		
		
		//Init engine
		Log.i("INFO", "Init engine");
		vGameThread = new Thread(vMain);
		vGameThread.start();
		
		
		
		
		
		//Screen not sleep:
		ms_PowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		ms_WakeLock = ms_PowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		vMain.unPause();
		if(ms_WakeLock != null)
			ms_WakeLock.acquire();
	}
	
	@Override
	public void onRestart(){
		super.onRestart();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		vMain.pause();
		if(ms_WakeLock != null)
			ms_WakeLock.release();
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		try {
			vGameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i("INFO", "Thread stoped with exit!");
	}
	
	
	
	//Dialogo de confirmación que captura el boton "atrás":
//		public boolean onKeyDown(int keyCode, KeyEvent event) {
//			return true;
//		}
	}


