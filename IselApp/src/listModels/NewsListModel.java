package listModels;

import java.io.Serializable;

import entities.NewsItem;

public class NewsListModel implements Serializable{

	NewsItem[] newsItem;
	
	public NewsListModel(NewsItem[] newItem){
		newsItem = newItem;
	}

	public NewsItem getItem(int pos){
		return newsItem[pos];
	}
	
	public NewsItem[] getItems() {
		return newsItem;
	}
}
