package udpTest;

import java.net.*;
import java.util.Scanner;

public class UDPClient {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			
			DatagramSocket soc=new DatagramSocket(6969);
			soc.setSoTimeout(1000);
			Scanner scan=new Scanner(System.in);
			String st=scan.nextLine();
			DatagramPacket pa=new DatagramPacket(st.getBytes(),st.length(),InetAddress.getByName("localhost"),9696);
			soc.send(pa);
			byte[] b=new byte[2000];
			DatagramPacket pac=new DatagramPacket(b,b.length);
			soc.receive(pac);
			String s=new String(pac.getData()).substring(0,pac.getLength());
			System.out.println(s);
		}catch(Exception e) {
			
		}
	}
}
