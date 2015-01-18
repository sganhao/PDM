package customAdapters;

import viewModels.NewsViewModel;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import entities.NewsItem;

public class NewsCustomAdapter extends BaseAdapter{

	private LayoutInflater _layoutInflater;
	private NewsItem[] _news;
	private Context _context;
	private int _layout;
	
	public NewsCustomAdapter (Context context, int layout, NewsItem[] news) {
		_context = context;
		_layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_news = news;
		_layout = layout;
	}

	@Override
	public int getCount() {
		return _news.length;
	}

	@Override
	public Object getItem(int id) {
		return _news[id];
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		final NewsItem item = (NewsItem) getItem(pos);

		if (view == null) {
			view = _layoutInflater.inflate(_layout, null);
			view.setTag(new NewsViewModel(view));
		}

		bindModelGroup(item, view.getTag());

		return view;
	}

	private void bindModelGroup(NewsItem newsItem, Object viewModelObject) {
		NewsViewModel viewModel = (NewsViewModel) viewModelObject;
		viewModel._news_classFullname.setText(newsItem.news_classFullname);
		viewModel._news_title.setText(newsItem.news_title);
		viewModel._news_date.setText(newsItem.printDate());
		
		if(!newsItem.news_isViewed) {
			viewModel._news_classFullname.setTextColor(Color.BLUE);
			viewModel._news_title.setTextColor(Color.BLUE);
		}else{
			viewModel._news_classFullname.setTextColor(Color.BLACK);
			viewModel._news_title.setTextColor(Color.BLACK);
		}		
	}
}
