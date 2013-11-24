package edu.virginia.cs2110.ghosthunter;

import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GameFragment extends Fragment implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, GameListener {

	public static final int ZOOM_FACTOR = 18;
	public static final int UPDATE_INTERVAL = 1000;
	public static final int FASTEST_INTERVAL = 500;
	public static final int ANIMATION_DURATION = 1000;

	private LocationClient locationClient;
	private LocationRequest locationRequest;
	private GoogleMap map;
	private Location hunterLoc;
	private LatLng mapCenter;
	private Marker hunterView;
	private ArrayList<Marker> ghostViews;
	private ArrayList<Marker> boneViews;
	private ProgressBar hunterHealth;
	private TextView hunterScore;
	
	private GameStore store;
	private AsyncGame game;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		store = GameStore.get(getActivity());
		locationClient = new LocationClient(getActivity(), this, this);

		// Create the LocationRequest object
		locationRequest = LocationRequest.create();
		// Use high accuracy
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		locationRequest.setInterval(UPDATE_INTERVAL);
		// Set the fastest update interval to 1 second
		locationRequest.setFastestInterval(FASTEST_INTERVAL);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_game, parent, false);

		map = ((SupportMapFragment) getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		map.setBuildingsEnabled(true);
		
		hunterHealth = (ProgressBar) v.findViewById(R.id.health_bar);
		hunterHealth.getProgressDrawable().setColorFilter(Color.argb(255, 52, 124, 23), Mode.MULTIPLY);
		hunterHealth.setMax(Hunter.INIT_HEALTH);
		hunterHealth.setProgress(Hunter.INIT_HEALTH);
		
		hunterScore = (TextView) v.findViewById(R.id.score);
		String text = getString(R.string.score);
		hunterScore.setText(text + " 0");
		
		ghostViews = new ArrayList<Marker>();
		boneViews = new ArrayList<Marker>();
		return v;
	}

	/*
	 * Called when the Activity becomes visible.
	 */
	@Override
	public void onStart() {
		super.onStart();
		// Connect the client.
		locationClient.connect();
	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	public void onStop() {
		locationClient.removeLocationUpdates(this);

		// Disconnecting the client invalidates it.
		locationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onLocationChanged(Location loc) {
		hunterLoc = loc;
		store.moveHunter(loc);
		mapCenter = new LatLng(loc.getLatitude(), loc.getLongitude());
		hunterView.setPosition(mapCenter);
		// map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, ZOOM_FACTOR));
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@TargetApi(11)
	@Override
	public void onConnected(Bundle dataBundle) {
		map.clear();
		ghostViews.clear();
		boneViews.clear();
		hunterLoc = locationClient.getLastLocation();
		mapCenter = new LatLng(hunterLoc.getLatitude(), hunterLoc.getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, ZOOM_FACTOR));
		
		store.initGame(hunterLoc);
		hunterView = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.alt_hunter)).position(mapCenter)); // .title("Health: " + store.getHealth() + "\nScore: " + store.getScore()));
		store.getHunter().setView(hunterView);
		
		ArrayList<Ghost> ghosts = store.getGhosts();
		for (Ghost g : ghosts) {
			LatLng pos = new LatLng(g.getLat(), g.getLon());
			
			final Marker ghost = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ghost)).position(pos));
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				ObjectAnimator animator = ObjectAnimator.ofFloat(ghost, "alpha", 0f, 0.2f, 0.4f, 0.6f, 0.8f, 1f);
				animator.setDuration(ANIMATION_DURATION);
				animator.start();
			}
			
			ghostViews.add(ghost);
			g.setView(ghost);
		}
		
		ArrayList<Bone> bones = store.getBones();
		for (Bone b : bones) {
			LatLng pos = new LatLng(b.getLat(), b.getLon());
		
			final Marker bone = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.grave)).position(pos));
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				ObjectAnimator animator = ObjectAnimator.ofFloat(bone, "alpha", 0f, 0.2f, 0.4f, 0.6f, 0.8f, 1f);
				animator.setDuration(ANIMATION_DURATION);
				animator.start();
			}
			
			boneViews.add(bone);
			b.setView(bone);
		}

		locationClient.requestLocationUpdates(locationRequest, this);
		
		if (game == null) {
			game = new AsyncGame(store, ghostViews, getActivity(), this);
			game.execute();
		}
	}
	
	@Override
	public void gameOver() {
		getActivity().finish();
	}
	
	@TargetApi(11)
	@Override
	public void removeGamePiece(String id, int index) {
		if (id.equals(HUNTER)) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				ObjectAnimator animator = ObjectAnimator.ofFloat(hunterView, "alpha", 0f, 1f, 0f, 1f, 0f, 1f);
				animator.setDuration(ANIMATION_DURATION);
				animator.start();
			}
			
			int health = store.getHealth();
			if (health <= 1) {
				hunterHealth.getProgressDrawable().setColorFilter(Color.argb(255, 228, 34, 23), Mode.MULTIPLY);
			}
			hunterHealth.setProgress(health);
		} else if (id.equals(GHOST)) {
			Ghost g = store.getGhosts().get(index);
			final int i = index;
			final Marker view = g.getView();
			/*
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.8f, 0.6f, 0.4f, 0.2f, 0f);
				animator.setDuration(ANIMATION_DURATION);
				animator.start();
			} */
			
			floatingMarkerAnimation(view);
			
			store.killGhost(i);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					view.remove();
					if (!store.hunterKilled()) {
						spawnGhosts();
					}
				}
			}, ANIMATION_DURATION);
		} else if (id.equals(BONE)) {
			Bone b = store.getBones().get(index);
			final int i = index;
			final Marker view = b.getView();
			/*
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.8f, 0.6f, 0.4f, 0.2f, 0f);
				animator.setDuration(ANIMATION_DURATION);
				animator.start();
			} */
			
			floatingMarkerAnimation(view);
			
			store.killBone(i);
			
			int score = store.getScore();
			String text = getString(R.string.score);
			hunterScore.setText(text + " " + score);
			
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					view.remove();
				}
			}, ANIMATION_DURATION);
		}
	}
	
	public void floatingMarkerAnimation(final Marker m) {
		final LatLng startPos = m.getPosition();

		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = map.getProjection();

		Point endPoint = proj.toScreenLocation(startPos);
		endPoint.y = 0;
		final LatLng endLatLng = proj.fromScreenLocation(endPoint);

		final Interpolator interpolator = new LinearInterpolator();
		handler.post(new Runnable() {
		    @Override
		    public void run() {
		        long elapsed = SystemClock.uptimeMillis() - start;
		        float t = interpolator.getInterpolation((float) elapsed / ANIMATION_DURATION);
		        double lng = t * endLatLng.longitude + (1 - t) * startPos.longitude;
		        double lat = t * endLatLng.latitude + (1 - t) * startPos.latitude;
		        m.setPosition(new LatLng(lat, lng));
		        m.setAlpha(1 - t);
		        if (t < 1.0) {
		            // Post again 10ms later.
		            handler.postDelayed(this, 10);
		        } else {
		            // animation ended
		        }
		    }
		});
	}
	
	@TargetApi(11)
	public void spawnGhosts() {
		// TODO one ghost is not moving after spawn
		Bone[] newBones = store.spawnGhosts();
		Ghost[] newGhosts = new Ghost[newBones.length];
		for (int i = 0; i < newGhosts.length; i++) {
			newGhosts[i] = newBones[i].getGhost();
		}
		for (int i = 0; i < newGhosts.length; i++) {
			LatLng pos = new LatLng(newGhosts[i].getLat(), newGhosts[i].getLon());
			LatLng posBone = new LatLng(newBones[i].getLat(), newGhosts[i].getLon());
			
			final Marker ghost = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ghost)).position(pos));
			final Marker bone = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.grave)).position(posBone));
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				ObjectAnimator animator = ObjectAnimator.ofFloat(ghost, "alpha", 0f, 0.2f, 0.4f, 0.6f, 0.8f, 1f);
				animator.setDuration(ANIMATION_DURATION);
				animator.start();
			}
			
			ghostViews.add(ghost);
			boneViews.add(bone);
			newGhosts[i].setView(ghost);
			newBones[i].setView(bone);
		}
	}
	
	public void cancel() {
		game.cancel(true);
	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(getActivity(), "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	// Global constants
	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(getActivity(),
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			showErrorDialog(connectionResult.getErrorCode());
		}
	}

	public void showErrorDialog(int errorCode) {
		// Get the error dialog from Google Play services
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
				getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
		// If Google Play services can provide an error dialog
		if (errorDialog != null) {
			// Create a new DialogFragment for the error dialog
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();
			// Set the dialog in the DialogFragment
			errorFragment.setDialog(errorDialog);
			// Show the error dialog in the DialogFragment
			errorFragment.show(getActivity().getSupportFragmentManager(),
					"Location Updates");
		}
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				/*
				 * Try the request again
				 */
				break;
			}
		}
	}

}
