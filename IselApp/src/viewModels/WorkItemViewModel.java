package viewModels;

import android.view.View;
import android.widget.TextView;

import com.example.iselapp.R;

public class WorkItemViewModel {

	public final TextView classFullname;
	public final TextView title;
	public final TextView dueDate;
	
	public WorkItemViewModel(View view){
		classFullname = (TextView) view.findViewById(R.id.workitem_classFullname);
		title = (TextView) view.findViewById(R.id.workitem_title);
		dueDate = (TextView) view.findViewById(R.id.workitem_duedate);
	}
}
