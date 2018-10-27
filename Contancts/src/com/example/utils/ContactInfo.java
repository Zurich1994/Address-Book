package com.example.utils;

public class ContactInfo {

	private int id;
	private String name;
	private String formattedNumber;
	private String phoneNum;
	private String pinyin;
	private String sortKey;
	private Long photoId;
	private String lookUpKey;
	private int selected = 0;
	
	
	public String getSortKey() {
		return sortKey;
	}
	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
	public Long getPhotoId() {
		return photoId;
	}
	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}
	public String getLookUpKey() {
		return lookUpKey;
	}
	public void setLookUpKey(String lookUpKey) {
		this.lookUpKey = lookUpKey;
	}
	public ContactInfo(String name){
		this.name = name;
	}
	public ContactInfo(String name,String pinyin){
		this.name = name;
		this.pinyin=pinyin;
	}
	public ContactInfo(){
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFormattedNumber() {
		return formattedNumber;
	}
	public void setFormattedNumber(String formattedNumber) {
		this.formattedNumber = formattedNumber;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	public int getSelected() {
		return selected;
	}
	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	
}
