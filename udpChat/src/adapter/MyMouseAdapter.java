package adapter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;

public class MyMouseAdapter extends MouseAdapter {

	JTextField jtf;
	public MyMouseAdapter(JTextField jtf)
	{
		this.jtf=jtf;
	}
	public void mouseClicked(MouseEvent e) {
		jtf.setText("");
	}
}