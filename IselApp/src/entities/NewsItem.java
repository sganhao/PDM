package entities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsItem implements Serializable{

	public String news_fullname;
	public int news_newsId;
	public int news_id;
	public String news_title;
	public Date news_when;
	public String news_content;
	public boolean news_isViewed;
	
	public NewsItem (String news_fullname, int news_newsId, int news_id, String news_title, String news_when, String news_content, boolean news_isViewed) throws ParseException {
		this.news_fullname = news_fullname;
		this.news_newsId = news_newsId;
		this.news_id = news_id;
		this.news_title = news_title;
		this.news_content = news_content;
		this.news_isViewed = news_isViewed;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		this.news_when = format.parse(news_when);
	}
}
