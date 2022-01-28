package data;
import java.io.Serializable;
import java.net.*;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String host;
	private String pseudonyme;
	private boolean disconnect;
	private int port;

	private boolean newMessage;
	
	public final static int portTCP = 1345;
	public final static int portUDP = 1234;
	
	
	/* Constructors*/
	public User(String host) throws UnknownHostException {
		this.host = host;
		this.disconnect = false;
		this.port = portTCP;
		this.newMessage = false;
	}
	
	public User(String host, String pseudo) throws UnknownHostException {
		this.pseudonyme = pseudo;
		this.host = host;
		this.disconnect = false;
		this.port = portTCP;
		this.newMessage = false;
	}
	
	public User(String host,String pseudo, int Port) throws UnknownHostException {
		this.pseudonyme = pseudo;
		this.host = host;
		this.disconnect = false;
		this.port = Port;
		this.newMessage = false;
	}
	
	public User(String host,String pseudo, Boolean newMessage) throws UnknownHostException {
		this.pseudonyme = pseudo;
		this.host = host;
		this.disconnect = false;
		this.port = portTCP;
		this.newMessage = newMessage;
	}
	
	/*Methods*/
	
	public void setUserIP(String host) {
		this.host = host;
	}
	public String getHost() {
		return this.host;
	}
	public void setPseudo(String newPseudo) {
		this.pseudonyme = newPseudo;
	}

	public String getPseudo() {
		return this.pseudonyme;
	}
	public int getPort() {
		 return this.port;
	}
	public void setPort(int Port) {
		 this.port = Port;
	}
	public Boolean getStatusNewMessage() {
		return this.newMessage;
	}

	public void setNewMessage(boolean newMsg) {
		this.newMessage = newMsg;
		
	}
	
	public void setDisconnect(boolean status) {
		this.disconnect = status;
	}
	public boolean getDisconnect() {
		return this.disconnect;
	}
	@Override
    public String toString() {
        return this.pseudonyme + " (" + this.host + " : " + this.port + ")";
    }

	
}