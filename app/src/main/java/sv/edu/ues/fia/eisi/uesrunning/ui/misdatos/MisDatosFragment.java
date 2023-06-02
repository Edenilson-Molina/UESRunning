package sv.edu.ues.fia.eisi.uesrunning.ui.misdatos;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sv.edu.ues.fia.eisi.uesrunning.GaleriaActivity;
import sv.edu.ues.fia.eisi.uesrunning.R;

public class MisDatosFragment extends Fragment {

    private MisDatosViewModel mViewModel;

    public static MisDatosFragment newInstance() {
        return new MisDatosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_datos, container, false);

        // Inflate the layout for this fragment

        //Button boton1 = view.findViewById(R.id.id_boton_calendario);
        //boton1.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        // Acción al pulsar el botón 1
        //        Intent intent = new Intent(getActivity(), Activity1.class);
        //        startActivity(intent);
        //    }
        //});

        //Button boton2 = view.findViewById(R.id.id_boton_miperfil);
        //boton2.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        // Acción al pulsar el botón 2
        //        Intent intent = new Intent(getActivity(), Activity2.class);
        //        startActivity(intent);
        //    }
        //});

        Button boton3 = view.findViewById(R.id.id_boton_galeria);
        boton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al pulsar el botón 3
                Intent intent = new Intent(getContext(), GaleriaActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MisDatosViewModel.class);
        // TODO: Use the ViewModel
    }

}