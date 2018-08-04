package file;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFileChooser;

public class ReceiveFileThread implements Runnable {
	
	ServerSocket ss = null;
	public int port=8888;
	public ReceiveFileThread()
	{
		while (true) {
			try {
				ss = new ServerSocket(port);
				break;
			} catch (IOException e) {
				port+=1;
			}
		}
	}
	public void run() {
		while (true) {
			Socket socket=null;
			FileOutputStream fos=null;
			try {
				socket=ss.accept();
				InputStream is=socket.getInputStream();
				DataInputStream dis=new DataInputStream(is);
				String fileName=dis.readUTF();
				System.out.println("�ļ����ǣ�"+fileName);
				//��ȡ�Է��������ļ�������д�뱾��Ӳ��
				//���������ļ�����
				File file=new File(fileName);
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(file);
			    int returnVal = chooser.showSaveDialog(null);
			    byte by[]=new byte[1024*8];
		    	int a=0;
		    	fos=new FileOutputStream(chooser.getSelectedFile().getAbsolutePath());
				System.out.println(chooser.getSelectedFile().getAbsolutePath());

			    if(returnVal == JFileChooser.APPROVE_OPTION) {//�������Save��ť
			    	
			    	while((a=dis.read(by))!=-1)//�����ļ�����
					{
						fos.write(by, 0,a);
						fos.flush();
					}
			    }
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally
			{
				try {
					fos.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}