package wizards.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import wizards.constants.ServerConstants;
import wizards.constants.Tags;

public class Client {
	public static ClientGUI cGUI;
	private static ClientThread cT;
	public static void main(String[] args) {
		cGUI=new ClientGUI();
	}

	public static void connect(InetAddress ip, String username) throws IOException {
		Socket user=new Socket(ip,ServerConstants.PORT);
		cGUI.goToWorld();
		cT=new ClientThread(user,username);
		Thread t=new Thread(cT);
		t.start();
	}
	public static boolean socketValid(Socket s) throws IOException{
		if(s.isClosed()||!s.isConnected()||!s.isBound()){
			s.close();
			return false;
		}
		return true;
	}
	public static boolean sendMessageToServer(String s, Socket socket) throws IOException{
		if(!socketValid(socket))return false;
		PrintWriter pw=new PrintWriter(socket.getOutputStream());
		pw.write(s);
		pw.flush();
		System.out.println("Sent message to server: "+s+"\n");
		return true;
	}
}
