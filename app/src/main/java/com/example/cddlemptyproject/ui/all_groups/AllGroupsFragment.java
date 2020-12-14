package com.example.cddlemptyproject.ui.all_groups;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cddlemptyproject.R;
import com.example.cddlemptyproject.logic.data.model.Group;
import com.example.cddlemptyproject.ui.all_groups.single_group_detail.SingleGroupDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class AllGroupsFragment extends Fragment implements AllGroupsRecyclerViewAdapter.ItemClickListener {

    private AllGroupsViewModel allGroupsViewModel;
    private AllGroupsRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        allGroupsViewModel =
                ViewModelProviders.of(this).get(AllGroupsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_all_groups, container, false);


        RecyclerView recyclerView = root.findViewById(R.id.rvAllGroups);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AllGroupsRecyclerViewAdapter(getContext(), new ArrayList<Group>());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);




        allGroupsViewModel.getGrupos().observe(getViewLifecycleOwner(), new Observer<List<Group>>() {
            @Override
            public void onChanged(List<Group> groups) {
                adapter.changeDataSet(groups);
//                adapter.notifyDataSetChanged();
            }
        });

        allGroupsViewModel.loadAvailableGroups();





        // set up the RecyclerView







        return root;


    }


    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position).getGroupName(), Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putString("group_uuid", adapter.getItem(position).getResourceUuid());
        bundle.putString("group_name", adapter.getItem(position).getGroupName());
        bundle.putString("group_leader", adapter.getItem(position).getGroupLeader());

        getActivity().setTitle(adapter.getItem(position).getGroupName());


        Fragment fragment = new SingleGroupDetailFragment();
        fragment.setArguments(bundle);
        loadFragment(fragment);

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