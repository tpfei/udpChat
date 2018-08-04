package Chat;

import javax.swing.*;

import java.awt.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.awt.event.*;

public class ChatWindow {
	JFrame f;
	JTextArea ta;
	JTextField tf;
	@SuppressWarnings("rawtypes")
	JComboBox cb;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ChatWindow(String username) {
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 400);
		f.setTitle(username);
		f.setLocation(300, 200);

		ta = new JTextArea();
		JScrollPane sp = new JScrollPane(ta);
		ta.setEditable(false);
		cb = new JComboBox();
		cb.addItem("All");
		tf = new JTextField();
		tf.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String messageTo = (String) cb.getSelectedItem();

					String message = tf.getText();
					String url = "jdbc:mysql://192.168.157.1:3306/chat";
					Connection conn = null;
					try {
						Class.forName("com.mysql.jdbc.Driver");
						conn = DriverManager.getConnection(url, "root",
								"tpf123456");

						if (messageTo.equals("All")) {
							String sql = "SELECT * FROM tb_online_user";

							Statement stmt = conn.createStatement();
							ResultSet rs = stmt.executeQuery(sql);// 执行查询

							@SuppressWarnings("resource")
							DatagramSocket ds = new DatagramSocket();

							while (rs.next()) {
								String ip = rs.getString("IP");
								int port = rs.getInt("PORT");
								String name = rs.getString("USERNAME");

								if (!name.equals(username)) {

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
											.getByAddress(ipByte));// 设置IP
									dp.setPort(port);// 设置端口
									ds.send(dp);
								}
							}
						} else// 私聊
						{
							String sql = "SELECT * FROM tb_online_user WHERE username=?";

							PreparedStatement pstmt = conn
									.prepareStatement(sql);
							// 给？赋值
							pstmt.setString(1, (String) cb.getSelectedItem());

							ResultSet rs = pstmt.executeQuery();// 执行查询

							@SuppressWarnings("resource")
							DatagramSocket ds = new DatagramSocket();

							while (rs.next()) {
								String ip = rs.getString("IP");
								int port = rs.getInt("PORT");

								DatagramPacket dp = new DatagramPacket(message
										.getBytes(), 0,
										message.getBytes().length);
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

					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					} catch (SocketException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
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

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});

		JButton jb = new JButton("私聊窗口");
		JPanel pl = new JPanel(new BorderLayout());
		pl.add(cb);
		pl.add(jb, BorderLayout.WEST);
		JPanel p = new JPanel(new BorderLayout());
		p.add(pl, BorderLayout.WEST);
		p.add(tf);
		f.getContentPane().add(p, BorderLayout.SOUTH);
		f.getContentPane().add(sp);
		f.setVisible(true);// 窗口可见
		// 启动添加数据的线程
		AddOnLineUserDataThread adt = new AddOnLineUserDataThread(username,
				this);
		adt.start();

	}
}

class AddOnLineUserDataThread extends Thread {
	String username;
	ChatWindow cw;

	AddOnLineUserDataThread(String username, ChatWindow cw) {
		this.username = username;
		this.cw = cw;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		// 启动一个新的线程，用于启动UDP服务（打开8888端口，有可能不是8888，随时准备接收消息）
		GetMessageThread gmt = new GetMessageThread(cw);
		gmt.start();

		// 开始添加数据
		String url = "jdbc:mysql://192.168.157.1:3306/chat";
		Connection conn = null;
		try {
			// 第一步：加载驱动
			Class.forName("com.mysql.jdbc.Driver");// 加载mysql的JDBC驱动
			// 第二步：创建连接
			conn = DriverManager.getConnection(url, "root", "tpf123456");
			conn.setAutoCommit(false);
			String sql = "INSERT INTO tb_online_user(username,ip,port) VALUES(?,?,?)";
			// 第三步：创建Statement对象
			PreparedStatement pstmt = conn.prepareStatement(sql);

			String ip = InetAddress.getLocalHost().getHostAddress();

			// 获得本机IP
			pstmt.setString(1, username);
			pstmt.setString(2, ip);// IP
			pstmt.setInt(3, GetMessageThread.port);// Port

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
		} catch (ClassNotFoundException e) {
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
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, "root", "tpf123456");
			String sql = "SELECT * FROM tb_online_user";

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);// 执行查询

			@SuppressWarnings("resource")
			DatagramSocket ds = new DatagramSocket();

			while (rs.next()) {
				String ip = rs.getString("IP");
				int port = rs.getInt("PORT");
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

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
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