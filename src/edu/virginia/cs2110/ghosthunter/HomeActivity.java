package edu.virginia.cs2110.ghosthunter;

import android.support.v4.app.Fragment;

public class HomeActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new HomeFragment();
	}

}
