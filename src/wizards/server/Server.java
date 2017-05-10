package wizards.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import wizards.constants.ServerConstants;
import wizards.entities.World;
import wizards.messages.Location;

public class Server {

	public static ArrayList<User> users=new ArrayList<User>();
	public static ArrayList<UserThread> uT=new ArrayList<UserThread>();
	public static World world=new World();
	public static final boolean ENABLE_CONSOLE_MESSAGES=true;
	
	public static void main(String[] args) {
		try {
			ServerSocket server=new ServerSocket(ServerConstants.PORT);
			WorldDisplayer wd=new WorldDisplayer();
			Thread tt=new Thread(wd);
			tt.start();
			while(true){
				if(ENABLE_CONSOLE_MESSAGES)System.out.println("Ready to accept socket connections!");
				Socket s=server.accept();
				if(ENABLE_CONSOLE_MESSAGES)System.out.println("Socket connection accepted!");
				
				User user=new User(s,null);
				synchronized(users){
					users.add(user);
					UserThread userThread=new UserThread(user);
					synchronized(uT){
						uT.add(userThread);
					}
					Thread uT=new Thread(userThread);
					uT.start();
				}
				if(ENABLE_CONSOLE_MESSAGES)System.out.println("Socket threads started!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	protected static void disconnectUser(User user,UserThread u){
		synchronized(users){
			users.remove(user);
			synchronized(uT){
				uT.remove(u);
			}
			if(!user.getSocket().isClosed()){
				try {
					user.getSocket().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(user.getUsername()+" has disconnected.");
		}
	}
	public static void sendLocationToUsers(Location l){
		synchronized(uT){
			for(UserThread u:uT){
				if(u.getUsername()==null)continue;
				if(!u.getUsername().equals(l.username)){
					u.addLocation(l);
				}
			}
		}
		
	}
	
	/*public static World getWorld(){
		return world;
	}
	public static void setWorld(World w){
		world=w;
	}*/
	@Deprecated
	private static String getUsername(Socket s) throws IOException{
		Scanner scanner=new Scanner(s.getInputStream());
		while(!scanner.hasNext()){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String username=scanner.nextLine();
		return username;
	}
}
