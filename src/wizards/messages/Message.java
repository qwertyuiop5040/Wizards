package wizards.messages;

import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = -4345638032407090561L;
	private String message=null;
	public Message(String s){
		message=s;
	}
	public Message(String tag, String s){
		if(tag!=null&&s!=null)message=tag+" "+s;
		else if(s!=null)message=s;
		else if(tag!=null)message=tag;
	}
	public String getMessage(){return message;}
}
