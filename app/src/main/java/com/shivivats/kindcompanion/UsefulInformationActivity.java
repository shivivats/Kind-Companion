package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

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

public class UsefulInformationActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 pager;

    UsefulInformationFragmentAdapter adapter;

    Toolbar toolbar;

    // Information: About anxiety, depression
    // Tools: About mindfulness, relaxation
    // Resources: TED talks maybe, youtube videos, youtube channels




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_useful_information);

        tabLayout = findViewById(R.id.knowledgeTabLayout);
        pager = findViewById(R.id.knowledgePager);

        toolbar = findViewById(R.id.knowledgeTopBar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Useful Knowledge");
        }

        adapter = new UsefulInformationFragmentAdapter(this);
        pager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, pager, (tab, position) -> {
            // add the names here for more tabs
            switch (position) {
                case 0:
                    tab.setText("Info");
                    break;
                case 1:
                    tab.setText("Tools");
                    break;
                case 2:
                    tab.setText("Resources");
                    break;
                default:
                    tab.setText("Tab " + position);
            }
        }).attach();

        Intent intent = getIntent();
        String currentTab = intent.getStringExtra("TAB_NAME");
        if (TextUtils.equals(currentTab, "INFO")) {
            pager.setCurrentItem(0);
        } else if (TextUtils.equals(currentTab, "TOOLS")) {
            pager.setCurrentItem(1);
        } else if (TextUtils.equals(currentTab, "RESOURCES")) {
            pager.setCurrentItem(2);
        }
    }


    private class UsefulInformationFragmentAdapter extends FragmentStateAdapter {

        //private Context myContext;
        int totalTabs = 3;

        public UsefulInformationFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
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
                    return new ToolsFragment();
                case 2:
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