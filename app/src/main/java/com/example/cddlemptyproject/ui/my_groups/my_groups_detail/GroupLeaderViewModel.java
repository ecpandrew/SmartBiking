package com.example.cddlemptyproject.ui.my_groups.my_groups_detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cddlemptyproject.backend.InternalDB;
import com.example.cddlemptyproject.logic.data.model.RoutesAvailable;
import com.example.cddlemptyproject.logic.data.model.RoutesPerformed;

import java.util.ArrayList;
import java.util.List;

public class GroupLeaderViewModel extends ViewModel {

    private MutableLiveData<List<RoutesAvailable>> routesAvailable;
    private MutableLiveData<List<RoutesAvailable>> membersAvailable;


    public GroupLeaderViewModel() {
        routesAvailable  = new MutableLiveData<List<RoutesAvailable>>();
        membersAvailable = new MutableLiveData<List<RoutesAvailable>>();
    }

    public MutableLiveData<List<RoutesAvailable>> getRoutesAvailable() {
        return routesAvailable;
    }


    public MutableLiveData<List<RoutesAvailable>> getMembersAvailable() {
        return membersAvailable;
    }

    public void loadGroupDetails(){

        routesAvailable.setValue(InternalDB.getAvailableRoutes());
        membersAvailable.setValue(InternalDB.getMembers());

    }


}