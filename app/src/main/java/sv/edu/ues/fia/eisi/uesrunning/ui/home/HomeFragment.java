package sv.edu.ues.fia.eisi.uesrunning.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import sv.edu.ues.fia.eisi.uesrunning.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
//import sv.edu.ues.fia.eisi.uesrunning.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements SensorEventListener {

    // SENSORES
    private SensorManager sensorManager;
    private Sensor stepSensor;
    // PARA LAS VISTAS
    private CircularProgressBar progressBar;
    private TextView contadortxt;
    private Button bt;
    int currentProgress;
    private boolean sensorsEnabled = true;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(requireContext(), "Permisos Concedidos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Permisos Denegados, las características están desabilitadas si se rechazan los permisos", Toast.LENGTH_LONG).show();
                }
            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Lanzador de permiso de actividad física
        requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);

        // Disparar el layout de home
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializando variables
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        progressBar = view.findViewById((R.id.progress_circular));
        progressBar.setProgressMax(10000); // Pasos recomendados a diario
        contadortxt = view.findViewById(R.id.step_counter);
        currentProgress = -1;

        bt = view.findViewById(R.id.toggleButton);
        bt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                toggleSensors(v);
            }
        });

        return view;
    }

    private void setProgress(int progress) {
        progressBar.setProgress(progress);
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
        sensorManager.registerListener((SensorEventListener) this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            float steps = event.values[0];
            // Actualiza tu contador de pasos en la interfaz de usuario
            // Actualiza el progreso
            // Obtén el progreso actual de tus datos o cálculos
            currentProgress += 1;
            contadortxt.setText(String.valueOf(currentProgress));
            setProgress(currentProgress);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Método requerido por la interfaz SensorEventListener,
        // no necesitas implementarlo en este caso.
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }




}