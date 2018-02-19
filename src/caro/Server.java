package caro;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	private ServerSocket server;
	private int num;
	private ArrayList<Socket> socs;
	public Server() throws IOException {
		server=new ServerSocket(9696);
		socs=new ArrayList<Socket>();
		num=20;
		new Listener().start();
		new Manager().start();
	}
	
	private class Listener extends Thread{
		public void run() {
			try {
				while(true) {
					Socket soc=server.accept();
					socs.add(soc);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class Manager extends Thread{
		
		public void run() {
			while(true) {
				try {
					Thread.sleep(300);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
				
				while(socs.size()>1) {
					Socket soc1=socs.remove(socs.size()-1),soc2=socs.remove(socs.size()-1);
				    new Match(new Socket[] {soc1,soc2});
				}
			}
			
			
			
		}
	}
	
	private class Match {
		private  Worker[] workers;
		private int[][] map;
		private ArrayList<Point> overs;
		private Match(Socket[] socs) {
			workers = new Worker[2];
			workers[0] = new Worker(socs[0],0);
			workers[1] = new Worker(socs[1],1);
			map=new int[num][num];
			overs=new ArrayList<Point>();
			workers[0].start();
			workers[1].start();
		}
		private class Worker extends Thread{
			
			int turn;
			DataOutputStream dos;
			DataInputStream dis;
			
			private Worker(Socket soc,int turn) {
				this.turn=turn;
				
				try {
					dis=new DataInputStream(soc.getInputStream());
					dos=new DataOutputStream(soc.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			public void run() {
				
				try {
					while(true) {
						
						int x,y;
						try{
							x=dis.readInt();
							y=dis.readInt();
						}catch(IOException e) {
							DataOutputStream dos=workers[1-turn].dos;
							dos.writeInt(-10);
							break;
						}
						
						if(overs.size()%2!=turn) continue;
						if(x<0||x>=num||y<0||y>=num) continue;
						overs.add(new Point(x,y));
						map[x][y]=turn+1;
						announce(x,y);
						if(checkWin(x,y)) {
							// lose
							DataOutputStream dos=workers[1-turn].dos;
							dos.writeInt(-7);
							// win
							dos=this.dos;
							dos.writeInt(-10);						
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			private void announce(int x,int y) throws IOException {
				for(int i=0;i<2;i++) {
					DataOutputStream dos=workers[i].dos;
					dos.writeInt(x);
					dos.writeInt(y);				
				}
			}
			
			final int[][] dir= {{1,1},{1,-1},{1,0},{0,1}};
			private boolean checkWin(int x,int y) {
				for(int i=0;i<dir.length;i++) {
					int len=1;
					for(int j=1;j<5
							&&x+j*dir[i][0]>=0&&x+j*dir[i][0]<num
							&&y+j*dir[i][1]>=0&&y+j*dir[i][1]<num
							&&map[x][y]==map[x+j*dir[i][0]][y+j*dir[i][1]];j++) len++;
					for(int j=-1;j>-5
							&&x+j*dir[i][0]>=0&&x+j*dir[i][0]<num
							&&y+j*dir[i][1]>=0&&y+j*dir[i][1]<num
							&&map[x][y]==map[x+j*dir[i][0]][y+j*dir[i][1]];j--) len++;
					if(len>4) return true;
				}
				return false;
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
 