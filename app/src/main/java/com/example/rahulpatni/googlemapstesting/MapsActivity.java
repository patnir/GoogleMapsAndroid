package com.example.rahulpatni.googlemapstesting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Vibrator;

import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener{

    private GoogleMap mMap;

    final int PERMISSION_LOCATION = 111;

    private GoogleApiClient googleApiClient;

    private MarkerOptions userMarker;

    private double[] locations;

    public static final String MAPS_EXTRA_ITEM = "maps.extra.item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btnBack = (Button) findViewById(R.id.btnBack);
        final EditText txtGetInfo = (EditText) findViewById(R.id.txtGetInfo);
        final TextView txtPutInto = (TextView) findViewById(R.id.txtPutInfo);

        locations = getIntent().getDoubleArrayExtra(MainActivity.MAIN_EXTRA_ITEM);

        if (locations.length == 0) {
            Toast.makeText(MapsActivity.this, "no text given bitch", Toast.LENGTH_LONG).show();
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API).build();

        txtPutInto.setText(locations[0] + " " + locations[1]);


        //GoogleMap.OnMapLoadedCallback();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MapsActivity.MAPS_EXTRA_ITEM, txtGetInfo.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //plotLocation(locations);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(locations[0], locations[1]);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in West Lala"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));


        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationServices();
                    Log.v("DONKEY", "Permission Granted - starting services");
                } else {
                    //show a dialog saying something like, "I can't run your location dummy - you denied permission!"
                    Log.v("DONKEY", "Permission not granted");
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            Log.v("DONKEY", "Requesting permissions");
        } else {
            Log.v("DONKEY", "Starting Location Services from onConnected");
            startLocationServices();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void setUserMarker(LatLng latng) {
        if (userMarker == null) {
            userMarker.position(latng).title("current location");
            mMap.addMarker(userMarker);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latng, 15));
    }


    @Override
    public void onLocationChanged(Location location) {

        Log.v("DONKEY", "Long: " + location.getLongitude() + " - Lat: " + location.getLatitude());


        //setUserMarker(new LatLng(location.getLatitude(), location.getLongitude()));

        checkLocationDistance(location);

    }

    public void checkLocationDistance(Location curr) {
        Location setLocation = new Location("");
        setLocation.setLatitude(locations[0]);

        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        setLocation.setLongitude(locations[1]);
        if (curr.distanceTo(setLocation) < 1000.00) {
            v.vibrate(5000);
        }
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    public void startLocationServices() {
        Log.v("MAPS", "Start location services called");

        try {
            LocationRequest req = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, req, this);
        }
        catch (SecurityException ex) {
            Toast.makeText(this, "Cannot continue without permission", Toast.LENGTH_SHORT).show();
        }

    }
}
