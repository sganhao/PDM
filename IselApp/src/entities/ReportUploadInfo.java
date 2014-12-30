package entities;

public class ReportUploadInfo {
	
	boolean reportUploadInfo_isRequired;
	int reportUploadInfo_maxFileSizeInMB;
	String reportUploadInfo_acceptedExtensions;
	
	public ReportUploadInfo (boolean reportUploadInfo_isRequired, int reportUploadInfo_maxFileSizeInMB, String reportUploadInfo_acceptedExtensions) {
		this.reportUploadInfo_isRequired = reportUploadInfo_isRequired;
		this.reportUploadInfo_maxFileSizeInMB = reportUploadInfo_maxFileSizeInMB;
		this.reportUploadInfo_acceptedExtensions = reportUploadInfo_acceptedExtensions;
	}
}