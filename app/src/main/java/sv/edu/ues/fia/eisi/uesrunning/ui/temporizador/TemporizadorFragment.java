package sv.edu.ues.fia.eisi.uesrunning.ui.temporizador;

import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.media.RingtoneManager;
import android.os.CountDownTimer;
import android.webkit.*;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import sv.edu.ues.fia.eisi.uesrunning.R;
import sv.edu.ues.fia.eisi.uesrunning.databinding.FragmentTemporizadorBinding;

public class TemporizadorFragment extends Fragment {
    private ProgressBar pbCarga;

    private EditText txtTiempo;
    private TextView tvCuentaAtras;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temporizador, container, false);
        pbCarga = view.findViewById(R.id.pbCarga);
        txtTiempo = view.findViewById(R.id.txt_tiempo);
        tvCuentaAtras = view.findViewById(R.id.tv_cuentaatras);

        button = view.findViewById(R.id.iniciar);
        button.setOnClickListener(this::play);
        return view;
    }

    public void play(View view) {
        long tiempoSegundos = Long.parseLong(txtTiempo.getText().toString());
        long tiempoMilisegundos = tiempoSegundos * 1000;
        new CountDownTimer(tiempoMilisegundos, 1000) {
            @Override
            public void onFinish() {
                Uri notificacion = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                Ringtone r = RingtoneManager.getRingtone(getActivity(), notificacion);
                r.play();
                this.cancel();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                int tiempoSegundos = (int) (millisUntilFinished / 1000) + 1;
                tvCuentaAtras.setText(String.format("%02d", tiempoSegundos));
            }
        }.start();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}