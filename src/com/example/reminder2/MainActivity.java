package com.example.reminder2;

import java.util.ArrayList;
import java.util.List;

import fragment.Section1Fragment;
import fragment.Section2Fragment;
import fragment.Section3Fragment;

import android.support.v4.app.Fragment;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity 
    implements Section1Fragment.OnAddClockCallback, Section2Fragment.OnRemoveClockCallback{

	private Button mDrawerButton;
	private TextView title;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	private String[] menus;
	private List<Fragment> fragments;
	private ArrayList<Integer> intentIdArray = new ArrayList<Integer>();
	static final String INTENT_ID_ARRAY = "intentIdArray";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean boolean1 = false;
        if (getIntent().getExtras() != null)
        	boolean1 = getIntent().getExtras().getBoolean("alert");
        if( boolean1 ){
        	String text = getIntent().getExtras().getString("text");
    		int _id = getIntent().getExtras().getInt("id");
        	Alert(text, _id);
        }
        else{
        	if(savedInstanceState != null){
            	intentIdArray = savedInstanceState.getIntegerArrayList(INTENT_ID_ARRAY);
            }
            SharedPreferences prefs = getSharedPreferences(INTENT_ID_ARRAY, Context.MODE_PRIVATE);
            Integer size = prefs.getInt(INTENT_ID_ARRAY+"_SIZE", 0);
            for (Integer i = 0; i < size; i++){
            	intentIdArray.add(prefs.getInt(INTENT_ID_ARRAY+i, 0));
            }
            setContentView(R.layout.activity_main);
            init();
            setAdapter();
            setListener();
            if (savedInstanceState == null) {
    			selectItem(0);
    		}
        }   
    }

	private void init() {
		mDrawerButton = (Button) findViewById(R.id.left);
		title = (TextView) findViewById(R.id.title);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		menus = new String[] {
				"Set Reminder", "Reminder List", //"Records"
		};
		fragments = new ArrayList<Fragment>();
		fragments.add(Section1Fragment.newInstance("Set Reminder"));
		fragments.add(Section2Fragment.newInstance("Reminder List"));
		//fragments.add(Section3Fragment.newInstance("Records"));
	}

	private void setAdapter() {
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, menus));
	}

	private void setListener() {
		mDrawerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
					mDrawerLayout.closeDrawer(mDrawerList);
				} else {
					mDrawerLayout.openDrawer(mDrawerList);
				}
			}
		});
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View v, int position,
					long id) {
				selectItem(position);
			}
		});
	}

	private void selectItem(int position) {
		Fragment fragment = fragments.get(position);
		FragmentManager manager = getSupportFragmentManager();
		Bundle args = new Bundle();
		args.putIntegerArrayList("pendingintents", intentIdArray);
		if(position==1 || position==0)
			fragment.setArguments(args);
		manager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		title.setText(menus[position]);
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void addClockCallback(int _id, long startTime, String text) {
		// TODO Auto-generated method stub
		intentIdArray.add(_id);
		addAlert(_id, startTime, text);
		addAlert(_id+10000, startTime + 600000L, text);
	}
	
	@Override
	public void removeClockCallback(int _id) {
		// TODO Auto-generated method stub
		removeAlert(_id);
		removeAlert(_id+10000);
	}

/*	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putIntegerArrayList(INTENT_ID_ARRAY, intentIdArray);
	}  */

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences(INTENT_ID_ARRAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(INTENT_ID_ARRAY + "_SIZE", intentIdArray.size());	
		for ( Integer i = 0; i < intentIdArray.size(); i++){
			editor.putInt(INTENT_ID_ARRAY + i, intentIdArray.get(i));	
		}
		editor.commit();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences(INTENT_ID_ARRAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(INTENT_ID_ARRAY + "_SIZE", intentIdArray.size());	
		for ( Integer i = 0; i < intentIdArray.size(); i++){
			editor.putInt(INTENT_ID_ARRAY + i, intentIdArray.get(i));	
		}
		editor.commit();
		super.onDestroy();
	}
	
	public void Alert(String text, int id){
		final int _id = id;
		final int _id2 = id+10000;
		Vibrator myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		myVibrator.vibrate(1000);
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder
			.setTitle("Hi!")
			.setMessage("Time to take medicine\n" +
					    "Drug Name:"+text+"\n" +
					    "If not take medicine, you might...\n")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					removeAlert(_id2);
					dialog.cancel();
					MainActivity.this.finish();
				}
			});
		if( _id<10000 ){
			builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
					MainActivity.this.finish();
				}
			});
		}
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void removeAlert(int id){
		Intent intent = new Intent(MainActivity.this, Alarm.class).setAction("hiwang123.Reminder.Alert");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
	    am.cancel(sender);
		sender.cancel();
	}

	public void addAlert(int _id, long startTime, String text){
		Intent intent = new Intent(MainActivity.this, Alarm.class).setAction("hiwang123.Reminder.Alert");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("id", _id);
		intent.putExtra("text", text);
		PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, _id, intent, 0);
	    AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
	    am.setRepeating( AlarmManager.RTC_WAKEUP, startTime,
	                     AlarmManager.INTERVAL_DAY, sender);
	}
}

