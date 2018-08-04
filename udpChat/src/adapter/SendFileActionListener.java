package adapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import util.DBConnection;

public class SendFileActionListener implements ActionListener {

	@SuppressWarnings("rawtypes")
	JComboBox cb;
	@SuppressWarnings("rawtypes")
	public SendFileActionListener(JComboBox cb)
	{
		this.cb=cb;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * JFileChooser类
		 */
		JFileChooser chooser = new JFileChooser();
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {//如果点了确定
	            String fileName=chooser.getSelectedFile().getAbsolutePath();
	            Socket socket=null;
	            FileInputStream fis=null;
	            try {
					Connection conn=null;
					conn=DBConnection.getConn();
					String sql="SELECT ip,tcpport FROM tb_online_user WHERE username=?";
					PreparedStatement pstmt=conn.prepareStatement(sql);
					pstmt.setString(1, ((String)cb.getSelectedItem()).trim());
					ResultSet rs=pstmt.executeQuery();//执行查询
					rs.next();
					String ip=rs.getString("IP");
					int tcpPort=rs.getInt("TCPPORT");
					
					socket=new Socket(ip,tcpPort);//建立Socket连接
					OutputStream os=socket.getOutputStream();
					DataOutputStream dos=new DataOutputStream(os);
					dos.writeUTF(chooser.getSelectedFile().getName());//先发送文件名到对方电脑
					dos.flush();
					fis=new FileInputStream(fileName);
					int a=0;
					byte by[]=new byte[1024*8];
					while((a=fis.read(by))!=-1)//传送文件内容
					{
						dos.write(by, 0,a);
						dos.flush();
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	            finally
	            {
	            	try {
	            		fis.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	            }
	    }
	}
}