package viewModels;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iselapp.R;

public class ParticipantViewModel {

	public TextView participant_fullname;
	public ImageView participant_image;

	public ParticipantViewModel(View view) {
		participant_fullname = (TextView) view.findViewById(R.id.participant_list_fullname);
		participant_image = (ImageView) view.findViewById(R.id.participant_list_avatar);
	}
}
