package handlers;

import java.io.Serializable;

import android.os.HandlerThread;

public class ImageHandlerThread extends HandlerThread implements Serializable{

	public ImageHandlerThread() {
		super("ImageHandlerThread");
	}
}
