package com.example.newsclass;

import java.io.Serializable;

public class Participant implements Serializable{
	
	int number;
	String fullName;
	String email;
	String avatarUri;
	boolean isTeacher;
	
	public Participant(int number, String fullName/*, String email*/, String avatarUri, boolean isTeacher){
		this.number = number;
		this.fullName = fullName;
		//this.email = email;
		this.avatarUri = avatarUri;
		this.isTeacher = isTeacher;
	}

}
