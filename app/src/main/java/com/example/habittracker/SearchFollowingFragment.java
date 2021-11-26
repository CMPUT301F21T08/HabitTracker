package com.example.habittracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchFollowingFragment extends DialogFragment {

    private EditText UserEmail;

    private OnFragmentInteractionListener listener;
    private FirebaseAuth authentication;
    private String email;
    /**
     * Default Constructor
     */
    public SearchFollowingFragment(){super();}
    public interface OnFragmentInteractionListener{
        void onConfirmPressed(String email_toFollow);
        void errorMessage(int i);
    }


    /**
     * The "onAttach" method for DialogFragment, also creates a listener interface
     * @param context
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else{
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }


    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    private boolean isEmailValid(CharSequence  email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
//    public static boolean isEmailValid(String email) {
//        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
//        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_search_following, null);

        // capture the views
        UserEmail = view.findViewById(R.id.Search_EditText);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(view);
        builder.setTitle("Add user by email");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Send Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //check email already exist or not.
                email = UserEmail.getText().toString();
                authentication = FirebaseAuth.getInstance();
                if (!isEmailValid(email)) {
                    listener.errorMessage(0);
                }else{
                    authentication.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                            if (isNewUser) {
                                listener.errorMessage(1);
                            }else{
                                listener.onConfirmPressed(email);
                            }
                        }
                    });
                }
            }
        });
        return builder.create();
    }
}
