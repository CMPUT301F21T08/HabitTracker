package com.example.habittracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.habittracker.listener.EventEditConfirmListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HabitEventEditTest {


    ProgressDialog editEventProgressDialog;
    EditText comment_editText;
    EditText location_editText;
    ImageView photo_imageView;
    Context context;
    Activity activity;

    Button confirmBtn;

    int eventIndexInList;
    HabitEvent testEvent;

    @BeforeEach
    public void setUp() {

        activity = new HabitEventEditActivity();
        context = activity.getApplicationContext();
        editEventProgressDialog = new ProgressDialog(activity);
        comment_editText = activity.findViewById(R.id.habitEvent_comment_editText);
        location_editText = activity.findViewById(R.id.habitEvent_enterLocation_editText);
        photo_imageView = activity.findViewById(R.id.habitEvent_photo_imageView);

        confirmBtn = activity.findViewById(R.id.habitEvent_confirm_button);

        testEvent = new HabitEvent("Run", "I've finished running", "Edmonton", "1234-5678", "1111-2222");

    }

    @Test
    public void testConfirmOnClickListener() {
        EventEditConfirmListener listener = new EventEditConfirmListener(context, activity, editEventProgressDialog, comment_editText, location_editText, eventIndexInList, testEvent, photo_imageView, "1111-2222");
        listener.onClick(confirmBtn);
    }
}
