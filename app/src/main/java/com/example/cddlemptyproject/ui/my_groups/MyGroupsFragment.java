package com.example.cddlemptyproject.ui.my_groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.cddlemptyproject.R;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cddlemptyproject.R;
import com.example.cddlemptyproject.backend.InternalDB;
import com.example.cddlemptyproject.logic.data.model.Group;
import com.example.cddlemptyproject.logic.models.User;
import com.example.cddlemptyproject.ui.all_groups.AllGroupsRecyclerViewAdapter;
import com.example.cddlemptyproject.ui.all_groups.AllGroupsViewModel;
import com.example.cddlemptyproject.ui.my_groups.my_groups_detail.GroupLeaderFragment;

import java.util.ArrayList;
import java.util.List;

public class MyGroupsFragment extends Fragment implements MyGroupsRecyclerViewAdapter.ItemClickListener {

    private MyGroupsViewModel myGroupsViewModel;
    MyGroupsRecyclerViewAdapter adapter;
    private User user;
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        myGroupsViewModel =
                ViewModelProviders.of(this).get(MyGroupsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_groups, container, false);

        user = InternalDB.getLoggedInUserAndre();

        RecyclerView recyclerView = root.findViewById(R.id.rvMyGroups);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyGroupsRecyclerViewAdapter(getContext(), new ArrayList<Group>());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);





        myGroupsViewModel.getMeusGrupos().observe(getViewLifecycleOwner(), new Observer<List<Group>>() {
            @Override
            public void onChanged(List<Group> groups) {
                adapter.changeDataSet(groups);
//                adapter.notifyDataSetChanged();
            }
        });

        myGroupsViewModel.loadAvailableGroups();




        // set up the RecyclerView







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

    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position).getGroupName(), Toast.LENGTH_SHORT).show();
        if(adapter.getItem(position).getGroupLeader().equals(user.getEmail())){
            Bundle bundle = new Bundle();
            bundle.putString("group_uuid", adapter.getItem(position).getResourceUuid());
            bundle.putString("group_name", adapter.getItem(position).getGroupName());
            bundle.putString("group_leader", adapter.getItem(position).getGroupLeader());
            Fragment fragment = new GroupLeaderFragment();
            fragment.setArguments(bundle);
            loadFragment(fragment);
        }else{

        }

    }
}