package com.shivivats.kindcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {

    // Information: About anxiety, depression

    Button anxietyInfoButton, depressionInfoButton, suicideInfoButton;

    public InfoFragment() {
        // required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        anxietyInfoButton = getActivity().findViewById(R.id.anxietyInfoButton);
        depressionInfoButton = getActivity().findViewById(R.id.depressionInfoButton);
        suicideInfoButton = getActivity().findViewById(R.id.suicideInfoButton);

        anxietyInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AnxietyInfoActivity.class);
            startActivity(intent);
        });

        depressionInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DepressionInfoActivity.class);
            startActivity(intent);
        });

        suicideInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SuicideInfoActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // can use this function later
    }


}