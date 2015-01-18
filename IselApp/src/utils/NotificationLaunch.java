package utils;

import newsActivities.MainActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.iselapp.R;

public class NotificationLaunch {
	
	private Context _ctx;
	private int _notificationId;

	public NotificationLaunch(Context context){
		_ctx = context;
		_notificationId = 0;
	}

	public void launchNotification(String str) {
		RemoteViews rv = new RemoteViews("com.example.iselapp",R.layout.notification_layout);
		rv.setTextViewText(R.id.notificationTextView, str);

		Notification.Builder builder = new Notification.Builder(_ctx)
		.setContentTitle("Notification")
		.setAutoCancel(true)
		.setSmallIcon(R.drawable.ic_launcher)
		.setOngoing(true)
		.setContent(rv);

		Intent i = new Intent(_ctx,MainActivity.class);
		PendingIntent pintent = PendingIntent.getActivity(_ctx, 1, i, 0);
		builder.setContentIntent(pintent);
		NotificationManager manager = (NotificationManager) _ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(_notificationId, builder.build());
		_notificationId++;
	}
}
