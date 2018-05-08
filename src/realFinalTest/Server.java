package realFinalTest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;






public class Server {
	ServerSocket server;
	private ArrayList<Worker> waitingWorkers;
	private ArrayList<Worker> workingWorkers;
	private ArrayList<Agent> agents;
	
	public Server(int port) throws IOException {
		server=new ServerSocket(port);
		waitingWorkers=new ArrayList<Worker>();
		workingWorkers=new ArrayList<Worker>();
		agents=new ArrayList<Agent>();
		new Manager().start();
		while(true) {
			try {
				Socket conn = server.accept();
				DataInputStream dis=new DataInputStream(conn.getInputStream());
				byte type=dis.readByte();
				if(type==1) {
					Agent agent=new Agent(conn);
					agent.start();
				}else {
					Worker worker=new Worker(conn);
					worker.start();
					waitingWorkers.add(worker);
				}
				new Worker(conn).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}	
	}	
	
	class Worker extends Thread{
		Socket conn;
		DataOutputStream dos;
		DataInputStream dis;
		Agent agent;
		public Worker(Socket conn) throws IOException{
			this.conn=conn;
			dos=new DataOutputStream(conn.getOutputStream());
			dis=new DataInputStream(conn.getInputStream());
		}
		@Override
		public void run() {	
			while(true) {
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(agent!=null) {
					try {
						dos.writeLong(agent.input);
						boolean ans=dis.readBoolean();
						agent.sendBack(ans);
						agents.remove(agent);
						agent=null;
						workingWorkers.remove(this);
						waitingWorkers.add(this);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	class Agent extends Thread{
		Socket conn;
		DataOutputStream dos;
		DataInputStream dis;
		long input;
		public Agent(Socket conn) throws IOException{
			this.conn=conn;
			dos=new DataOutputStream(conn.getOutputStream());
			dis=new DataInputStream(conn.getInputStream());
		}
		@Override
		public void run() {	
			while(true) {
				
				try {
					String s=dis.readUTF();
					
					long l=Long.parseLong(s);
					input=l;
					agents.add(this);	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		public void sendBack(boolean res) {
			try {
				dos.writeBoolean(res);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	class Manager extends Thread{
		@Override
		public void run() {
			while(true) {
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				while(agents.size()>0&&waitingWorkers.size()>0) {
					Worker worker=waitingWorkers.remove(0);
					Agent agent=agents.remove(0);
					workingWorkers.add(worker);
					worker.agent=agent;
					
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			new Server(6969);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
