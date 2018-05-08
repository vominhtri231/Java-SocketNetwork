package realFinalTest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {
	Socket soc;
	DataInputStream dis;
	DataOutputStream dos;
	Scanner scanner;
	public Client() throws UnknownHostException, IOException {
		soc=new Socket("localhost",6969);
		dis=new DataInputStream(soc.getInputStream());
		dos=new DataOutputStream(soc.getOutputStream());
		dos.writeByte(1);
		scanner=new Scanner(System.in);
		while(true){
			getInput();
		}
	}
	public void getInput() throws IOException{
		String s=scanner.nextLine();
		dos.writeUTF(s);
		boolean out=dis.readBoolean();
		System.out.println(out);
	}
	
	
	
	
	public static void main(String[] args) {
		try {
			new Client();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
