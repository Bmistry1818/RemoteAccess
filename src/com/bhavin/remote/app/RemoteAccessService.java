package com.bhavin.remote.app;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;

public class RemoteAccessService extends Service {
	AudioManager profile = null;
	BluetoothAdapter bluetoothAdapter = null;
	WifiManager wifiManager = null;
	boolean silent = false;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		profile = (AudioManager) getSystemService(AUDIO_SERVICE);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle data = intent.getExtras();
				if (data != null) {
					Object[] pdus = (Object[]) data.get("pdus");
					for (Object pdu : pdus) {
						SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
						if (message.getDisplayMessageBody().equalsIgnoreCase(
								"silent")) {
							profile.setRingerMode(AudioManager.RINGER_MODE_SILENT);
						} else if (message.getDisplayMessageBody()
								.equalsIgnoreCase("vibrate")) {
							profile.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
						} else if (message.getDisplayMessageBody()
								.equalsIgnoreCase("normal")) {
							profile.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						} else if (message.getDisplayMessageBody()
								.equalsIgnoreCase("bluetoothon")) {
							bluetoothAdapter.enable();
							bluetoothAdapter.startDiscovery();
						} else if (message.getDisplayMessageBody()
								.equalsIgnoreCase("bluetoothoff")) {
							bluetoothAdapter.disable();
							bluetoothAdapter.cancelDiscovery();
						} else if (message.getDisplayMessageBody()
								.equalsIgnoreCase("wifion")) {
							wifiManager.setWifiEnabled(true);
						} else if (message.getDisplayMessageBody()
								.equalsIgnoreCase("wifiOff")) {
							wifiManager.setWifiEnabled(false);
						}
					}

				}

			}
		}, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
	}

}
