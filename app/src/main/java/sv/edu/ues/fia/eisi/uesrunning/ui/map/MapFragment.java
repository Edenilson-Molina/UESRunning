package sv.edu.ues.fia.eisi.uesrunning.ui.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


import java.util.ArrayList;

import sv.edu.ues.fia.eisi.uesrunning.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements SensorEventListener {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private MyLocationNewOverlay mLocationOverlay;
    private Polyline trackPolyline;
    private SensorManager sensorManager;
    private Sensor stepSensor;

    // PARA LAS VISTAS
    private CircularProgressBar progressBar;

    private TextView contadortxt;

    private Button bt;

    int currentProgress;

    int redon;

    private boolean sensorsEnabled = true;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration
        Context ctx = requireContext().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //Zoom-Buttons:
        map.setTilesScaledToDpi(true);
        map.setMaxZoomLevel(25.0);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        // Initialize the track polyline
        trackPolyline = new Polyline();

        // Get the MapController
        IMapController mapController = map.getController();
        mapController.setZoom(17.5);

        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        this.mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();
        mLocationOverlay.setDrawAccuracyEnabled(true);
        map.getOverlays().add(this.mLocationOverlay);

        // Show cursor
        Location lastFix = mLocationOverlay.getLastFix();
        if (lastFix != null) {
            double latitude = lastFix.getLatitude();
            double longitude = lastFix.getLongitude();

            // Create a GeoPoint with the current location
            GeoPoint currentLocation = new GeoPoint(latitude, longitude);

            // Set the center of the map to the current location
            mapController = map.getController();
            mapController.setCenter(currentLocation);
        }

        String[] permissions = new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        requestPermissionsIfNecessary(permissions);

        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        progressBar = view.findViewById((R.id.progress_circular));
        progressBar.setProgress(100000); // Pasos recomendados a diario
        contadortxt = view.findViewById(R.id.step_counter3);
        currentProgress = -1;
        bt = view.findViewById(R.id.toggleButton);
        bt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                toggleSensors(v);
            }
        });
        // Inflate the layout for this fragment
        return view;


    }
    public void toggleSensors(View view) {
        if (sensorsEnabled) {
            sensorManager.unregisterListener(this);
            sensorsEnabled = false;
            bt.setText("Activar");
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorsEnabled = true;
            currentProgress -= 1;
            bt.setText("Desactivar");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permissions[i]);
            }
        }
        if (permissionsToRequest.size() > 0) {
            requestPermissions(
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            requestPermissions(
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void setProgress(int progress) {
        progressBar.setProgress(progress);
    }
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            float steps = event.values[0];
            // Actualiza tu contador de pasos en la interfaz de usuario
            // Actualiza el progreso
            // Obtén el progreso actual de tus datos o cálculos


            currentProgress +=1;

            if(currentProgress%3==0){
                redon +=1;

            }



            contadortxt.setText(String.valueOf(redon));
            setProgress(currentProgress);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Método requerido por la interfaz SensorEventListener,
        // no necesitas implementarlo en este caso.
    }


}