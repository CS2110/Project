package edu.virginia.cs2110.ghosthunter;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class GameFragment extends Fragment {
	
	public static final int ZOOM_FACTOR = 18;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_game, parent, false);

		GoogleMap map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		// map.setMyLocationEnabled(true);
		LocationManager lm = (LocationManager) getActivity().getSystemService(GameActivity.LOCATION_SERVICE);
		Location currentLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		LatLng mapCenter;
		
		if (currentLocation != null) {
			mapCenter = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		} else {
			mapCenter = new LatLng(38.0299, -78.4790);
		}
		
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, ZOOM_FACTOR));
		/*
		// Flat markers will rotate when the map is rotated,
		// and change perspective when the map is tilted.
		map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ghost_icon)).position(mapCenter).flat(true).rotation(245));

		CameraPosition cameraPosition = CameraPosition.builder().target(mapCenter).zoom(13).bearing(90).build();
	        
		// Animate the change in camera view over 2 seconds
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
		*/
		return v;
	}
	
}
