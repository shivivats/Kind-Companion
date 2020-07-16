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

    // Information: About anxiety, depression
    // Tools: About mindfulness, relaxation
    // Resources: TED talks maybe, youtube videos, youtube channels


    // ReachOut - Their website and app collection is really awesome. There is even more: you can search for your goals and needs, and apps are filtered by your choices. This one is a must check-out!
    // https://au.reachout.com/tools-and-apps

    // Headspace - They have a really useful and great (meditation) app. It's free, but there are also transactions within this. A lot of them are really worth it. For example it has a complimentary subscription via “get some”, “give some” program. Wors and compliments can be very supportive or some more "SOS" exercises in case of sudden meltdowns.
    // https://play.google.com/store/apps/details?id=com.getsomeheadspace.android&hl=de_AT

    // Flowy - Give it a try. It is still in development, but the current approach is already pretty decent. Gaming and breathing are deeply connected.
    // https://play.google.com/store/apps/details?id=com.playlab.flowyfree&hl=de

    // Sam the Chatbot - The next one isn't quite an app, but interesting in the way it reacts. I stumbled over this a while ago:
    // https://headtohealth.gov.au/sam-the-chatbot
    // Sam is a virtual assistant and it's on you, to tell him your situation and he will recommend suitable information, apps and ongoing to move further. The background database works quite well.

    // Stopp, Breathe & Think - They have a nice YouTube Channel, with meditation tipps and how to practice. There is also an app.
    // https://www.youtube.com/channel/UCkB9zEEqnP9kMIf5VChd99Q

    //

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