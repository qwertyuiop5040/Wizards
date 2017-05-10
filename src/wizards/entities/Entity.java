package wizards.entities;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;

public abstract class Entity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7324778635449509033L;
	
	protected boolean tangible=false;
	protected PointF location;
	protected int width;
	protected int height;
	protected Entity(){
		this(0,0,0,0);
	}
	protected Entity(int x,int y, int width, int height){
		location=new PointF(x,y);
		this.width=width;
		this.height=height;
	}

	public PointF getLocation(){return location;}
	public void setLocation(PointF p){location=new PointF(p.x,p.y);}
	public boolean isTangible(){return tangible;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public boolean intersects(Entity e){
		if(location.x>e.getLocation().x){
			if(!(location.x-width/2<e.getLocation().x+e.getWidth()/2))return false;
		}else{
			if(!(location.x+width/2>=e.getLocation().x-e.getWidth()/2))return false;
		}
		if(location.y>e.getLocation().y){
			if(!(location.y-height/2<e.getLocation().y+e.getHeight()/2))return false;
		}else{
			return (location.y+height/2>=e.getLocation().y-e.getHeight()/2);
		}
		return true;
	}
}
