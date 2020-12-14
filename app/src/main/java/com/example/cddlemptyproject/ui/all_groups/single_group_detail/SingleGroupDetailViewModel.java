package com.example.cddlemptyproject.ui.all_groups.single_group_detail;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cddlemptyproject.backend.InternalDB;
import com.example.cddlemptyproject.logic.data.InterSCityDataCollector;
import com.example.cddlemptyproject.logic.data.model.Group;
import com.example.cddlemptyproject.logic.data.model.RoutesAvailable;
import com.example.cddlemptyproject.logic.data.model.RoutesPerformed;

import java.util.List;

public class SingleGroupDetailViewModel extends ViewModel {

    private MutableLiveData<List<RoutesAvailable>> routesAvailable;
    private MutableLiveData<List<RoutesPerformed>> routesPerformed;


    public SingleGroupDetailViewModel() {
        routesAvailable = new MutableLiveData<List<RoutesAvailable>>();
        routesPerformed = new MutableLiveData<List<RoutesPerformed>>();

    }

    public MutableLiveData<List<RoutesAvailable>> getRoutesAvailable() {
        return routesAvailable;
    }

    public MutableLiveData<List<RoutesPerformed>> getRoutesPerformed() {
        return routesPerformed;
    }


    public void loadGroupDetails(){

        routesAvailable.setValue(InternalDB.getAvailableRoutes());
        routesPerformed.setValue(InternalDB.getPerformedRoutes());

    }


}