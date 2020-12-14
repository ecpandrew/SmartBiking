package com.example.cddlemptyproject.ui.all_groups;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cddlemptyproject.backend.InternalDB;
import com.example.cddlemptyproject.logic.data.model.Group;

import java.util.List;

public class AllGroupsViewModel extends ViewModel {

    private MutableLiveData<List<Group>> grupos;


    public AllGroupsViewModel() {
        grupos = new MutableLiveData<List<Group>>();
    }



    public MutableLiveData<List<Group>> getGrupos() {
        return grupos;
    }

    public void loadAvailableGroups(){
        grupos.setValue(InternalDB.getAllGroups());
    }
}