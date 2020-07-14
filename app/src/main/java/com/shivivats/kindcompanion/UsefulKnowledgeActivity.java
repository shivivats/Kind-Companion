package com.shivivats.kindcompanion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class UsefulKnowledgeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 pager;

    ViewPagerFragmentAdapter adapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useful_knowledge);

        tabLayout = findViewById(R.id.knowledgeTabLayout);
        pager = findViewById(R.id.knowledgePager);

        toolbar = findViewById(R.id.knowledgeTopBar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Useful Knowledge");

        adapter = new ViewPagerFragmentAdapter(this);
        pager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                // add the names here for more tabs
                switch (position) {
                    case 0:
                        tab.setText("Info");
                        break;
                    case 1:
                        tab.setText("Resources");
                        break;
                    default:
                        tab.setText("Tab " + position);
                }
            }
        }).attach();
    }


    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        //private Context myContext;
        int totalTabs = 2;

        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // return a NEW fragment instance in this function
            switch (position) {
                case 0:
                    return new InfoFragment();
                case 1:
                    return new ResourcesFragment();
                //case 2:
                //return new TicketsFragment();
            }
            return new InfoFragment();
        }

        @Override
        public int getItemCount() {
            return totalTabs;
        }
    }
}