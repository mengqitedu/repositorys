package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

public class DBUtils {
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	
	private static BasicDataSource dataSource;
	
	static {
		Properties prop = new Properties();
		InputStream ips = DBUtils.class
			.getClassLoader()
			.getResourceAsStream("jdbc.properties");
		try {
			prop.load(ips);
			driver = prop.getProperty("driver");
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");
		
			//创建数据源对象
			dataSource = 
					new BasicDataSource();
			dataSource.setDriverClassName(driver);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			dataSource.setInitialSize(3);
			dataSource.setMaxActive(3);
		
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				ips.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	public static Connection getConn() 
			throws Exception {
//		Class.forName(driver);
//		Connection conn = 
//				DriverManager.getConnection(
//				url, username, password);
		
		return dataSource.getConnection();
	}
	public static void close(Connection conn,
			Statement stat, ResultSet rs) {
		try {
			if(rs!=null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(stat!=null) {
				stat.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn!=null) {
				//打开自动提交
				conn.setAutoCommit(true);
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}




