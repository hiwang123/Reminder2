package fragment;

import com.example.reminder2.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Section3Fragment extends Fragment {
	public static final String TAG = "content";
	private View view;
	private TextView textView;
	private String content;

	public static Section3Fragment newInstance(String content) {
		Section3Fragment fragment = new Section3Fragment();
		Bundle args = new Bundle();
		args.putString(TAG, content);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		content = bundle != null ? bundle.getString(TAG) : "";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.section3, container, false);
		init();
		return view;
	}

	private void init() {
		
	}
}

