package sv.edu.ues.fia.eisi.uesrunning.ui.temporizador;

import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.media.RingtoneManager;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import sv.edu.ues.fia.eisi.uesrunning.R;

public class TemporizadorFragment extends Fragment {

    private CountDownTimer countDownTimer; // Variable miembro para el CountDownTimer
    private Ringtone ringtone;
    private EditText txtSegundos;
    private EditText txtMinutos;
    private EditText txtHoras;
    private TextView tvCuentaAtras;
    private Button button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temporizador, container, false);

        txtSegundos = view.findViewById(R.id.txt_segundos);
        tvCuentaAtras = view.findViewById(R.id.tv_cuentaatras);
        txtMinutos = view.findViewById(R.id.txt_minutos);
        txtHoras = view.findViewById(R.id.txt_horas);

        button = view.findViewById(R.id.iniciar);
        button.setOnClickListener(this::play);

        return view;
    }

    public void play(View view) {
        txtHoras.setEnabled(false);
        txtMinutos.setEnabled(false);
        txtSegundos.setEnabled(false);
        button.setEnabled(false);
        long tiempoSegundos = Long.parseLong(txtSegundos.getText().toString());
        long tiempoMilisegundos = tiempoSegundos * 1000;
        long seg = Long.parseLong(txtSegundos.getText().toString()) * 1000;
        long min = Long.parseLong(txtMinutos.getText().toString()) * 60 * 1000;
        long hor = Long.parseLong(txtHoras.getText().toString()) * 60 * 60 * 1000;
        tiempoMilisegundos = seg + min + hor;
        if (tiempoSegundos == 0 && min==0 && hor ==00) {
            Toast.makeText(getActivity(), "Ingrese un tiempo mayor a 0", Toast.LENGTH_SHORT).show();
            return; // Salir del método sin iniciar el temporizador
        }else{
        new CountDownTimer(tiempoMilisegundos, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long tiempoSegundos = (millisUntilFinished / 1000 + 1);
                long horas = tiempoSegundos / 3600;
                tiempoSegundos = tiempoSegundos % 3600;
                long min = tiempoSegundos / 60;
                tiempoSegundos = tiempoSegundos % 60;
                tvCuentaAtras.setText(String.format("%02d:%02d:%02d", horas, min, tiempoSegundos));
            }

            @Override
            public void onFinish() {

                Uri notificacion = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                ringtone= RingtoneManager.getRingtone(getActivity(), notificacion);
                ringtone.play();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Tiempo Terminado");
                builder.setMessage("El tiempo ha terminado");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ringtone.stop();
                        tvCuentaAtras.setText(String.format("00:00:00"));
                        txtHoras.setText(String.format("00"));
                        txtMinutos.setText(String.format("00"));
                        txtSegundos.setText(String.format("00"));
                        button.setEnabled(true);
                        txtHoras.setEnabled(true);
                        txtMinutos.setEnabled(true);
                        txtSegundos.setEnabled(true);
                    }


                });

                builder.show();
            }
        }.start();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}