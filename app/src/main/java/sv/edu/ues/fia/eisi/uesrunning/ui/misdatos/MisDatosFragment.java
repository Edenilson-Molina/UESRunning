package sv.edu.ues.fia.eisi.uesrunning.ui.misdatos;

import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        Button boton1 = view.findViewById(R.id.id_boton_calendario);
        boton1.setOnClickListener(view1 -> {
            // Acción al pulsar el botón 3 (CALENDARIO FRAGMENT)
            Fragment newFragment = new CalendarioFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            // ESTO DE ABAJO DEJARLO IGUAL
            transaction.replace(R.id.nav_host_fragment_content_main, newFragment);
            transaction.addToBackStack("MisDatosFragment");
            transaction.setReorderingAllowed(true);
            // Commit the transaction
            transaction.commit();
        });

        Button boton2 = view.findViewById(R.id.id_boton_miperfil);
        boton2.setOnClickListener(view1 -> {
            // Acción al pulsar el botón 3 (MIPERFIL FRAGMENT)
            Fragment newFragment = new MiPerfilFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            // ESTO DE ABAJO DEJARLO IGUAL
            transaction.replace(R.id.nav_host_fragment_content_main, newFragment);
            transaction.addToBackStack("MisDatosFragment");
            transaction.setReorderingAllowed(true);
            // Commit the transaction
            transaction.commit();
        });

        Button boton3 = view.findViewById(R.id.id_boton_galeria);
        boton3.setOnClickListener(view1 -> {
            // Acción al pulsar el botón 3 (GALERIA FRAGMENT)
            Fragment newFragment = new GaleriaFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            // ESTO DE ABAJO DEJARLO IGUAL
            transaction.replace(R.id.nav_host_fragment_content_main, newFragment);
            transaction.addToBackStack("MisDatosFragment");
            transaction.setReorderingAllowed(true);
            // Commit the transaction
            transaction.commit();
        });

        return view;
    }



}