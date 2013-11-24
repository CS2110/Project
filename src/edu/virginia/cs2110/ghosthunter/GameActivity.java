package edu.virginia.cs2110.ghosthunter;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class GameActivity extends SingleFragmentActivity {
	
	public static final String SCORE = "score";
	public static final String LEVEL = "level";
	
	private GameFragment fragment;
	private boolean doubleBackToExitPressedOnce;
	
	@Override
	protected Fragment createFragment() {
		fragment = new GameFragment();
		return fragment;
	}
	
	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			fragment.cancel();
			finish();
			super.onBackPressed();
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Press the back key again to end the game.",
				Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}
	
}
