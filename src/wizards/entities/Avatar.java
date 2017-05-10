package wizards.entities;

import java.awt.Point;

public class Avatar extends Entity{
	public static final int DEFAULT_SPEED=10;
	public static final int DEFAULT_HP=50;
	public static final int WIDTH=100;
	public static final int HEIGHT=100;
	private String username;
	private int level;
	private int speed=DEFAULT_SPEED;
	private int maxHP=DEFAULT_HP;
	private int hp=maxHP;
	private boolean dead=false;
	public Avatar(int x,int y, String username){
		super(x,y,WIDTH,HEIGHT);
		this.username=username;
		tangible=true;
		level=1;
	}
	public Avatar(Point p, String username){
		this((int)p.x,(int)p.y,username);
	}
	public Avatar(PointF p, String username){
		this((int)p.x,(int)p.y,username);
	}
	public Avatar(String username){
		this(0,0,username);
	}
	public int getLevel(){return level;}
	public void setLevel(int l){
		level=l;
		speed=DEFAULT_SPEED+(l-1);
		maxHP=DEFAULT_HP+(l-1)*3;
	}
	
	public void moveUp(){
		if(!pathClear())return;
		location.y+=speed;
	}
	public void moveDown(){
		if(!pathClear())return;
		location.y-=speed;
	}
	public void moveLeft(){
		if(!pathClear())return;
		location.x-=speed;
	}
	public void moveRight(){
		if(!pathClear())return;
		location.x+=speed;
	}

	public boolean pathClear(){
		return true;
	}
	public int getSpeed(){return speed;}
	public int getHP(){return hp;}
	public void damage(int damage){dead=(hp-=damage)<=0;}
	public String getUsername(){return username;}
}
