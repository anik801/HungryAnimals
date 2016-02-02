package com.hungryanimals.john;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class MenuGFX extends Activity implements OnTouchListener{
	CustomView2 ourView;
	float x,y;
	SoundPool sp;
	int sound1,sound2,sound3,sound4;
	MediaPlayer ourSong;
	boolean init=true;
	static WakeLock wL;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		PowerManager pM= (PowerManager)getSystemService(Context.POWER_SERVICE);
		wL = pM.newWakeLock(PowerManager.FULL_WAKE_LOCK,"etc");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ourView = new CustomView2(this);
		ourView.setOnTouchListener(this);
		setContentView(ourView);
		
		sp=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
		sound1=sp.load(this, R.raw.backsound, 1);
		sound2=sp.load(this, R.raw.lion, 1);
		sound3=sp.load(this, R.raw.frog, 1);
		sound4=sp.load(this, R.raw.byebye, 1);
		x=y=0;
		ourSong=MediaPlayer.create(MenuGFX.this,R.raw.menumusic);
		ourSong.start();
		init=true;
		wL.acquire();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourView.pause();
		finish();
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
		x=event.getX();
		y=event.getY();
		return false;
	}
	

	public class CustomView2 extends SurfaceView implements Runnable{
		Bitmap bg,bg1,about,help,play,exit;
		Bitmap about2,help2,play2;
		Thread ourThread = null;
		SurfaceHolder ourHolder;
		boolean isRunning = true;
		Canvas canvas;
		String st;
		Paint paint;
		float playX,playY,helpX,helpY,aboutX,aboutY,exitX,exitY;
		float buttonLength;
		float canvasWidth, canvasHeight;	
		
		public CustomView2(Context context) {
			super(context);
			ourHolder = getHolder();
			//frog1 = BitmapFactory.decodeResource(getResources(), R.drawable.f1);
			bg1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
			help = BitmapFactory.decodeResource(getResources(), R.drawable.help);
			about = BitmapFactory.decodeResource(getResources(), R.drawable.about);
			play = BitmapFactory.decodeResource(getResources(), R.drawable.play);
			exit = BitmapFactory.decodeResource(getResources(), R.drawable.exit);
			st="";
			about2= BitmapFactory.decodeResource(getResources(), R.drawable.about2);
			help2 = BitmapFactory.decodeResource(getResources(), R.drawable.help2);
			play2 = BitmapFactory.decodeResource(getResources(), R.drawable.play2);
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
					Thread.sleep(50);
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
				canvas.drawBitmap(play, playX, playY, null);
				canvas.drawBitmap(help, helpX, helpY, null);
				canvas.drawBitmap(about, aboutX, aboutY, null);
				canvas.drawBitmap(exit, exitX, exitY, null);
				
				//st=x+"    \t"+y;
				//canvas.drawText(st, 500, 100, paint);				
				checkClick();
				ourHolder.unlockCanvasAndPost(canvas);
			}

		}
		private void initialize() {
			// TODO Auto-generated method stub
			bg=Bitmap.createScaledBitmap(bg1,canvas.getWidth(), canvas.getHeight(), true);
			canvasWidth=canvas.getWidth();
			canvasHeight=canvas.getHeight();
			playX=80*canvasWidth/800;
			playY=120*canvasHeight/480;
			helpX=250*canvasWidth/800;
			helpY=300*canvasHeight/480;
			aboutX=450*canvasWidth/800;
			aboutY=100*canvasHeight/480;
			exitX=600*canvasWidth/800;
			exitY=300*canvasHeight/480;
			buttonLength=play.getHeight();
			
			paint = new Paint(); 
			paint.setColor(Color.WHITE); 
			paint.setStyle(Style.FILL); 
			paint.setColor(Color.BLACK); 
			paint.setTextSize(20); 
			init=false;
		}

		private void checkClick() {
			// TODO Auto-generated method stub
			if(x>=playX && x<=playX+buttonLength && y>=playY && y<=playY+buttonLength){
				sp.play(sound3, 1, 1, 0, 0, 1);
				try {
					canvas.drawBitmap(bg, 0, 0, null);
					canvas.drawBitmap(play2, playX, playY, null);
					canvas.drawBitmap(help, helpX, helpY, null);
					canvas.drawBitmap(about, aboutX, aboutY, null);
					canvas.drawBitmap(exit, exitX, exitY, null);
					x=y=0;
					@SuppressWarnings("rawtypes")
					Class ourClass = Class.forName("com.hungryanimals.john." + "Play");
					Intent ourIntent = new Intent(MenuGFX.this, ourClass);
					startActivity(ourIntent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(x>=helpX && x<=helpX+buttonLength && y>=helpY && y<=helpY+buttonLength){
				sp.play(sound1, 1, 1, 0, 0, 1);
				try {
					canvas.drawBitmap(bg, 0, 0, null);
					canvas.drawBitmap(play, playX, playY, null);
					canvas.drawBitmap(help2, helpX, helpY, null);
					canvas.drawBitmap(about, aboutX, aboutY, null);
					canvas.drawBitmap(exit, exitX, exitY, null);
					x=y=0;
					@SuppressWarnings("rawtypes")
					Class ourClass = Class.forName("com.hungryanimals.john." + "HelpGFX");
					Intent ourIntent = new Intent(MenuGFX.this, ourClass);
					startActivity(ourIntent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(x>=aboutX && x<=aboutX+buttonLength && y>=aboutY && y<=aboutY+buttonLength){
				sp.play(sound2, 1, 1, 0, 0, 1);
				try {
					canvas.drawBitmap(bg, 0, 0, null);
					canvas.drawBitmap(play, playX, playY, null);
					canvas.drawBitmap(help, helpX, helpY, null);
					canvas.drawBitmap(about2, aboutX, aboutY, null);
					canvas.drawBitmap(exit, exitX, exitY, null);
					x=y=0;
					@SuppressWarnings("rawtypes")
					Class ourClass = Class.forName("com.hungryanimals.john." + "AboutGFX"); //test
					Intent ourIntent = new Intent(MenuGFX.this, ourClass);
					startActivity(ourIntent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else if(x>=exitX && x<=exitX+buttonLength && y>=exitY && y<=exitY+buttonLength){
				sp.play(sound4, 1, 1, 0, 0, 1);
				//System.exit(0);
				wL.release();
				finish();
			}
				
			
		}

	}


}
