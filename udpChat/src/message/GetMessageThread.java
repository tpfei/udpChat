package message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import ui.ChatWindow;

public class GetMessageThread extends Thread {

	public static int port = 65188;
	DatagramSocket ds = null;
	DatagramPacket dp = null;
	/*
	 * ���췽������UDP�˿�
	 */
	ChatWindow cw;

	public GetMessageThread(ChatWindow cw) {
		this.cw = cw;
		while (true) {
			try {
				ds = new DatagramSocket(port);
				break;// ʲôʱ��ִ�У�
			} catch (SocketException e) {
				port += 1;
				continue;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void run() {
		while (true)// һֱ������Ϣ
		{
			byte by[] = new byte[1024 * 8];
			dp = new DatagramPacket(by, by.length);
			try {
				ds.receive(dp);
				String message = new String(by, 0, dp.getLength());
				cw.ta.append(message + "\n");
				cw.ta.setCaretPosition(cw.ta.getText().length());
				if (message.contains("������")) {
					message = message.replace("������", "");
					cw.cb.addItem(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}