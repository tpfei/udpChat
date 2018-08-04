package ui;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import message.GetMessageThread;
import util.DBConnection;

public class AddOnLineUserDataThread extends Thread {
	String username;
	ChatWindow cw;

	int tcpPort;
	public AddOnLineUserDataThread(String username, ChatWindow cw, int tcpPort) {
		this.username = username;
		this.cw = cw;
		this.tcpPort=tcpPort;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		// 启动一个新的线程，用于启动UDP服务（打开8888端口，有可能不是8888，随时准备接收消息）
		GetMessageThread gmt = new GetMessageThread(cw);
		gmt.start();

		// 开始添加数据
		Connection conn = null;
		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			String sql = "INSERT INTO tb_online_user(username,ip,udpport,tcpport) VALUES(?,?,?,?)";
			// 第三步：创建Statement对象
			PreparedStatement pstmt = conn.prepareStatement(sql);
			String ip = InetAddress.getLocalHost().getHostAddress();
			// 获得本机IP
			pstmt.setString(1, username);
			pstmt.setString(2, ip);// IP
			pstmt.setInt(3, GetMessageThread.port);// Port
			pstmt.setInt(4,tcpPort);

			// 第四步：执行SQL语句
			pstmt.executeUpdate();// 添加数据：执行更新
			conn.commit();

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			try {
				// 第六步：关闭数据库连接
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 开始提示XX上线

		try {
			conn = DBConnection.getConn();
			String sql = "SELECT * FROM tb_online_user";

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);// 执行查询

			@SuppressWarnings("resource")
			DatagramSocket ds = new DatagramSocket();

			while (rs.next()) {
				String ip = rs.getString("IP");
				int port = rs.getInt("UDPPORT");
				String name = rs.getString("USERNAME");

				if (!name.equals(username)) {
					// 给自己发XX在聊天室（在自己的聊天窗口添加XX正在聊天室）
					cw.ta.append(name + "正在聊天室\n");
					cw.ta.setCaretPosition(cw.ta.getText().length());
					cw.cb.addItem(name);
					String message = username + "上线了\n";
					// 发消息
					DatagramPacket dp = new DatagramPacket(message.getBytes(),
							0, message.getBytes().length);
					// 192.168.2.220
					byte ipByte[] = new byte[4];
					String str[] = ip.split("\\.");

					ipByte[0] = (byte) Integer.parseInt(str[0]);
					ipByte[1] = (byte) Integer.parseInt(str[1]);
					ipByte[2] = (byte) Integer.parseInt(str[2]);
					ipByte[3] = (byte) Integer.parseInt(str[3]);

					dp.setAddress(InetAddress.getByAddress(ipByte));// 设置IP
					dp.setPort(port);// 设置端口
					ds.send(dp);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}