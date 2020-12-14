package com.example.cddlemptyproject.ui.all_groups.single_group_detail.routes_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cddlemptyproject.backend.InternalDB;
import com.example.cddlemptyproject.logic.data.model.Group;

import java.util.List;

public class PerformedRouteDisplayViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<Group>> grupos;


    public PerformedRouteDisplayViewModel() {
        mText = new MutableLiveData<>();
        grupos = new MutableLiveData<List<Group>>();
        mText.setValue("Grupos Disponíveis");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List<Group>> getGrupos() {
        return grupos;
    }

    public void loadAvailableGroups(){
        grupos.setValue(InternalDB.getAllGroups());
    }
}