package sv.edu.ues.fia.eisi.uesrunning.ui.temporizador;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TemporizadorViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TemporizadorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Este es el temporizador");
    }

    public LiveData<String> getText() {
        return mText;
    }
}