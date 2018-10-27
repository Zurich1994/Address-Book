package com.example.message;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.contancts.R;
import com.example.message.*;
import com.example.utils.*;
import com.example.utils.ContactInfo;



import java.util.Set;
import java.util.regex.Pattern;



import com.example.contancts.*;
import com.example.message.*;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
public class SelectContactsToSendActivity extends Activity {

	private SelectContactsToSendAdapter adapter;
	private ListView personList;

	private List<ContactInfo> list;
	private AsyncQueryHandler asyncQuery;
	private QuickAlphabeticBar alpha;

	private List<ContactInfo> selectContactList = new ArrayList<ContactInfo>();
	private Button returnBtn;
	private Button doneBtn;
	private Map<String, String> selectMap = null;


	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_contacts_to_send);


		if(null != getIntent().getStringExtra("data")){
			String data = getIntent().getStringExtra("data");
			Gson gson = new Gson();
			Type listRet = new TypeToken<List<ContactInfo>>() { }.getType();
			selectContactList = gson.fromJson(data, listRet);
			selectMap = new HashMap<String, String>();
			for(ContactInfo cb : selectContactList){
				selectMap.put(cb.getPhoneNum(), cb.getName());
			}
		}

		personList = (ListView) findViewById(R.id.list);

		alpha = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		init();

		returnBtn = (Button) findViewById(R.id.btn_return);
		doneBtn = (Button) findViewById(R.id.btn_done);

		returnBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SelectContactsToSendActivity.this.finish();
			}
		});
		doneBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Gson gson = new Gson();
				String data = gson.toJson(selectContactList);
				Map<String, String> map = new HashMap<String, String>();
				map.put("list", data);
				BaseIntentUtil.intentSysDefault(SelectContactsToSendActivity.this, NewSMSActivity.class, map);
			}
		});


	}

	private void init(){
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

	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		/**
		 * 查询结束的回调函数
		 */
		@Override
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
					//					if (number.startsWith("+86")) {// 去除多余的中国地区号码标志，对这个程序没有影响。
					//						cb.setPhoneNum(number.substring(3));
					//					} else {
					cb.setPhoneNum(number);
					//					}
					cb.setSortKey(sortKey);
					cb.setId(contactId);
					cb.setPhotoId(photoId);
					cb.setLookUpKey(lookUpKey);
					
					if(null == selectMap){
					}else{
						if (selectMap.containsKey(number)) {
							cb.setSelected(1);
						}
					}
					
					list.add(cb);


				}
				if (list.size() > 0) {
					setAdapter(list);
				}
			}
		}

	}


	private void setAdapter(List<ContactInfo> list) {
		adapter = new SelectContactsToSendAdapter(this, list, alpha);
		personList.setAdapter(adapter);
		alpha.init(SelectContactsToSendActivity.this);
		alpha.setListView(personList);
		alpha.setHight(alpha.getHeight());
		alpha.setVisibility(View.VISIBLE);
		personList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ContactInfo cb= (ContactInfo) adapter.getItem(position);
				boolean check = SelectContactsToSendAdapter.isSelected.get(position);
				if(check){
					SelectContactsToSendAdapter.isSelected.put(position, false);
					selectContactList.remove(cb);
				}else{
					SelectContactsToSendAdapter.isSelected.put(position, true);
					selectContactList.add(cb);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}






}