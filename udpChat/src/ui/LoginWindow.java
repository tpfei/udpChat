package ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import adapter.MyActionListener;
import adapter.MyMouseAdapter;

public class LoginWindow
{
	public static void main(String args[])
	{
		JFrame frame=new JFrame("登录窗口");//房子面积
		frame.setSize(400,300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel accountJLabel=new JLabel("帐号：");
		accountJLabel.setBounds(70,100,40,20);

		JLabel passwordJLabel=new JLabel("密码：");
		passwordJLabel.setBounds(70,120,40,20);//40像素宽

		JTextField accountJTF=new JTextField("tpf2");
		accountJTF.addMouseListener(new MyMouseAdapter(accountJTF));
		accountJTF.setBounds(120,100,180,20);

		JTextField passwordJTF=new JTextField("1234");
		passwordJTF.addMouseListener(new MyMouseAdapter(passwordJTF));
		passwordJTF.setBounds(120,120,180,20);

		JButton loginButton=new JButton("登录");
		loginButton.addActionListener(new MyActionListener(accountJTF,passwordJTF,frame));
		loginButton.setBounds(120,180,180,30);
		frame.getContentPane().setLayout(null);//设置组件如何摆放
		
		frame.getContentPane().add(accountJLabel);//得到容器，并且把组件添加到容器中
		frame.getContentPane().add(passwordJLabel);
		frame.getContentPane().add(accountJTF);
		frame.getContentPane().add(passwordJTF);
		frame.getContentPane().add(loginButton);
	
		frame.setVisible(true);//框架显示出来
	}
}