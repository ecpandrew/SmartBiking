package com.example.cddlemptyproject.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cddlemptyproject.backend.InternalDB;
import com.example.cddlemptyproject.logic.models.User;

public class HomeViewModel extends ViewModel {


    private MutableLiveData<String> userName;
    private MutableLiveData<String> userEmail;



    public HomeViewModel() {
        userName = new MutableLiveData<>();
        userEmail = new MutableLiveData<>();
        userName.setValue("Usuário");
        userEmail.setValue("E-mail");
    }

    public LiveData<String> getEmail() {

        return userEmail;
    }
    public LiveData<String> getName() {

        return userName;
    }

    public void loadCurrentUser(){ //Here a request to a internal db must be made
        User user = InternalDB.getLoggedInUserAndre();
        userName.setValue(user.getNome());
        userEmail.setValue(user.getEmail());
    }

}