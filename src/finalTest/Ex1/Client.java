package finalTest.Ex1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	String address;
	int port;
	Scanner scan;
	public Client(String address,int port) throws UnknownHostException, IOException{
		this.address=address;
		this.port=port;
		scan=new Scanner(System.in);
		Communicate();
	}
	
	
	public void Communicate() throws IOException {
		while(true) { 
			System.out.print("Lenh :");
			String command=scan.nextLine();
			if(command.length()==0) break;
			String response=sendRequest(command);
			System.out.println("Tra loi :"+response);
		}
	}
		

	
	public String sendRequest(String command) {
		String response=""; 
		try {
			Socket child=new Socket(this.address,port);
			DataOutputStream cDos=new DataOutputStream(child.getOutputStream());
			DataInputStream cDis=new DataInputStream(child.getInputStream());
			cDos.writeUTF(command);
			response=cDis.readUTF();
			cDos.close();
			cDis.close();
			child.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	
	public static void main(String[] args) {
		try {
			new Client(args[0],Integer.parseInt(args[1]));
		} catch ( NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}

