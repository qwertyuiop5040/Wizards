package wizards.entities;

import java.io.Serializable;

public class PointF implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2275429778434688584L;
	private static final double TOLERANCE=1.0;
	public double x,y;
	public PointF(){
		this(0.0,0.0);
	}
	public PointF(double x, double y){
		this.x=x;
		this.y=y;
	}
	public PointF(int x, int y){
		this((double)x,(double)y);
	}
	public PointF(PointF p){
		this.x=p.x;
		this.y=p.y;
	}
	public boolean equals(PointF p){
		return distance(p)<TOLERANCE;
	}
	public double distance(PointF p){
		return Math.pow(Math.pow(x-p.x, 2.0)+Math.pow(y-p.y, 2.0) , 0.5);
	}
	public void normalize(){
		double magnitude=magnitude();
		x*=magnitude;
		y*=magnitude;
	}
	public double magnitude(){
		return Math.pow(x*x+y*y, 0.5);
	}
	public boolean isNormalized(){
		return magnitude()-1.0<=TOLERANCE/5;
	}
	@Override
	public String toString(){return "Point at ("+x+","+y+")";}
}
