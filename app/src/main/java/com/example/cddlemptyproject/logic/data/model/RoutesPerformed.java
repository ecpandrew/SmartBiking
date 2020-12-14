package com.example.cddlemptyproject.logic.data.model;

import java.util.List;

public class RoutesPerformed {


    private String routeName;
    private boolean checked;

    public RoutesPerformed(String routeName){
        this.routeName = routeName;
        this.checked = false;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean getChecked() {
        return checked;
    }


    public String getRouteName() {
        return routeName;
    }

}
