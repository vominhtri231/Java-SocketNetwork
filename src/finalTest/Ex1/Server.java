package finalTest.Ex1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Hashtable;




public class Server {
	ServerSocket server;
	Date time;
	ClientListener listener;

	
	public Server(int port) throws IOException {
		server=new ServerSocket(port);
		time=new Date();
		listener=new ClientListener();
		listener.start();	
	}
	
	
	
	class ClientListener extends Thread{
		boolean isRun;
		ClientListener(){
			isRun=true;
		}
		@Override
		public void run() {
			while(isRun) {
				Socket conn;
				try {
					conn = server.accept();
					new Worker(conn).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		public void stopListening() throws IOException {
			isRun=false;
			server.close();
		}
	}
	
	class Worker extends Thread{
		Socket conn;
		DataOutputStream dos;
		DataInputStream dis;
		public Worker(Socket conn) throws IOException{
			this.conn=conn;
			dos=new DataOutputStream(conn.getOutputStream());
			dis=new DataInputStream(conn.getInputStream());
		}
		@Override
		public void run() {
			try {
				String input=dis.readUTF();
				int pos=input.indexOf("@@@");
				
				if(pos<0) {
					dos.writeUTF("invalid");
					close();
					return;
				}
				
				String message=input.substring(0,pos);
				String data=input.substring(pos+3);
				
				switch(message) {
					case "time":
						sendTime();
						break;
					case "fre":
						sendFre(data);
						break;
					case "stop":
						end();
						break;
					default:
						dos.writeUTF("invalid");
				}
				close();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		private void sendTime() throws IOException {
			dos.writeUTF(""+(new Date().getTime()-time.getTime()));
		}
		
		private void sendFre(String data) throws IOException {		
			Hashtable<Character,Integer> table=new Hashtable<Character,Integer>();
			char mostAppear='@';int maxFre=-1;
			for(int i=0;i<data.length();i++) {
				char c=data.charAt(i);
				int fre=1;
				if(table.containsKey(c)) {
					fre=table.get(c);
					fre++;
				}
				table.put(c,fre);
				if(fre>maxFre) {
					maxFre=fre;
					mostAppear=c;
				}
			}
			
			dos.writeUTF(mostAppear+"-"+maxFre);
		}
		
		private void end() throws IOException {
			listener.stopListening();
			dos.writeUTF("end");
		}
		
		private void close() throws IOException {
			dos.close();
			dis.close();
			conn.close();
		}
	}
	
	public static void main(String[] args) {
		try {
			new Server(Integer.parseInt(args[0]));
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
}