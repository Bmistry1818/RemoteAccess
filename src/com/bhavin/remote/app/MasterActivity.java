package com.bhavin.remote.app;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MasterActivity extends Activity {

	ToggleButton toggleButton = null;
	boolean getStatusOfService;

	public boolean statusOfService() {
		try {
			ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			for (RunningServiceInfo info : activityManager
					.getRunningServices(Integer.MAX_VALUE)) {
				if ("com.bhavin.smsandroidmanager".equals(info.service
						.getClassName())) {
					return true;
				}
			}

		} catch (SecurityException exception) {
			exception.printStackTrace();
		}
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master);
		toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
		getStatusOfService = statusOfService();
		if (getStatusOfService == true) {
			toggleButton.setChecked(true);
		}
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked == true) {
					Toast.makeText(getApplicationContext(),
							"Remote Access Service is Started . . .",
							Toast.LENGTH_LONG).show();
					startService(new Intent(MasterActivity.this,
							RemoteAccessService.class));
				} else {
					Toast.makeText(getApplicationContext(),
							"Remote Access Service is Stoped . . .",
							Toast.LENGTH_LONG).show();

					stopService(new Intent(MasterActivity.this,
							RemoteAccessService.class));
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.master, menu);
		return true;
	}

}
