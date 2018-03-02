package udpTest;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPserver {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			DatagramSocket soc=new DatagramSocket(9696);
			byte[] b=new byte[2000];
			while(true) {
				DatagramPacket pac=new DatagramPacket(b,b.length);
				soc.receive(pac);
				String s=new String(pac.getData()).substring(0,pac.getLength());
				s=s.toUpperCase();
				DatagramPacket sent=new DatagramPacket(s.getBytes(), s.length(), pac.getAddress(), pac.getPort()); 
				soc.send(sent);
			}
			
 		}catch(Exception e) {
			
		}
	}
}
