package sv.edu.ues.fia.eisi.uesrunning.ui.home;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

import sv.edu.ues.fia.eisi.uesrunning.R;


public class HomeFragment extends Fragment implements SensorEventListener {

    public Context context = null;

    // SENSORES
    private SensorManager sensorManager;
    private Sensor stepSensor;

    //private CircularProgressBar progressBar;

    // PARA LOS GESTOS
    private GestureDetector gestureDetector;

    private TextView contadortxt;
    private Button bt;
    int currentProgress;

    int contador_pausa = 0;
    private boolean sensorsEnabled = false;


    ArrayList<String> speechResults;

    // PARA CRONOMETRO
    private TextView txtView;
    private Button btnReset;
    private Handler handler;
    private boolean isRunning;
    private long startTime;
    private long elapsedTime;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(requireContext(), "Permisos Concedidos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Permisos Denegados, las características están desabilitadas si se rechazan los permisos", Toast.LENGTH_LONG).show();
                }
            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.VIBRATE);
            }
        }

        // Disparar el layout de home
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializando variables
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        //progressBar = view.findViewById((R.id.progress_circular));
        //progressBar.setProgress(100000); // Pasos recomendados a diario
        contadortxt = view.findViewById(R.id.step_counter);
        currentProgress = 0;


        // PARA MANEJAR EL ACTIVAR Y DESACTIVAR CON BOTON
        bt = view.findViewById(R.id.toggleButton);
        bt.setOnClickListener(this::toggleSensors);

        // PARA MANEJAR EL CRONOMETRO
        txtView = view.findViewById(R.id.txtView);
        btnReset = view.findViewById(R.id.btn_reset);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        handler = new Handler();


        return view;
    }

    private void startTimer() {
        startTime = SystemClock.elapsedRealtime() - elapsedTime;
        handler.postDelayed(timerRunnable, 0);
        btnReset.setEnabled(false);
        isRunning = true;
    }

    private void stopTimer() {
        handler.removeCallbacks(timerRunnable);
        btnReset.setEnabled(true);
        isRunning = false;
    }

    private void resetTimer() {
        stopTimer();
        elapsedTime = 0;
        updateTimer();
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            elapsedTime = SystemClock.elapsedRealtime() - startTime;
            updateTimer();
            handler.postDelayed(this, 1000);
        }
    };

    private void updateTimer() {
        int hours = (int) (elapsedTime / 3600000);
        int minutes = (int) ((elapsedTime / 60000) % 60);
        int seconds = (int) ((elapsedTime / 1000) % 60);

        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        txtView.setText(time);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View ve, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(ve, savedInstanceState);


        gestureDetector = new GestureDetector(requireContext(), new GestureDetector.SimpleOnGestureListener() {

            // Para el doble tap
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                toggleSensors(ve);
                return true;
            }


            // Para el long press
            @Override
            public void onLongPress(MotionEvent e) {
                startSpeechRecognition();
            }




        });

        // Establecer el listener de onTouch para el TextView
        contadortxt.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });



        contadortxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0);
                contadortxt.dispatchTouchEvent(event);
                return true;
            }
        });
    }

    public void startSpeechRecognition() {

        Log.d("Speech", "startSpeechRecognition: ");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(intent, 28);
        } else{
            Log.e("ERROR","Su dispositivo no admite entrada de voz");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 28 && resultCode == RESULT_OK && data != null)
        {
            // Verificar si parte de los datos obtenidos es: "contar pasos"
            speechResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.d("Speech", "onActivityResult: " + speechResults.get(0));
            if (speechResults.get(0).contains("contar pasos") || speechResults.get(0).contains("contar paso") || speechResults.get(0).contains("detener")) {
                toggleSensors(getView());
            }
        }

    }

    //private void setProgress(int progress) {
    //    progressBar.setProgress(progress);
    //}

    public void toggleSensors(View view) {
        // Obtener el contexto utilizando requireContext()
        context = requireContext();
        NotificationManager notificationManager = null;

        //Lanzador de permiso de actividad física
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED){
            requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        } else {
            // Crea un canal de notificación
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Canal 1";
                String description = "Canal para notificaciones de contador iniciado";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("channel_1", name, importance);
                channel.setDescription(description);
                channel.setVibrationPattern(new long[]{0, 250, 100, 250});
                channel.enableVibration(true);

                // Registra el canal en el sistema
                notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

                name = "Canal 2";
                description = "Canal para notificaciones de contador detenido";
                importance = NotificationManager.IMPORTANCE_DEFAULT;
                channel = new NotificationChannel("channel_2", name, importance);
                channel.setDescription(description);
                channel.setVibrationPattern(new long[]{0, 250, 100, 250});
                channel.enableVibration(true);

                // Registra el canal en el sistema
                notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            // Crea un objeto NotificationCompat.Builder para construir la notificación
            NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context, "channel_1")
                    .setSmallIcon(R.drawable.icon_correr_notification)
                    .setContentTitle("Contador iniciado")
                    .setContentText("Empieza a correr para ver tu progreso")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setVibrate(new long[]{0, 250, 100, 250})
                    .setAutoCancel(true)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE);

            // Crea un objeto NotificationCompat.Builder para construir la notificación
            NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context, "channel_2")
                    .setSmallIcon(R.drawable.icon_parar_notificaction)
                    .setContentTitle("Contador detenido")
                    .setContentText("Toma un descanso")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setVibrate(new long[]{0, 250, 100, 250})
                    .setAutoCancel(true)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE);

            if (sensorsEnabled && bt.getText().equals("Desactivar")) {
                sensorManager.unregisterListener(this);
                sensorsEnabled = false;
                if (notificationManager != null) {
                    notificationManager.notify(1, builder2.build());
                    notificationManager.cancel(2);
                }
                bt.setText("Activar");
                stopTimer();
            } else {
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
                sensorsEnabled = true;
                currentProgress -= 1;
                if (notificationManager != null) {
                    notificationManager.notify(2, builder1.build());
                    notificationManager.cancel(1);
                }
                bt.setText("Desactivar");
                startTimer();
            }
        }


    }


    public void onResume() {
        super.onResume();
        // Registra el listener del sensor
        //sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
            //setProgress(currentProgress);

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