package com.example.contancts;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.setting.HttpUnity;
import com.example.utils.ContactInfo;
import com.example.utils.SimpleContactInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingFragment extends Fragment implements OnClickListener
{
	private TelephonyManager manager;
	private TextView phoneNum;
	private RelativeLayout upload;
	private RelativeLayout recc;
	private RelativeLayout exit;
	
	private MyHandler myHandler;
	private boolean exist;
	private AlertDialog mDialog = null;
	private ProgressDialog mProgressDialog = null;
	private AsyncQueryHandler asyncQuery;
	private List<SimpleContactInfo> list;
	private Map<Integer, SimpleContactInfo> contactIdMap = null;
	//message ��ʶ��Ϣ
	private int upl = 1;
	private int rec = 2;
	//HTTP ���� ������
	private int state;
	
	//���� ��������ַ
	private String upl_url = "http://192.168.155.1:8088/ContactsServer/UnifyContacts";    //�ϴ�ͨѶ¼URL
	private String recc_url = "http://192.168.155.1:8088/ContactsServer/GetBackupContacts";   //�ָ�ͨѶ¼URL
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.tab04, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		asyncQuery = new MyAsyncQueryHandler(getActivity().getContentResolver());
		initView();	
		
		myHandler = new MyHandler();
		manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		//��ʾ�����绰����
		phoneNum.setText(manager.getLine1Number());
		exit.setOnClickListener(this);
		upload.setOnClickListener(this);
		recc.setOnClickListener(this);
		
	}
	private void initData() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // ��ϵ�˵�Uri
		String[] projection = { 
				ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1,
				"sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
		}; // ��ѯ����
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc"); // ����sort_key�����ѯ
		
	}
	private void initView() {
		phoneNum = (TextView) getActivity().findViewById(R.id.phoneNum);
		exit = (RelativeLayout) getActivity().findViewById(R.id.login_out);
		upload = (RelativeLayout) getActivity().findViewById(R.id.uploadlayout);
		recc = (RelativeLayout) getActivity().findViewById(R.id.recclayout);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_out:
			 System.exit(0);
			break;
		case R.id.recclayout:
			showProgressDialog("�ָ�ͨѶ¼");
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					byte[] result = HttpUnity.httpRecc(recc_url);
					String json;
					if(result!=null){
					try {
						//byteתΪString
						json = new String(result,"GB2312");
						//json = "[{\"���������\":\"Sdf\",\"phoneNum\":\"45345435\"},{\"name\":\"������\",\"phoneNum\":\"6876843\"}]";
						 //�ӷ������л�ȡ��JSON ת��ΪJAVA
						Gson gson = new Gson();          
						List<SimpleContactInfo> mContactInfos= gson.fromJson(json, new TypeToken<List<SimpleContactInfo>>(){}.getType());
						reccContact(mContactInfos);
						Message msg = new Message();
						msg.what = rec;
						msg.arg1 = 1;
						myHandler.sendMessage(msg);	
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}   
	
					}else{
						Message msg = new Message();
						msg.what = rec;
						msg.arg1 = 2;
						myHandler.sendMessage(msg);
					}
				}
			}).start();
			
			break;
		case R.id.uploadlayout:
			initData();
			showProgressDialog("�ϴ�ͨѶ¼");
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					//����progressDialog����������			
					state = uploadContact();
					Message msg = new Message();
					msg.what = upl;
					msg.arg1 = state;
					myHandler.sendMessage(msg);			
				}
			}).start();		
			break;
		default:			
			break;
		}
		
	}
	private int uploadContact() {
		Gson gson = new Gson();
		String json = gson.toJson(list);
		try {
			System.out.println(json);
			state = HttpUnity.httpUpload(upl_url, json);
			return state;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}
	private void showProgressDialog(String title){
		mProgressDialog = new ProgressDialog(getActivity());
		//mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setTitle(title);
		mProgressDialog.setMessage("���Ժ�");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		
	}
	private void showDialog(String title,String str){
		AlertDialog.Builder builder2=new AlertDialog.Builder(getActivity());
        builder2.setTitle(title);
        builder2.setMessage(str);
        builder2.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which)
            {
                // TODO �Զ����ɵķ������
                dialog.dismiss();
                
            }
        });
        builder2.show();
	}
	private void reccContact(List<SimpleContactInfo> mContactInfos) {
		for(int i=0;i<mContactInfos.size();i++){
		exist = isExist(mContactInfos.get(i).getTel_1());
		if(!exist){
			String path = ContactsContract.AUTHORITY;
		    Uri uri = Uri.parse("content://" + path + "/raw_contacts");  
		    ContentResolver resolver = getActivity().getContentResolver();
		    ContentValues values = new ContentValues();
		    
		    long id = ContentUris.parseId(resolver.insert(uri, values));
		    uri = Uri.parse("content://" + path + "/data");  
	        values.put("raw_contact_id", id); // �������  
	        values.put("mimetype", "vnd.android.cursor.item/name");  
	        values.put("data1", mContactInfos.get(i).getName());  
	        //���  
	        resolver.insert(uri, values);  
	//��գ���ֹ���ݲ�����  
	        values.clear();  
	        
	        values.put("raw_contact_id", id);
	        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
	        values.put("data1", mContactInfos.get(i).getTel_1());  
	        values.put("data2", "1");  
	         
	        resolver.insert(uri, values);  
	        values.clear();  
			
		}	
		}
	}
	private boolean isExist(String number){
		Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/"+number);
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{android.provider.ContactsContract.Data.DISPLAY_NAME}, null, null, null);   
        if(cursor.moveToFirst()){
            cursor.close();
            return true;

        }
        else
        	cursor.close();
        	return false;
      
		
	}
	private class MyAsyncQueryHandler extends AsyncQueryHandler{

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			// TODO Auto-generated method stub
			if (cursor != null && cursor.getCount() > 0) {				
				contactIdMap = new HashMap<Integer, SimpleContactInfo>();			
				list = new ArrayList<SimpleContactInfo>();
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);
					int contactId = cursor.getInt(4);
					Long photoId = cursor.getLong(5);
					String lookUpKey = cursor.getString(6);
					if (contactIdMap.containsKey(contactId)) {				
					}else{
						SimpleContactInfo cb = new SimpleContactInfo();
						cb.setName(name);
						cb.setTel_1(number);
						list.add(cb);				
						contactIdMap.put(contactId, cb);						
					}
				}
		}
		
	}
}
	/**
	 * @author Administrator
	 *
	 */
	private class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			mProgressDialog.dismiss();
			switch (msg.what) {
			case 1:
				//progressDialog ��ʧ�� ��ʾAlertDialog		
				if(msg.arg1 == 200){
					showDialog("����", "�ϴ��ɹ�");
				}else{
				showDialog("����", "��������ʧ��");
			}
				break;
			case 2:
				if(msg.arg1 == 1){
					showDialog("�ָ�", "�ָ��ɹ�");
				}else{
					showDialog("�ָ�", "��������ʧ��");
				}
				break;
			default:
				showDialog("���ź�", "��������ʧ��");
				
				break;
			}
		}
		
	}
}
