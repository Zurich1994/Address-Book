package Dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtils {
	private static String url="jdbc:mysql://localhost/contacts";
	private static String user="root";
	private static String password="";
	private DbUtils(){}
	@SuppressWarnings("finally")
	public static Connection getConnection()
	{
		
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection(url,user,password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return conn;
		}
	}
	public static boolean closeConnection(Connection conn)
	{
		boolean result=false;
		if(conn!=null)
		{
			try {
				conn.close();
				result=true;
			} catch (SQLException e) {
				result=false;
				e.printStackTrace();			
			}
		}
		else
		{
			result = false;
		}
		return result;
	}
	public static boolean closeResultSet(ResultSet rs)
	{
		boolean result=false;
		if(rs!=null)
		{
			try {
				rs.close();
				result=true;
			} catch (SQLException e) {
				result=false;
				e.printStackTrace();			
			}
		}
		else
		{
			result = false;
		}
		return result;
	}
	public static boolean closeStatement(Statement st)
	{
		boolean result=false;
		if(st!=null)
		{
			try {
				st.close();
				result=true;
			} catch (SQLException e) {
				result=false;
				e.printStackTrace();				
			}
		}
		else
		{
			result = false;
		}
		return result;
	}
	public static String getUrl() {
		return url;
	}
	public static void setUrl(String url) {
		DbUtils.url = url;
	}
	public static String getUser() {
		return user;
	}
	public static void setUser(String user) {
		DbUtils.user = user;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		DbUtils.password = password;
	}
	
}