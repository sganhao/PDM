package workItemsActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.example.iselapp.R;

public class WorkItemLinkActivity extends Activity {

	private String TAG = "IselApp";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		Log.d(TAG , "WorkItemLinkActivity -> onCreate....");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workitem_link_layout);
		
		Intent i = getIntent();
		String workItem_link = i.getExtras().getString("workItem_link");
		WebView webView = (WebView) findViewById(R.id.workitem_item_linkToSelf);
		webView.loadUrl(workItem_link);
	}
}
