package com.example.setting;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class HttpUnity {
	public static int httpUpload(String url,String json){
		int code = 0;
		try {
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(20000);
			conn.setReadTimeout(20000);
			conn.setDoOutput(true);
			String params = "contactsJson="+json;
			conn.getOutputStream().write(params.getBytes("GB2312"));
		    code = conn.getResponseCode();
			return code;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return code;
		
	}
	public static byte[] httpRecc(String url){
		try {
			String json = null;
			Gson gson = new Gson();
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(URI.create(url));
			
		      //设置请求超时  
	        int timeoutConnection = 3 * 10000;  
	        HttpConnectionParams.setConnectionTimeout(request.getParams(),timeoutConnection);  
	        //设置响应超时  
	        int timeoutSocket = 5 * 10000;  
	        HttpConnectionParams.setSoTimeout(request.getParams(), timeoutSocket); 
			
			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			NameValuePair pair1 = new BasicNameValuePair("user", "15555215554");
			NameValuePair pair2 = new BasicNameValuePair("password", "15555215554");
			pairList.add(pair1);
			pairList.add(pair2);
			UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(
					pairList, "GB2312");
			request.setEntity(requestEntity);
			HttpResponse resp = client.execute(request);
			if(200 == resp.getStatusLine().getStatusCode()){
				HttpEntity resEntity = resp.getEntity();
				byte[] result = EntityUtils.toByteArray(resEntity);
				return result;
			}else{
				return null;
			}
				
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
