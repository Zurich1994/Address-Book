package com.example.call;

import java.util.List;
import com.example.utils.ContactInfo;
import android.content.Intent;

public class MyApplication extends android.app.Application {

	
	private List<ContactInfo> ContactInfo;
	
	public List<ContactInfo> getContactInfo() {
		return ContactInfo;
	}

	public void setContactInfo(List<ContactInfo> contactInfo) {
		ContactInfo = contactInfo;
	}

	public void onCreate() {
		
		
		Intent startService = new Intent(MyApplication.this, T9Service.class);
		startService(startService);
	}
}
