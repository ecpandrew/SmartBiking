package com.example.cddlemptyproject;


import android.os.Bundle;
import android.view.MenuItem;

import com.example.cddlemptyproject.logic.data.InterSCityDataCollector;
import com.example.cddlemptyproject.ui.all_groups.AllGroupsFragment;
import com.example.cddlemptyproject.ui.home.HomeFragment;
import com.example.cddlemptyproject.ui.my_groups.MyGroupsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    // Armazenar o usuário autenticado em um BD

    private String title = "Perfil do Usuário";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        loadFragment(new HomeFragment());
        setTitle(title);

        navView.setOnNavigationItemSelectedListener(this);





//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_my_groups, R.id.navigation_all_groups)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);


    }



    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
//                removeAllFragments(getSupportFragmentManager());
                fragment = new HomeFragment();
                title = "Perfil do Usuário";
                setTitle(title);
                break;

            case R.id.navigation_all_groups:
//                removeAllFragments(getSupportFragmentManager());

                fragment = new AllGroupsFragment();
                title = "Grupos Disponíveis";
                setTitle(title);
                break;

            case R.id.navigation_my_groups:
//                removeAllFragments(getSupportFragmentManager());
                fragment = new MyGroupsFragment();
                title = "Meus Grupos";
                setTitle(title);
                break;
        }

        return loadFragment(fragment);
    }

//    private void clearFragments(){
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        if(fragments != null){
//            for (Fragment fragment : fragments){
//                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//            }
//        }
//    }
    private static void removeAllFragments(FragmentManager fragmentManager) {
    while (fragmentManager.getBackStackEntryCount() > 1) {
        fragmentManager.popBackStackImmediate();
    }
}

}