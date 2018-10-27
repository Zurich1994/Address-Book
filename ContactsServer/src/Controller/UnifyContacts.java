package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Dao.Contact;
import Dao.DbUtils;
import Dao.ImpContacts;
import Dao.ParseUtils;

/**
 * Servlet implementation class UnifyContacts
 */
@WebServlet("/UnifyContacts")
public class UnifyContacts extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final int ok = 0;
	private final int fail = 1;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UnifyContacts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
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
		//String user = request.getParameter("user");
		String user = "15555215554";
		StringBuffer contactsJson = new StringBuffer(request.getParameter("contactsJson"));//StringBuffer线程安全
		//System.out.println("unifyContacts");
		/**************************************************模拟测试数据************************/
		/* String user = "15555215554";
		List<Contact> virContacts = new ArrayList<Contact>();
		for(int i = 0;i<5;i++)
		{
			Contact temp = new Contact();
			temp.setName(i+"新增联系人");
			virContacts.add(temp);
		}
		Contact c = new Contact();
		c.setName("王鑫");
		virContacts.add(c);
		Gson gson = new Gson();
		StringBuffer contactsJson = new StringBuffer(gson.toJson(virContacts));*/
		/**********************************************************************************/
		ImpContacts ic = new ImpContacts();
		List<Contact> clientContacts = new ArrayList<Contact>();
		List<Contact> serverContacts = new ArrayList<Contact>();
		List<Contact> unifiedContacts = new ArrayList<Contact>();
		clientContacts = ParseUtils.parseContactslist(contactsJson.toString());//解析客户端传来的json
		System.out.println(clientContacts.size());
		serverContacts = ic.getAllServerContacts(user);
		System.out.println(serverContacts.size());
		unifiedContacts = processUnify(clientContacts, serverContacts);
		System.out.println(unifiedContacts.size());
		int unifyresult[]= ic.unifyContacts(unifiedContacts, user);	
		try {
			PrintWriter out = response.getWriter();		
			for(int i:unifyresult)
			{
				out.print(i);
			}
			//对executeBatch的结果捷星处理
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private List<Contact> processUnify(List<Contact> clientContacts,List<Contact> serverContacts)
	{
		List<Contact> unifiedContacts = new ArrayList<Contact>();//统一后的联系人名单
		boolean oldContact = false;//以服务器的联系人名单为基础，判断是否为新增的联系人
		for(Contact c1:clientContacts)
		{
			for(Contact c2:serverContacts)
			{
				if(c1.getName().equals(c2.getName()))//存在此联系人则将客户端联系人信息覆盖
				{
					oldContact=true;
					Contact temp = new Contact();
					temp=c1;
					unifiedContacts.add(temp);
					serverContacts.remove(c2);//在服务器端被操作的数据被移除，减少循环次数提高效率
					break;
				}
			}
			if(!oldContact)//新联系人将被添加
			{
				unifiedContacts.add(c1);
			}
			oldContact = false;
		}
		for(Contact c:serverContacts)//这些为服务器中原来存在的联系人  现在被删除了   系统将备份
		{
			c.setDel(1);//将相应的del位置1表示用户已删除此联系人
			unifiedContacts.add(c);
		}
		
		return unifiedContacts;
	}
}
