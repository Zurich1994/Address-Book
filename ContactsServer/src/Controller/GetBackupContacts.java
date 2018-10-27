package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dao.Contact;
import Dao.DbUtils;
import Dao.ImpContacts;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
@

/**
 * Servlet implementation class GetBackupContacts
 */
WebServlet("/GetBackupContacts")
public class GetBackupContacts extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final int ok = 0;
	private final int wrong_password=1;
	private final int no_user=2;
	private final int server_wrong=3;
    /**
     * Default constructor. 
     */
    public GetBackupContacts() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request,response);
	}
	
	private int check(String user,String password)
	{
		int result = no_user;
		String sql="select * from login where user=?";		
		Connection conn =null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		conn = DbUtils.getConnection();		
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1, user);
			rs = ps.executeQuery();
			if(rs.next())
			{
				if(password.equals(rs.getString("password")))
				{
					result=ok;
				}else {
					result=wrong_password;
				}
			}else {
				result=no_user;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private String returnAllContacts(String user,String password)
	{   Gson gson = new Gson();
		ImpContacts ic = new ImpContacts();
		List<Contact> contacts;
		String result = "init";
		int check_result = server_wrong;
		check_result = check(user,password);
		switch(check_result)
		{
		case ok:
			contacts = ic.getAllContacts(user);
			//JSONArray temp = JSONArray.fromObject(contacts);
			Type type = new TypeToken<List<Contact>>(){}.getType();
			//gson.toJson(scontactsrc, typeOfSrc)
			result = gson.toJson(contacts, type);			
			break;
		case no_user:
			result="不存在此用户名";
			break;
		case wrong_password:
			result="密码错误";break;
		case server_wrong:
			result="抱歉,服务器出错,请稍后重试";
			break;
		}
		System.out.println(result);
		return result;
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response)
	{
		 response.setCharacterEncoding("gb2312");
		 try {
			request.setCharacterEncoding("gb2312");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// response.setContentType("charset=gb2312");
		 String user=request.getParameter("user");
		 String password=request.getParameter("password");
	     try {
			PrintWriter out = response.getWriter();
			out.print(returnAllContacts(user,password));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
