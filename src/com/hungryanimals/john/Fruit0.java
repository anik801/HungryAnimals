package com.hungryanimals.john;

public class Fruit0{
	int index=0;
	int x=0;
	float dy=5;
	float y;
	static float maxY=450;
	boolean visible;
	int maxOffset=0;
	public Fruit0(){
		index=400;
		y=-20;
		visible=true;
	}
	public Fruit0(int X,int I, float Y,float DY){
		index=I;
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
			index++;
			x++;
			if(x>=8)
				x=0;
			if(index>=8)
				index=0;
			restart();
		}
			
		
	}
	public int getIndex(){
		return index;
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
		index++;
		x++;
		if(x>=8)
			x=0;
		if(index>=8)
			index=0;
		y=-20;
		visible=true;
	}

}
