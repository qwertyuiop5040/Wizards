package wizards.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.Map.Entry;

import wizards.messages.Location;


public class World implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4706008210993251702L;
	
	public static final int DEFAULT_WIDTH=2400;
	public static final int DEFAULT_HEIGHT=2400;
	public static final int MINIMUM_NUM_WALLS=10;
	private int width=DEFAULT_WIDTH;
	private int height=DEFAULT_HEIGHT;
	private int numWalls=MINIMUM_NUM_WALLS;
	private Hashtable<PointF,Entity> entities=new Hashtable<PointF,Entity>();
	private Hashtable<String,Avatar> users=new Hashtable<String, Avatar>();
	private Random random=new Random();
	public World(){
		for(int i=0;i<4;i++){
			numWalls+=random.nextInt((i+1)*2);
		}
		addRocks(numWalls);
	}
	public World(World w){
		if(this==w)return;
		width=w.width;
		height=w.height;
		numWalls=w.numWalls;
		for(Entry<String, Avatar> entry:w.users.entrySet()){
			users.put(entry.getKey(),entry.getValue());
		}
		for(Entry<PointF,Entity> entry:w.entities.entrySet()){
			entities.put(entry.getKey(),entry.getValue());
		}
		
	}
	public void addAvatar(String username){
		Avatar a=new Avatar(username);
		do{
			a.setLocation(getRandomSpace(Avatar.WIDTH/2,width-Avatar.WIDTH/2,Avatar.HEIGHT/2,
					height-Avatar.HEIGHT/2));
		}while(!spaceEmpty(a));
		users.put(username, a);
	}
	public void addRocks(int numWalls){
		
		for(int i=0;i<numWalls;i++){
			int sideLength=Wall.MIN_SIDE_LENGTH+random.nextInt(Wall.MAX_SIDE_LENGTH)+1;
			Wall wall=new Wall(sideLength, sideLength);
			PointF p;
			do{
				p=getRandomSpace(sideLength/2,width-sideLength/2,sideLength/2,height-sideLength/2);
				wall.setLocation(p);
			}while(!spaceEmpty(wall));
			entities.put(wall.getLocation(), wall);
		}
	}
	/*
	 * Gets a random point on the map.
	 */
	private PointF getRandomSpace(int minX, int maxX, int minY, int maxY){
		return new PointF(minX+random.nextInt(maxX-minX),minY+random.nextInt(maxY-minY));
	}
	
	public boolean spaceEmpty(Entity e){
		for(HashMap.Entry<PointF,Entity> entry:entities.entrySet()){
			if(entry.getValue().intersects(e)){
				return false;
			}
		}
		return true;
	}
	public void updateAvatarLocation(Location l){
		Avatar temp=users.get(l.username);
		if(temp==null){
			users.put(l.username, new Avatar(l.p,l.username));
			return;
		}
		temp.setLocation(l.p);
		users.replace(l.username,temp);
	}
	public Hashtable<PointF,Entity> getEntities(){return entities;}
	public Hashtable<String,Avatar> getAvatars(){return users;}
}
