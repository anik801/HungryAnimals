package com.hungryanimals.john;

//import com.testproject.R;

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

public class AboutGFX extends Activity implements OnTouchListener{
	CustomView2 ourView;
	float x,y;
	MediaPlayer ourSong;
	SoundPool sp;
	int sound;
	boolean init=true;
	WakeLock wL;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//PowerManager pM= (PowerManager)getSystemService(Context.POWER_SERVICE);
		//wL = pM.newWakeLock(PowerManager.FULL_WAKE_LOCK,"etc");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ourView = new CustomView2(this);
		ourView.setOnTouchListener(this);
		setContentView(ourView);
		
		x=y=0;
		sp=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
		sound=sp.load(this, R.raw.backsound, 1);
		ourSong=MediaPlayer.create(AboutGFX.this,R.raw.aboutmusic);
		ourSong.start();
		init=true;
		//wL.acquire();
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
		Bitmap bg,bg1,bg2,back;
		Thread ourThread = null;
		SurfaceHolder ourHolder;
		boolean isRunning = true;
		float bx,by;
		String st;
		Canvas canvas;
		boolean init;
		Paint paint;
		float backX,backY,buttonLength;
		float canvasWidth, canvasHeight;
		float bg2Y;
		public CustomView2(Context context) {
			super(context);
			ourHolder = getHolder();
			//frog1 = BitmapFactory.decodeResource(getResources(), R.drawable.f1);
			bg1 = BitmapFactory.decodeResource(getResources(), R.drawable.aboutpage);
			bg2 = BitmapFactory.decodeResource(getResources(), R.drawable.aboutnote);
			back = BitmapFactory.decodeResource(getResources(), R.drawable.back);
			bx=0;
			by=500;
			st="";
			init=true;
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
					Thread.sleep(10);
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
				canvas.drawBitmap(bg2, bx, by, null);
				canvas.drawBitmap(back, backX, backY, null);
				by-=3;
				//st=x+"    \t"+y;
				//canvas.drawText(st, 500, 100, paint);
				
				checkClick();
				ourHolder.unlockCanvasAndPost(canvas);
				if(by<-1*bg2Y)
					by=canvasHeight;				
			}

		}
		private void initialize() {
			// TODO Auto-generated method stub
			bg=Bitmap.createScaledBitmap(bg1,canvas.getWidth(), canvas.getHeight(), true);
			//bg2=Bitmap.createScaledBitmap(bg2,canvas.getWidth(), canvas.getHeight(), true);
			bg2Y=bg2.getHeight();
			canvasWidth=canvas.getWidth();
			canvasHeight=canvas.getHeight();
			backX=600*canvasWidth/800;
			backY=300*canvasHeight/480;
			buttonLength=back.getHeight();
			bx=1*canvasWidth/800;
			by=canvasHeight;
			
			paint = new Paint(); 
			paint.setColor(Color.WHITE); 
			paint.setStyle(Style.FILL); 
			paint.setColor(Color.BLACK); 
			paint.setTextSize(20*canvasWidth/800); 
			init=false;
		}

		private void checkClick() {
			if (x >= backX && x <= backX+buttonLength && y >= backY && y <= backY+buttonLength) {
				sp.play(sound, 1, 1, 0, 0, 1);
				try {
					x=y=0;
					@SuppressWarnings("rawtypes")
					Class ourClass = Class.forName("com.hungryanimals.john." + "MenuGFX");
					Intent ourIntent = new Intent(AboutGFX.this, ourClass);
					startActivity(ourIntent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
			}
				
			
		}

	}


}
