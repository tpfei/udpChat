package Chat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SelectDemo {

	public static void main(String[] args) {
//		Connection conn=null;
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			String url="jdbc:mysql://119.23.203.154:3306/chat";
//			conn=DriverManager.getConnection(url, "root","123456");
//			
//			String sql="SELECT * FROM tb_online_user";
//
//			Statement stmt=conn.createStatement();
//			ResultSet rs=stmt.executeQuery(sql);//Ö´ÐÐ²éÑ¯
//			
//			while(rs.next())
//			{
//				int port=rs.getInt("PORT");
//				System.out.println("¶Ë¿Ú£º"+port);
//			}
		
		
		Connection conn=null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://192.168.157.1:3306/chat";
			conn=DriverManager.getConnection(url,"root","tpf123456");
			String sql="select * from tb_online_user";
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
		while(rs.next())
		{
			int port=rs.getInt("port");
			String ip=rs.getString("ip");
			System.out.println(port+ip);
		}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}