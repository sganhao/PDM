package customAdapters;

import viewModels.WorkItemViewModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import entities.WorkItem;

public class WorkItemCustomAdapter extends BaseAdapter{

	private LayoutInflater _layoutInflater;
	private WorkItem[] _workItems;
	private int _layout;
	
	public WorkItemCustomAdapter (Context context, int layout, WorkItem[] workItems) {
		_layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_workItems = workItems;
		_layout = layout;
	}
	
	@Override
	public int getCount() {
		return _workItems.length;
	}

	@Override
	public WorkItem getItem(int id) {
		return _workItems[id];
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		final WorkItem item = (WorkItem) getItem(pos);

		if (view == null) {
			view = _layoutInflater.inflate(_layout, null);
			view.setTag(new WorkItemViewModel(view));
		}

		bindModel(item, view.getTag());

		return view;
	}
	
	private void bindModel(WorkItem workItem, Object viewModelObject) {
		WorkItemViewModel viewModel = (WorkItemViewModel) viewModelObject;
		viewModel.classFullname.setText(""+workItem.workItem_classFullname);
		viewModel.title.setText("Title: " + workItem.workItem_title);
		viewModel.dueDate.setText("Due Date: " + workItem.printDueDate());	
	}

}
