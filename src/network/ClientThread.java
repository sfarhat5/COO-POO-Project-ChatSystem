package network;


public interface ClientThread {
	
	void sendMessageTo (String host, int port, String message) throws Exception;

}
