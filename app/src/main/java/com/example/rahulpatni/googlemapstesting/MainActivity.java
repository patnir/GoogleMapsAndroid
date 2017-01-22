package com.example.rahulpatni.googlemapstesting;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_EXTRA_ITEM = "main.extra.item";

    EditText txtGetLocation;
    TextView txtPutLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGetLocation = (EditText) findViewById(R.id.txtGetLocation);

        txtPutLocation = (TextView) findViewById(R.id.txtPutLocation);

        Button btnMap = (Button)findViewById(R.id.btnMap);

        String info = getIntent().getStringExtra(MapsActivity.MAPS_EXTRA_ITEM);

        if (info == null || info.length() == 0) {
            Toast.makeText(MainActivity.this, "no text bitch", Toast.LENGTH_LONG);
        } else {
            txtPutLocation.setText(info);
        }

        btnMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String location = txtGetLocation.getText().toString();

                if (location.length() == 0) {
                    Toast.makeText(MainActivity.this,"Failed bitch", Toast.LENGTH_LONG).show();
                }
                else {
                    loadMap(location);
                }
            }
        });
    }

    private void loadMap(String location) {
        List<Address> addresses = null;
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocationName(location, 1);
        } catch(Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (addresses == null) {
            return;
        }

        double latitude = 0, longitude = 0;

        if(addresses.size() > 0) {
            latitude= addresses.get(0).getLatitude();
            longitude= addresses.get(0).getLongitude();
        }

        double[] locations = {latitude, longitude};

        Intent intent = new Intent(MainActivity.this, MapsActivity.class);

        intent.putExtra(MAIN_EXTRA_ITEM, locations);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                txtPutLocation.setText(data.getStringExtra(MapsActivity.MAPS_EXTRA_ITEM));
            }
        }
    }
}
