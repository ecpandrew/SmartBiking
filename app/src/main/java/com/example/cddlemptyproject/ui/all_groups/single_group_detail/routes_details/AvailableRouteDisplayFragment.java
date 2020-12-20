package com.example.cddlemptyproject.ui.all_groups.single_group_detail.routes_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cddlemptyproject.R;
import com.example.cddlemptyproject.logic.data.model.RoutesRegistered;
import com.example.cddlemptyproject.ui.all_groups.AllGroupsFragment;
import com.example.cddlemptyproject.ui.all_groups.single_group_detail.SingleGroupDetailFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvailableRouteDisplayFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;

    private AvailableRouteDisplayViewModel availableRouteDisplayViewModel;
    private RoutesRegistered rota;
    Bundle extras;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        extras = getArguments();
        rota = extras.getParcelable("route");

        availableRouteDisplayViewModel =
                ViewModelProviders.of(this).get(AvailableRouteDisplayViewModel.class);

        View root = inflater.inflate(R.layout.fragment_available_routes_detail, container, false);



        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.available_map);
        mapFragment.getMapAsync(this);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), getCallBack());


        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        Double[] latitude = rota.getLatitude_array();
        Double[] longitude = rota.getLongitude_array();

        LatLng[] locations = new LatLng[latitude.length-1];

        for (int i = 0; i < latitude.length-1 ; i++) {
            locations[i] =  new LatLng(latitude[i],longitude[i]);
        }


        LatLng start = locations[0];
        LatLng end = locations[locations.length-1];

//        LatLng p1 = new LatLng(-2.4882957,-44.2821775);
//        LatLng p2 = new LatLng(-2.4881416,-44.2858803 );
//        LatLng p3 = new LatLng(-2.4883633, -44.292835);
//        LatLng p4 = new LatLng(-2.4889117, -44.2888085);



        mMap.addMarker(new MarkerOptions()
                .position(start)
                .title("Inicio"));

        mMap.addMarker(new MarkerOptions()
                .position(end)
                .title("Fim"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,15));


        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .addAll(
                        Arrays.asList(locations)
                ));




    }

    private OnBackPressedCallback getCallBack(){
        return new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().setTitle(R.string.title_all_groups);
                Fragment fragment = new SingleGroupDetailFragment();
                fragment.setArguments(extras);
                loadFragment(fragment);
            }
        };
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }


}
