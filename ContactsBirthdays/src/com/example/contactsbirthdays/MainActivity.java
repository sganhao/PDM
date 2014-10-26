package com.example.contactsbirthdays;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ListView;


public class MainActivity extends Activity {

	private Uri _contactUri;
	private String birthday;
	private int year;
	private int month;
	private int day;

	private ListView _listView;	
	private ContactsCustomAdapter adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_listView = (ListView) findViewById(R.id.listView1);		

		SharedPreferences prefs = getSharedPreferences("dateSelected",0);

		ContactsAsyncTask cAsync = new ContactsAsyncTask(prefs) {

			@Override
			protected void onPostExecute(ContactInfo[] result) {
				if(result != null){

					adapter = new ContactsCustomAdapter(
							MainActivity.this, 
							R.layout.item_layout,
							result
							);

					_listView.setAdapter(adapter);
					_listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View v,
								int arg2, long arg3) {
							ViewModel view = (ViewModel) v.getTag();
							Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(People.CONTENT_URI + "/" + view.contactName.getTag().toString()));
							startActivity(i);
						}
					});
				}
			}
		};

		cAsync.execute(getContentResolver());
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
			i.putExtra("finishActivityOnSaveCompleted", true);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data){
		final Intent dat = data;

		if(reqCode == 0 && resCode == RESULT_OK){

			final Calendar ca = Calendar.getInstance();
			year = ca.get(Calendar.YEAR);
			month = ca.get(Calendar.MONTH);
			day = ca.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(
					this, 
					new DatePickerDialog.OnDateSetListener() {

						// when dialog box is closed, below method will be called.
						public void onDateSet(DatePicker view, int selectedYear,
								int selectedMonth, int selectedDay) {

							year = selectedYear;
							month = selectedMonth;
							day = selectedDay;
							birthday = "" + day + "/" + month + "/" + year;							
							_contactUri = dat.getData();

							Cursor c = getContentResolver().query(_contactUri, null, null, null, null);
							c.moveToFirst();
							int rawContactID = c.getInt(c.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));

							ContentValues values = new ContentValues();
							values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE);
							values.put(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY);
							values.put(ContactsContract.CommonDataKinds.Event.RAW_CONTACT_ID,rawContactID);
							values.put(ContactsContract.CommonDataKinds.Event.START_DATE, birthday);

							c = getContentResolver().query(
									ContactsContract.Data.CONTENT_URI,
									null,
									"RAW_CONTACT_ID = ? AND " + ContactsContract.CommonDataKinds.Event.TYPE + " = ?",
									new String[] { "" + rawContactID, ""+ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY},
									null);

							if(c.getCount() == 0){
								getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
								c.close();
							}else{
								getContentResolver().update(
										ContactsContract.Data.CONTENT_URI, 
										values,
										"RAW_CONTACT_ID = ? AND MIMETYPE = ?",
										new String[] { "" + rawContactID, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE}
										);
								c.close();
							}
						}
					}, 
					year, 
					month, 
					day).show();
		}
	}
}
