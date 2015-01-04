package fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.example.reminder2.Alarm;
import com.example.reminder2.MainActivity;
import com.example.reminder2.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class Section1Fragment extends Fragment {
	public static final String TAG = "content";
	private View view;
	private String content;
	private TimePicker mTimePicker;
	private Button mButton;
	private EditText mEditText;
	private Calendar c = Calendar.getInstance();
	private ArrayList<Integer> intentIdArray = new ArrayList<Integer>();
	
	OnAddClockCallback mCallback; 
	
	public interface OnAddClockCallback { 
        public void addClockCallback(int sender_id, long startTime, String text); 
    } 
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {   
            mCallback = (OnAddClockCallback) activity;   
        } catch (ClassCastException e) {   
            throw new ClassCastException(activity.toString()   
                    + " must implement OnAddClockCallback");   
        }  
	}

	public static Section1Fragment newInstance(String content) {
		Section1Fragment fragment = new Section1Fragment();
		Bundle args = new Bundle();
		args.putString(TAG, content);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle.getIntegerArrayList("pendingintents") != null)
			  intentIdArray = (ArrayList<Integer>) bundle.getIntegerArrayList("pendingintents");   //傳MainActivity之intentIdArray之指標
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.section1, container, false);
		init();
		return view;
	}

	private void init() {
		mEditText = (EditText) view.findViewById(R.id.editText2);
		LinearLayout touch = (LinearLayout)view.findViewById(R.id.section1);
		touch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mEditText.clearFocus();
				InputMethodManager imm = (InputMethodManager)getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
			}
		});
		
		mTimePicker = (TimePicker) view.findViewById(R.id.timePicker1);
		mTimePicker.setIs24HourView(true);
		mTimePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
		mTimePicker.setCurrentMinute(c.get(Calendar.MINUTE));
		mTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				c.set(Calendar.HOUR_OF_DAY, hourOfDay);
				c.set(Calendar.MINUTE, minute);
				c.set(Calendar.SECOND, 0);
			}
		});
		
		mButton = (Button) view.findViewById(R.id.button1);
		mButton.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c_clone=(Calendar) c.clone();
				final int _id = (int) c_clone.get(Calendar.HOUR_OF_DAY)*100 + c_clone.get(Calendar.MINUTE);
				final String text = mEditText.getText().toString();
				if(intentIdArray.contains(_id)){
			    	Toast.makeText(getActivity(), String.format("%02d:%02d", _id/100, _id%100) + " Existed", Toast.LENGTH_LONG).show(); 
			    	return ;
			    }
			    if(c_clone.getTimeInMillis() < System.currentTimeMillis()){
			    	c_clone.add(Calendar.DATE, 1);
			    }
			    long startTime = c_clone.getTimeInMillis();  
			    Toast.makeText(getActivity(), "Add " + String.format("%02d:%02d", _id/100, _id%100), Toast.LENGTH_LONG).show(); 
			    mCallback.addClockCallback(_id, startTime, text);
			}
		});
	}
}

