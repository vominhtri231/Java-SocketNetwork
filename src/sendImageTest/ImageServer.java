package sendImageTest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;


public class ImageServer {
	ServerSocket serverSoc;
	ImageServer() throws IOException{
		serverSoc=new ServerSocket(9696);
		new Listener().start();
	}
	
	class Listener extends Thread{
		public void run() {
			while(true) {
				try {
					Socket soc=serverSoc.accept();
					Worker worker=new Worker(soc);
					worker.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class Worker extends Thread{
		Socket soc;
		DataInputStream dis;
		DataOutputStream dos;
		Worker(Socket soc) throws IOException{
			this.soc=soc;
			dis=new DataInputStream(soc.getInputStream());
			dos=new DataOutputStream(soc.getOutputStream());
		}
		
		public void run() {
			try {	
				Byte commad=dis.readByte();
				if(commad==1) {
					sendImage();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}

		private void sendImage() throws IOException {
			BufferedImage image=ImageIO.read(getClass().getResource("/ima/main.jpg"));
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", bos);
			int size=bos.size();
			dos.writeInt(size);
			dos.write(bos.toByteArray());
		}
	}
	
	public static void main(String[] args) {
		try {
			new ImageServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
