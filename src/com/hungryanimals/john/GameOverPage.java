package com.hungryanimals.john;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class GameOverPage extends Activity implements OnTouchListener {
	CustomView2 ourView;
	float x, y;
	static int score=0;
	int highScore=0;
	MediaPlayer ourSong;
	SoundPool sp;
	int sound;
	boolean init=true;
	WakeLock wL;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Play.wL.release();
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		PowerManager pM= (PowerManager)getSystemService(Context.POWER_SERVICE);
		wL = pM.newWakeLock(PowerManager.FULL_WAKE_LOCK,"etc");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,	WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ourView = new CustomView2(this);
		ourView.setOnTouchListener(this);
		setContentView(ourView);
		x = y = 0;		
		sp=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
		sound=sp.load(this, R.raw.backsound, 1);
		ourSong=MediaPlayer.create(GameOverPage.this,R.raw.aboutmusic);
		ourSong.start();	
		init=true;
		//wL.acquire();
	}
	
	//File
	SharedPreferences sharedPreferences;
	int savedValue;
	public void SaveInt(String key, int value){
	       sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	       SharedPreferences.Editor editor = sharedPreferences.edit();
	       editor.putInt(key, value);
	       editor.commit();
	}
	public void LoadInt(){
	       sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	       savedValue = sharedPreferences.getInt("key", 0);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourView.pause();
		finish();
		/*
		try {
			x=y=0;
			@SuppressWarnings("rawtypes")
			Class ourClass = Class.forName("com.testproject." + "MenuGFX");
			Intent ourIntent = new Intent(GameOverPage.this, ourClass);
			startActivity(ourIntent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		*/
		ourSong.stop();
		//wL.release();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ourView.resume();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		x = event.getX();
		y = event.getY();
		return false;
	}

	public class CustomView2 extends SurfaceView implements Runnable {
		Bitmap bg,bg1, back;
		Thread ourThread = null;
		SurfaceHolder ourHolder;
		boolean isRunning = true;
		String st,st2;
		Canvas canvas;
		Paint paint;
		float backX,backY,buttonLength;
		float canvasWidth, canvasHeight;
		float scoreX,scoreY;
		float scoreX2,scoreY2;
		public CustomView2(Context context) {
			super(context);
			ourHolder = getHolder();
			// frog1 = BitmapFactory.decodeResource(getResources(),
			// R.drawable.f1);
			bg1 = BitmapFactory.decodeResource(getResources(),R.drawable.gameoverpage);
			back = BitmapFactory.decodeResource(getResources(), R.drawable.back);
			st = "";
		}

		public void pause() {
			isRunning = false;
			while (true) {
				try {
					ourThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			ourThread = null;
		}

		public void resume() {
			isRunning = true;
			ourThread = new Thread(this);
			ourThread.start();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			

			while (isRunning) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (!ourHolder.getSurface().isValid())
					continue;
				canvas = ourHolder.lockCanvas();
				if(init)
					initialize();

				canvas.drawBitmap(bg, 0, 0, null);
				canvas.drawBitmap(back, backX, backY, null);

				st = "" + score;
				canvas.drawText(st, scoreX, scoreY, paint);
				st2 = "" + highScore;
				canvas.drawText(st2, scoreX2, scoreY2, paint);

				checkClick();
				ourHolder.unlockCanvasAndPost(canvas);
			}

		}
		private void initialize() {
			// TODO Auto-generated method stub
			bg=Bitmap.createScaledBitmap(bg1,canvas.getWidth(), canvas.getHeight(), true);
			//back=Bitmap.createScaledBitmap(back,canvas.getWidth(), canvas.getHeight(), true);
			canvasWidth=canvas.getWidth();
			canvasHeight=canvas.getHeight();
			backX=600*canvasWidth/800;
			backY=300*canvasHeight/480;
			scoreX=450*canvasWidth/800;
			scoreY=170*canvasHeight/480;
			scoreX2=450*canvasWidth/800;
			scoreY2=250*canvasHeight/480;
			buttonLength=back.getHeight();
			
			paint = new Paint();
			paint.setStyle(Style.FILL);
			paint.setColor(Color.WHITE);
			paint.setTextSize(80*canvasWidth/800);
			init=false;
			LoadInt();
			highScore=savedValue;
			if(score>highScore){
				highScore=score;
				SaveInt("key",score);
			}
				
		}

		private void checkClick() {
			// TODO Auto-generated method stub

			if (x >= backX && x <= backX+buttonLength && y >= backY && y <= backY+buttonLength) {
				sp.play(sound, 1, 1, 0, 0, 1);
				try {
					x = y = 0;
					@SuppressWarnings("rawtypes")
					Class ourClass = Class.forName("com.hungryanimals.john."+ "MenuGFX");
					Intent ourIntent = new Intent(GameOverPage.this, ourClass);
					startActivity(ourIntent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		
		
		

	}

}
