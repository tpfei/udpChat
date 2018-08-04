package adapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JTextField;

import file.ReceiveFileThread;
import ui.AddOnLineUserDataThread;
import ui.ChatWindow;
import util.DBConnection;

public class MyActionListener implements ActionListener {

	JTextField accountJTF;
	JTextField passwordJTF;
	JFrame frame;

	public MyActionListener(JTextField accountJTF, JTextField passwordJTF,
			JFrame frame) {
		this.accountJTF = accountJTF;
		this.passwordJTF = passwordJTF;
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		/*
		 * 1、要把用户名和密码拿到 2、连接数据库 3、开始验证用户名和密码
		 */
		String username = accountJTF.getText();
		String password = passwordJTF.getText();
		Connection conn = null;
		try {
			conn = DBConnection.getConn();
			String sql = "SELECT * FROM tb_user WHERE username=? AND password=?";
			// Statement：语句
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();// execute query
			if (rs.next())// 指针在SQL中有一个专业属于：游标
			{
				System.out.println("登录成功");
				// 开始显示聊天窗口
				ChatWindow ct = new ChatWindow(username);
				// 启动添加数据的线程
				
				//再启动一个线程（专门用于接收文件）
				ReceiveFileThread rft=new ReceiveFileThread();
				Thread t=new Thread(rft);
				t.start();
				//把TCP端口添加到数据库
				AddOnLineUserDataThread adt = new AddOnLineUserDataThread(username, ct,rft.port);
				adt.start();
				frame.setVisible(false);
			} else {
				System.out.println("用户名或密码错误");
			}

		} catch (SQLException e1) {
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