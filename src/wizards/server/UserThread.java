package wizards.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Scanner;
import java.util.Map.Entry;

import wizards.constants.Tags;
import wizards.entities.Avatar;
import wizards.entities.PointF;
import wizards.entities.World;
import wizards.messages.Location;
import wizards.messages.Message;

public class UserThread implements Runnable{
	public boolean ENABLE_CONSOLE_MESSAGES=true;
	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private User u;
	private ArrayDeque<Location>toSend=new ArrayDeque<Location>();
	private boolean keepGoing=true;
	private UserOutputThread uot;
	private Thread outputThread;
	private class UserOutputThread implements Runnable{
		public void run() {
			while(keepGoing){
				synchronized(toSend){
					while(!toSend.isEmpty()){
						Location l=toSend.pollFirst();
						if(l==null)System.out.println("o this is null"+toSend.isEmpty());
						if(ENABLE_CONSOLE_MESSAGES)System.out.println("Sent location "+l);
						try {
							oos.writeObject(l);
							oos.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	public UserThread(User user){
		this.u=user;
		s=u.getSocket();
		try {
			oos=new ObjectOutputStream(s.getOutputStream());
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		uot=new UserOutputThread();
		outputThread=new Thread(uot);
		outputThread.start();
	}
	public void run() {
		try {
			ois=new ObjectInputStream(s.getInputStream());
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		while(keepGoing){
			if(s.isClosed()||!s.isBound()||!s.isConnected()){
				synchronized(Server.users){
					Server.disconnectUser(u,this);
					return;
				}
			}
			try {
				Object fromUser=ois.readObject();
				synchronized(Server.users){
					if(fromUser instanceof Message){
						String message=((Message)fromUser).getMessage();
						if(ENABLE_CONSOLE_MESSAGES)
							System.out.println("New message from user "+u.getUsername()+": "+message);
						if(message.startsWith(Tags.USERNAME)&&message.length()>3){
							String username=message.substring(3,message.length());
							u.setString(username);
							synchronized(Server.world){
								Server.world.addAvatar(username);
								Server.sendLocationToUsers(new Location(username,
									Server.world.getAvatars().get(username).getLocation()));
							}
							oos.writeObject(Server.world);
							oos.flush();
							if(ENABLE_CONSOLE_MESSAGES)
								System.out.println(username+" has connected.");
						}
					}else if(fromUser instanceof Location){
						
						Location l=(Location)(fromUser);
						Server.world.getAvatars().get(l.username).setLocation(l.p);
						Server.sendLocationToUsers(l);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	public void addLocation(Location l){
		synchronized(toSend){
			toSend.push(l);
		}
	}
	public String getUsername(){return u.getUsername();}
	public void sendLocationToServer(Location l){
		Server.sendLocationToUsers(l);
	}
}
