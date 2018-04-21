package sendImageTest;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ImageClient extends JFrame{
	Socket soc;
	DataInputStream dis;
	DataOutputStream dos;
	BufferedImage ima;
	ImageClient() throws IOException{
		 soc=new Socket("localhost",9696);
		 dis=new DataInputStream(soc.getInputStream());
		 dos=new DataOutputStream(soc.getOutputStream());
		 new Listener().start();
		 dos.writeByte(1);
		 
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setLayout(null);
		 this.setSize(500, 500);
		 this.setVisible(true);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if(ima!=null)
			g.drawImage(ima, ima.getHeight(), ima.getWidth(),null);
	}
	
	private ImageClient getOutter() {
		return this;
	}
	
	class Listener extends Thread{
		
		public void run() {
			try {
				int size=dis.readInt();
				byte[] buff=new byte[size];
				dis.read(buff);
				ima=ImageIO.read(new ByteArrayInputStream(buff));
				
				getOutter().repaint();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			new ImageClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
