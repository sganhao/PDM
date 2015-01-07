package fragments;

import listModels.WorkItemListModel;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.iselapp.R;

import customAdapters.WorkItemCustomAdapter;

public class WorkItemListFragment extends ListFragment {

	private static final String TAG = "IselApp";
	private WorkItemListModel _workItemListModel;
	
	@Override
	public void onCreate(Bundle state){
		Log.d(TAG,"NewsItemListFragment - onCreate");
		super.onCreate(state);
		
		Bundle args = getArguments();
		_workItemListModel = (WorkItemListModel) args.getSerializable("key");
		
		this.setListAdapter(
				new WorkItemCustomAdapter(getActivity(), R.layout.workitem_list_layout, _workItemListModel.getItems()));	
	}	

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Callback cb = (Callback) getActivity();
		cb.onListItemClick(position);
	}
	
	public static WorkItemListFragment newInstance(WorkItemListModel model){
		WorkItemListFragment f = new WorkItemListFragment();
		Bundle args = new Bundle();
		args.putSerializable("key", model);
		f.setArguments(args);
		return f;
	}
	
	public interface Callback{
		void onListItemClick(int position);
	}
}
