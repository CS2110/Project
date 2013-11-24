package edu.virginia.cs2110.ghosthunter;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class AsyncGame extends AsyncTask<Void, Object, Void> {
	
	public static final int WAIT = 100;
	public static final int POST_GAME_DELAY = 1000;
	
	private GameStore store;
	private ArrayList<Marker> ghostViews;
	private ArrayList<Marker> boneViews;
	private Context activity;
	private GameListener listener;
	
	private int nearGhosts;
	
	public AsyncGame(GameStore store, ArrayList<Marker> ghostViews, ArrayList<Marker> boneViews, Context activity, GameListener listener) {
		this.store = store;
		this.ghostViews = ghostViews;
		this.boneViews = boneViews;
		this.activity = activity;
		this.listener = listener;
	}
	
	@Override
	protected void onPreExecute() {
		nearGhosts = 0; 
	}
	
	@Override
	protected Void doInBackground(Void... args) {
		while(!isCancelled()) {
			// Proximity alerts
			int near = store.checkProximity();
			if (near != nearGhosts) {
				nearGhosts = near;
				if (near > 0) {
					publishProgress(near);
				}
			}
			
			// Check for collision
			int g = store.hunterAttacked();
			if (g >= 0) {
				publishProgress(GameListener.GHOST, g);
				publishProgress(GameListener.HUNTER, 0);
				if (store.hunterKilled()) {
					break;
				}
			}
			
			// Moving ghosts
			ArrayList<LatLng> moveGhosts = store.moveGhosts();
			
			// Refresh screen
			try {
				Thread.sleep(WAIT);
			} catch(InterruptedException ex) {}
			publishProgress(moveGhosts);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onProgressUpdate(Object... updates) {
		Object o = updates[0];
		if (o instanceof ArrayList<?>) {
			ArrayList<LatLng> positions = (ArrayList<LatLng>) o;
			for (int i = 0; i < positions.size(); i++) {
				ghostViews.get(i).setPosition(positions.get(i));
			}
		} else if (o instanceof Integer) {
			Integer i = (Integer) o;
			String text = "";
			if (i == 1) {
				text = " ghost is near!";
			} else {
				text = " ghosts are near!";
			}
			if (activity != null)
				Toast.makeText(activity, o + text, Toast.LENGTH_SHORT).show();
		} else if (o instanceof String) {
			String id = (String) o;
			Integer index = (Integer) updates[1];
			listener.removeGamePiece(id, index);
		}
	}
	
	@Override
	protected void onPostExecute(Void param) {
		Toast.makeText(activity, "Game Over!", Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				store.gameOver();
				listener.gameOver();
			}
		}, POST_GAME_DELAY);
	}
	
	@Override
	protected void onCancelled() {
		Toast.makeText(activity, "Game Over!", Toast.LENGTH_SHORT).show();
		store.gameOver();
	}
	
}
