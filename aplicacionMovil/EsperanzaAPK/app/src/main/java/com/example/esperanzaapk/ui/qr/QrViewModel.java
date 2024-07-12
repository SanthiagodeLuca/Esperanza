package com.example.esperanzaapk.ui.qr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QrViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public QrViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Este es el fragmento del QR");
    }

    public LiveData<String> getText() {
        return mText;
    }
}