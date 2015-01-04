package entities;

import java.io.Serializable;
import java.util.Calendar;

public class NewsItem implements Serializable{

	public String news_classFullname;
	public int news_classId;
	public int news_id;
	public String news_title;
	public Calendar news_when;
	public String news_content;
	public boolean news_isViewed;
	
	public NewsItem (String news_classFullname, int news_classId, int news_id, String news_title, long news_when, String news_content, boolean news_isViewed){
		this.news_classFullname = news_classFullname;
		this.news_classId = news_classId;
		this.news_id = news_id;
		this.news_title = news_title;
		this.news_content = news_content;
		this.news_isViewed = news_isViewed;
		this.news_when = Calendar.getInstance();
		this.news_when.setTimeInMillis(news_when);
	}
	
	public String printDate(){
		return  "Date: " +
				news_when.get(Calendar.DAY_OF_MONTH) + "/" + 
				news_when.get(Calendar.MONTH) + "/" + 
				news_when.get(Calendar.YEAR) + " - " + 
				news_when.get(Calendar.HOUR) + ":" +
				news_when.get(Calendar.MINUTE) + ":" +
				news_when.get(Calendar.SECOND);
	}
}
