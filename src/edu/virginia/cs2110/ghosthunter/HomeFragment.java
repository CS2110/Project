package edu.virginia.cs2110.ghosthunter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	
	public static final int ANIMATION_DURATION = 1000;
	public static final int DIFFICULTY_EASY = 1;
	public static final int DIFFICULTY_MEDIUM = 2;
	public static final int DIFFICULTY_HARD = 3;
	public static final int DEFAULT_SCORE = 0;
	
	public static final String FILE_NAME = "ghost_hunter.json";
	public static final String JSON_LEVEL = "level";
	public static final String JSON_SCORE = "score";
	private static final String DIALOG_LEVEL = "level";
	private static final int REQUEST_LEVEL = 0;
	private static final int REQUEST_SCORE = 1;
	
	private TextView newGameText;
	private TextView highScoreLabel;
	private TextView highScoreValue;
	private ImageButton levelPickerButton;
	
	private int difficultyLevel;
	private int highScore;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			loadGameData();
		} catch (Exception ex) {
			Log.d("Loading Error", ex.getMessage());
			difficultyLevel = DIFFICULTY_EASY;
			highScore = DEFAULT_SCORE;
		}
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
						HomeFragment.this.startActivityForResult(intent, REQUEST_SCORE);
					}
					
				}, ANIMATION_DURATION - 200);
			}
		});
		
		highScoreLabel = (TextView) v.findViewById(R.id.high_score_label);
		highScoreLabel.setTypeface(font, Typeface.BOLD);
		
		highScoreValue = (TextView) v.findViewById(R.id.high_score);
		highScoreValue.setText("" + highScore);
		
		levelPickerButton = (ImageButton) v.findViewById(R.id.level_difficulty);
		levelPickerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				LevelPickerFragment dialog = LevelPickerFragment.newInstance(difficultyLevel);
				dialog.setTargetFragment(HomeFragment.this, REQUEST_LEVEL);
				dialog.show(fm, DIALOG_LEVEL);
			}
		});
		
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
		if (data == null) return;
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_SCORE) {	
			int score = data.getIntExtra(GameActivity.SCORE, highScore);
			if (score > highScore) {
				highScore = score;
				highScoreValue.setText("" + highScore);
			}
		} else if (requestCode == REQUEST_LEVEL) {
			difficultyLevel = data.getIntExtra(LevelPickerFragment.EXTRA_LEVEL, difficultyLevel);
		}
		try {
			saveGameData();
		} catch (Exception ex) {
			Log.d("Saving Error", ex.getMessage());
		}
	}
	
	private void saveGameData() throws IOException, JSONException {
		// Create JSONObject
		JSONObject json = new JSONObject();
		json.put(JSON_LEVEL, difficultyLevel);
		json.put(JSON_SCORE, highScore);
		
		// Write file to disk
		Writer writer = null;
		try {
			OutputStream out = getActivity().getApplicationContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(json.toString());		
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	private void loadGameData() throws IOException, JSONException {
		BufferedReader reader = null;
		try {
			// Open and read file into a StringBuilder
			InputStream in = getActivity().getApplicationContext().openFileInput(FILE_NAME);
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
			difficultyLevel = json.getInt(JSON_LEVEL);
			highScore = json.getInt(JSON_SCORE);
		} catch (FileNotFoundException ex) {
			// Occurs if first time user
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

}
