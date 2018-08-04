package adapter;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import message.SendMessage;
import ui.ChatWindow;
import util.DBConnection;

public class MyKeyAdapter extends KeyAdapter {

	ChatWindow cw;
	String username;

	public MyKeyAdapter(ChatWindow cw, String username) {
		this.cw = cw;
		this.username = username;
	}

	@SuppressWarnings("unused")
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			String messageTo = (String) cw.cb.getSelectedItem();
			String message = cw.tf.getText();
			Connection conn = null;
			try {
				conn = DBConnection.getConn();
				if (messageTo.equals("All")) {
					String sql = "SELECT * FROM tb_online_user";
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql);// 执行查询
					while (rs.next()) {
						String ip = rs.getString("IP");
						int port = rs.getInt("UDPPORT");
						String name = rs.getString("USERNAME");
						if (!name.equals(username)) {
							SendMessage.sendMessage(message, ip, port);
						}
					}
				} else// 私聊
				{
					String sql = "SELECT * FROM tb_online_user WHERE username=?";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					// 给？赋值
					pstmt.setString(1, (String) cw.cb.getSelectedItem());
					ResultSet rs = pstmt.executeQuery();// 执行查询
					@SuppressWarnings("resource")
					DatagramSocket ds = new DatagramSocket();

					while (rs.next()) {
						String ip = rs.getString("IP");
						int port = rs.getInt("UDPPORT");
						SendMessage.sendMessage(message, ip, port);
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (SocketException e1) {
				e1.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}