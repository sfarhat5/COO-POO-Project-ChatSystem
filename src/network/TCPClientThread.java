package network;

import java.io.File;

import data.Interface;

public class TCPClientThread implements ClientThread {

	
	
	public void sendMessageTo(Interface inter, String host, int port, String message) throws Exception {
        Thread t = new Thread(new TCPClient(inter, host, port, message, 1));
        t.start();
    }

    public void sendImageTo(Interface inter, String host, int port, File selectedFile) throws Exception {
        Thread t = new Thread(new TCPClient(inter, host, port, selectedFile, 2));
        t.start();
    }
    
    @Override
	public void sendMessageTo(String host, int port, String message) throws Exception {
    	throw new UnsupportedOperationException("Not supported yet.");

	}

	

}