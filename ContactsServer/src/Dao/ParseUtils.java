package Dao;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ParseUtils {
	public static List<Contact> parseContactslist(String contactsJson)
	{
		Gson gson = new Gson();
		List<Contact> contacts = new ArrayList<Contact>();
		Type type = new TypeToken<ArrayList<Contact>>(){}.getType();
		contacts = gson.fromJson(contactsJson, type);
		/****************************************************/
		for(Contact c:contacts)
		{
			System.out.println(c.getCompany());
			System.out.println(c.getDel());
			System.out.println(c.getLocation());
			System.out.println(c.getName());
			System.out.println(c.getPicture());
			System.out.println(c.getRemark());
			System.out.println(c.getTel_1());
			System.out.println(c.getTel_2());
			System.out.println(c.getDate());
		}
		/****************************************************/
		return contacts;
	}
	
}
