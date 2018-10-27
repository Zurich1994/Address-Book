package com.example.message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contancts.MessageFragment;
import com.example.contancts.R;

/**
 * 鐭俊娑堟伅鍒楄〃 鏀跺彂浜�
 * 
 * @author Administrator
 * 
 */
public class MessageBoxList extends Activity {
	private ListView talkView;
	
	private Button fasong;
	private EditText neirong;
	private List<MessageBean> messages = null;
	private AsyncQueryHandler asyncQuery;
	private String address;
	private SimpleDateFormat sdf;
	private Button btn_return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_list_view);
		
		
		btn_return=(Button) findViewById(R.id.btn_return);
		fasong=(Button) findViewById(R.id.fasong);
		neirong=(EditText) findViewById(R.id.neirong);
		address=getIntent().getStringExtra("phoneNumber");
		TextView tv=(TextView) findViewById(R.id.topbar_title);
		tv.setText(getPersonName(address));
		
		
		
		
		String thread = getIntent().getStringExtra("threadId");
		sdf = new SimpleDateFormat("MM-dd HH:mm");
		
		init(thread);
		btn_return.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MessageBoxList.this.setResult(RESULT_OK);
				MessageBoxList.this.finish();
				
			}
			
		});
		
		fasong.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String nei=neirong.getText().toString();
				ContentValues values = new ContentValues();
				values.put("address", address);
				values.put("body", nei);
				getContentResolver().insert(Uri.parse("content://sms/sent"), values);
				
				SmsManager smsMagager = SmsManager.getDefault();
				
				if (nei.length() > 70) {
					List<String> sms = smsMagager.divideMessage(nei);
					for (String con : sms) {
						smsMagager.sendTextMessage(getIntent().getStringExtra("phoneNumber"), null, con, null, null);
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(nei.length() == 0){
					Toast.makeText(MessageBoxList.this, "输入不可为空哦", Toast.LENGTH_SHORT).show();
				}
				else {
					smsMagager.sendTextMessage(getIntent().getStringExtra("phoneNumber"), null, nei , null, null);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					}
				onCreate(null);
				}
		});
	}

	
	private void init(String thread) {
		asyncQuery = new MessageAsynQueryHandler(getContentResolver());
		talkView = (ListView) findViewById(R.id.message_list);
		messages = new ArrayList<MessageBean>();

		Uri uri = Uri.parse("content://sms");
		String[] projection = new String[] { "date", "address", "person",
				"body", "type" }; // 鏌ヨ鐨勫垪
		asyncQuery.startQuery(0, null, uri, projection,
				"thread_id = " + thread, null, "date asc");
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
							MessageBoxList.this, messages));
					talkView.setDivider(null);
					talkView.setSelection(messages.size());
				} else {
					Toast.makeText(MessageBoxList.this, "请输入内容",
							Toast.LENGTH_SHORT).show();
				}
			}
			super.onQueryComplete(token, cookie, cursor);
		}
	}
	
	
	public String getPersonName(String number) {   
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME, };   
		Cursor cursor = this.getContentResolver().query(   
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,   
				projection,    
				ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + number + "'",  
				null,        
				null);  
		if( cursor == null ) {   
			return number;   
		}
		String name = number;
		for( int i = 0; i < cursor.getCount(); i++ ) {   
			cursor.moveToPosition(i);   
			name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));   
		}
		cursor.close();
		return name;   
	}
	
	
}
