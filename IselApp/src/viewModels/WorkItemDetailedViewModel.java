package viewModels;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.iselapp.R;

public class WorkItemDetailedViewModel {

	public final TextView classFullName;
	public final TextView title;
	public final TextView startDate;
	public final TextView dueDate;
	public final TextView acronym;
	public final WebView linkToSelf;
	
	public WorkItemDetailedViewModel(View view){
		classFullName = (TextView) view.findViewById(R.id.workitem_item_classFullname);
		title = (TextView) view.findViewById(R.id.workitem_item_title);
		startDate = (TextView) view.findViewById(R.id.workitem_item_startdate);
		dueDate = (TextView) view.findViewById(R.id.workitem_item_duedate);
		acronym = (TextView) view.findViewById(R.id.workitem_item_acronym);
		linkToSelf = (WebView) view.findViewById(R.id.workitem_item_linkToSelf);
	}
	
}
