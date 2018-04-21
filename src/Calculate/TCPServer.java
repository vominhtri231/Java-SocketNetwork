package Calculate;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;





public class TCPServer {
	ServerSocket serverSoc;
	TCPServer() throws IOException{
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
			
			while(true) {
				String expression;
				try {
					expression = dis.readUTF();
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
				try {
					cal(expression);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
		}

		private void cal(String expression) throws IOException {
			double ans=Calculater.myCalculate(expression);
			dos.writeDouble(ans);
		}

	}
	
	public static void main(String[] args) {
		try {
			new TCPServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
