package fragments;

import viewModels.WorkItemDetailedViewModel;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iselapp.R;

import entities.WorkItem;

public class WorkItemFragment extends Fragment{
	
	private static String TAG = "IselApp";
	private View _view;
	private WorkItem _item;

	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstance){
		Log.d(TAG , "ItemFragment.onCreateView");
		_view = inflater.inflate(R.layout.workitem_fragment_layout, group, false);
		Bundle b = getArguments();
		_item = (WorkItem) b.getSerializable("item");
		bindModel();
		return _view;		
	}
	
	public static WorkItemFragment newInstance(WorkItem model){
		WorkItemFragment f = new WorkItemFragment();
		Bundle b = new Bundle();
		b.putSerializable("item", model);
		f.setArguments(b);
		Log.d(TAG, "ItemFragment.newInstance " + model);
		return f;
	}
	
	private void bindModel(){
		WorkItemDetailedViewModel viewModel = new WorkItemDetailedViewModel(_view);
		viewModel.classFullName.setText("" + _item.workItem_classFullname);
		viewModel.acronym.setText(_item.workItem_Acronym);
		viewModel.title.setText(_item.workItem_title);
		viewModel.startDate.setText("StartDate: " + _item.printStartDate());
		viewModel.dueDate.setText("DueDate: " + _item.printDueDate());
		viewModel.linkToSelf.loadDataWithBaseURL("", _item.workItem_linkToSelf, "text/html", "UTF-8", "");
	}
}