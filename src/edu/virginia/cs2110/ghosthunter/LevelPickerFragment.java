package edu.virginia.cs2110.ghosthunter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LevelPickerFragment extends DialogFragment {
	
	public static final String EXTRA_LEVEL = "edu.virginia.cs2110.ghosthunter.level";
	
	private int level;
	
	private RadioButton easyButton;
	private RadioButton mediumButton;
	private RadioButton hardButton;
	
	public static LevelPickerFragment newInstance(int level) {
		LevelPickerFragment fragment = new LevelPickerFragment();
		fragment.setLevel(level);
		
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {		
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_level, null);
		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/crochet_pattern.ttf");
		
		easyButton = (RadioButton) v.findViewById(R.id.easy_level);
		easyButton.setTypeface(font, Typeface.BOLD);
		mediumButton = (RadioButton) v.findViewById(R.id.medium_level);
		mediumButton.setTypeface(font, Typeface.BOLD);
		hardButton = (RadioButton) v.findViewById(R.id.hard_level);
		hardButton.setTypeface(font, Typeface.BOLD);
		
		switch(level) {
		case HomeFragment.DIFFICULTY_MEDIUM:
			mediumButton.setChecked(true);
			break;
		case HomeFragment.DIFFICULTY_HARD:
			hardButton.setChecked(true);
			break;
		default:
			easyButton.setChecked(true);
		}
		
		RadioGroup levelPicker = (RadioGroup) v.findViewById(R.id.dialog_level_picker);
		levelPicker.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (easyButton.isChecked()) {
					level = HomeFragment.DIFFICULTY_EASY;
				} else if (mediumButton.isChecked()) {
					level = HomeFragment.DIFFICULTY_MEDIUM;
				} else if (hardButton.isChecked()) {
					level = HomeFragment.DIFFICULTY_HARD;
				}
			}
		});
		
		return new AlertDialog.Builder(getActivity())
		.setView(v)
		.setTitle(R.string.level_picker_title)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_OK);
			}
		})
		.create();
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	private void sendResult(int resultCode) {
		if (getTargetFragment() == null) {
			return;
		}
		
		Intent i = new Intent();
		i.putExtra(EXTRA_LEVEL, level);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
}
