package fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.example.reminder2.Alarm;
import com.example.reminder2.R;

import fragment.Section1Fragment.OnAddClockCallback;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Section2Fragment extends Fragment {
	public static final String TAG = "content";
	private View view;
	private String content;
	private ListView mListView;
	private ArrayList<Integer> intentIdArray = new ArrayList<Integer>();
	private BaseAdapter adapter;

	OnRemoveClockCallback mCallback; 
	
	public interface OnRemoveClockCallback { 
        public void removeClockCallback(int _id); 
    } 
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {   
            mCallback = (OnRemoveClockCallback) activity;   
        } catch (ClassCastException e) {   
            throw new ClassCastException(activity.toString()   
                    + " must implement OnAddClockCallback");   
        } 
	}

	public static Section2Fragment newInstance(String content) {
		Section2Fragment fragment = new Section2Fragment();
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
		view = inflater.inflate(R.layout.section2, container, false);
		init(); 
		return view;
	}

	private void init() {
		mListView = (ListView) view.findViewById(R.id.listView1);
		adapter = new BaseAdapter() {
			
			TextView mTime;
			Button mCancelButton;
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				final int pos = position;
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list, null);
				mTime = (TextView) convertView.findViewById(R.id.textView1);
				mCancelButton = (Button) convertView.findViewById(R.id.button1);
				
				int _id = intentIdArray.get(pos);
				mTime.setText(String.format("%02d:%02d", _id/100, _id%100));
				mCancelButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int _id = intentIdArray.get(pos);
						Toast.makeText(getActivity(), "Remove " + String.format("%02d:%02d", _id/100, _id%100) , Toast.LENGTH_SHORT).show();
						intentIdArray.remove(pos);
						mCallback.removeClockCallback(_id);
						init();
					}
				});
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return intentIdArray.size();
			}
		};
		mListView.setAdapter(adapter);
	}
}

