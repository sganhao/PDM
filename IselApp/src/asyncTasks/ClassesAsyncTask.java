package asyncTasks;

import entities.ClassItem;
import android.database.Cursor;
import android.os.AsyncTask;

public class ClassesAsyncTask extends AsyncTask<Void, Void, ClassItem[]> {

	private Cursor _cursor;

	public ClassesAsyncTask(Cursor c) {
		_cursor = c;
	}
	
	@Override
	protected ClassItem[] doInBackground(Void... params) {
		ClassItem [] classes = null;
		try{
		 classes = new ClassItem[_cursor.getCount()];
		int idx = 0;
		_cursor.moveToFirst();
		do{
		
			classes[idx] = new ClassItem(
					_cursor.getInt(_cursor.getColumnIndex("_classId")), 
					_cursor.getString(_cursor.getColumnIndex("_classFullname")), 
					_cursor.getInt(_cursor.getColumnIndex("_classShowNews")) == 1 ? true : false
							);
			idx++;
		}while (_cursor.moveToNext());
		}catch(NullPointerException e){
			return null;
		}finally{
			_cursor.close();
		}
		return classes;
	}

}
