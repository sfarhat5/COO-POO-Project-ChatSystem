package network;
import java.net.*;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.*;

import data.Interface;
//import data.Message;
import data.User;
import database.*;

public class TCPServer implements Runnable {

	private ServerSocket serverSocket = null;
	private Socket chatSocket;
	private Interface inter;
	private boolean running = true;
	@SuppressWarnings("unused")
	private static History history;

	public TCPServer (Interface inter, History history) throws IOException {	
		this.inter = inter;
		TCPServer.history = History.getInstance();
		this.serverSocket = new ServerSocket(User.portTCP);
		this.inter.getUser().setPort(this.serverSocket.getLocalPort());
	}

	public void terminate() throws IOException {
		running = false;
		this.serverSocket.close();
	}
	
	public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            imageByte = Base64.getDecoder().decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
	
	  public void initializeElements(String filename, String pseudo, BufferedImage img, String ext) {
	        JFrame frame = new JFrame();
	        frame.setTitle(filename + " by: " + pseudo);

	        JMenuBar menubar = new JMenuBar();
	        JMenu menu = new JMenu("File");
	        JMenuItem down = new JMenuItem("Download");
	        menu.add(down);
	        menubar.add(menu);
	        frame.setJMenuBar(menubar);

	        down.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent e) {
	                // Download file
	                JFileChooser fileChooser = new JFileChooser();
	                fileChooser.setDialogTitle("Download");
	                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	                int returnValue = fileChooser.showOpenDialog(null);
	                if (returnValue == JFileChooser.APPROVE_OPTION) {
	                    File selectedFile = fileChooser.getSelectedFile();
	                    System.out.println(selectedFile);
	                    File outputfile = new File(filename);
	                    try {
	                        File path = new File(selectedFile.toString() + "/" + outputfile.toString());
	                        ImageIO.write(img, ext, path);
	                    } catch (IOException ex) {
	                        Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
	                    }

	                }

	            }
	        });

	        frame.getContentPane().add(new JScrollPane(new JLabel(new ImageIcon(img))));
	        frame.pack();
	        if (img.getHeight() >= 800 || img.getWidth() >= 800) {
	            frame.setSize(new Dimension(800, 800));
	        }
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	    }


	public void run() {
		try {
			while (running) {
				System.out.println("[TCP] " + inter.getUser().getPseudo() + " is listening by TCP at port " + inter.getUser().getPort() + "...");
				this.chatSocket = this.serverSocket.accept();

				/* Receive the message */
				InputStream input = chatSocket.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(input));
				String message = in.readLine();

				System.out.println(message);
                Interface client = new Interface(new User(chatSocket.getInetAddress().getHostAddress()));
				/* Write the message on the chat window between this inter and client */
				//Interface client = new Interface (new User(chatSocket.getInetAddress().getHostAddress()));
                if (message.charAt(0) == "[".charAt(0)) {
                    /* Write the message on the chat window between this inter and client */

                    String seg[] = message.split(":");
                    System.out.println("TCP Server seg[0] :" +seg[0] );
                    System.out.println("TCP Server seg[1] :" +seg[1] );
                    System.out.println("TCP Server seg[2] :" +seg[2] );
                    System.out.println("TCP Server seg[3] :" +seg[3] );
                   
                    client.getUser().setPseudo(seg[2]);
                    System.out.println("TCP Server run client.getUser.getHost" + client.getUser().getHost());
                    if (!this.inter.getChatWindowForUser(client.getUser().getHost()).isVisible()) {
                        this.inter.updateOnlineList(new User(client.getUser().getHost(),client.getUser().getPseudo(),  true));
                        this.inter.updateHome();
                    }

                    if (message != null) {
                        this.inter.getChatWindowForUser(client.getUser().getHost()).write(seg[0] + seg[1]);
                        this.inter.getChatWindowForUser(client.getUser().getHost()).setTitle(client.getUser().getPseudo() + ": Chat");
                        System.out.println(seg[0] + seg[1]);
                    }
                } else {
                    String seg[] = message.split(":", 4);
                    System.out.println(seg[3]);
                    this.inter.getChatWindowForUser(client.getUser().getHost()).write("[" + seg[1] + "]" + " " + seg[3]);
                    BufferedImage img = decodeToImage(seg[0]);

                    initializeElements(seg[3], seg[1], img, seg[2]);

                }
			
			}
		}
		catch (IOException | SQLException e) {
            e.printStackTrace();
        }
	}
}