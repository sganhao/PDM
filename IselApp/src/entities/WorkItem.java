package entities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkItem implements Serializable{
	
	int workItem_classId;
	int workItem_id;
	String workItem_Acronym;
	String workItem_title;
	boolean workItem_reqGroupSubmission;
	Date workItem_startDate;
	Date workItem_dueDate;
	boolean workItem_acceptsLateSubmission;
	boolean workItem_acceptsResubmission;
	InfoDocument workItem_infoDocument;
	AttachmentDocument workItem_attachmentDocument;
	ReportUploadInfo workItem_reportUploadInfo;
	AttachmentUploadInfo workItem_attachmentUploadInfo;
	
	
	
	public WorkItem(int classId, int workItemId, String acronym, String title, boolean reqGroupSubmission,
			String startDate, String dueDate, boolean acceptsLateSubmission, boolean acceptsResubmission,
			InfoDocument infoDoc, AttachmentDocument attachDoc,
			ReportUploadInfo repUpInfo, AttachmentUploadInfo attachUpInfo) throws ParseException {
		
		workItem_classId = classId;
		workItem_id = workItemId;
		workItem_Acronym = acronym;
		workItem_title = title;
		workItem_reqGroupSubmission = reqGroupSubmission;
		workItem_acceptsLateSubmission = acceptsLateSubmission;
		workItem_acceptsResubmission = acceptsResubmission;
		workItem_infoDocument = infoDoc;
		workItem_attachmentDocument = attachDoc;
		workItem_reportUploadInfo = repUpInfo;
		workItem_attachmentUploadInfo = attachUpInfo;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		workItem_startDate = format.parse(startDate);
		workItem_dueDate = format.parse(dueDate);
	}
}