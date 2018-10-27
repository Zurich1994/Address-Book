package com.example.contancts;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.call.MyApplication;
import com.example.call.T9Adapter;

public class CallFragment extends Fragment implements OnClickListener
{
	private EditText phone;
	private Button delete;
	private Button callp;
	private Button down;
	private Map<Integer, Integer> map = new HashMap<Integer, Integer>();	
	private ListView listView;

	private T9Adapter adapter;
	private MyApplication application;
	private LinearLayout allCall;
	private ImageButton up;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.tab01, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		allCall=(LinearLayout) getActivity().findViewById(R.id.allCall);
		application = (MyApplication)getActivity().getApplication();
		listView = (ListView)getActivity().findViewById(R.id.contactList);
        up = (ImageButton) getActivity().findViewById(R.id.up);
        down = (Button) getActivity().findViewById(R.id.down);
		phone = (EditText) getActivity().findViewById(R.id.phone);
		phone.setInputType(InputType.TYPE_NULL);
		listView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
			
                allCall.setVisibility(View.GONE);
                up.setVisibility(View.VISIBLE);
				return false;
			}
		});
		phone.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null == application.getContactInfo() || application.getContactInfo().size()<1){
				}else{
					if(null == adapter){
						adapter = new T9Adapter(getActivity().getApplication(),CallFragment.this);
						adapter.assignment(application.getContactInfo());
						listView.setAdapter(adapter);
						listView.setTextFilterEnabled(true);
						
					}else{
						adapter.getFilter().filter(s);
}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
		
				
			}

			@Override
			public void afterTextChanged(Editable s) {

				
			}
			
		});

		for (int i = 0; i < 12; i++) {
			View v = getActivity().findViewById(R.id.dialNum1 + i);
			v.setOnClickListener(this);
		}
		callp=(Button)getActivity(). findViewById(R.id.callp);
		callp.setOnClickListener(this);
		up.setOnClickListener(this);
		down.setOnClickListener(this);
		delete = (Button)getActivity(). findViewById(R.id.delete);
		delete.setOnClickListener(this);
		delete.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				phone.setText("");
				return false;
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialNum0:
			if (phone.getText().length() < 12) {
				
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum1:
			if (phone.getText().length() < 12) {
			  
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum2:
			if (phone.getText().length() < 12) {

				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum3:
			if (phone.getText().length() < 12) {
		
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum4:
			if (phone.getText().length() < 12) {
			
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum5:
			if (phone.getText().length() < 12) {
		
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum6:
			if (phone.getText().length() < 12) {
		
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum7:
			if (phone.getText().length() < 12) {
		
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum8:
			if (phone.getText().length() < 12) {
		
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum9:
			if (phone.getText().length() < 12) {
		
				input(v.getTag().toString());
			}
			break;
		case R.id.dialx:
			if (phone.getText().length() < 12) {
		
				input(v.getTag().toString());
			}
			break;
		case R.id.dialj:
			if (phone.getText().length() < 12) {
			
				input(v.getTag().toString());
			}
			break;
		case R.id.delete:
			delete();
			break;
		case R.id.callp:
		
				call(phone.getText().toString());
	
			break;
		case R.id.up:
			allCall.setVisibility(View.VISIBLE);
			up.setVisibility(View.GONE);
			break;
		case R.id.down:
			allCall.setVisibility(View.GONE);
            up.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	private void input(String str) {
		int c = phone.getSelectionStart();
		String p = phone.getText().toString();
		phone.setText(p.substring(0, c) + str + p.substring(phone.getSelectionStart(), p.length()));
		phone.setSelection(c + 1, c + 1);
	}
	private void delete() {
		int c = phone.getSelectionStart();
		if (c > 0) {
			String p = phone.getText().toString();
			phone.setText(p.substring(0, c - 1) + p.substring(phone.getSelectionStart(), p.length()));
			phone.setSelection(c - 1, c - 1);
		}
	}
	private  void call(String phone) {
		Uri number = Uri.parse("tel:"+phone);
		Intent dail = new Intent(Intent.ACTION_CALL,number);
		startActivity(dail);
		
	}	

}
