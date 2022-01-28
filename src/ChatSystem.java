import data.*;
import UI.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.InterfaceAddress;
import java.net.SocketException;
import java.util.Enumeration;

public class ChatSystem {
	public static void main(String[] args) throws Exception {
	
		String ip = getLocalAddress().getHostAddress();
		Interface inter = new Interface(new User(ip));
		LoginForm loginWindow = new LoginForm(inter);
		loginWindow.display();
	}

	private static InetAddress getLocalAddress() {
		try {
			Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
			while (b.hasMoreElements()) {
				for (InterfaceAddress f : b.nextElement().getInterfaceAddresses()) {
					if (f.getAddress().isSiteLocalAddress()) {
						return f.getAddress();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}


}
