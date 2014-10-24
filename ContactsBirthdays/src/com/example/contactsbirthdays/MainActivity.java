package com.example.contactsbirthdays;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

	private Uri _contactUri;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
            return true;
        }else if(id == R.id.display_contacts){
        	Intent i = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        	i.setType(Phone.CONTENT_TYPE);
			startActivityForResult(i, 0);
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
	protected void onActivityResult(int reqCode, int resCode, Intent data){
		if(reqCode == 0 && resCode == RESULT_OK){
			_contactUri = data.getData();
			Builder builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.MIMETYPE,0)
					.withValue(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/birthday")
					.withValue("birthday","11/12/1990");
			builder.build();
			Intent i = new Intent(Intent.ACTION_EDIT);
			Uri contactUri = Contacts.getLookupUri(getContentResolver(), data.getData());
			i.setDataAndType(contactUri, Contacts.CONTENT_ITEM_TYPE);
			i.putExtra("finishActivityOnSaveCompleted", true);
			startActivity(i);
		}
    }
    
}
