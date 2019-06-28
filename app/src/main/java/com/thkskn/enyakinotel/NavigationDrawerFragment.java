package com.thkskn.enyakinotel;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thkskn.enyakinotel.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements KeskinAdapter.ClickListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private RecyclerView recyclerView;
    private KeskinAdapter adapter;


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerlist);
        adapter = new KeskinAdapter(getActivity(),getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    public List<Information> getData(){
        List<Information> data = new ArrayList();
        int[] icons = {R.drawable.ic_9,R.drawable.ic_10,
                R.drawable.ic_11,R.drawable.ic_12};
        String[] titles = getResources().getStringArray(R.array.nav_drawer);

        for (int i=0;i<titles.length;i++){
            Information current = new Information();
            current.iconId = icons[i];
            current.title = titles[i];
            data.add(current);
        }
        return data;
    }


    public void setUp(DrawerLayout drawerLayout,Toolbar toolbar) {

        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.acik,R.string.kapali){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //hamburger icon shows here for navigationDrawer
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    @Override
    public void itemClicked(View view, int position) {
        switch (position){
            case 0 : startActivity(new Intent(getActivity(),MainActivity.class));
                break;
            case 1 : startActivity(new Intent(getActivity(),Account.class));
                mDrawerLayout.closeDrawers();
                break;
            //case 2 : startActivity(new Intent(getActivity(),SettingsActivity.class)); break;
            case 3 : getActivity().finish(); break;
        }

    }
}
