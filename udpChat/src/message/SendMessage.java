package message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SendMessage {

	public static void sendMessage(String message,String ip,int port)
	{
		DatagramSocket ds;
		try {
			ds = new DatagramSocket();
			DatagramPacket dp = new DatagramPacket(
					message.getBytes(), 0, message
							.getBytes().length);
			// 192.168.2.220
			byte ipByte[] = new byte[4];
			String str[] = ip.split("\\.");

			ipByte[0] = (byte) Integer.parseInt(str[0]);
			ipByte[1] = (byte) Integer.parseInt(str[1]);
			ipByte[2] = (byte) Integer.parseInt(str[2]);
			ipByte[3] = (byte) Integer.parseInt(str[3]);

			dp.setAddress(InetAddress
					.getByAddress(ipByte));// …Ë÷√IP
			dp.setPort(port);// …Ë÷√∂Àø⁄
			ds.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}