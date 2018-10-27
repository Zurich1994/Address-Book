package com.example.call;

import java.util.ArrayList;
import java.util.List;

import com.example.contancts.CallFragment;
import com.example.contancts.R;
import com.example.utils.ContactInfo;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class T9Adapter extends BaseAdapter implements Filterable{

	private LayoutInflater mInflater;
	private List<ContactInfo> list;
	private List<ContactInfo> allContactList;
	private Context context;
	private String filterNum;
    private CallFragment mCallFragment;
	public T9Adapter(Context context,CallFragment cf) {     
		mInflater = LayoutInflater.from(context); 
		this.list = new ArrayList<ContactInfo>();
		this.context=context;
		mCallFragment = cf;
		//callf = cf;
		//mCallback = callback;
	}   

	public void assignment(List<ContactInfo> list){
		this.allContactList = list;
		this.list = this.allContactList;
	}
	public void add(ContactInfo bean) {
		list.add(bean);
	}
	public void remove(int position){
		list.remove(position);
	}
	public int getCount() {  
		return list.size();     
	}            
	public ContactInfo getItem(int position) {    
		return list.get(position);     
	}          
	public long getItemId(int position) {
		return 0;   
	}           
	public View getView(final int position, View convertView, ViewGroup parent) {   

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.dial_item, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.TvDialContactName);  
			holder.pinyin = (TextView) convertView.findViewById(R.id.TvPinyinAddr);  
			holder.number = (TextView) convertView.findViewById(R.id.TvNumAddr);  
			holder.callTo = (ImageButton) convertView.findViewById(R.id.callTo);  
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.name.setText(list.get(position).getName());
		String formattedNumber = list.get(position).getPinyin();
		
		if (null == filterNum || "".equals(filterNum)) {
			holder.pinyin.setVisibility(View.INVISIBLE);
			holder.number.setText(list.get(position).getPhoneNum());
		} else {
			holder.pinyin.setVisibility(View.VISIBLE);
			holder.number.setText(Html.fromHtml(list.get(position).getPhoneNum().replace(filterNum, "<font color='#cc0000'>" + filterNum + "</font>")));
			if (!TextUtils.isEmpty(filterNum)) {
				for (int i = 0; i < filterNum.length(); i++) {
					char c = filterNum.charAt(i);
					if (TextUtils.isDigitsOnly(String.valueOf(c))) {
						char[] zms = digit2Char(Integer.parseInt(c + ""));
						if (zms != null) {
							for (char c1 : zms) {
								formattedNumber = formattedNumber.replaceAll(String.valueOf(c1).toUpperCase(), "%%" + String.valueOf(c1).toUpperCase() + "@@");
							}
							formattedNumber = formattedNumber.replaceAll("%%", "<font color='#cc0000'>");
							formattedNumber = formattedNumber.replaceAll("@@", "</font>");
						}
					}
				}
				holder.pinyin.setText(Html.fromHtml(formattedNumber));
			}
		}
		holder.callTo.setClickable(false);
		holder.callTo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phone = (String) list.get(position).getPhoneNum();
				Uri number = Uri.parse("tel:"+phone);
				Intent dail = new Intent(Intent.ACTION_CALL,number);
				mCallFragment.startActivity(dail);
			}
		});
		
		

		convertView.setTag(holder);
		return convertView;
	}   

	private final class ViewHolder {   
		public TextView name;        
		public TextView pinyin;        
		public TextView number;    
		private ImageButton callTo;
	}
	
	public char[] digit2Char(int digit) {
		char[] cs = null;
		switch (digit) {
		case 0:
			cs = new char[] {};
			break;
		case 1:
			break;
		case 2:
			cs = new char[] { 'a', 'b', 'c' };
			break;
		case 3:
			cs = new char[] { 'd', 'e', 'f' };
			break;
		case 4:
			cs = new char[] { 'g', 'h', 'i' };
			break;
		case 5:
			cs = new char[] { 'j', 'k', 'l' };
			break;
		case 6:
			cs = new char[] { 'm', 'n', 'o' };
			break;
		case 7:
			cs = new char[] { 'p', 'q', 'r', 's' };
			break;
		case 8:
			cs = new char[] { 't', 'u', 'v' };
			break;
		case 9:
			cs = new char[] { 'w', 'x', 'y', 'z' };
			break;
		}
		return cs;
	}
	
	public Filter getFilter() {
		Filter filter = new Filter() {
			protected void publishResults(CharSequence constraint, FilterResults results) {
				list = (ArrayList<ContactInfo>) results.values;
				if (results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
			protected FilterResults performFiltering(CharSequence s) {
				String str = s.toString();
				filterNum = str;
				FilterResults results = new FilterResults();
				ArrayList<ContactInfo> contactList = new ArrayList<ContactInfo>();
				if (allContactList != null && allContactList.size() != 0) {
					for(ContactInfo cb : allContactList){
						if(cb.getFormattedNumber().indexOf(str)>=0 || cb.getPhoneNum().indexOf(str)>-1){
							contactList.add(cb);
						}
					}
				}
				results.values = contactList;
				results.count = contactList.size();
				return results;
			}
		};
		return filter;
	}


}
