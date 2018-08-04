package Chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

//import org.lanqiao.ui.ChatWindow;

public class GetMessageThread extends Thread{

	public static int port=8888;
	DatagramSocket ds=null;
	DatagramPacket dp=null;
	/*
	 * ���췽������UDP�˿�
	 */
	ChatWindow cw;
	public GetMessageThread(ChatWindow cw)
	{
		this.cw=cw;
		try {
			ds=new DatagramSocket(port);
			
		} catch (SocketException e) {
			try {
				port+=1;
				ds=new DatagramSocket(port);
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void run()
	{
		while(true)//һֱ������Ϣ
		{
			byte by[]=new byte[1024*8];
			dp=new DatagramPacket(by,by.length);
			try {
				ds.receive(dp);
				String message=new String(by,0,dp.getLength());
				cw.ta.append(message+"\n");
				cw.ta.setCaretPosition(cw.ta.getText().length());
				if(message.contains("������"))
				{
					message=message.replace("������","");
					cw.cb.addItem(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}