package edu.virginia.cs2110.ghosthunter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
				startActivity(intent);
			}
		}, DURATION);
	}
}
