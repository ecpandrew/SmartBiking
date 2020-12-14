package com.example.cddlemptyproject.ui.all_groups.single_group_detail;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cddlemptyproject.R;
import com.example.cddlemptyproject.logic.data.model.RoutesAvailable;
import com.example.cddlemptyproject.logic.data.model.RoutesPerformed;
import com.example.cddlemptyproject.ui.all_groups.AllGroupsFragment;
import com.example.cddlemptyproject.ui.all_groups.AllGroupsRecyclerViewAdapter;
import com.example.cddlemptyproject.ui.all_groups.single_group_detail.routes_details.AvailableRouteDisplayFragment;

import java.util.ArrayList;
import java.util.List;


public class SingleGroupDetailFragment extends Fragment  {

    private SingleGroupDetailViewModel singleGroupDetailViewModel;

    private AvailableRoutesRecyclerViewAdapter availableRoutesRecyclerViewAdapter;
    private PerformedRoutesRecyclerViewAdapter performedRoutesRecyclerViewAdapter;
    Bundle extras;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        extras = getArguments();
        String uuid = extras != null ? extras.getString("group_uuid") : null;
        String group_name = extras != null ? extras.getString("group_name") : null;
        String group_leader = extras != null ? extras.getString("group_leader") : null;
        requireActivity().setTitle(group_name);

        singleGroupDetailViewModel = ViewModelProviders.of(this).get(SingleGroupDetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_single_group_detail, container, false);
        final TextView groupNameTextView = root.findViewById(R.id.group_detail_name);
        final TextView groupLeaderTextView = root.findViewById(R.id.group_detail_leader);
        final TextView groupUUIDTextView = root.findViewById(R.id.group_detail_uuid);

        groupNameTextView.setText(group_name);
        groupLeaderTextView.setText("Líder: " + group_leader);
        groupUUIDTextView.setText("rUUID: "+uuid);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), getCallBack());

        RecyclerView availableRecyclerView = root.findViewById(R.id.group_available_routes);
        RecyclerView.LayoutManager availableLayoutManager = new LinearLayoutManager(getContext());
        availableRecyclerView.setLayoutManager(availableLayoutManager);
        availableRoutesRecyclerViewAdapter = new AvailableRoutesRecyclerViewAdapter(getContext(), new ArrayList<>());
        availableRecyclerView.setAdapter(availableRoutesRecyclerViewAdapter);


        RecyclerView performedRecyclerView = root.findViewById(R.id.group_performed_routes);
        RecyclerView.LayoutManager performedLayoutManager = new LinearLayoutManager(getContext());
        performedRecyclerView.setLayoutManager(performedLayoutManager);
        performedRoutesRecyclerViewAdapter = new PerformedRoutesRecyclerViewAdapter(getContext(), new ArrayList<>());
        performedRecyclerView.setAdapter(performedRoutesRecyclerViewAdapter);


        singleGroupDetailViewModel.getRoutesAvailable().observe(getViewLifecycleOwner(), new Observer<List<RoutesAvailable>>() {
            @Override
            public void onChanged(List<RoutesAvailable> routesAvailables) {
                availableRoutesRecyclerViewAdapter.changeDataSet(routesAvailables);
            }
        });

        singleGroupDetailViewModel.getRoutesPerformed().observe(getViewLifecycleOwner(), new Observer<List<RoutesPerformed>>() {
            @Override
            public void onChanged(List<RoutesPerformed> routesPerformed) {
                performedRoutesRecyclerViewAdapter.changeDataSet(routesPerformed);
            }
        });

        singleGroupDetailViewModel.loadGroupDetails();


        availableRoutesRecyclerViewAdapter.setClickListener(getAvailableRoutesClickListener());
        performedRoutesRecyclerViewAdapter.setClickListener(getPerformedRoutesClickListener());


        return root;
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

    private OnBackPressedCallback getCallBack(){
        return new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().setTitle(R.string.title_all_groups);
                Fragment fragment = new AllGroupsFragment();
                loadFragment(fragment);
            }
        };
    }



    private AvailableRoutesRecyclerViewAdapter.ItemClickListener getAvailableRoutesClickListener(){
        return new AvailableRoutesRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getActivity().setTitle("Detalhes do Percurso");
                Fragment fragment = new AvailableRouteDisplayFragment();
                fragment.setArguments(extras);
                loadFragment(fragment);
            }

        };
    }

    private PerformedRoutesRecyclerViewAdapter.ItemClickListener getPerformedRoutesClickListener(){
        return new PerformedRoutesRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("LALALA", performedRoutesRecyclerViewAdapter.getItem(position).getRouteName());

            }
        };
    }
}
