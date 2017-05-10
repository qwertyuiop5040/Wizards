package wizards.entities;

import java.awt.Point;

public class Wall extends Entity{
	public static final int MIN_SIDE_LENGTH=40;
	public static final int MAX_SIDE_LENGTH=100;
	public Wall(int x,int y, int w, int h){
		super(x,y,w,h);
		tangible=true;
	}
	public Wall(PointF p, int w, int h){
		this((int)p.x,(int)p.y,w,h);
	}
	public Wall(int w,int h) {
		this(0,0,w,h);
	}
	
}
