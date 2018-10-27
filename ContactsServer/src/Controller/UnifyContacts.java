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
		StringBuffer contactsJson = new StringBuffer(request.getParameter("contactsJson"));//StringBuffer�̰߳�ȫ
		//System.out.println("unifyContacts");
		/**************************************************ģ���������************************/
		/* String user = "15555215554";
		List<Contact> virContacts = new ArrayList<Contact>();
		for(int i = 0;i<5;i++)
		{
			Contact temp = new Contact();
			temp.setName(i+"������ϵ��");
			virContacts.add(temp);
		}
		Contact c = new Contact();
		c.setName("����");
		virContacts.add(c);
		Gson gson = new Gson();
		StringBuffer contactsJson = new StringBuffer(gson.toJson(virContacts));*/
		/**********************************************************************************/
		ImpContacts ic = new ImpContacts();
		List<Contact> clientContacts = new ArrayList<Contact>();
		List<Contact> serverContacts = new ArrayList<Contact>();
		List<Contact> unifiedContacts = new ArrayList<Contact>();
		clientContacts = ParseUtils.parseContactslist(contactsJson.toString());//�����ͻ��˴�����json
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
			//��executeBatch�Ľ�����Ǵ���
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private List<Contact> processUnify(List<Contact> clientContacts,List<Contact> serverContacts)
	{
		List<Contact> unifiedContacts = new ArrayList<Contact>();//ͳһ�����ϵ������
		boolean oldContact = false;//�Է���������ϵ������Ϊ�������ж��Ƿ�Ϊ��������ϵ��
		for(Contact c1:clientContacts)
		{
			for(Contact c2:serverContacts)
			{
				if(c1.getName().equals(c2.getName()))//���ڴ���ϵ���򽫿ͻ�����ϵ����Ϣ����
				{
					oldContact=true;
					Contact temp = new Contact();
					temp=c1;
					unifiedContacts.add(temp);
					serverContacts.remove(c2);//�ڷ������˱����������ݱ��Ƴ�������ѭ���������Ч��
					break;
				}
			}
			if(!oldContact)//����ϵ�˽������
			{
				unifiedContacts.add(c1);
			}
			oldContact = false;
		}
		for(Contact c:serverContacts)//��ЩΪ��������ԭ�����ڵ���ϵ��  ���ڱ�ɾ����   ϵͳ������
		{
			c.setDel(1);//����Ӧ��delλ��1��ʾ�û���ɾ������ϵ��
			unifiedContacts.add(c);
		}
		
		return unifiedContacts;
	}
}
