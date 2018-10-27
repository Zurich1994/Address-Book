package com.example.message;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.contancts.R;
import com.example.message.*;
import com.example.utils.ContactInfo;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class NewSMSActivity extends Activity {

	
	private ListView talkView;
	private Button btn_return;
	private Button add_btn;
	private Button fasong;
	private List<ContactInfo> selectContactList = null;
	private List<MessageBean> messages = null;
	

	private MyViewGroup mvg;
	private LinearLayout ll;
	private EditText etMess;
	private SimpleDateFormat sdf;
	
	
	private EditText neirong;
	
	private int extiTextId = 100001;
	private String[] chars = new String[]{" ", ","};

	private ListView queryListView;
	private NewSmsAdapter adapter;

	private AsyncQueryHandler asyncQuery;
	private List<ContactInfo> list;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		
		
		
		setContentView(R.layout.new_sms);
		
		
		
		neirong=(EditText) findViewById(R.id.neirong);

		queryListView = (ListView) findViewById(R.id.list);
		
		btn_return = (Button) findViewById(R.id.btn_return);
		btn_return.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				NewSMSActivity.this.finish();
			}
		});
		add_btn = (Button) findViewById(R.id.add_btn);
		add_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				if(null == etMess || "".equals(etMess.getText().toString())){
				}else{
					String phoneNum = etMess.getText().toString();
					if(isNum(etMess.getText().toString().trim())){
						createView1(phoneNum, phoneNum);
						etMess.setText("");
					}else{
						etMess.setText("");
					}
				}
				
				if(null == selectContactList || selectContactList.size() < 1){
					BaseIntentUtil.intentSysDefault(NewSMSActivity.this, SelectContactsToSendActivity.class, null);
				}else{
					Gson gson = new Gson();
					String data = gson.toJson(selectContactList);
					Map<String, String> map = new HashMap<String, String>();
					map.put("data", data);
					BaseIntentUtil.intentSysDefault(NewSMSActivity.this, SelectContactsToSendActivity.class, map);
				}
			}
		});

		fasong = (Button) findViewById(R.id.fasong);
		fasong.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				String nei=neirong.getText().toString();
				
				ContentValues values = new ContentValues();
//				values.put("address", address);
				values.put("body", nei);
				getContentResolver().insert(Uri.parse("content://sms/sent"), values);
				
				SmsManager smsMagager = SmsManager.getDefault();

				
				
				if(null == etMess || "".equals(etMess.getText().toString())){
				}else{
					String phoneNum = etMess.getText().toString();
					if(isNum(etMess.getText().toString().trim())){
						createView1(phoneNum, phoneNum);
						etMess.setText("");
					}else{
						etMess.setText("");
					}
				}

				if(null == selectContactList || selectContactList.size()<1){
					Toast.makeText(NewSMSActivity.this, "�����뷢��Ŀ��", Toast.LENGTH_SHORT).show();
				}else{

					for(ContactInfo cb : selectContactList){
						if (nei.length() > 70) {
							List<String> sms = smsMagager.divideMessage(nei);
							for (String con : sms) {
								smsMagager.sendTextMessage(cb.getPhoneNum(), null, con, null, null);
							}
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(nei.length() == 0){
							Toast.makeText(NewSMSActivity.this, "�������ݲ���Ϊ��", Toast.LENGTH_SHORT).show();
						}
						else {
							smsMagager.sendTextMessage(cb.getPhoneNum(), null, nei , null, null);
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							}
						onCreate(null);
						
								}
				}

			}
			
			
			
		});

		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		query();
		

		initMyGroupView();

		if(null != getIntent().getStringExtra("list")){
			String data = getIntent().getStringExtra("list");
			Gson gson = new Gson();
			Type listRet = new TypeToken<List<ContactInfo>>() { }.getType();
			selectContactList = gson.fromJson(data, listRet);
			for(ContactInfo cb : selectContactList){
				createView2(cb.getName().trim());
				final View child = mvg.getChildAt(mvg.getChildCount()-1);
				autoHeight(child);
			}
		}


	}







	private void query(){
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
	private void initMyGroupView(){
		ll = (LinearLayout) findViewById(R.id.l1);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		/**********************************************************************************************/
		mvg = new MyViewGroup(NewSMSActivity.this);   
		mvg.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 70));
		//		mvg.setBackgroundColor(Color.GREEN);
		etMess = new EditText(NewSMSActivity.this);
		etMess.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
		etMess.setSelection(etMess.getText().length());
		etMess.setGravity(Gravity.CENTER_VERTICAL);
		etMess.setMinWidth(100);
		etMess.setHeight(60);
		etMess.setTag("edit");
		etMess.getBackground().setAlpha(0);
		etMess.setId(extiTextId);
		etMess.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(isNum(s.toString())){
					if(s.length() >= 1){
						boolean bool = false;
						//length() == 15ֱ�����ɰ�ť
						if(s.length() == 15){
							bool = true;
						}
						//����û������15����֤�Ƿ��пո�
						if(!bool){
							String c = s.toString().substring(start, start+count);
							for (int i = 0; i < chars.length; i++) {
								if(chars[i].equals(c)){
									bool = true;
									break;
								}
							}
						}
						//bool == true ����Button
						if(bool){
							createView1(s.toString(), s.toString());
							etMess.setText("");
						}
						//�������������Ƿ��Ѿ�����
						final View child = mvg.getChildAt(mvg.getChildCount()-1);
						autoHeight(child);
					}
				}else{
					adapter.getFilter().filter(s);
					queryListView.setVisibility(View.VISIBLE);
				}
				
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		mvg.addView(etMess);
		ll.addView(mvg);
		etMess.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(isNum(etMess.getText().toString().trim())){
						createView1(etMess.getText().toString().trim(), etMess.getText().toString().trim());
						etMess.setText("");
					}else{
						etMess.setText("");
						queryListView.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
	}

	/**
	 * ΪMyViewGroup�Զ�����߶�
	 * @param child
	 */
	private void autoHeight(final View child) {
		if (child != null){
			new Handler() {
			}.postDelayed(new Runnable() {
				public void run() {
					if (child.getBottom() > mvg.getBottom() || mvg.getBottom() - child.getBottom() >= child.getHeight()) {
						LayoutParams l = mvg.getLayoutParams();
						l.height = child.getBottom();
						mvg.setLayoutParams(l);
					}
				}
			}, 500);
		}
	}

	/**
	 * ����MyViewGroup����Ԫ��
	 * @param text
	 */
	private void createView1(String text, String number) {

		if(etMess.getText().toString().equals(" ")||etMess.getText().toString().equals("")){
		}else{
			TextView t = new TextView(this);
			t.setText(text);
			t.setTextColor(Color.BLACK);
			t.setGravity(Gravity.CENTER);
			t.setBackgroundResource(R.drawable.bg_sms_contact_btn);
			t.setHeight(60);
			t.setPadding(2, 0, 2, 0);
			t.setOnClickListener(new MyListener());
			t.setTag(number);
			mvg.addView(t, mvg.getChildCount() - 1);

			ContactInfo cb = new ContactInfo();
			cb.setName(text);
			cb.setPhoneNum(number);
			if(null == selectContactList){
				selectContactList = new ArrayList<ContactInfo>();
			}
			selectContactList.add(cb);
			queryListView.setVisibility(View.INVISIBLE);
		}
	}

	private void createView2(String text) {

		TextView t = new TextView(this);
		t.setText(text);
		t.setTextColor(Color.BLACK);
		t.setGravity(Gravity.CENTER);
		t.setHeight(60);
		t.setPadding(2, 0, 2, 0);
		t.setBackgroundResource(R.drawable.bg_sms_contact_btn);
		t.setOnClickListener(new MyListener());
		t.setTag(text);
		mvg.addView(t, mvg.getChildCount() - 1);
	}

	/**
	 * MyViewGroup��Ԫ�ص��¼�
	 * @author LDM
	 */
	private class MyListener implements OnClickListener{
		public void onClick(View v) {
			mvg.removeView(v);
			String number = (String) v.getTag();
			for(ContactInfo cb: selectContactList){
				if(cb.getPhoneNum().equals(number)){
					selectContactList.remove(cb);
					break;
				}
			}
			autoHeight(mvg.getChildAt(mvg.getChildCount() -1));
		}
	}



	/**
	 * ���ݿ��첽��ѯ��AsyncQueryHandler
	 * 
	 * @author administrator
	 * 
	 */
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {

				list = new ArrayList<ContactInfo>();
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);
					int contactId = cursor.getInt(4);
					Long photoId = cursor.getLong(5);
					String lookUpKey = cursor.getString(6);

					ContactInfo cb = new ContactInfo();
					cb.setName(name);
					cb.setPhoneNum(number);
					cb.setSortKey(sortKey);
					cb.setId(contactId);
					cb.setPhotoId(photoId);
					cb.setLookUpKey(lookUpKey);
					
					list.add(cb);
				}
				if (list.size() > 0) {
					setAdapter(list);
				}
			}
		}
	}

	private void setAdapter(List<ContactInfo> list) {
		adapter = new NewSmsAdapter(this);
		adapter.assignment(list);
		queryListView.setAdapter(adapter);
		queryListView.setTextFilterEnabled(true);
		queryListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ContactInfo cb= (ContactInfo) adapter.getItem(position);
				boolean b = true;
				if(null == selectContactList || selectContactList.size()<1){
				}else{
					for(ContactInfo cbean : selectContactList){
						if(cbean.getPhoneNum().equals(cb.getPhoneNum())){
							b = false;
							break;
						}
					}
				}
				if(b){
					etMess.setText(cb.getName());
					createView1(etMess.getText().toString().trim(), cb.getPhoneNum());
					etMess.setText("");
				}else{
					queryListView.setVisibility(View.INVISIBLE);
					etMess.setText("");
				}
			}
		});
		queryListView.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
					((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(NewSMSActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
				}
			}
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}

	
	
	private class MessageAsynQueryHandler extends AsyncQueryHandler {

		public MessageAsynQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String date = sdf.format(new Date(cursor.getLong(cursor
							.getColumnIndex("date"))));
					if (cursor.getInt(cursor.getColumnIndex("type")) == 1) {
						MessageBean d = new MessageBean(
								cursor.getString(cursor
										.getColumnIndex("address")),
								date,
								cursor.getString(cursor.getColumnIndex("body")),
								R.layout.list_say_he_item);
						messages.add(d);
					} else {
						MessageBean d = new MessageBean(
								cursor.getString(cursor
										.getColumnIndex("address")),
								date,
								cursor.getString(cursor.getColumnIndex("body")),
								R.layout.list_say_me_item);
						messages.add(d);
					}
				}
				if (messages.size() > 0) {
					talkView.setAdapter(new MessageBoxListAdapter(
							NewSMSActivity.this, messages));
					talkView.setDivider(null);
					talkView.setSelection(messages.size());
				} else {
					Toast.makeText(NewSMSActivity.this, "û�ж��Ž��в���",
							Toast.LENGTH_SHORT).show();
				}
			}
			super.onQueryComplete(token, cookie, cursor);
		}
	}
	


	private boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}






}