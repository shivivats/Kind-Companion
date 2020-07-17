package com.shivivats.kindcompanion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResourcesFragment extends Fragment {

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

    // Helplines

    public ResourcesFragment() {
        // required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resources, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}