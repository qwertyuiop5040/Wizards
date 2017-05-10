package wizards.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import wizards.entities.Avatar;
import wizards.entities.Entity;
import wizards.entities.PointF;
import wizards.entities.World;
import wizards.messages.Location;

public class WorldGUI extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	
	
	public static final int WIDTH=600;
	public static final int HEIGHT=600;
	public static final int avatarImage_WIDTH=WIDTH/10;
	public static final int avatarImage_HEIGHT=HEIGHT/10;
	public static final int WALL_WIDTH=WIDTH/8;
	public static final int WALL_HEIGHT=HEIGHT/8;
	public static final int PROJECTILE_WIDTH=WIDTH/40;
	public static final int PROJECTILE_HEIGHT=HEIGHT/15;
	public static final Color BACKGROUND_COLOR=Color.WHITE;
	private BufferedImage avatarImage;
	private BufferedImage blockImage;
	private BufferedImage projectileImage;
	private World world=null;
	private String username;
	private Displayer dp=new Displayer();
	private Thread displayThread;
	private Avatar avatar=null;
	public WorldGUI(){
		setSize(WIDTH, HEIGHT);
		setFocusable(true);
		setBackground(BACKGROUND_COLOR);
		avatarImage=new BufferedImage(avatarImage_WIDTH,avatarImage_HEIGHT,BufferedImage.TYPE_4BYTE_ABGR);
		for(int i=0;i<avatarImage.getWidth();i++)
			for(int j=0;j<avatarImage.getHeight();j++)
				if(Math.abs(i-avatarImage.getWidth()/2)<j/Math.pow(3.0, 0.5))
					avatarImage.setRGB(i, j, 0xFF0000FF);
		blockImage=new BufferedImage(WALL_WIDTH,WALL_HEIGHT,BufferedImage.TYPE_4BYTE_ABGR);
		for(int i=0;i<blockImage.getWidth();i++)
			for(int j=0;j<blockImage.getHeight();j++)
				blockImage.setRGB(i, j, ((i+j)%255)+((i%255)<<8)+((j%255)<<16)+((255<<24)));
		projectileImage=new BufferedImage(PROJECTILE_WIDTH,PROJECTILE_HEIGHT,BufferedImage.TYPE_4BYTE_ABGR);
		for(int i=0;i<projectileImage.getWidth();i++)
			for(int j=0;j<projectileImage.getHeight();j++){
				int mid=projectileImage.getWidth()/2;
				int height=projectileImage.getHeight();
				if((j<mid&&Math.sqrt((double)(i-mid)*(i-mid)+(j-mid)*(j-mid))<(double)mid)||(j>height-mid
						&&Math.sqrt((double)(i-mid)*(i-mid)+(j-(height-mid))*(j-(height-mid)))<(double)mid))
					projectileImage.setRGB(i, j, 0x00FFFFFF);
				else
					projectileImage.setRGB(i, j, 0xFFFF8800);//orange
			}
		
		InputMap im=this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am=this.getActionMap();
		for(int i=KeyEvent.VK_LEFT;i<=KeyEvent.VK_DOWN;i++){
			int keyIndex=i-KeyEvent.VK_LEFT;
			String commandString=KeyStatus.keyCommands[keyIndex];
			im.put(KeyStroke.getKeyStroke(i,0,false), commandString+"Pressed");
			im.put(KeyStroke.getKeyStroke(i,0,true),commandString+"Released");
			am.put(commandString+"Pressed", new KeyAction(commandString,true));
			am.put(commandString+"Released", new KeyAction(commandString,false));
		}
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0,false), KeyStatus.keyCommands[KeyStatus.LEFT]+"Pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W,0,false), KeyStatus.keyCommands[KeyStatus.UP]+"Pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,false), KeyStatus.keyCommands[KeyStatus.RIGHT]+"Pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0,false), KeyStatus.keyCommands[KeyStatus.DOWN]+"Pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0,true), KeyStatus.keyCommands[KeyStatus.LEFT]+"Released");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W,0,true), KeyStatus.keyCommands[KeyStatus.UP]+"Released");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,true), KeyStatus.keyCommands[KeyStatus.RIGHT]+"Released");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0,true), KeyStatus.keyCommands[KeyStatus.DOWN]+"Released");
		
		displayThread=new Thread(dp);
		displayThread.start();
		setVisible(true);
	}
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(avatarImage, WIDTH/2-avatarImage_WIDTH/2,HEIGHT/2-avatarImage_HEIGHT/2, avatarImage_WIDTH,avatarImage_HEIGHT, null);
		//g.drawImage(blockImage, WIDTH/2-WALL_WIDTH/2,HEIGHT/4-WALL_HEIGHT/2,WALL_WIDTH,WALL_HEIGHT,null);
		if(world!=null){
			if(world.getAvatars().get(username)!=null){
				if(world.getEntities()!=null){
					int temp=85;
					PointF loc=world.getAvatars().get(username).getLocation();
					for(HashMap.Entry<PointF,Entity> entry:world.getEntities().entrySet()){
						PointF entryLoc=entry.getKey();
						double distX=entryLoc.x-loc.x;
						double distY=loc.y-entryLoc.y;
						if(Math.abs(distX)<WIDTH/2+WALL_WIDTH/2&&Math.abs(distY)<HEIGHT/2+WALL_HEIGHT/2){
							g.drawImage(blockImage, WIDTH/2+(int)distX-WALL_WIDTH/2,
								HEIGHT/2+(int)distY-WALL_HEIGHT/2,WALL_WIDTH,WALL_HEIGHT,null);
						}
						g.drawString(entry.getValue().getLocation().toString(),75,temp+=20);
					}
					temp=85;
					for(Entry<String, Avatar> entry:world.getAvatars().entrySet()){
						PointF entryLoc=entry.getValue().getLocation();
						double distX=entryLoc.x-loc.x;
						double distY=loc.y-entryLoc.y;
						if(Math.abs(distX)<WIDTH/2&&Math.abs(distY)<HEIGHT/2&&
								!((Avatar)entry.getValue()).getUsername().equals(username)){
							g.drawImage(avatarImage, WIDTH/2+(int)distX-avatarImage_WIDTH/2,
								HEIGHT/2+(int)distY-avatarImage_HEIGHT/2,avatarImage_WIDTH,
								avatarImage_HEIGHT,null);
							
						}
						g.drawString(entry.getValue().getLocation().toString(),220,temp+=20);
					}
				}
				g.drawString(world.getAvatars().get(username).getLocation().toString(),75,75);
				
			}
		}
	}
	public void setWorld(World w){
		if(w!=null){
			world=w;
			if(avatar==null){
				avatar=new Avatar(world.getAvatars().get(username).getLocation(), username);
				System.out.println(avatar.getLocation());
			}/*else{
				world.getAvatars().replace(username, avatar);
			}*/
		}
	}
	public World getWorld(){return world;}
	public void updateAvatarLocation(Location l){
		world.updateAvatarLocation(l);
	}
	/*public void setAvatarLocation(PointF p){
		avatar.setLocation(p);
	}*/
	public Avatar getAvatar(){return avatar;}
	public void setUsername(String username) {this.username=username;}
	
	private class Displayer implements Runnable{

		@Override
		public void run() {
			boolean keepGoing=true;
			while(keepGoing){
				try {
					Thread.sleep(20);
					if(KeyStatus.keyDown[KeyStatus.LEFT])
						getAvatar().moveLeft();
					if(KeyStatus.keyDown[KeyStatus.DOWN])
						getAvatar().moveDown();
					if(KeyStatus.keyDown[KeyStatus.RIGHT])
						getAvatar().moveRight();
					if(KeyStatus.keyDown[KeyStatus.UP])
						getAvatar().moveUp();
					repaint();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@SuppressWarnings("serial")
	private class KeyAction extends AbstractAction{
		
		private String cmd;
		private boolean pressed;//key is pressed=true, released=false;
		public KeyAction(String cmd, boolean pressed){
			this.cmd=cmd;
			this.pressed=pressed;
		}
		@Override
        public void actionPerformed(ActionEvent e) {
			System.out.println("hi from "+cmd);
			for(int i=0;i<KeyStatus.keyCommands.length;i++)
				if(cmd.equalsIgnoreCase(KeyStatus.keyCommands[i])){
					System.out.println("hi from "+cmd);
					KeyStatus.keyDown[i]=pressed;
				}
        }
	}
	private static class KeyStatus{
		public static final String keyCommands[]={"left","up","right","down","space"};
		public static final int LEFT=0;
		public static final int UP=1;
		public static final int RIGHT=2;
		public static final int DOWN=3;
		public static final int SPACE=4;
		public static boolean keyDown[]={false,false,false,false,false};
		public static final int getIndexOfCommand(String s){
			for(int i=0;i<keyCommands.length;i++)
				if(s.equalsIgnoreCase(keyCommands[i]))
					return i;
			return -1;
		}
	}
}
