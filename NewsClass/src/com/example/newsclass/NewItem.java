package com.example.newsclass;

import java.io.Serializable;
import java.util.Date;

public class NewItem implements Serializable{
	
	public String classFullname;
	public int newsId;
	public int classId;
	public String title;
	public Date when;
	public String content;
	public boolean isViewed;
	
	public NewItem (String classFullname, int newsId, int classId, String title, Date when, String content, boolean isViewed) {
		this.classFullname = classFullname;
		this.newsId = newsId;
		this.classId = classId;
		this.title = title;
		this.when = when;
		this.content = content;
		this.isViewed = isViewed;
	}	
}
