package customAdapters;

import java.util.LinkedHashSet;
import java.util.Set;

import participantsActivities.ParticipantsActivity;
import viewModels.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.Toast;

import com.example.iselapp.R;

import entities.ClassItem;

public class ClassesCursorAdapter extends CursorAdapter implements OnScrollListener{

	private int _count;
	private int _scrollFirst;
	private int _scrollCount;
	private boolean _updating;
	private LayoutInflater _layoutInflater;
	private Set<Integer> _classesIdsToAdd;
	private Set<Integer> _classesIdsToRemove;
	private Context _context;
	
	
	public ClassesCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		_layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_count = 30 ;
		_classesIdsToAdd = new LinkedHashSet<Integer>();
		_classesIdsToRemove = new LinkedHashSet<Integer>();
		_context = context;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewModel viewModel = (ViewModel) view.getTag();
		ClassItem classItem = getClassItem(cursor);
		viewModel.class_fullName.setText(classItem.getFullname());
		viewModel.class_selectionBox.setChecked(classItem.getShowNews());
		
		viewModel.class_selectionBox.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v ;
				ClassItem c = (ClassItem) cb.getTag();

				c.setShowNews(cb.isChecked());

				if(c.getShowNews()){
					if(_classesIdsToRemove.contains(c.getId()))
						_classesIdsToRemove.remove(c.getId());
					else
						_classesIdsToAdd.add(c.getId());
				}else{
					if(_classesIdsToAdd.contains(c.getId()))
						_classesIdsToAdd.remove(c.getId());
					else
						_classesIdsToRemove.add(c.getId());
				}
			}  
		});   

		viewModel.class_btnParticipants.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if(ni.isConnected()){
					Button btn = (Button) v;
					ClassItem c = (ClassItem) btn.getTag();
					Intent i = new Intent(_context, ParticipantsActivity.class);
					i.putExtra("classId", c.getId());
					_context.startActivity(i);
				}else{
					Toast.makeText(_context, "WIFI CONNECTION REQUIRED", Toast.LENGTH_SHORT).show();
				}
			}  
		});
		viewModel.class_selectionBox.setTag(classItem);
		viewModel.class_btnParticipants.setTag(classItem);
	}

	private ClassItem getClassItem(Cursor cursor) {
		return new ClassItem(
				cursor.getInt(cursor.getColumnIndex("_id")), 
				cursor.getString(cursor.getColumnIndex("_classFullname")), 
				cursor.getInt(cursor.getColumnIndex("_classShowNews")) == 1 ? true : false
						);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = _layoutInflater.inflate(R.layout.settings_item_layout, parent, false);
		view.setTag(new ViewModel(view));
		return view;
	}
	
	@Override
	public void onScroll(AbsListView list, int first, int count, int total) {
		_scrollFirst = first;
		_scrollCount = count;		
	}

	@Override
	public void onScrollStateChanged(AbsListView list, int state) {
		if(state != 0) return;		
		if(_scrollFirst+_scrollCount >= _count - 2){			
			if(_updating) return;
			_updating = true;

			new AsyncTask<Void,Void,Void>(){

				@Override
				protected Void doInBackground(Void... arg0) {

					return null;
				}

				@Override
				protected void onPostExecute(Void arg){
					_count += 15;
					ClassesCursorAdapter.this.notifyDataSetChanged();
					_updating = false;
				}
			}.execute();
		}			
	}

	
	public Set<Integer> getClassesIdsToAdd() {
		return _classesIdsToAdd;
	}
	public Set<Integer> getClassesIdsToRemove() {
		return _classesIdsToRemove;
	}
}
