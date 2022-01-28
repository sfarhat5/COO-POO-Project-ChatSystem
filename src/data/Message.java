package data;
import java.io.Serializable;
import java.util.*;
@SuppressWarnings("serial")
public class Message implements Serializable {
	private User sender = null;
	private User receiver = null;
	private String senderHost;
	private String receiverHost;
	private int receiverPort;
	private ArrayList<String> message;
	private int ind = 0;
	private Date date = null;
	
	
	public Message(User from, User to) {
		this.sender = from;
		this.receiver = to;
		this.senderHost = from.getHost();
		this.receiverHost= to.getHost();
		this.message = new ArrayList<>(1000);
		
	}
	
	public Message(String sender, String reciever) {
		this.senderHost = sender;
		this.receiverHost= reciever;
		this.message = new ArrayList<>(1000);
	}
	
	public User getSender() {
		return this.sender;
	}

	public User getReceiver() {
		return this.receiver;
	}
	
	public String getSenderHost() {
		return this.senderHost;
	}

	public String getReceiverHost() {
		return this.receiverHost;
	}
	
	public void addMessage(String message) {
            this.message.add(message);
            ind++;
    }
	
	public ArrayList<String> getMessage() {
        return this.message;
    }
	
	public int getReceiverPort() {
        return this.receiverPort;
    }
	
	public Date getDate() {
		return this.date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
    public String toString() {
        String res = "";
        for (int i = 0; i < ind; i++) {
            res = res + this.message.get(i) + System.lineSeparator();
        }
        return res;
    }
	
	
	
	

}