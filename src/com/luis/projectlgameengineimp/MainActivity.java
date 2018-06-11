package com.luis.projectlgameengineimp;


import com.luis.lgameengine.gameutils.Settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * 
 * @author Luis Valdes Frances
 */

public class MainActivity extends Activity{
	
	private Thread vGameThread;
	private Main vMain;
	
	//Screen do not sleep:
	public static PowerManager ms_PowerManager;
    public static PowerManager.WakeLock ms_WakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Log.i("Debug", "lGameEngine INIT");
		Settings.getInstance().init(
				this, new boolean[]{
									false,
									false,
									false,
									true});
		
		vMain = new Main(this);
		
		// Rescale surface view to layout size:
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(vMain, Settings.getInstance().getRealWidth(), Settings.getInstance().getRealHeight());
		setContentView(layout);
		//setContentView(vMain);
		
		Log.i("Debug", "lGameEngine START");
		vGameThread = new Thread(vMain);
		vGameThread.start();
		
		//Screen not sleep:
		ms_PowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		ms_WakeLock = ms_PowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		
		//this.finish();
		
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


