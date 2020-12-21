package com.example.cddlemptyproject.ui.all_groups.single_group_detail.routes_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cddlemptyproject.R;
import com.example.cddlemptyproject.logic.data.model.RoutesPerformed;
import com.example.cddlemptyproject.ui.all_groups.single_group_detail.SingleGroupDetailFragment;


public class PerformedRouteDisplayFragment extends Fragment{



    private PerformedRouteDisplayViewModel performedRouteDisplayViewModel;
    private RoutesPerformed rota;
    Bundle extras;

    TextView percurso;
    TextView evento;
    TextView velocidade_media;
    TextView distancia_percorrida;
    TextView duracao;
    TextView data;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        extras = getArguments();
        rota = extras.getParcelable("route");

        performedRouteDisplayViewModel =
                ViewModelProviders.of(this).get(PerformedRouteDisplayViewModel.class);

        View root = inflater.inflate(R.layout.fragment_performed_routes_details, container, false);

        percurso = root.findViewById(R.id.percurso);
        evento = root.findViewById(R.id.evento);
        velocidade_media = root.findViewById(R.id.velocidade_media);
        distancia_percorrida = root.findViewById(R.id.distancia_percorrida);
        duracao = root.findViewById(R.id.duracao);
        data = root.findViewById(R.id.data);





        performedRouteDisplayViewModel.getPercurso().observe(getViewLifecycleOwner(), t->{
            percurso.setText(t);
        });
        performedRouteDisplayViewModel.getEvento().observe(getViewLifecycleOwner(), t->{
            evento.setText(t);
        });
        performedRouteDisplayViewModel.getVelocidade_media().observe(getViewLifecycleOwner(), t->{
            velocidade_media.setText(t);
        });
        performedRouteDisplayViewModel.getDistancia_percorrida().observe(getViewLifecycleOwner(), t->{
            distancia_percorrida.setText(t);
        });
        performedRouteDisplayViewModel.getDuracao().observe(getViewLifecycleOwner(), t->{
            duracao.setText(t);
        });
        performedRouteDisplayViewModel.getData().observe(getViewLifecycleOwner(), t->{
            data.setText(t);
        });





        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), getCallBack());

        performedRouteDisplayViewModel.loadData(
                rota.getPercurso(),
                rota.getEvento(),
                rota.getDistancia_percorrida(),
                rota.getVelocidade_media(),
                rota.getDuracao(),
                rota.getDate()

        );

        return root;
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
