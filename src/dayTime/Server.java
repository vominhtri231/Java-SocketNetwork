package dayTime;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Vector;

public class Server { 
	private final int MAX_WORKER=10;
	private ServerSocket server;
	private Vector<Worker> waitingWorkers;
	private Vector<Worker> workingWorkers;
	
	public Server() throws IOException {
		server=new ServerSocket(9696);
		waitingWorkers=new Vector<Worker>();
		workingWorkers=new Vector<Worker>();
		new Listener().start();
		new Manager().start();
	}
	
	
	class Worker extends Thread{
		Socket conn;
		public Worker(Socket conn){
			this.conn=conn;
		}
		@Override
		public void run() {
			try {
				DataOutputStream dos=new DataOutputStream(conn.getOutputStream());
				Thread.sleep(5000);
				String day=new Date().toString();
				dos.writeUTF("Time:"+day);
				conn.close();
				workingWorkers.remove(this);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class Listener extends Thread{
		@Override
		public void run() {
			while(true) {
				try {
					Socket conn=server.accept();
					waitingWorkers.add(new Worker(conn));
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
			
	}
	
	class Manager extends Thread{
		@Override
		public void run() {
			while(true) {
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				while(workingWorkers.size()<MAX_WORKER&&waitingWorkers.size()>0) {
					Worker worker=waitingWorkers.remove(0);
					workingWorkers.add(worker);
					worker.start();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			new Server();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
