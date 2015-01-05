package fragments;

import com.example.iselapp.R;

import entities.NewsItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsItemFragment extends Fragment{
	
	private static String TAG = "IselApp";
	private View _view;
	private NewsItem _item;

	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstance){
		Log.d(TAG , "ItemFragment.onCreateView");
		_view = inflater.inflate(R.layout.newsitem_fragment_layout, group, false);
		Bundle b = getArguments();
		_item = (NewsItem) b.getSerializable("item");
		bindModel();
		return _view;		
	}
	
	public static NewsItemFragment newInstance(NewsItem model){
		NewsItemFragment f = new NewsItemFragment();
		Bundle b = new Bundle();
		b.putSerializable("item", model);
		f.setArguments(b);
		Log.d(TAG, "ItemFragment.newInstance "+model);
		return f;
	}
	
	private void bindModel(){
		TextView className = (TextView) _view.findViewById(R.id.newsitem_item_classFullname);
		className.setText("" + _item.news_classFullname);
		
		TextView title = (TextView) _view.findViewById(R.id.newsitem_item_title);
		title.setText(_item.news_title);
		
		TextView date = (TextView) _view.findViewById(R.id.newsitem_item_date);
		date.setText(_item.printDate());
		
		TextView content = (TextView) _view.findViewById(R.id.newsitem_item_content);
		content.setText("\n" + _item.news_content);
	}
}
