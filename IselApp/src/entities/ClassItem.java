package entities;

import java.io.Serializable;

public class ClassItem implements Serializable{
	private int class_id;
	private String class_fullname;
	private boolean class_showNews;
	
	public ClassItem(int class_id, String class_fullname, boolean class_showNews){
		this.class_id = class_id;
		this.class_fullname = class_fullname;
		this.class_showNews = class_showNews;
	}	
	
	public boolean getShowNews() {
		return class_showNews;
	}
	
	public void setShowNews(boolean flag){
		class_showNews = flag;
	}
	
	public String getFullname() {
		return class_fullname;
	}
	
	public int getId() {
		return class_id;
	}	
}
