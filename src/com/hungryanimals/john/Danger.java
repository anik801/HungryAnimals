package com.hungryanimals.john;

public class Danger {
	int x=0;
	float dy=5;
	float y;
	static float maxY=450;
	boolean visible;
	int maxOffset=0;
	public Danger(){
		y=-20;
		visible=true;
	}
	public Danger(int X, float Y,float DY){
		x=X;
		y=Y; 
		dy=DY;
		visible=true;
	}
	public boolean getVisible() {
		return visible;
	}
	public void move() {
		y+=dy;
		if(y>maxY+maxOffset){
			visible=false;
			x++;
			if(x>=8)
				x=0;
			restart();
		}
			
		
	}

	public int getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public void eatFruit(){
		visible=false;
		restart();
	}
	public void restart(){
		x++;
		if(x>=8)
			x=0;
		y=-20;
		visible=true;
	}

}