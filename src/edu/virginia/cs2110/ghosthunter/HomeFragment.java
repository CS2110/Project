package edu.virginia.cs2110.ghosthunter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	
	public static final int ANIMATION_DURATION = 1000;
	public static final int DIFFICULTY_EASY = 1;
	public static final int DIFFICULTY_MEDIUM = 2;
	public static final int DIFFICULTY_HARD = 3;
	public static final int DEFAULT_SCORE = 0;
	
	private TextView newGameText;
	private TextView highScoreLabel;
	private TextView highScoreValue;
	
	private int difficultyLevel;
	private int highScore;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getActivity().getIntent();
		difficultyLevel = intent.getIntExtra(SplashScreenActivity.JSON_LEVEL, DIFFICULTY_EASY);
		highScore = intent.getIntExtra(SplashScreenActivity.JSON_SCORE, DEFAULT_SCORE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_home, parent, false);
		
		newGameText = (TextView) v.findViewById(R.id.new_game);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/crochet_pattern.ttf");
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
						Intent intent = new Intent(getActivity(), GameActivity.class);
						intent.putExtra(GameActivity.LEVEL, difficultyLevel);
						HomeFragment.this.startActivityForResult(intent, 0);
					}
					
				}, ANIMATION_DURATION - 200);
			}
		});
		
		highScoreLabel = (TextView) v.findViewById(R.id.high_score_label);
		highScoreLabel.setTypeface(font, Typeface.BOLD);
		
		highScoreValue = (TextView) v.findViewById(R.id.high_score);
		highScoreValue.setText("" + highScore);
		
		return v;
	}
	
	@TargetApi(11)
	@Override
	public void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ObjectAnimator animator = ObjectAnimator.ofFloat(newGameText, "alpha", 0f, 0.2f, 0.4f, 0.6f, 0.8f, 1f);
			animator.setDuration(ANIMATION_DURATION);
			animator.start();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		int score = data.getIntExtra(GameActivity.SCORE, highScore);
		if (score > highScore) {
			highScore = score;
			highScoreValue.setText("" + highScore);
			try {
				saveGameData();
			} catch (Exception ex) {
				Log.d("Saving Error", ex.getMessage());
			}
		}
	}
	
	private void saveGameData() throws IOException, JSONException {
		// Create JSONObject
		JSONObject json = new JSONObject();
		json.put(SplashScreenActivity.JSON_LEVEL, difficultyLevel);
		json.put(SplashScreenActivity.JSON_SCORE, highScore);
		
		// Write file to disk
		Writer writer = null;
		try {
			OutputStream out = getActivity().getApplicationContext().openFileOutput(SplashScreenActivity.FILE_NAME, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(json.toString());		
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}
