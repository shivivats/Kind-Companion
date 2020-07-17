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

public class ToolsFragment extends Fragment {

    // Tools: About mindfulness, relaxation

    Button mindfulnessButton, relaxationButton;

    public ToolsFragment() {
        // required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tools, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        mindfulnessButton = getActivity().findViewById(R.id.mindfullnessToolsButton);
        relaxationButton = getActivity().findViewById(R.id.relaxationToolsButton);

        mindfulnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MindfulnessToolActivity.class);
                startActivity(intent);
            }
        });

        relaxationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RelaxationToolActivity.class);
                startActivity(intent);
            }
        });
    }
}