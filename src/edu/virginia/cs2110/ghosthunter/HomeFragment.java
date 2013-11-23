package edu.virginia.cs2110.ghosthunter;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	
	public static final int ANIMATION_DURATION = 1000;
	
	private TextView newGameText;
	
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
						HomeFragment.this.startActivity(intent);
					}
					
				}, ANIMATION_DURATION - 200);
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

}
