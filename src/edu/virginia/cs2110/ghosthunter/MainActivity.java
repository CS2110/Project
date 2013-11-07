package edu.virginia.cs2110.ghosthunter;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static final int ANIMATION_DURATION = 1000;
	
	private TextView newGameText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		newGameText = (TextView) findViewById(R.id.new_game);
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/crochet_pattern.ttf");
		newGameText.setTypeface(font, Typeface.BOLD);
		
		newGameText.setOnClickListener(new View.OnClickListener() {
			@TargetApi(11)
			@Override
			public void onClick(View v) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					ObjectAnimator animator = ObjectAnimator.ofFloat(newGameText, "alpha", 1f, 0.8f, 0.6f, 0.4f, 0.2f, 0f);
					animator.setDuration(ANIMATION_DURATION);
					animator.start();
				}
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						Intent intent = new Intent(MainActivity.this, TestActivity.class);
						MainActivity.this.startActivity(intent);
					}
					
				}, ANIMATION_DURATION - 100);
			}
		});
	}
	
	@TargetApi(11)
	@Override
	protected void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ObjectAnimator animator = ObjectAnimator.ofFloat(newGameText, "alpha", 0f, 0.2f, 0.4f, 0.6f, 0.8f, 1f);
			animator.setDuration(ANIMATION_DURATION);
			animator.start();
		}
	}

}
