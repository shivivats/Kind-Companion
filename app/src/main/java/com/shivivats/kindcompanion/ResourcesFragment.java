package com.shivivats.kindcompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResourcesFragment extends Fragment {

    // Resources: TED talks maybe, youtube videos, youtube channels

    // Helplines

    Button reachOutResourceButton, headspaceResourceButton, flowyResourceButton, stopBreatheThinkResourceButton, samTheChatbotResourceButton, befreindersWorldwideResourceButton, psycomResourceButton, yourLifeCountsResourceButton, suicidePreventionLifelineResourceButton;


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
    public void onResume() {
        super.onResume();


        reachOutResourceButton = getActivity().findViewById(R.id.reachOutResourceButton);
        headspaceResourceButton = getActivity().findViewById(R.id.headspaceResourceButton);
        //flowyResourceButton = getActivity().findViewById(R.id.flowyResourceButton);
        stopBreatheThinkResourceButton = getActivity().findViewById(R.id.stopBreatheThinkResourceButton);
        samTheChatbotResourceButton = getActivity().findViewById(R.id.samTheChatbotResourceButton);
        befreindersWorldwideResourceButton = getActivity().findViewById(R.id.befreindersWorldwideResourceButton);
        psycomResourceButton = getActivity().findViewById(R.id.psycomResourceButton);
        yourLifeCountsResourceButton = getActivity().findViewById(R.id.yourLifeCountsResourceButton);
        suicidePreventionLifelineResourceButton = getActivity().findViewById(R.id.suicidePreventionLifelineResourceButton);

        reachOutResourceButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://au.reachout.com/tools-and-apps"));
            startActivity(intent);
        });

        headspaceResourceButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.getsomeheadspace.android"));
            startActivity(intent);
        });
/*
        flowyResourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
                startActivity(intent);
            }
        });
*/
        stopBreatheThinkResourceButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCkB9zEEqnP9kMIf5VChd99Q"));
            startActivity(intent);
        });

        samTheChatbotResourceButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://headtohealth.gov.au/sam-the-chatbot"));
            startActivity(intent);
        });

        befreindersWorldwideResourceButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.befrienders.org/"));
            startActivity(intent);
        });

        psycomResourceButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.psycom.net/"));
            startActivity(intent);
        });

        yourLifeCountsResourceButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://yourlifecounts.org/"));
            startActivity(intent);
        });

        suicidePreventionLifelineResourceButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://suicidepreventionlifeline.org/help-yourself/"));
            startActivity(intent);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}