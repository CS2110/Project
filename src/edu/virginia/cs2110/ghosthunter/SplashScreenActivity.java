package edu.virginia.cs2110.ghosthunter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SplashScreenActivity extends Activity {
	
	public static final int DURATION = 2000;
	
	public static final String FILE_NAME = "ghost_hunter.json";
	public static final String JSON_LEVEL = "level";
	public static final String JSON_SCORE = "score";
	
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
				loadGameData();
			} catch (Exception ex) {
				Log.d("Loading Error", ex.getMessage());
			}
			
			try {
				Thread.sleep(DURATION);
			} catch (InterruptedException ex) {
				// Do nothing
			}
			return null;
		}
		
		private void loadGameData() throws IOException, JSONException {
			BufferedReader reader = null;
			try {
				// Open and read file into a StringBuilder
				InputStream in = SplashScreenActivity.this.getApplicationContext().openFileInput(FILE_NAME);
				reader = new BufferedReader(new InputStreamReader(in));
				StringBuilder jsonString = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					// Line breaks are omitted and irrelevant
					jsonString.append(line);
				}
				// Parse JSON
				JSONObject json = new JSONObject(jsonString.toString());
				// Bundle info into intent
				int level = json.getInt(JSON_LEVEL);
				int score = json.getInt(JSON_SCORE);
				intent.putExtra(JSON_LEVEL, level);
				intent.putExtra(JSON_SCORE, score);
			} catch (FileNotFoundException ex) {
				// Occurs if first time user
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		}

		@Override
		protected void onPostExecute(Void params) {
			// Dismiss dialog and launch MainActivity
			startActivity(intent);
		}

	}
	
}
