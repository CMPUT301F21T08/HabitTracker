package com.example.habittracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteConfirmFragment extends DialogFragment {

    private TextView confirmationTextView;
    private String confirmationPrompt;

    private OnDeleteConfirmFragmentInteractionListener listener;

    /**
     * Default Constructor
     */
    public DeleteConfirmFragment() {
        super();
    }

    /**
     * Constructor that takes in the customized warning text
     * @param confirmation customized warning text
     */
    public DeleteConfirmFragment(String confirmation) {
        super();
        this.confirmationPrompt = confirmation;
    }

    /**
     * Interface used to modify data on the activity end
     */
    public interface OnDeleteConfirmFragmentInteractionListener {
        void onConfirmDeletePressed();  // Pass the position of the entry to main activity and use this to determine whether to change an item or add new one
    }

    /**
     * The "onAttach" method for DialogFragment, also creates a listener interface
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDeleteConfirmFragmentInteractionListener) {
            listener = (OnDeleteConfirmFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + "must implement OnDeleteConfirmFragmentInteractionListener");
        }
    }

    /**
     * The "onCreateDialog" method for DialogFragment
     * This function do actual manipulation to the data
     * and constructs a alertDialog to show the fragment
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_delete_habit_event_confirm, null);
        confirmationTextView = view.findViewById(R.id.tv_delete_confirm);

        // Set the customized prompt to this fragment
        confirmationTextView.setText(confirmationPrompt);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(view);
        builder.setTitle("Warning");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onConfirmDeletePressed(); // Call the delete method on the activity end to delete the corresponding entry.
            }
        });

        return builder.create();
    }
}
