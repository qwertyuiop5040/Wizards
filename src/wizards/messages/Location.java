package wizards.messages;

import java.io.Serializable;

import wizards.entities.PointF;

public class Location implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	public String username;
	public PointF p;
	public Location(String u, PointF p1){
		username=u;
		p=p1;
	}
	@Override
	public String toString(){
		return "User: "+username+" at "+p;
	}
}
