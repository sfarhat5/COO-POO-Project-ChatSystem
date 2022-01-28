package data;
import java.net.*;
import java.sql.SQLException;
import java.util.*;
import database.*;

import UI.*;

public class Interface {
	private User user;
	private ArrayList<User> onlineList; 
	private String message = "";
	private HomeForm home;
	private final HashMap<String, ChatWindowForm> chatWindowForUser; //String -> ipAddress
	private final History history = null;

	/* Constructors*/

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Interface(User user) {
		this.user = user;
		this.onlineList = new ArrayList();
		this.home = new HomeForm(this,History.getInstance());
		//this.home = new HomeForm(this);
		this.chatWindowForUser = new HashMap<>();

	}

	/*Methods*/
	public History getHistory() {
		return this.history;
	}
	
	public User getUser() {
		return this.user;
	}
	public String userName(){
		return this.user.getPseudo();
	}
	public ArrayList<User> getOnlineList(){
		return this.onlineList;
	}
	public String getMessage() {
		return this.message;
	}
	public void setMessage(String msg) {
		this.message = msg;
	}
	
	public void update(User newUser) {
        this.user = newUser;
    }
	

	public void addOnlineUser (User newUser) {
		this.onlineList.add(newUser);
	}

	public void removeOfflineUser(User user) {
		if (this.onlineList != null) {
			for (User u : this.onlineList) {
				if (u.getHost().equals(user.getHost()) && u.getPort() == user.getPort() && u.getPseudo().equals(user.getPseudo())) {
					this.onlineList.remove(u);
				}
			}
		}
	}


	public void updateOnlineList(User u) {
		if (u.getHost().equals(this.user.getHost())) {
			return;
		}

		for (User userInList : onlineList) {
			if (userInList.getHost().equals(u.getHost())) {
				System.out.println("[user] This user is already on the list");
				if (userInList.getPseudo().equals(u.getPseudo())) {
				} else {
					System.out.println(" Name change " + userInList.getPseudo() + " -> " + u.getPseudo());
					userInList.setPseudo(u.getPseudo());
					this.message = " Name change " + userInList.getPseudo() + " -> " + u.getPseudo();
				}
				if (u.getDisconnect() == false) {
					System.out.println(" Status:connected ");
					userInList.setDisconnect(false);
				} else {
					System.out.println(" Status:disconnected ");
					userInList.setDisconnect(true);
				}
				if (u.getStatusNewMessage() == false) {
					userInList.setNewMessage(false);
				} else {
					System.out.println(" New Message Status:new message ");
					userInList.setNewMessage(true);
				}
				return;
			}
		}
		System.out.println("[user] New User:UserIP>" + u.getHost() + " Pseudo>" + u.getPseudo());
		this.addOnlineUser(u);

	} 
	public HomeForm getHome() {
		return this.home;
	}

	public void updateHome() throws SQLException {
		this.home.getOnlineList().removeAllElements();
		for (User u : this.getOnlineList()) {
			if (u.getDisconnect() == false) {
				if (u.getStatusNewMessage()) {
					this.home.getOnlineList().addElement("[!] " + u.getPseudo() + ":" + u.getHost());
					System.out.println("u.getPseudo : " + u.getPseudo());
					System.out.println("u.getHost : " + u.getHost());
					
				} else {
					this.home.getOnlineList().addElement(u.getPseudo() + ":" + u.getHost());
				}
				if (!this.existChatWindow(u)) { 
					Message msg = new Message(this.getUser(),u);
					if (History.getInstance().existHistory(msg)) {
						  msg = History.getInstance().getMessage(this.getUser().getHost(), u.getHost());
					}
					else {
						History.getInstance().addHistory((msg));
					}
					ChatWindowForm chatWindow = new ChatWindowForm(this, new Interface(u),msg);
					this.setChatWindowForUser(u, chatWindow);
				}
			}
		}

	}	

	public InetAddress getBroadcast() throws UnknownHostException {

		InetAddress myIpAddress = InetAddress.getByName(this.user.getHost());
		NetworkInterface temp;
		InetAddress iAddr = null;
		try {
			temp = NetworkInterface.getByInetAddress(myIpAddress);
			List<InterfaceAddress> addresses = temp.getInterfaceAddresses();

			for (InterfaceAddress inetAddress : addresses) {
				iAddr = inetAddress.getBroadcast();
			}
			System.out.println("Call in User.getBroadcast : " + iAddr);
			return iAddr;

		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String findPseudobyIP(String IP) {
		String res = "";
		for (User u : this.getOnlineList()) {
			if (u.getHost().equals(IP)) {
				res = u.getPseudo();
			}
		}
		return res;
	}

	public void setChatWindowForUser(User user, ChatWindowForm chatWindow) {
		this.chatWindowForUser.put(user.getHost(), chatWindow);
	}

	public ChatWindowForm getChatWindowForUser(String ipAddress) {
		return this.chatWindowForUser.get(ipAddress);
	}

	public boolean existChatWindow(User user) {
		return this.chatWindowForUser.containsKey(user.getHost());
	}

	public void closeAllChatWindow() {
		for (ChatWindowForm c : chatWindowForUser.values()) {
			c.closeWindow();
		}
	}

	public boolean checkPseudo() {
		Boolean res = true;
		for (User user : this.getOnlineList()) {
			System.out.println(user.getPseudo());
			if (this.getUser().getPseudo().equals(user.getPseudo())) {
				res = false;
			}
		}
		return res;
	}
	
	@Override
    public String toString() {
        String str = "This peer is " + this.user.toString() + " and his friends list :\n";
        for (User u : onlineList) {
            str += "- " + u.toString() + "\n";
        }
        return str;
    }
	










}