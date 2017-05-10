package wizards.server;

import java.util.Map.Entry;

import wizards.entities.Avatar;

public class WorldDisplayer implements Runnable{
	public boolean ENABLE_CONSOLE_MESSAGES=false;
	public void run() {
		while(true){
			if(this.ENABLE_CONSOLE_MESSAGES)
				synchronized(Server.world){
					for(Entry<String, Avatar> entry:Server.world.getAvatars().entrySet()){
						System.out.println("user "+entry.getValue().getLocation()+" "+entry.getKey());
					}
				}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
