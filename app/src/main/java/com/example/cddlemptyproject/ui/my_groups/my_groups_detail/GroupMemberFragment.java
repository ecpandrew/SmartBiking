package com.example.cddlemptyproject.ui.my_groups.my_groups_detail;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cddlemptyproject.R;
import com.example.cddlemptyproject.logic.data.model.Coordenadas;
import com.example.cddlemptyproject.logic.data.model.RoutesRegistered;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class GroupMemberFragment extends Fragment implements OnMapReadyCallback{

    private GroupMemberViewModel groupMemberViewModel;
    private GoogleMap mMap;
    private Marker marcador;
    private Handler handler = new Handler();
//    private String resource_uuid = "85acec75-1b9f-4997-a7d4-5bfa91ff9233";
    Bundle extras;
    String group_uuid;
    String group_name;
    String group_leader;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            groupMemberViewModel.loadRoutesOfGroup(group_uuid);
            handler.postDelayed(this,5 * 1000); // loop a cada 5 segundos
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        extras = getArguments();
        group_uuid = extras != null ? extras.getString("group_uuid") : null;
        group_name = extras != null ? extras.getString("group_name") : null;
        group_leader = extras != null ? extras.getString("group_leader") : null;

        // Iniciando a view Model, as requisições não ficam nessa atividade e sem nessa classe GroupMemberViewModel
        groupMemberViewModel = ViewModelProviders.of(this).get(GroupMemberViewModel.class);
        groupMemberViewModel.setVoleyContext(requireActivity().getApplicationContext());


        View root = inflater.inflate(R.layout.fragment_group_member, container, false);

        // Colocando o mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.member_map);
        mapFragment.getMapAsync(this);

        // Mudando o titulo da atividade
        requireActivity().setTitle("Você é o Lider do Grupo");


        // aqui o marcador atualiza cada vez que o valor da variavel coordenadasAtual dentro da view model atualiza
        groupMemberViewModel.getCoordenadaAtual().observe(getViewLifecycleOwner(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng coordenadas) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas,15));
                marcador.setPosition(coordenadas);

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        startLoop();
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Definindo posição inicial do mapa: essa coordenada é la na litoranea proximo a estatua dos pescadores
        LatLng start = new LatLng(-2.4902906, -44.296496);

        marcador = mMap.addMarker(new MarkerOptions().position(start).title("new Marker"));

    }



    private void startLoop() {
        handler.postDelayed(runnable, 5*1000);
    }

    private void endLoop(){
        handler.removeCallbacks(runnable); //stop handler when activity not visible
    }

    @Override
    public void onDestroy() {
        endLoop();
        super.onDestroy();
    }
}
