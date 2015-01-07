package entities;

import java.io.Serializable;
import java.util.Calendar;

public class WorkItem implements Serializable{
	
	public int workItem_classId;
	public String workItem_classFullname;
	public int workItem_id;
	public String workItem_Acronym;
	public String workItem_title;
	public boolean workItem_reqGroupSubmission;
	public Calendar workItem_startDate;
	public Calendar workItem_dueDate;
	public boolean workItem_acceptsLateSubmission;
	public boolean workItem_acceptsResubmission;
	public ReportUploadInfo workItem_reportUploadInfo;
	public AttachmentUploadInfo workItem_attachmentUploadInfo;
	
	public WorkItem(int classId, String classFullname, int workItemId, String acronym, String title, boolean reqGroupSubmission,
			long startDate, long dueDate, boolean acceptsLateSubmission, boolean acceptsResubmission,
			ReportUploadInfo repUpInfo, AttachmentUploadInfo attachUpInfo) {
		workItem_classId = classId;
		workItem_classFullname = classFullname;
		workItem_id = workItemId;
		workItem_Acronym = acronym;
		workItem_title = title;
		workItem_reqGroupSubmission = reqGroupSubmission;
		workItem_acceptsLateSubmission = acceptsLateSubmission;
		workItem_acceptsResubmission = acceptsResubmission;
		workItem_reportUploadInfo = repUpInfo;
		workItem_attachmentUploadInfo = attachUpInfo;
		workItem_startDate = Calendar.getInstance();
		workItem_startDate.setTimeInMillis(startDate);
		workItem_dueDate = Calendar.getInstance();
		workItem_dueDate.setTimeInMillis(dueDate);
	}

	public String printStartDate(){
		return  workItem_startDate.get(Calendar.HOUR_OF_DAY) + ":" +
				workItem_startDate.get(Calendar.MINUTE) + ":" +
				workItem_startDate.get(Calendar.SECOND) + " - " +
				workItem_startDate.get(Calendar.DAY_OF_MONTH) + "/" + 
				(workItem_startDate.get(Calendar.MONTH) + 1) + "/" + 
				workItem_startDate.get(Calendar.YEAR); 
				
	}
	
	public String printDueDate(){
		return  workItem_dueDate.get(Calendar.HOUR_OF_DAY) + ":" +
				workItem_dueDate.get(Calendar.MINUTE) + ":" +
				workItem_dueDate.get(Calendar.SECOND) + " - " +
				workItem_dueDate.get(Calendar.DAY_OF_MONTH) + "/" + 
				(workItem_dueDate.get(Calendar.MONTH) + 1) + "/" + 
				workItem_dueDate.get(Calendar.YEAR); 
				
	}
}