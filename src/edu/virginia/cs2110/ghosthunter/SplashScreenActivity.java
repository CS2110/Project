package edu.virginia.cs2110.ghosthunter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class SplashScreenActivity extends Activity {
	
	public static final int DURATION = 2000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		TextView splashText = (TextView) findViewById(R.id.splash_text);
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/crochet_pattern.ttf");
		splashText.setTypeface(font, Typeface.BOLD);
		
		// Execute AsyncLoad
		new AsyncLoad().execute();
	}
	
	private class AsyncLoad extends AsyncTask<Void, Void, Void> {
	    
		private Intent intent;
		
		@Override
		protected void onPreExecute() {
			intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Necessary loading occurs here
			
			try {
				Thread.sleep(DURATION);
			} catch (InterruptedException ex) {
				// Do nothing
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void params) {
			// Dismiss dialog and launch MainActivity
			startActivity(intent);
		}

	}
	
}
