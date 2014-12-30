package entities;

public class AttachmentDocument {
	
	int attachmentDocument_id;
	String attachmentDocument_filename;
	
	public AttachmentDocument (int attachmentDocument_id, String attachmentDocument_filename) {
		this.attachmentDocument_id = attachmentDocument_id;
		this.attachmentDocument_filename = attachmentDocument_filename;
	}
}