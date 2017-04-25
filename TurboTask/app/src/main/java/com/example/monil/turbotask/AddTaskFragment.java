package com.example.monil.turbotask;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by monil on 4/19/2017.
 */

public class AddTaskFragment extends Fragment implements View.OnClickListener{

    private Button button;
    private static EditText dueDateTxt;
    private Spinner priority;
    private Spinner classTag;

    public static AddTaskFragment newInstance() {
        AddTaskFragment fragment = new AddTaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO: puyush said that all of the fragment layouts should be changed to this.
        //The tutorial I was following did it a little differently in other fragment classes
        View v = inflater.inflate(R.layout.add_task_fragment, container, false);

        // by doing it it like this we can now get elements by id on that view
        dueDateTxt = (EditText) v.findViewById(R.id.dueDate);
        dueDateTxt.setOnClickListener(this);

        priority = (Spinner) v.findViewById(R.id.priority);
        classTag = (Spinner) v.findViewById(R.id.classTag);
        ArrayAdapter<CharSequence> priority_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.priority_array, android.R.layout.simple_spinner_item);
        priority_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setPrompt("Select your priority!");

        priority.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        priority_adapter,
                        R.layout.contact_spinner_row_nothing_selected_priority,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getActivity()));

        ArrayAdapter<CharSequence> classTag_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.class_array, android.R.layout.simple_spinner_item);
        classTag_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classTag.setPrompt("Select your class!");

        classTag.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        classTag_adapter,
                        R.layout.contact_spinner_row_nothing_selected_classtag,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getActivity()));
        return  v;
    }

    @Override
    public void onClick(View v) {
        DialogFragment picker = new DatePickerFragment();
        picker.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());
            dueDateTxt.setText(new StringBuilder().append(month + 1).append("/").append(day).append("/").append(year));
        }


    }
}