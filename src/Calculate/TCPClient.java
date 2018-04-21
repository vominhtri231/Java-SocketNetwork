package Calculate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class TCPClient {
	Socket soc;
	DataInputStream dis;
	DataOutputStream dos;
	Scanner scanner;
	Listener listener;
	TCPClient() throws UnknownHostException, IOException{
		soc=new Socket("localhost",9696);
		dis=new DataInputStream(soc.getInputStream());
		dos=new DataOutputStream(soc.getOutputStream());
		listener=new Listener();
		listener.start();
		scanner=new Scanner(System.in);
		while(true) {
			String s=scanner.nextLine();
			if(s==null||s.length()==0) break;
			dos.writeUTF(s);
		}
		listener.stop=true;
	}
	class Listener extends Thread{
		
		boolean stop=false;
		public void run() {
			while(true) {
				if(stop) break;
				try {
					double ans=dis.readDouble();
					System.out.println(ans);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			new TCPClient();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
