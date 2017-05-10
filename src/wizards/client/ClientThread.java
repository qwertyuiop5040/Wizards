package wizards.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

import wizards.constants.Tags;
import wizards.entities.*;
import wizards.messages.Location;
import wizards.messages.Message;
import wizards.server.Server;


public class ClientThread implements Runnable{
	private static final boolean ENABLE_CONSOLE_MESSAGES=true;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private String username;
	private ClientOutputThread cot=new ClientOutputThread();
	private Thread outputThread;
	private boolean keepGoing=true;
	
	public ClientThread(Socket s, String username){
		socket=s;
		try {
			oos=new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new Message(Tags.USERNAME,username));
			oos.flush();
			if(ENABLE_CONSOLE_MESSAGES)System.out.println("OutputObjectStream connected!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.username=username;
		Client.cGUI.getWorldGUI().setUsername(username);
		outputThread=new Thread(cot);
		outputThread.start();
	}

	public void run() {
		try {
			ois=new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		int a=0;
		while(keepGoing){
			System.out.println(++a);
			try {
				Object fromServer=ois.readObject();
				if(fromServer instanceof Message){
					String message=((Message)fromServer).getMessage();
				}else if(fromServer instanceof World){
					World world=((World)fromServer);
					for(Entry<String, Avatar> entry:world.getAvatars().entrySet()){
						System.out.println("User: "+entry.getKey()+" at"+ entry.getValue().getLocation());
					}
					Client.cGUI.updateWorldGUI(world);
					System.out.println(world==null);
					if(ENABLE_CONSOLE_MESSAGES)System.out.println("Received world from server!");
				}else if(fromServer instanceof Location){
					WorldGUI tempWorld=Client.cGUI.getWorldGUI();
					Location temp=((Location)(fromServer));
					if(!username.equals(temp.username)&&Client.cGUI.getWorldGUI()!=null&&tempWorld.getWorld()!=null){
						if(ENABLE_CONSOLE_MESSAGES)System.out.println(temp);
						Client.cGUI.updateWorldGUI(temp);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				if(e instanceof SocketException)return;
			}
		}
	}

	public static boolean socketValid(Socket s) throws IOException{
		if(s.isClosed()||!s.isConnected()||!s.isBound()){
			s.close();
			return false;
		}
		return true;
	}
	
	private class ClientOutputThread implements Runnable{
		public void run() {
			while(keepGoing){
				try{
					Thread.sleep(25);
					WorldGUI temp=Client.cGUI.getWorldGUI();
					if(temp!=null&&temp.getAvatar()!=null){
						PointF aLoc=temp.getAvatar().getLocation();
						if(aLoc!=null){
							if(!aLoc.equals(temp.getWorld().getAvatars().get(username).getLocation())){
								Location temp2=new Location(username,new PointF(aLoc.x,aLoc.y));
								Client.cGUI.getWorldGUI().updateAvatarLocation(temp2);
								oos.writeObject(temp2);
								oos.flush();
								if(ENABLE_CONSOLE_MESSAGES)System.out.println("c"+temp2.p);
							}
						}
					}
				}catch (IOException e) {
					e.printStackTrace();
					if(e instanceof SocketException)return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		public void sendMessageToServer(String s) throws IOException{
			sendMessageToServer(null,s);
		}
		public void sendMessageToServer(String tag,String s) throws IOException{
			Message message=new Message(tag,s);;
			oos.writeObject(message);
			oos.flush();
			if(ENABLE_CONSOLE_MESSAGES)System.out.println("Message sent to server "+message.getMessage());
		}
	}
}
