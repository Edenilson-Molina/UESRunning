package sv.edu.ues.fia.eisi.uesrunning.ui.misdatos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sv.edu.ues.fia.eisi.uesrunning.R;
import sv.edu.ues.fia.eisi.uesrunning.databinding.FragmentGaleriaBinding;

public class GaleriaFragment extends Fragment {

    private static final int REQUEST_CAMERA_PERMISSION = 1001 ;
    private FragmentGaleriaBinding binding;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private LinearLayout layoutImages;

    private Gallery gallery;
    private List<File> imageFiles;

    String currentPhotoPath;

    View view;
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
        // Primero, verificar si el permiso de la cámara ya está otorgado
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Toodo bien
            Log.d("TAG", "onCreateView: Permiso de camara concedido");
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_galeria, container, false);

        Button btnCapture = view.findViewById(R.id.boton_tomar_foto);

        gallery = view.findViewById(R.id.linear_image_layout);

        // Obtener la lista de archivos de imágenes
        imageFiles = getAllImageFiles();

        if (imageFiles.isEmpty()) {
            Toast.makeText(getContext(), "Toma tu primera foto de entrenamiento", Toast.LENGTH_SHORT).show();
        } else {
            // Crear un adaptador personalizado para el componente Gallery
            GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), imageFiles);

            // Asignar el adaptador al componente Gallery
            gallery.setAdapter(galleryAdapter);
        }


        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });



        return view;
    }


    private List<File> getAllImageFiles() {
        List<File> files = new ArrayList<>();

        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null && storageDir.exists() && storageDir.isDirectory()) {
            File[] imageFilesArray = storageDir.listFiles();
            if (imageFilesArray != null) {
                for (File file : imageFilesArray) {
                    if (file.isFile()) {
                        files.add(file);
                    }
                }
            }
        }

        return files;
    }

    private void actualizarGaleria() {
        // Obtener la lista de archivos de imágenes
        imageFiles = getAllImageFiles();

        // Crear un adaptador personalizado para el componente Gallery
        GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), imageFiles);

        // Asignar el adaptador al componente Gallery
        gallery.setAdapter(galleryAdapter);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Verificar que podemos usar la cámara
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Creamos el archivo donde se guardará la foto
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Errores aqui
            }
            // Si no hubo problemas al crear el archivo
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(getContext(), "Foto guardada con exito", Toast.LENGTH_SHORT).show();
            actualizarGaleria();
        }
    }

    private File createImageFile() throws IOException {

        // Crear un archivo de imagen
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefijo */
                ".jpg",         /* sufijo */
                storageDir      /* directorio donde se guarda */
        );
        // Obtenemos el path del archivo, por si queremos usarlo
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}