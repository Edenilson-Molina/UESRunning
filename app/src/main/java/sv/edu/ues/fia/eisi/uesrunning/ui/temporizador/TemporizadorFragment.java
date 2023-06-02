package sv.edu.ues.fia.eisi.uesrunning.ui.temporizador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import sv.edu.ues.fia.eisi.uesrunning.R;
import sv.edu.ues.fia.eisi.uesrunning.databinding.FragmentTemporizadorBinding;

public class TemporizadorFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temporizador, container, false);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}