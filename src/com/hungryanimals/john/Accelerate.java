package com.hungryanimals.john;

import java.util.Random;

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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class Accelerate extends Activity implements SensorEventListener {

	Bitmap frog1, frog2, frogN, danger, backGround1;
	Bitmap frogGreen,frogYellow,frogRed,frogM;
	Thread ourThread = null;
	SurfaceHolder ourHolder;
	boolean isRunning = false;
	float frogX = 400, frogY = 380;
	float dx = 0;
	float x, y, sensorX, sensorY, mlp = 8;
	SensorManager sm;
	CustomView1 csView;
	Canvas canvas;
	Bitmap[] fruit = new Bitmap [8];
	boolean init=true;
	int score;
	WakeLock wL;
	MediaPlayer ourSong;
	SoundPool sp;
	int sound1,sound2,sound3,sound4;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//Wake Lock
		//PowerManager pM= (PowerManager)getSystemService(Context.POWER_SERVICE);
		//wL = pM.newWakeLock(PowerManager.FULL_WAKE_LOCK,"etc");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			Sensor s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
		}
		frog1 = BitmapFactory.decodeResource(getResources(), R.drawable.f1);
		frog2 = BitmapFactory.decodeResource(getResources(), R.drawable.f2);
		
		backGround1 = BitmapFactory.decodeResource(getResources(),R.drawable.bg1);

		fruit[0]=BitmapFactory.decodeResource(getResources(), R.drawable.fruit0);
		fruit[1]=BitmapFactory.decodeResource(getResources(), R.drawable.fruit1);
		fruit[2]=BitmapFactory.decodeResource(getResources(), R.drawable.fruit2);
		fruit[3]=BitmapFactory.decodeResource(getResources(), R.drawable.fruit3);
		fruit[4]=BitmapFactory.decodeResource(getResources(), R.drawable.fruit4);
		fruit[5]=BitmapFactory.decodeResource(getResources(), R.drawable.fruit5);
		fruit[6]=BitmapFactory.decodeResource(getResources(), R.drawable.fruit6);
		fruit[7]=BitmapFactory.decodeResource(getResources(), R.drawable.fruit7);
		danger=BitmapFactory.decodeResource(getResources(), R.drawable.danger1);
		frogGreen=BitmapFactory.decodeResource(getResources(),R.drawable.froggreen);
		frogYellow=BitmapFactory.decodeResource(getResources(),R.drawable.frogyellow);
		frogRed=BitmapFactory.decodeResource(getResources(),R.drawable.frogred);
		x = y = sensorX = sensorY = 0;
		csView = new CustomView1(this);
		setContentView(csView);
		csView.resume();
		//wL.acquire();
		
		sp=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
		sound1=sp.load(this, R.raw.eat4, 1);
		sound2=sp.load(this, R.raw.eat2, 1);
		sound3=sp.load(this, R.raw.eat3, 1);
		sound4=sp.load(this, R.raw.hurt, 1);
		
		ourSong=MediaPlayer.create(Accelerate.this,R.raw.gamemusic);
		ourSong.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sm.unregisterListener(this);
		csView.pause();
		finish(); //untested
		endGame();
		//wL.release();
		ourSong.stop();
	}

	private void endGame() {
		// TODO Auto-generated method stub
		try {
			x=y=0;
			//score=50;
			GameOverPage.score=score;
			@SuppressWarnings("rawtypes")
			Class ourClass = Class.forName("com.hungryanimals.john." + "GameOverPage");
			Intent ourIntent = new Intent(Accelerate.this, ourClass);
			startActivity(ourIntent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	@Override
	public void onSensorChanged(SensorEvent e) {
		// TODO Auto-generated method stub
		// This sleep is slowing down the game

		try {
			Thread.sleep(5);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		sensorX = e.values[1];
		if(sensorX>1.5)
			dx=1;
		else if(sensorX<-1.5)
			dx=-1;
		else
			dx=0;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public class CustomView1 extends SurfaceView implements Runnable {
		String st = "";
		Paint paint = new Paint(); 
		Random rn=new Random();
		float canvasWidth=800;
		float canvasHeight=450;
		float frogHeight,frogWidth;
		Bitmap resized = null;
		int size=0;
		
		long ft1,ft2,ftd=1000;
		long frogT1,frogT2,frogT3,frogT4,frogTD=200;
		float fLimitX1=10,fLimitX2=70;
		Fruit0 fr0,fr1,fr2;
		Danger dg;
		float[] fruitX=new float[8];
		//int fruitX[]={500,100,300,700,400,600,200,150}; //8
		int health;
		long gameTime1,gameTime2,gameTimeDiff=120000;
		String st2="";
		float stX,stY,st2X,st2Y;
		float ex1a,ex2a,ex1b,ex2b;
		boolean first30,first60,first90;
		float divX1,divY1,divX2,divY2;
		public CustomView1(Context context) {
			super(context);
			ourHolder = getHolder();

		}
		public void pause() {
			isRunning = false;
			while (true) {
				try {
					ourThread.join();
				} catch (InterruptedException e) {
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
			while (isRunning) {
				if (!ourHolder.getSurface().isValid())
					continue;
				canvas = ourHolder.lockCanvas();
				//Main gameFrame
				{
					if(init)					
						initialize();
					//canvas.drawBitmap(backGround1, 0, 0, null);
					canvas.drawBitmap(resized, 0, 0, null);	
					
					if(frogX+dx*mlp+frogWidth<canvasWidth && frogX+dx*mlp>0)
						frogX=frogX+dx*mlp;
					canvas.drawBitmap(frogN, frogX, frogY, null);
					
					//Moving fruits
					ft2=System.currentTimeMillis();
					if(ft2-ft1>=ftd)
					{
						moveFruit();
						speedControl();
						
						
					}
					if(ft2-frogT1>=frogTD && ft2-frogT2>=frogTD && ft2-frogT3>=frogTD && ft2-frogT4>=frogTD)
					{
						frogN=frog1;
						frogT1=frogT2=frogT3=frogT4=ft2;
					}
					//GameOver Condition
					if(ft2-gameTime1>=gameTimeDiff)
						((Activity) getContext()).finish();
						//endGame(); //testing
						
					//Changing frog position	
					if(frogX+dx*mlp+frogWidth<canvasWidth && frogX+dx*mlp>0)
						frogX=frogX+dx*mlp;
					canvas.drawBitmap(frogN, frogX, frogY, null);
					canvas.drawBitmap(frogM, 10, 10, null);
					
					//Score & Time
					st="Score: "+score;
					canvas.drawText(st, stX, stY, paint);	
					st2="Time: "+(120-((ft2-gameTime1)/1000));
					canvas.drawText(st2, st2X, st2Y, paint);
				}
				ourHolder.unlockCanvasAndPost(canvas);
			}
		}
		private void speedControl() {
			// TODO Auto-generated method stub
			if(first30 && ft2-gameTime1>=30000){
				fr0.dy+=1;
				fr1.dy+=1;
				fr2.dy+=1;
				dg.dy+=1;
				first30=false;
			}
			else if(first30 && ft2-gameTime1>=60000){
				fr0.dy+=1;
				fr1.dy+=1;
				fr2.dy+=1;
				dg.dy+=1;
				first60=false;
			}
			else if(first30 && ft2-gameTime1>=90000){
				fr0.dy+=1;
				fr1.dy+=1;
				fr2.dy+=1;
				dg.dy+=1;
				first90=false;
			}
			
		}
		private void moveFruit() {
			fr0.move();
			fr1.move();
			fr2.move();	
			dg.move();
			
			//Mouth Open Timing
			if(frogY-fr0.getY()<=ex2a && frogY-fr0.getY()>=ex2b && fruitX[fr0.getX()]-frogX>=fLimitX1 && fruitX[fr0.getX()]-frogX<=fLimitX2){
				frogT1=System.currentTimeMillis();
				frogN=frog2;
			}
			if(frogY-fr1.getY()<=ex2a &&frogY-fr1.getY()>=ex2b && fruitX[fr1.getX()]-frogX>=fLimitX1 && fruitX[fr1.getX()]-frogX<=fLimitX2){
				frogT2=System.currentTimeMillis();
				frogN=frog2;
			}
			if(frogY-fr2.getY()<=ex2a && frogY-fr2.getY()>=ex2b && fruitX[fr2.getX()]-frogX>=fLimitX1 && fruitX[fr2.getX()]-frogX<=fLimitX2){
				frogT3=System.currentTimeMillis();
				frogN=frog2;
			}
			if(frogY-dg.getY()<=ex2a &&frogY-dg.getY()>=ex2b && fruitX[dg.getX()]-frogX>=fLimitX1 && fruitX[dg.getX()]-frogX<=fLimitX2+30){
				frogT4=System.currentTimeMillis();
				frogN=frog2;
			}
			
			canvas.drawBitmap(fruit[fr0.getIndex()],fruitX[fr0.getX()], fr0.getY(),null);
			canvas.drawBitmap(fruit[fr1.getIndex()],fruitX[fr1.getX()], fr1.getY(),null);
			canvas.drawBitmap(fruit[fr2.getIndex()],fruitX[fr2.getX()], fr2.getY(),null);
			canvas.drawBitmap(danger,fruitX[dg.getX()], dg.getY(),null);
			if(frogY-fr0.getY()<=ex1a && frogY-fr0.getY()>=ex1b && fruitX[fr0.getX()]-frogX>=fLimitX1 && fruitX[fr0.getX()]-frogX<=fLimitX2){
				score+=10;
				fr0.eatFruit();
				sp.play(sound1, 1, 1, 0, 0, 1);
			}
			if(frogY-fr1.getY()<=ex1a && frogY-fr1.getY()>=ex1b && fruitX[fr1.getX()]-frogX>=fLimitX1 && fruitX[fr1.getX()]-frogX<=fLimitX2){
				score+=20;
				fr1.eatFruit();
				sp.play(sound2, 1, 1, 0, 0, 1);
			}
			if(frogY-fr2.getY()<=ex1a && frogY-fr2.getY()>=ex1b && fruitX[fr2.getX()]-frogX>=fLimitX1 && fruitX[fr2.getX()]-frogX<=fLimitX2){
				score+=30;
				fr2.eatFruit();
				sp.play(sound3, 1, 1, 0, 0, 1);
			}
			if(frogY-dg.getY()<=ex1a && frogY-dg.getY()>=ex1b && fruitX[dg.getX()]-frogX>=fLimitX1 && fruitX[dg.getX()]-frogX<=fLimitX2+15){
				score-=50;
				health--;
				dg.eatFruit();
				sp.play(sound4, 1, 1, 0, 0, 1);
				
				switch(health){
				case 2:
					frogM=frogYellow;
					break;
				case 1:
					frogM=frogRed;
					break;
				case 0:
					((Activity) getContext()).finish();
					//endGame(); //testing
					break;
				}
			}
						
			if(score<=0)
				score=0;
			
		}
		void initialize(){
			canvasWidth=canvas.getWidth();
			canvasHeight=canvas.getHeight();
			frogHeight=frog1.getHeight();
			frogWidth=frog1.getWidth();
			divX1=canvasWidth/800;
			divY1=canvasHeight/480;
			divX2=50*divX1;
			divY2=50*divY1;
			//resized = Bitmap.createScaledBitmap(backGround1,canvas.getWidth(), canvas.getHeight(), true);			
			resized = Bitmap.createScaledBitmap(backGround1,(int)canvasWidth, (int)canvasHeight, true);	
			
			frog1 = Bitmap.createScaledBitmap(frog1,(int)(80*divX1), (int)(80*divY1), true);
			frog2 = Bitmap.createScaledBitmap(frog2,(int)(80*divX1), (int)(80*divY1), true);	
			
			fruit[0]=Bitmap.createScaledBitmap(fruit[0],(int)divX2, (int)divY2, true);
			fruit[1]=Bitmap.createScaledBitmap(fruit[1],(int)divX2, (int)divY2, true);
			fruit[2]=Bitmap.createScaledBitmap(fruit[2],(int)divX2, (int)divY2, true);
			fruit[3]=Bitmap.createScaledBitmap(fruit[3],(int)divX2, (int)divY2, true);
			fruit[4]=Bitmap.createScaledBitmap(fruit[4],(int)divX2, (int)divY2, true);
			fruit[5]=Bitmap.createScaledBitmap(fruit[5],(int)divX2, (int)divY2, true);
			fruit[6]=Bitmap.createScaledBitmap(fruit[6],(int)divX2, (int)divY2, true);
			fruit[7]=Bitmap.createScaledBitmap(fruit[7],(int)divX2, (int)divY2, true);
			danger=Bitmap.createScaledBitmap(danger,(int)(70*divX1), (int)(70*divY1), true);
			
			if(canvasHeight>=480)
				frogY = canvasHeight - frogHeight*divY1 + 10*divY1;
			else
				frogY = canvasHeight - frogHeight*divY1 - 15*divY1;
			
			paint.setColor(Color.WHITE); 
			paint.setStyle(Style.FILL); 
			paint.setColor(Color.BLACK); 
			paint.setTextSize(60*canvasHeight/800);
			score=0;
			frogN=frog1;
			fr0= new Fruit0(0,5,-20,6*divY1);
			fr1= new Fruit0(2,0,-20,5*divY1);
			fr2= new Fruit0(7,1,-50,4*divY1);
			dg=new Danger(1,-10,7*divY1);
			health=3;			
			frogM=frogGreen;
			ft1=System.currentTimeMillis();
			gameTime1=ft1;
			
			fruitX[0]=500*divX1;
			fruitX[1]=100*divX1;
			fruitX[2]=300*divX1;
			fruitX[3]=700*divX1;
			fruitX[4]=400*divX1;
			fruitX[5]=600*divX1;
			fruitX[6]=200*divX1;
			fruitX[7]=150*divX1;
			
			mlp=5*divX1;
			stX=600*divX1;
			stY=50*divY1;
			st2X=600*divX1;
			st2Y=80*divY1;
			
			ex1a=5*divY1;
			ex2a=15*divY1;
			ex1b=-1*ex1a;
			ex2b=-1*ex2a;
			//ex1=5;
			//ex2=20;
			Fruit0.maxY=canvasHeight;
			Danger.maxY=canvasHeight;
			
			fLimitX1=-10*divX1;
			fLimitX2=40*divX1;
			
			first30=first60=first90=true;
			init=false;
		}
	}

}
