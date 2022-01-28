package network;

import java.net.*;
import java.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import data.User;
import data.Interface;
//import data.Message;
import java.io.ByteArrayOutputStream;

public class TCPClient implements Runnable {

	private Socket chatSocket;
	private final String message;
	private final String host;
	private PrintWriter output;
	private final int type;
	private final int port;
	private final File file;
	private Interface inter;



	public TCPClient(Interface inter, String host, int port, String message, int type) throws IOException {
		this.inter = inter;
		this.host = host;
		this.port = port;
		this.message = message;
		this.type = type;
		this.file = null;
	}

	public TCPClient(Interface inter, String host, int port, File selectedFile, int type) throws IOException {
		this.inter = inter;
		this.host = host;
		this.port = port;
		this.file = selectedFile;
		this.message = selectedFile.getName();
		this.type = type;
	}

	public static String encodeToString(BufferedImage image, String type) {
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, type, bos);
			byte[] imageBytes = bos.toByteArray();
			imageString = Base64.getEncoder().encodeToString(imageBytes);

			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageString;
	}



	public void run() {
		try {
			switch (type) {
			case 1: //Text Message
				/* Request a connection to the given User  */
				System.out.println("connecting to port " + port + " and host " + host);
				chatSocket = new Socket(host, User.portTCP);
				/* Initialization the output channel */
				this.output = new PrintWriter(chatSocket.getOutputStream());

				/* Send the message...*/
				output.println(message + ":" + this.inter.getUser().getPseudo() + ":" + this.inter.getUser().getPort());
				output.flush();
				/* Close the socket */
				chatSocket.close();
				break;
			case 2: //Image Message
				String path = this.file.getAbsoluteFile().toString();
				String[] split = path.split("[.]");
				String ext = split[1];
				System.out.println("Sending: " + path + " extension: " + ext);
				System.out.println("Sending Image!");

				chatSocket = new Socket(host, User.portTCP);

				BufferedImage bimg = ImageIO.read(this.file);
				String imgAsString = encodeToString(bimg, ext);

				this.output = new PrintWriter(chatSocket.getOutputStream());
				output.println(imgAsString + ":" + this.inter.getUser().getPseudo() + ":" + ext + ":" + this.file.getName());
				System.out.println("FILE NAME: " + this.file.getName());
				output.flush();
				chatSocket.close();
				break;
			default:
				System.out.println("Invalid type of message");
				break;
			} 
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
