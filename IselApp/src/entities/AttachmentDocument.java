package entities;

import java.io.Serializable;

public class AttachmentDocument implements Serializable {
	
	public int attachmentDocument_id;
	public String attachmentDocument_filename;
	
	public AttachmentDocument (int attachmentDocument_id, String attachmentDocument_filename) {
		this.attachmentDocument_id = attachmentDocument_id;
		this.attachmentDocument_filename = attachmentDocument_filename;
	}
}