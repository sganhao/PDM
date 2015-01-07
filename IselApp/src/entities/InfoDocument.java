package entities;

import java.io.Serializable;

public class InfoDocument implements Serializable {

	public int infoDocument_id;
	public String infoDocument_filename;

	public InfoDocument (int infoDocument_id, String infoDocument_filename) {
		this.infoDocument_id = infoDocument_id;
		this.infoDocument_filename = infoDocument_filename;
	}
}
