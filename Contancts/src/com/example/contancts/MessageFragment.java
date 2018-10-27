package com.example.contancts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;






import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.message.*;





public class MessageFragment extends Fragment 
{
	
	protected static final int BACKUP_SUCCESS = 0;//备份成功
	protected static final int BACKUP_ERROR = 1;//备份失败
	protected static final int RECOVERY_SUCCESS = 2;//还原成功
	protected static final int RECOVERY_ERROR = 3;//还原失败
	
	private int result = 0;
	private ProgressDialog pd;
	
	private ListView smsListView;
	private SMSAdpter smsAdpter;
	private RexseeSMS rsms;
	private Button newSms;
	
	
	private Handler handler=new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what){
			
			case BACKUP_SUCCESS:
				Toast.makeText(getActivity().getApplicationContext(), getString(R.string.backup_success)+result, Toast.LENGTH_LONG).show();
				break;
			case BACKUP_ERROR:
				Toast.makeText(getActivity().getApplicationContext(), getString(R.string.backup_error)+result, Toast.LENGTH_LONG).show();
				break;
			case RECOVERY_SUCCESS:
				Toast.makeText(getActivity().getApplicationContext(), getString(R.string.recovery_success)+result, Toast.LENGTH_LONG).show();
				break;
			case RECOVERY_ERROR:
				Toast.makeText(getActivity().getApplicationContext(), getString(R.string.recovery_error)+result, Toast.LENGTH_LONG).show();
			break;
			}
			
		};
		
		
	};
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.sms_list_view, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		

		smsListView = (ListView) getActivity().findViewById(R.id.sms_list);
		smsAdpter = new SMSAdpter(getActivity());
		rsms = new RexseeSMS(getActivity());
		
		List<SMSBean> list_mmt = rsms.getThreadsNum(rsms.getThreads(0));
		
		
		
		smsAdpter.assignment(list_mmt);
		//smsListView.setAdapter(smsAdpter);
		smsListView.setAdapter(smsAdpter);
		
		smsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, String> map = new HashMap<String, String>();
				SMSBean sb = (SMSBean) smsAdpter.getItem(position);
				map.put("phoneNumber", sb.getAddress());
				map.put("threadId", sb.getThread_id());
				BaseIntentUtil.intentSysDefault(getActivity(),
						MessageBoxList.class, map);
			}
		});		
		
		newSms = (Button) getActivity(). findViewById(R.id.newSms);
		newSms.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BaseIntentUtil.intentSysDefault(getActivity(), NewSMSActivity.class, null);
				
			}
		});
		
	}
		
	public void showLoding(Context context,String msg){
		pd = new ProgressDialog(context);
		pd.setTitle(R.string.backup_tip);
		pd.setMessage(msg);
		
		pd.setCancelable(false);
		
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		
		
	}
	
	
	public String getPersonName(String number) {   
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME, };   
		Cursor cursor = this.getActivity().getContentResolver().query(   
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
