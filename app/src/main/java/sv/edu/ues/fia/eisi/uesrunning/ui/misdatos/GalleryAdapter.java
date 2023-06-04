package sv.edu.ues.fia.eisi.uesrunning.ui.misdatos;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class GalleryAdapter extends BaseAdapter {

    private Context context;
    private List<File> imageFiles;

    public GalleryAdapter(Context context, List<File> imageFiles) {
        this.context = context;
        this.imageFiles = imageFiles;
    }

    @Override
    public int getCount() {
        return imageFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return imageFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new Gallery.LayoutParams(600, 600));
        } else {
            imageView = (ImageView) convertView;
        }

        File imageFile = imageFiles.get(position);

        // Obtener la miniatura de la imagen del archivo correspondiente
        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        // Bajar la calidad de la imagen
        // Verificar que no sea null
        if(imageBitmap == null){
            Toast.makeText(context, "Toma tu primera foto de entrenamiento", Toast.LENGTH_SHORT).show();
            return imageView;
        }

        // Asignar la imagen al ImageView
        imageView.setImageBitmap(imageBitmap);

        return imageView;
    }
}
