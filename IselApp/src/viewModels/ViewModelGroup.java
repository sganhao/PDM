package viewModels;

import com.example.iselapp.R;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class ViewModelGroup {
	
	public TextView _news_classFullname;
	public TextView _news_title;
	public TextView _news_date;
	
	public ViewModelGroup(View view) {
		_news_classFullname = (TextView) view.findViewById(R.id.newsitem_classFullname);
		_news_title = (TextView) view.findViewById(R.id.newsitem_title);
		_news_date = (TextView) view.findViewById(R.id.newsitem_date);
		
		_news_classFullname.setTypeface(null, Typeface.BOLD_ITALIC);
		_news_title.setTypeface(null, Typeface.BOLD_ITALIC);
		_news_date.setTypeface(null, Typeface.BOLD_ITALIC);
	}
}
