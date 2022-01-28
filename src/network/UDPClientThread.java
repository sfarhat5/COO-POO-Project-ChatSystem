package network;

import java.net.UnknownHostException;
import data.*;

public class UDPClientThread implements ClientThread {
	
	public void sendBroadcast(Interface inter) throws UnknownHostException{
        Thread t = new Thread (new UDPClient(inter, "broadcast"));
        t.start();
    }
    
    public void sendDisconnect(Interface inter) throws UnknownHostException{
        Thread t = new Thread (new UDPClient(inter, "disconnect"));
        t.start();
    }
    
    public void sendRename(Interface inter) throws UnknownHostException{
        Thread t = new Thread (new UDPClient(inter, "rename"));
        t.start();
    }

	@Override
	public void sendMessageTo(String host, int port, String message) throws Exception {
		Thread t = new Thread (new UDPClient(host,port,message));
        t.start();
	}

}