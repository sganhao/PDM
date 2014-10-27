package com.example.contactsbirthdays;

import android.net.Uri;

public class ContactInfo {

	private int id;
	private String name;
	private Uri image;
	private String birthday;
	
	public ContactInfo(int id, String name, String image, String birthday){
		this.id = id;
		this.name = name;
		this.birthday = birthday;
		this.image = (Uri.parse(image));
	}

	public int getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public String getBirthday() {
		return birthday;
	}


	public Uri getImage() {
		return image;
	}

}
