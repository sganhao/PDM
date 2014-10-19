package com.example.newsclass;

public class Clazz {
	private int id;
	private String fullname;
	private boolean showNews;
	
	public Clazz(int id, String name, boolean flag){
		this.id = id;
		fullname = name;
		showNews = flag;
	}
	
	
	public boolean getShowNews() {
		return showNews;
	}
	
	public String getFullname() {
		return fullname;
	}
	
	public int getId() {
		return id;
	}
	
}
