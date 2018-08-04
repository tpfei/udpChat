package Chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

//import org.lanqiao.ui.ChatWindow;

public class Client
{
	public static void main(String args[])
	{
		JFrame frame=new JFrame("��¼����");//�������
		frame.setSize(400,300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel accountJLabel=new JLabel("�ʺţ�");
		accountJLabel.setBounds(70,100,40,20);

		JLabel passwordJLabel=new JLabel("���룺");
		passwordJLabel.setBounds(70,120,40,20);//40���ؿ�

		JTextField accountJTF=new JTextField("qq");
		accountJTF.addMouseListener(
				new MouseListener()
				{

					@Override
					public void mouseClicked(MouseEvent e) {
						accountJTF.setText("");
					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
				}
		);
		accountJTF.setBounds(120,100,180,20);

		JTextField passwordJTF=new JTextField("1234");
		passwordJTF.addMouseListener(
				new MouseListener()
				{

					@Override
					public void mouseClicked(MouseEvent e) {
						passwordJTF.setText("");
					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
				}
		);
		passwordJTF.setBounds(120,120,180,20);

		JButton loginButton=new JButton("��¼");
		loginButton.addActionListener(
				new ActionListener()
				{
					@SuppressWarnings("unused")
					@Override
					public void actionPerformed(ActionEvent e) {
						/*1��Ҫ���û����������õ�
						2���������ݿ�
						3����ʼ��֤�û���������
						*/
						String username=accountJTF.getText();
						String password=passwordJTF.getText();
						
						String url="jdbc:mysql://192.168.157.1:3306/chat";//chat�����ݿ�����
						Connection conn=null;
						try {
							conn=DriverManager.getConnection(url, "root","tpf123456");
							
							String sql="SELECT * FROM tb_user WHERE username=? AND password=?";
							//Statement�����
							PreparedStatement pstmt=conn.prepareStatement(sql);
							pstmt.setString(1,username);
							pstmt.setString(2,password);
							ResultSet rs=pstmt.executeQuery();//execute   query
							if(rs.next())//ָ����SQL����һ��רҵ���ڣ��α�
							{
								System.out.println("��¼�ɹ�");
								//��ʼ��ʾ���촰��
								ChatWindow ct=new ChatWindow(username);
								frame.setVisible(false);
							}
							else
							{
								System.out.println("�û������������");
							}
							
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						finally
						{
							try {
								conn.close();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
		);
		loginButton.setBounds(120,180,180,30);
		frame.getContentPane().setLayout(null);//���������ΰڷ�
		
		frame.getContentPane().add(accountJLabel);//�õ����������Ұ������ӵ�������
		frame.getContentPane().add(passwordJLabel);
		frame.getContentPane().add(accountJTF);
		frame.getContentPane().add(passwordJTF);
		frame.getContentPane().add(loginButton);
	
		frame.setVisible(true);//�����ʾ����
	}
}