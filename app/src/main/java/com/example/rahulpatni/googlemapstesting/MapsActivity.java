package com.example.rahulpatni.googlemapstesting;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.MainThread;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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


    void plotLocation(double[] locations) {
        try {
        for (int i = 0; i < 10; i++) {
            wait(1000);

        } }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }



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
}
