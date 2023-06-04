package sv.edu.ues.fia.eisi.uesrunning.ui.misdatos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import sv.edu.ues.fia.eisi.uesrunning.R;
import sv.edu.ues.fia.eisi.uesrunning.databinding.FragmentGaleriaBinding;

public class GaleriaFragment extends Fragment {

    private static final int REQUEST_CAMERA_PERMISSION = 1001 ;
    private FragmentGaleriaBinding binding;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private LinearLayout layoutImages;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(requireContext(), "Permisos Concedidos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Permisos Denegados, las características están desabilitadas si se rechazan los permisos", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Lanzador del permiso de Camara
        requestPermissionLauncher.launch(Manifest.permission.CAMERA);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_galeria, container, false);

        Button btnCapture = view.findViewById(R.id.boton_tomar_foto);

        layoutImages = view.findViewById(R.id.linear_image_layout);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        return view;
    }

    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // El permiso no está otorgado, se debe solicitar
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // El permiso está otorgado, puede continuar con la acción
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Aquí puedes guardar la imagen y mostrarla en un recuadro
            addImageToLayout(imageBitmap);
        }
    }

    private void addImageToLayout(Bitmap imageBitmap) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(imageBitmap);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 10, 0, 0); // Espacio entre las imágenes
        imageView.setLayoutParams(layoutParams);
        layoutImages.addView(imageView);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}