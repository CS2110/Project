package edu.virginia.cs2110.ghosthunter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class AsyncWait extends AsyncTask<Integer, Void, Void> {
    
	private Intent intent;
	private Context activity;
	
	public AsyncWait(Context activity, Intent intent) {
		this.activity = activity;
		this.intent = intent;
	}

	@Override
	protected Void doInBackground(Integer... duration) {
		int wait = 1000;
		if (duration.length > 0) {
			wait = duration[0];
		}
		try {
			Thread.sleep(wait);
		} catch (InterruptedException ex) {
			// Do nothing
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void params) {
		// Dismiss dialog and launch MainActivity
		activity.startActivity(intent);
	}

}