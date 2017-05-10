package wizards.deprecated;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import wizards.client.Client;
import wizards.client.WorldGUI;
import wizards.constants.Tags;
import wizards.entities.PointF;
import wizards.messages.Location;
import wizards.messages.Message;

public class ClientOutputThread implements Runnable {

	private static final boolean ENABLE_CONSOLE_MESSAGES=true;
	private Socket socket;
	private ObjectOutputStream oos;
	private String username;
	public ClientOutputThread(Socket s, String u) {
		username=u;
		socket=s;
		try {
			oos=new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new Message(Tags.USERNAME,username));
			oos.flush();
			if(ENABLE_CONSOLE_MESSAGES)System.out.println("OutputObjectStream connected!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		boolean keepGoing=true;
		while(keepGoing){
			try{
				Thread.sleep(20);
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
