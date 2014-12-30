package entities;

public class AttachmentUploadInfo {
	
	boolean attachmentUploadInfo_isRequired;
	int attachmentUploadInfo_maxFileSizeInMB;
	String attachmentUploadInfo_acceptedExtensions;
	
	public AttachmentUploadInfo (boolean attachmentUploadInfo_isRequired, int attachmentUploadInfo_maxFileSizeInMB, String attachmentUploadInfo_acceptedExtensions) {
		this.attachmentUploadInfo_isRequired = attachmentUploadInfo_isRequired;
		this.attachmentUploadInfo_maxFileSizeInMB = attachmentUploadInfo_maxFileSizeInMB;
		this.attachmentUploadInfo_acceptedExtensions = attachmentUploadInfo_acceptedExtensions;
	}
}