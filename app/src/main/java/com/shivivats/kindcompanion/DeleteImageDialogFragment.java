package com.shivivats.kindcompanion;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DeleteImageDialogFragment extends DialogFragment {


    // Use this instance of the interface to deliver action events
    DeleteImageListener listener;

    // Override the Fragment.onAttach() method to instantiate the listener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DeleteImageListener) context;
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
        builder.setMessage("Delete the image?")
                .setPositiveButton("Yes", (dialog, id) -> listener.onDeleteImageDialogPositiveClick(DeleteImageDialogFragment.this))
                .setNegativeButton("No", (dialog, id) -> {
                    // User cancelled the dialog
                    listener.onDeleteImageDialogNegativeClick(DeleteImageDialogFragment.this);
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface DeleteImageListener {
        void onDeleteImageDialogPositiveClick(DialogFragment dialog);

        void onDeleteImageDialogNegativeClick(DialogFragment dialog);
    }
}