package com.example.contactsbirthdays;

import android.net.Uri;

public class ContactInfo {

	private int id;
	private String name;
	private Uri image;
	private String birthday;
	
	public ContactInfo(int id, String name, String image, String birthday){
		this.setId(id);
		this.setName(name);
		this.setBirthday(birthday);
		this.setImage(Uri.parse(image));
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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Uri getImage() {
		return image;
	}

	public void setImage(Uri image) {
		this.image = image;
	}
}
