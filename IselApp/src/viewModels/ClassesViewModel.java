package viewModels;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.iselapp.R;

public class ClassesViewModel {
	
	public final TextView class_fullName;
	public final CheckBox class_selectionBox;
	public final Button class_btnParticipants;
	
	public ClassesViewModel(View view){
		class_fullName = (TextView) view.findViewById(R.id.settings_item_fullname);
		class_selectionBox = (CheckBox) view.findViewById(R.id.settings_item_checkBox);
		class_btnParticipants = (Button) view.findViewById(R.id.settings_item_buttonParticipants);
	}
}
