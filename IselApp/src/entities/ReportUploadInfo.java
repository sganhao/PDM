package entities;

import java.io.Serializable;

public class ReportUploadInfo implements Serializable {
	
	public boolean reportUploadInfo_isRequired;
	public int reportUploadInfo_maxFileSizeInMB;
	public String reportUploadInfo_acceptedExtensions;
	
	public ReportUploadInfo (boolean reportUploadInfo_isRequired, int reportUploadInfo_maxFileSizeInMB, String reportUploadInfo_acceptedExtensions) {
		this.reportUploadInfo_isRequired = reportUploadInfo_isRequired;
		this.reportUploadInfo_maxFileSizeInMB = reportUploadInfo_maxFileSizeInMB;
		this.reportUploadInfo_acceptedExtensions = reportUploadInfo_acceptedExtensions;
	}
}