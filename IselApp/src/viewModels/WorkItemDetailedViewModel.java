package viewModels;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.iselapp.R;

public class WorkItemDetailedViewModel {

	public final TextView classFullName;
	public final TextView title;
	public final TextView startDate;
	public final TextView dueDate;
	public final TextView acronym;
	public final CheckBox individualWorkItem;
	public final CheckBox lateSubmission;
	public final CheckBox reSubmission;
	public final CheckBox repUpInfo;
	public final CheckBox attUpInfo;
	
	public WorkItemDetailedViewModel(View view){
		classFullName = (TextView) view.findViewById(R.id.workitem_item_classFullname);
		title = (TextView) view.findViewById(R.id.workitem_item_title);
		startDate = (TextView) view.findViewById(R.id.workitem_item_startdate);
		dueDate = (TextView) view.findViewById(R.id.workitem_item_duedate);
		acronym = (TextView) view.findViewById(R.id.workitem_item_acronym);
		individualWorkItem = (CheckBox) view.findViewById(R.id.workitem_item_indidual);
		lateSubmission = (CheckBox) view.findViewById(R.id.workitem_item_lateSubmission);
		reSubmission = (CheckBox) view.findViewById(R.id.workitem_item_reSubmission);
		repUpInfo = (CheckBox) view.findViewById(R.id.workitem_item_repUpInfo);
		attUpInfo = (CheckBox) view.findViewById(R.id.workitem_item_attUpInfo);
		
		individualWorkItem.setClickable(false);
		individualWorkItem.setText(" GroupSubmission");
		lateSubmission.setClickable(false);
		lateSubmission.setText(" LateSubmission");
		reSubmission.setClickable(false);
		reSubmission.setText(" Resubmission");
		repUpInfo.setClickable(false);
		attUpInfo.setClickable(false);
	}
	
}
