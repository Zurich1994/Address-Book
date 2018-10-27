package com.example.utils;

public class SimpleContactInfo {

	private String name;
	private String tel_1;

	public SimpleContactInfo(String name){
		this.name = name;
	}
	public SimpleContactInfo(){
	}
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

}
