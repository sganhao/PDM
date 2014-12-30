package entities;

import java.io.Serializable;
import java.util.Date;

public class WorkItem implements Serializable{
	
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
}