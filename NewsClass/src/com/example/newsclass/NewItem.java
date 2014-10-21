package com.example.newsclass;

import java.util.Date;

public class NewItem {
	
	public int id;
	public String title;
	public Date when;
	public String content;
	public boolean isViewed;
	
	public NewItem (int id, String title, Date when, String content, boolean isViewed) {
		this.id = id;
		this.title = title;
		this.when = when;
		this.content = content;
		this.isViewed = isViewed;
	}
	
}
