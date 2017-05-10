package wizards.server;

import java.net.Socket;

public class User {

	private Socket s;
	private String username;
	
	public User(){
		this(null,null);
	}
	public User(Socket s, String username){
		this.s=s;
		this.username=username;
	}
	public String getUsername(){return username;}
	public void setString(String username){this.username=username;}
	public Socket getSocket(){return s;}
}
