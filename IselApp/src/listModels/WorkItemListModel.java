package listModels;

import java.io.Serializable;

import entities.WorkItem;

public class WorkItemListModel implements Serializable{

	WorkItem[] _workitems;
	
	public WorkItemListModel(WorkItem[] workItem){
		_workitems = workItem;
	}

	public WorkItem getItem(int pos){
		return _workitems[pos];
	}
	
	public WorkItem[] getItems() {
		return _workitems;
	}
}
