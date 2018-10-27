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
	//message 标识信息
	private int upl = 1;
	private int rec = 2;
	//HTTP 连接 返回码
	private int state;
	
	//连接 服务器地址
	private String upl_url = "http://192.168.155.1:8088/ContactsServer/UnifyContacts";    //上传通讯录URL
	private String recc_url = "http://192.168.155.1:8088/ContactsServer/GetBackupContacts";   //恢复通讯录URL
	
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
		//显示本机电话号码
		phoneNum.setText(manager.getLine1Number());
		exit.setOnClickListener(this);
		upload.setOnClickListener(this);
		recc.setOnClickListener(this);
		
	}
	private void initData() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
		String[] projection = { 
				ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1,
				"sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
		}; // 查询的列
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
		
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
			showProgressDialog("恢复通讯录");
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					byte[] result = HttpUnity.httpRecc(recc_url);
					String json;
					if(result!=null){
					try {
						//byte转为String
						json = new String(result,"GB2312");
						//json = "[{\"撒旦发射点\":\"Sdf\",\"phoneNum\":\"45345435\"},{\"name\":\"王晓航\",\"phoneNum\":\"6876843\"}]";
						 //从服务器中获取的JSON 转化为JAVA
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
			showProgressDialog("上传通讯录");
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					//设置progressDialog，发送请求			
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
		mProgressDialog.setMessage("请稍候");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		
	}
	private void showDialog(String title,String str){
		AlertDialog.Builder builder2=new AlertDialog.Builder(getActivity());
        builder2.setTitle(title);
        builder2.setMessage(str);
        builder2.setPositiveButton("确定",new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which)
            {
                // TODO 自动生成的方法存根
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
	        values.put("raw_contact_id", id); // 添加名字  
	        values.put("mimetype", "vnd.android.cursor.item/name");  
	        values.put("data1", mContactInfos.get(i).getName());  
	        //添加  
	        resolver.insert(uri, values);  
	//清空（防止数据残留）  
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
				//progressDialog 消失， 显示AlertDialog		
				if(msg.arg1 == 200){
					showDialog("备份", "上传成功");
				}else{
				showDialog("备份", "网络连接失败");
			}
				break;
			case 2:
				if(msg.arg1 == 1){
					showDialog("恢复", "恢复成功");
				}else{
					showDialog("恢复", "网络连接失败");
				}
				break;
			default:
				showDialog("很遗憾", "网络连接失败");
				
				break;
			}
		}
		
	}
}
