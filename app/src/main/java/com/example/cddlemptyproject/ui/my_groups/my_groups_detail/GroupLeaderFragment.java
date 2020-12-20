package com.example.cddlemptyproject.ui.my_groups.my_groups_detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cddlemptyproject.R;
import com.example.cddlemptyproject.TrackActivity;
import com.example.cddlemptyproject.logic.data.InterSCityDataPoster;
import com.example.cddlemptyproject.logic.data.model.RoutesAvailable;
import com.example.cddlemptyproject.logic.data.model.RoutesRegistered;
import com.example.cddlemptyproject.ui.all_groups.single_group_detail.AvailableRoutesRecyclerViewAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GroupLeaderFragment extends Fragment implements OnMapReadyCallback, Spinner.OnItemSelectedListener{

    private GroupLeaderViewModel groupLeaderViewModel;
    private GoogleMap mMap;

    private AvailableRoutesCheckRecyclerViewAdapter availableRoutesCheckRecyclerViewAdapter;

    private ArrayList<Marker> marcadores;

    private Button removerUltimoMaracador;
    private Button enviarPercurso;
    private Button startTrackActivity;
    private Polyline polyline1;

    EditText routeName;
    EditText routeLevel;
    EditText eventName;
    Bundle extras;
    String group_uuid;
    String group_name;
    String group_leader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        extras = getArguments();
        group_uuid = extras != null ? extras.getString("group_uuid") : null;
        group_name = extras != null ? extras.getString("group_name") : null;
        group_leader = extras != null ? extras.getString("group_leader") : null;

        groupLeaderViewModel =
                ViewModelProviders.of(this).get(GroupLeaderViewModel.class);
        groupLeaderViewModel.setContext(requireActivity().getApplicationContext());
        View root = inflater.inflate(R.layout.fragment_group_leader, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.leader_map);
        mapFragment.getMapAsync(this);

        requireActivity().setTitle("Você é o Lider do Grupo");

        removerUltimoMaracador = root.findViewById(R.id.removerMarker);
        enviarPercurso = root.findViewById(R.id.enviar);
        startTrackActivity = root.findViewById(R.id.materialButtonSend);
        routeName = root.findViewById(R.id.route_set_name);
        routeLevel = root.findViewById(R.id.route_set_level);
        eventName = root.findViewById(R.id.eventName);
        RecyclerView availableRecyclerView = root.findViewById(R.id.pick_available_routes);
        RecyclerView.LayoutManager availableLayoutManager = new LinearLayoutManager(getContext());
        availableRecyclerView.setLayoutManager(availableLayoutManager);
        availableRoutesCheckRecyclerViewAdapter = new AvailableRoutesCheckRecyclerViewAdapter(getContext(), new RoutesRegistered[]{});
        availableRecyclerView.setAdapter(availableRoutesCheckRecyclerViewAdapter);




        groupLeaderViewModel.getRoutesAvailable().observe(getViewLifecycleOwner(), new Observer<RoutesRegistered[]>() {
            @Override
            public void onChanged(RoutesRegistered[] routesRegistereds) {
                availableRoutesCheckRecyclerViewAdapter.changeDataSet(routesRegistereds);
            }

        });



        groupLeaderViewModel.loadRoutesOfGroup(group_uuid);

        marcadores = new ArrayList<>();

        removerUltimoMaracador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLastMarker();
            }
        });

        enviarPercurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray latitude_array = new JSONArray();
                JSONArray longitude_array = new JSONArray();

                for(Marker marker : marcadores){
                    try {
                        latitude_array.put(marker.getPosition().latitude);
                        longitude_array.put(marker.getPosition().longitude);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                sendToInterSCity(group_uuid, latitude_array, longitude_array);
                removeAllMarker();
                routeName.setText("");
                routeLevel.setText("");
                groupLeaderViewModel.loadRoutesOfGroup(group_uuid);

            }
        });

        startTrackActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = availableRoutesCheckRecyclerViewAdapter.getSelectedPosition();
                if(pos == -1){
                    Toast.makeText(requireContext().getApplicationContext(),"Selecione um percurso", Toast.LENGTH_SHORT).show();
                }else{
                    if(eventName.getText().toString().isEmpty()){
                        Toast.makeText(requireContext().getApplicationContext(),"Insira o nome do evento!", Toast.LENGTH_SHORT).show();

                    }else{
                        RoutesRegistered routeSelected =  availableRoutesCheckRecyclerViewAdapter.getItem(pos);
                        Intent intent = new Intent(requireActivity().getApplicationContext(), TrackActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("group_uuid", group_uuid );
                        bundle.putString("event", eventName.getText().toString());
                        bundle.putString("nome_percurso", routeSelected.getNome());
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }

                }
            }
        });




        return root;
    }

    private void sendToInterSCity(String group_uuid, JSONArray latitude, JSONArray longitude) {

        InterSCityDataPoster poster = new InterSCityDataPoster(requireActivity().getApplicationContext());
        poster.postNewRouteToInterSCity(group_uuid,
                routeName.getText().toString(),
                routeLevel.getText().toString(),
                latitude, longitude);
    }

    private void removeAllMarker() {
        for(Marker marcador : marcadores){
            marcador.remove();
        }
        marcadores.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng start = new LatLng(-2.4902906, -44.296496);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marcadores.add(
                        mMap.addMarker(new MarkerOptions().position(latLng).title("new Marker")));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });


    }



    public void removeLastMarker(){

        if(marcadores.size()>=1){
            int index = marcadores.size()-1;
            Marker marker = marcadores.get(index);
            marcadores.remove(index);
            marker.remove();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
