package com.example.message;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * 
 * @author BenZeph
 * 
 */
public class SmsBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String phoneNum = "";
		StringBuilder sb = new StringBuilder();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Object[] myObjectPuds = (Object[]) bundle.get("pdus");
			SmsMessage[] messages = new SmsMessage[myObjectPuds.length];
			for (int i = 0; i < myObjectPuds.length; i++)
				messages[i] = SmsMessage
						.createFromPdu((byte[]) myObjectPuds[i]);
			for (SmsMessage currentMessage : messages) {
				sb.append("��Ϣ����:\n");
				sb.append(currentMessage.getDisplayOriginatingAddress());
				phoneNum = currentMessage.getDisplayOriginatingAddress();
				sb.append("\n����Ϣ����\n");
				sb.append(currentMessage.getDisplayMessageBody());
			}
		}

	}

}
