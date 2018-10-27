package com.example.message;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

public class CommunicationModel {

		//备份短信
	public static int backupSMS(Context context,String fileName,ProgressDialog pd){
		int total = 0;
		File file;
		try {
			
			ContentResolver resolver=context.getContentResolver();
			Uri uri=Uri.parse("content://sms/");
			Cursor cursor=resolver.query(uri, null, null, null, null);
			int max = cursor.getCount();
			pd.setMax(max);
//文件			
//			createMkdir("/data/data/contancts");
			
			if (Environment.getExternalStorageState().equals(  
                    Environment.MEDIA_MOUNTED)){
				//创建文件输出流
				file = new File(Environment.getExternalStorageDirectory(),fileName);
//				file = new File(Environment.getExternalFilesDir());
				
			}
			else{
				//创建文件输出流
				file = new File(context.getFilesDir(), fileName);
			}
						
								
				FileOutputStream fos=new FileOutputStream(file);
				//创建xml构造器
				XmlSerializer serializer=Xml.newSerializer();
				//构造
				serializer.setOutput(fos, "UTF-8");
				serializer.startDocument("UTF-8", true);
				
				serializer.startTag(null, "sms_list");
				serializer.attribute(null,"total",max +"");
				
			
				while(cursor.moveToNext()){
					String id = cursor.getString(cursor.getColumnIndex("_id"));
					String address = cursor.getString(cursor.getColumnIndex("address"));
					String type = cursor.getString(cursor.getColumnIndex("type"));
					String date = cursor.getString(cursor.getColumnIndex("date"));
					String body = cursor.getString(cursor.getColumnIndex("body"));
				
					serializer.startTag(null, "sms");
					serializer.attribute(null,"id", id);
					serializer.startTag(null, "address");
					serializer.text(address);
					serializer.endTag(null, "address");
					serializer.startTag(null, "date");
					serializer.text(date);
					serializer.endTag(null, "date");
					serializer.startTag(null, "body");
					serializer.text(body);
					serializer.endTag(null, "body");
					
					
					serializer.endTag(null, "sms");
					
					total++;
					pd.setProgress(total);
					Log.i("CommunicationModel", "id:"+id+"address:"+address+"type:"+type+
							"date:"+date+"body:"+body);
				}
				
				serializer.endTag(null, "sms_list");
				serializer.endDocument();
				
				//资源释放
				cursor.close();
				fos.flush();
				fos.close();
				

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			total = -1;
			e.printStackTrace();
		}		
		
		return total;
	}
	
	/**
	 * 创建文件夹
	 * @param folderPath
	 */
	public static void createMkdir(String folderPath) {
	    File folder = new File(folderPath);
	    if (!folder.exists()) {
	        folder.mkdir();
	        Log.i(folderPath,folder.getAbsolutePath());
	        Log.i(folderPath, folder.getParent());
	    }else {
	    	Log.i(folderPath,"11111111111111111111111111");
	    }
		}

	
}


