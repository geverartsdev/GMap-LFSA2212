package com.geverartsdev.lfsa2212.gmap;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.geverartsdev.lfsa2212.gmap.adapters.PotholeAdapter;
import com.geverartsdev.lfsa2212.gmap.objects.Pothole;
import com.geverartsdev.lfsa2212.gmap.services.FirestoreService;
import com.geverartsdev.lfsa2212.gmap.services.LocationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "SensorActivity";

    private SensorManager sensorManager;
    private Sensor sensor;

    private float[] lastMeasurements;
    private Long lastTime = Long.MIN_VALUE;
    private List<Pothole> potholes;
    private PotholeAdapter potholeAdapter;

    private Switch locationSwitch;
    private ListView potholesView;
    private SeekBar thresholdBar;
    private TextView gValue;
    private TextView alert;
    private TextView thresholdView;
    private float threshold = 10;

    private LocationService locationService;
    private FirestoreService firestoreService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        locationService = new LocationService(this);
        firestoreService = new FirestoreService();

        locationSwitch = findViewById(R.id.locationSwitch);
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!locationService.start()) locationSwitch.setChecked(false);
                } else {
                    locationService.stop();
                }
            }
        });

        potholes = new ArrayList<>();
        potholeAdapter = new PotholeAdapter(this, R.layout.pothole_item, potholes);

        potholesView = findViewById(R.id.potholesListView);
        potholesView.setAdapter(potholeAdapter);

        thresholdView = findViewById(R.id.threshold);

        alert = findViewById(R.id.alert);
        alert.setVisibility(View.INVISIBLE);

        thresholdBar = findViewById(R.id.thresholdBar);
        thresholdBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                threshold = progress;
                thresholdView.setText("Threshold : " + threshold);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        gValue = findViewById(R.id.gView);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lastMeasurements == null) this.lastMeasurements = event.values;

        float diff = Math.abs(this.lastMeasurements[0] - event.values[0])
                + Math.abs(this.lastMeasurements[1] - event.values[1])
                + Math.abs(this.lastMeasurements[2] - event.values[2]);

        this.lastMeasurements = Arrays.copyOf(event.values, 3);

        // Log.d(TAG, "onSensorChanged : " + diff);

        if (diff > threshold) potHoleDetected(diff);
        else alert.setVisibility(View.INVISIBLE);

        gValue.setText("Acceleration : " + diff);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void potHoleDetected(float intensity) {
        alert.setVisibility(View.VISIBLE);
        if (potholes.size() == 0 || System.currentTimeMillis() - lastTime > 1000) {
            Location location = locationService.getCurrentBestLocation();
            Pothole pothole = new Pothole(intensity, location);
            potholes.add(0, pothole);
            lastTime = System.currentTimeMillis();

            if (location != null) firestoreService.addPothole(pothole);
        } else {
            potholes.get(0).updateIntensityIfGreater(intensity);
        }
        potholeAdapter.notifyDataSetChanged();
    }
}
