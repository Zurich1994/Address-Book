package Dao;

import java.sql.Date;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Contact {
	private String name;//��ϵ������
	private String tel_1;//��ϵ�˵绰1
	private String tel_2;//��ϵ�˵绰2
	private String location;//�����ϵ�˵ص�
	private Date date;//�����ϵ��ʱ��
	private String company;//��ϵ�����ڹ�˾
	private String remark;//�Դ���ϵ�˵ı�ע
	private int del;//�Ƿ��û�ɾ��
	private String picture;//�û�ͷ���ַ
	
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public int getDel() {
		return del;
	}
	public void setDel(int del) {
		this.del = del;
	}
	//private String picture;//��ϵ��ͷ�񵥶��ڱ𴦴洢
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel_1() {
		return tel_1;
	}
	public void setTel_1(String tel_1) {
		this.tel_1 = tel_1;
	}
	public String getTel_2() {
		return tel_2;
	}
	public void setTel_2(String tel_2) {
		this.tel_2 = tel_2;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Contact()
	{
		name="������ϵ��";
		tel_1="00000000";
		tel_2="";
		location="";
		company="";
		remark="";
		del=0;
		picture="";
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		try {
			java.util.Date date = sdf.parse("0001-01-01");
			java.sql.Date   formatDate = new java.sql.Date(date.getTime()); 
			this.date=formatDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
