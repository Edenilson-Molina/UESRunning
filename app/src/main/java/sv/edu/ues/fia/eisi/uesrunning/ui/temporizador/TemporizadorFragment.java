package sv.edu.ues.fia.eisi.uesrunning.ui.temporizador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import sv.edu.ues.fia.eisi.uesrunning.databinding.FragmentTemporizadorBinding;

public class TemporizadorFragment extends Fragment {

    private FragmentTemporizadorBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TemporizadorViewModel temporizadorViewModel =
                new ViewModelProvider(this).get(TemporizadorViewModel.class);

        binding = FragmentTemporizadorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        temporizadorViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}