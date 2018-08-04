package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

	public static Connection getConn()
	{
		Connection conn=null;
		Properties prop=new Properties();
		try {
			//从项目路径下去加载文件，就能做到与平台无关，与特定PC无关
			prop.load(
					Class.forName("util.DBConnection").getResourceAsStream("/config/db.properties")
			);
			//FileInputStream fis=new FileInputStream("C:\\Users\\liwei\\workspacec\\Chat_UDP\\bin\\org\\lanqiao\\config\\/db.properties");
			//prop.load(fis);
			String url=prop.getProperty("url");
			String user=prop.getProperty("user");
			String password=prop.getProperty("password");
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection(url, user, password);
			return conn;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
}