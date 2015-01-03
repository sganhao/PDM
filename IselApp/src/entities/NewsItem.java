package entities;

import java.io.Serializable;
import java.util.Date;

public class NewsItem implements Serializable{

	public String news_classFullname;
	public int news_classId;
	public int news_id;
	public String news_title;
	public Date news_when;
	public String news_content;
	public boolean news_isViewed;
	
	public NewsItem (String news_classFullname, int news_classId, int news_id, String news_title, Date news_when, String news_content, boolean news_isViewed) {
		this.news_classFullname = news_classFullname;
		this.news_classId = news_classId;
		this.news_id = news_id;
		this.news_title = news_title;
		this.news_when = news_when;
		this.news_content = news_content;
		this.news_isViewed = news_isViewed;
	}
}
