package com.example.cddlemptyproject.ui.my_groups;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cddlemptyproject.backend.InternalDB;
import com.example.cddlemptyproject.logic.data.model.Group;

import java.util.ArrayList;
import java.util.List;

public class MyGroupsViewModel extends ViewModel {

    private MutableLiveData<List<Group>>  myGroups;

    public MyGroupsViewModel() {
        myGroups = new MutableLiveData<List<Group>>();
    }



    public MutableLiveData<List<Group>> getMeusGrupos() {
        return myGroups;
    }

    public void loadAvailableGroups(){
        List<Group> all_groups = InternalDB.getAllGroups();
        List<String> ids = InternalDB.getMyGroups(InternalDB.getLoggedInUserAndre().getEmail());
        ArrayList<Group> meus_grupos = new ArrayList<>();
        for (Group group: all_groups) {

            if(ids.contains(group.getResourceUuid())){
                meus_grupos.add(group);
            }



        }

        myGroups.setValue(meus_grupos);


    }
}