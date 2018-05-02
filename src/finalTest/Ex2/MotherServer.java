package finalTest.Ex2;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MotherServer {
	
	ServerSocket server;
	int port,childPort;
	ChildListener childListener;
	
	final int UNAVAILABLE_PORT=-1;
	
	public MotherServer(int port) throws IOException {
		this.port=port;
		server=new ServerSocket(port);
		childPort=UNAVAILABLE_PORT;
		new ChildServer("localhost",port);		
		new ClientListener().start();
	}
	

	
	public class ClientListener extends Thread{

		public void run() {
			
			while(true) {
				try {
					Socket soc=server.accept();
					DataInputStream dis=new DataInputStream(soc.getInputStream());
					DataOutputStream dos =new DataOutputStream(soc.getOutputStream());
					String command=dis.readUTF();
					
					if(command.equals("???????")) {
						childListener=new ChildListener(soc,dis,dos);
						childListener.start();		
						continue;
					}
					
					dos.writeInt(childPort);
					dos.close();
					dis.close();
					soc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		
	}
	
	
	public class ChildListener extends Thread{
		Socket child;
		DataInputStream dis;
		DataOutputStream dos;
		boolean isRun;
		Timer timer;
		public ChildListener(Socket soc,DataInputStream dis,DataOutputStream dos){
			child=soc;
			this.dis=dis;
			this.dos=dos;
			isRun=true;
		}
		
		public void run() {
			
			while(isRun) {
				try {		
					childPort=dis.readInt();
					System.out.println(childPort);
					
					if(timer!=null) {
						timer.cancel();
					}
					
					timer=new Timer();
					timer.schedule(new TimerTask() {
						public void run() {
							try {
								System.out.println("Tam dung");
								childListener.stopListening();
								childPort=UNAVAILABLE_PORT;	
								new ChildServer("localhost",port);				
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}, 2000);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
				
		public void stopListening() throws IOException {
			isRun=false;
			dis.close();
			dos.close();
			child.close();
		}
	}
	
	public static void main(String[] args) {
		try {
			new MotherServer(Integer.parseInt(args[0]));
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
}

