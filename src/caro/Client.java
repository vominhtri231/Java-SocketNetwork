package caro;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Client extends JFrame implements MouseListener{
	Socket soc;
	int size=30;
	int num=20;
	int margin=30;
	ArrayList<Point> overs=new ArrayList<Point>();
	DataOutputStream dos;
	DataInputStream dis;
	Listener listener;
	public Client() throws UnknownHostException, IOException {
		soc=new Socket("localhost",9696);
		dos=new DataOutputStream(soc.getOutputStream());
		dis=new DataInputStream(soc.getInputStream());
		this.setTitle("co caro");
		this.setSize(margin*2+num*size,margin*2+num*size);
		this.setLocationRelativeTo(null);
		this.addMouseListener(this);
		this.setDefaultCloseOperation(3);
		this.setResizable(false);
		this.setVisible(true);
		listener=new Listener();
		listener.start();
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		for(int i=0;i<=num;i++) {
			g.drawLine(margin, margin+i*size,margin+num*size , margin+i*size);
			g.drawLine(margin+i*size,margin,margin+i*size,margin+num*size  );
		}
		
		g.setFont(new Font("Arial",Font.BOLD , size));
		for(int i=0;i<overs.size();i++) {
			String str;
			if(i%2<1) {
				g.setColor(Color.BLUE);
				str="O";
			}else {
				g.setColor(Color.RED);
				str="X";
			}
			g.drawString(str, overs.get(i).x*size+margin+size/8, overs.get(i).y*size+margin+3*size/4+size/8);
		}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		int x=e.getX();
		int y=e.getY();
		if(x<margin||x>=margin+num*size||y<margin||y>=margin+num*size) return;
		int xi=(x-margin)/size,yi=(y-margin)/size;
		Point press=new Point(xi,yi);
		if(!overs.contains(press)) {
			try {
				dos.writeInt(xi);
				dos.writeInt(yi);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		this.repaint();
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	private Client getOuter() {
		return this;
	}
	
	class Listener extends Thread{
		boolean stop=false;
		public void run() {
			while(true) {
				if(stop) break;
				try {
					int x=dis.readInt();
					if(x<0) {
						if(x==-7) announce(" defeat ");
						else announce(" victory ");
						break;
					}
					int y=dis.readInt();
					overs.add(new Point(x,y));
					getOuter().repaint();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void announce(String s) throws IOException {
		JOptionPane.showMessageDialog(this, s);
		dos.close();
		dis.close();
		soc.close();
		listener.stop=true;
		this.dispose();
	}
	
	public static void main(String[] args) {
		try {
			new Client();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
