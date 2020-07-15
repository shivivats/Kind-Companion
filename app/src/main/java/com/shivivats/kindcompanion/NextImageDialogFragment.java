package com.shivivats.kindcompanion;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class NextImageDialogFragment extends DialogFragment {

    // Use this instance of the interface to deliver action events
    NextImageListener listener;

    // Override the Fragment.onAttach() method to instantiate the listener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NextImageListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Move onto the next image? Canvas will be wiped.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onNextImageDialogPositiveClick(NextImageDialogFragment.this);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onNextImageDialogNegativeClick(NextImageDialogFragment.this);
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NextImageListener {
        void onNextImageDialogPositiveClick(DialogFragment dialog);

        void onNextImageDialogNegativeClick(DialogFragment dialog);
    }
}