package Dao;

import java.sql.Date;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Contact {
	private String name;//联系人姓名
	private String tel_1;//联系人电话1
	private String tel_2;//联系人电话2
	private String location;//添加联系人地点
	private Date date;//添加联系人时间
	private String company;//联系人所在公司
	private String remark;//对此联系人的备注
	private int del;//是否被用户删除
	private String picture;//用户头像地址
	
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
	//private String picture;//联系人头像单独在别处存储
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
		name="新增联系人";
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
