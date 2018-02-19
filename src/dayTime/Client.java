package dayTime;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			Socket soc=new Socket("localhost",9696);
			DataInputStream dis=new DataInputStream(soc.getInputStream());
			System.out.println(dis.readUTF());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
