package wizards.entities;

public class Projectile extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_HEIGHT=40;
	public static final int DEFAULT_WIDTH=15;
	public static final double DEFAULT_SPEED=25.0;
	private double speed=DEFAULT_SPEED;
	private PointF velocity;
	private double angle;
	public Projectile(int x, int y,double angleAboveWest) {
		super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		tangible=true;
		angle=angleAboveWest;
		velocity=new PointF(Math.cos(angleAboveWest),Math.sin(angleAboveWest));
		velocity.normalize();
	}
	
	public void move(){
		//if(!velocity.isNormalized())velocity.normalize();
		location.x+=velocity.x*speed;
		location.y+=velocity.y*speed;
	}
}
