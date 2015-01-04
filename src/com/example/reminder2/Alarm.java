package com.example.reminder2;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("hiwang123.Reminder.Alert")){
			//Toast.makeText(context, "Alarmed", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(context, MainActivity.class);
			i.putExtra("text", intent.getExtras().getString("text"));
			i.putExtra("id", intent.getExtras().getInt("id"));
			i.putExtra("alert", true);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		    context.startActivity(i);  
		}	
	}
}
