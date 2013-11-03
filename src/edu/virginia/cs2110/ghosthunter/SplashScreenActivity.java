package edu.virginia.cs2110.ghosthunter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		TextView splashText = (TextView) findViewById(R.id.splash_text);
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/crochet_pattern.ttf");
		splashText.setTypeface(font, Typeface.BOLD);
		
		// Execute AsyncLoad
		new AsyncLoad().execute();
		
		/*
		new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent that will start the Menu-Activity.
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 3000);	
        */	
	}
	
	private class AsyncLoad extends AsyncTask<Void, Void, Void> {
	    
		Intent intent;
		
		@Override
	    protected void onPreExecute() {
			// Initial set-up
			intent = new Intent(SplashScreenActivity.this, MainActivity.class);
		}

		@Override
		protected Void doInBackground(Void... voids) {
			// Load necessary data into intent
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ex) {
				
			}
			return null;
		}
  
		@Override
		protected void onPostExecute(Void params) {
			// Dismiss dialog and launch MainActivity
			startActivity(intent);

			// Close this activity
			finish();
		}

	}

	
}
