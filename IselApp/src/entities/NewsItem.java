package entities;

import java.io.Serializable;
import java.util.Date;

public class NewsItem implements Serializable{

	public String news_fullname;
	public int news_newsId;
	public int news_id;
	public String news_title;
	public Date news_when;
	public String news_content;
	public boolean news_isViewed;
	
	public NewsItem (String news_fullname, int news_newsId, int news_id, String news_title, Date news_when, String news_content, boolean news_isViewed) {
		this.news_fullname = news_fullname;
		this.news_newsId = news_newsId;
		this.news_id = news_id;
		this.news_title = news_title;
		this.news_when = news_when;
		this.news_content = news_content;
		this.news_isViewed = news_isViewed;
	}
}
