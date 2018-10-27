package com.example.contancts;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactsList.CharacterParser;
import com.example.contactsList.PinyinComparator;
import com.example.contactsList.ReFlashListView;
import com.example.contactsList.ReFlashListView.IReflashListener;
import com.example.contactsList.SideBar;
import com.example.contactsList.SideBar.OnTouchingLetterChangedListener;
import com.example.contactsList.SortAdapter;
import com.example.utils.ContactInfo;

public class ContactsFragment extends Fragment implements IReflashListener
{
	private Context mContext;
	private ReFlashListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private AsyncQueryHandler asyncQuery;
	private CharacterParser characterParser;
	private List<ContactInfo> list;
	private Map<Integer, ContactInfo> contactIdMap = null;
	private PinyinComparator pinyinComparator;
	private String[] lianxiren1 = new String[] { "����绰", "���Ͷ���", "�鿴��ϸ","ɾ��" };
	
	private LinearLayout addContact;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.tab02, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext=getActivity();
		asyncQuery = new MyAsyncQueryHandler(getActivity().getContentResolver());
		findView();
		initData();
		addContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Uri insertUri = android.provider.ContactsContract.Contacts.CONTENT_URI;
				Intent intent = new Intent(Intent.ACTION_INSERT, insertUri);
				startActivityForResult(intent, 1008);
			}
		});
	}
	@Override
	public void onResume() {
		initData();
		super.onResume();
	}
	private void findView() {
		sideBar = (SideBar)getActivity(). findViewById(R.id.sidrbar);
		dialog = (TextView) getActivity().findViewById(R.id.dialog);
		sortListView = (ReFlashListView)getActivity().findViewById(R.id.country_lvcountry);		
		addContact = (LinearLayout) getActivity().findViewById(R.id.addContact);
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
	
		// ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar.setTextView(dialog);

		// �����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@SuppressLint("NewApi")
			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}
			}
		});

	}
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		/**
		 * ��ѯ�����Ļص�����
		 */
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				
				contactIdMap = new HashMap<Integer, ContactInfo>();
				
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

					if (contactIdMap.containsKey(contactId)) {
						
					}else{
						
						ContactInfo cb = new ContactInfo();
						cb.setName(name);
						cb.setPhoneNum(number);
						cb.setSortKey(sortKey);
						cb.setId(contactId);
						cb.setPhotoId(photoId);
						cb.setLookUpKey(lookUpKey);
						list.add(cb);				
						contactIdMap.put(contactId, cb);
						
					}
				}
				if (list.size() > 0) {
					List<String> constact = new ArrayList<String>();
					for(int i=0;i<list.size();i++){
						constact.add(list.get(i).getName());
					}
					
					//��ȡ������ƴ��������
					String[] names = new String[] {};
					names = constact.toArray(names);
					list = filledData(list,names);

					// ����a-z��������Դ����
					Collections.sort(list, pinyinComparator);
					if(adapter == null){
						adapter = new SortAdapter(mContext, list);
						sortListView.setInterface(ContactsFragment.this);
					}else{
						adapter.updateListView(list);
					}
					
					sortListView.setAdapter(adapter);
					sortListView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Log.i("TAG",position+"");
							ContactInfo cb = (ContactInfo) adapter.getItem(position-1);
							showContactDialog(lianxiren1, cb,position-1);
						}
					});
				}
			}
		}

	}
	protected List<ContactInfo> filledData(List<ContactInfo> list,String[] date) {

		//List<ContactInfo> mSortList = new ArrayList<ContactInfo>();

		for (int i = 0; i < date.length; i++) {
			/*ContactInfo sortModel = new ContactInfo();
			sortModel.setName(date[i]);*/
			// ����ת����ƴ��
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if (sortString.matches("[A-Z]")) {
				list.get(i).setPinyin(sortString.toUpperCase());
			} else {
				list.get(i).setPinyin("#");
			}
		}
		return list;

	
	}
	private void showContactDialog(final String[] arg ,final ContactInfo cb,final int position){
		new AlertDialog.Builder(getActivity()).setTitle(cb.getName()).setItems(arg,
				new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which){

				Uri uri = null;

				switch(which){

				case 0://��绰
					String toPhone = cb.getPhoneNum();
					uri = Uri.parse("tel:" + toPhone);
					Intent it = new Intent(Intent.ACTION_CALL, uri);
					startActivity(it);
					break;

				case 1://����Ϣ

					break;

				case 2:// �鿴��ϸ       �޸���ϵ������

					uri = ContactsContract.Contacts.CONTENT_URI;
					Uri personUri = ContentUris.withAppendedId(uri, cb.getId());
					Intent intent2 = new Intent();
					intent2.setAction(Intent.ACTION_VIEW);
					intent2.setData(personUri);
					startActivity(intent2);
					break;

				case 3:// ɾ��
					showDelete(cb.getId(), position);
					break;
				}
			}

		}).show();
	}


	private void showDelete(final int contactsID, final int position) {
		new AlertDialog.Builder(getActivity()).setIcon(R.drawable.ic_launcher).setTitle("�Ƿ�ɾ������ϵ��")
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//Դ��ɾ��
				Uri deleteUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactsID);
				Uri lookupUri = ContactsContract.Contacts.getLookupUri(getActivity().getContentResolver(), deleteUri);
				if(lookupUri != Uri.EMPTY){
					getActivity().getContentResolver().delete(deleteUri, null, null);
				}
				adapter.remove(position);
				adapter.notifyDataSetChanged();
				Toast.makeText(getActivity(), "����ϵ���Ѿ���ɾ��.", Toast.LENGTH_SHORT).show();
			}
		})
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		}).show();
	}

@Override
public void onReflash() {
	// TODO Auto-generated method stub\
	Handler handler = new Handler();
	handler.postDelayed(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//��ȡ��������
			initData();
			//֪ͨlistview ˢ���������
			sortListView.reflashComplete();
		}
	}, 2000);
	
}
	
}
