package com.shivivats.kindcompanion;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DeleteNoteDialogFragment extends DialogFragment {


    // Use this instance of the interface to deliver action events
    DeleteNoteListener listener;

    // Override the Fragment.onAttach() method to instantiate the listener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DeleteNoteListener) context;
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
        builder.setTitle("Delete the note?")
                .setPositiveButton("Yes", (dialog, id) -> listener.onDeleteNoteDialogPositiveClick(DeleteNoteDialogFragment.this))
                .setNegativeButton("No", (dialog, id) -> {
                    // User cancelled the dialog
                    listener.onDeleteNoteDialogNegativeClick(DeleteNoteDialogFragment.this);
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface DeleteNoteListener {
        void onDeleteNoteDialogPositiveClick(DialogFragment dialog);

        void onDeleteNoteDialogNegativeClick(DialogFragment dialog);
    }
}
