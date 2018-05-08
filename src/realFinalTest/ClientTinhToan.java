package realFinalTest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientTinhToan {
	Socket soc;
	DataInputStream dis;
	DataOutputStream dos;
	public ClientTinhToan() throws UnknownHostException, IOException {
		soc=new Socket("localhost",6969);
		dis=new DataInputStream(soc.getInputStream());
		dos=new DataOutputStream(soc.getOutputStream());
		dos.writeByte(2);
		while(true){
			try {
				long input=dis.readLong();
				dos.writeBoolean(isPrime(input));
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	private boolean isPrime(long input) {
		for(int i=2;i*i<=input;i++) {
			if(input%i==0) return false;
		}
		return true; 
	}
	
	public static void main(String[] args) {
		try {
			new ClientTinhToan();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	

}
