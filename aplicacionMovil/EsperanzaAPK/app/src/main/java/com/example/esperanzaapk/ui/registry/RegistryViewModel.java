package com.example.esperanzaapk.ui.registry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegistryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RegistryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Este es el fragmento de los registros");
    }

    public LiveData<String> getText() {
        return mText;
    }
}