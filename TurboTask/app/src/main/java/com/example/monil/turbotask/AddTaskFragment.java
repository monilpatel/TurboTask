package com.example.monil.turbotask;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by monil on 4/19/2017.
 */

public class AddTaskFragment extends Fragment implements View.OnClickListener{

    public static String TAG_ADD_TASK = "add_task_fragment";
    private EditText inputTask;
    private Button saveTask;
    private static EditText dueDateTxt;
    private Spinner priority;
    private Spinner classTag;
    private FirebaseUser user;
    private DatabaseReference dbRef;

    public static AddTaskFragment newInstance() {
        AddTaskFragment fragment = new AddTaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbRef = FirebaseDatabase.getInstance().getReference("data");
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_task_fragment, container, false);

        inputTask = (EditText)v.findViewById(R.id.inputTask);
        saveTask = (Button) v.findViewById(R.id.saveTask);
        saveTask.setOnClickListener(this);
        dueDateTxt = (EditText) v.findViewById(R.id.dueDate);
        dueDateTxt.setOnClickListener(this);

        priority = (Spinner) v.findViewById(R.id.priority);
        ArrayAdapter<CharSequence> priority_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.priority_array, android.R.layout.simple_spinner_item);
        priority_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setPrompt("Select your priority!");
        priority.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        priority_adapter,
                        R.layout.contact_spinner_row_nothing_selected_priority,
                        getActivity()));

        classTag = (Spinner) v.findViewById(R.id.classTag);
        ArrayAdapter<CharSequence> classTag_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.class_array, android.R.layout.simple_spinner_item);
        classTag_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classTag.setPrompt("Select your class!");

        classTag.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        classTag_adapter,
                        R.layout.contact_spinner_row_nothing_selected_classtag,
                        getActivity()));
        return  v;
    }

    @Override
    public void onClick(View v) {
        if(v.equals(dueDateTxt)) {
            DialogFragment picker = new DatePickerFragment();
            picker.show(getFragmentManager(), "datePicker");
        }
        else if(v.equals(saveTask)){
            Log.d("buttons", "task saved!" );
            String uid = user.getUid();
            String name = inputTask.getText().toString();
            String date = dueDateTxt.getText().toString();
            String className = "";
            String priorityNum = "";
            if(classTag.getSelectedItem() != null){
                className = classTag.getSelectedItem().toString();
            }

            if(priority.getSelectedItem() != null){
                priorityNum = priority.getSelectedItem().toString();
            }

            if(!uid.equals("") && !name.equals("") && !date.equals("") && classTag != null && priority != null){
                createNewTask(uid, name, date,className,  Integer.parseInt(priorityNum ));
                Toast.makeText(getContext(),"Task Saved!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void createNewTask(String uid, String name, String date, String className, int priority){
        String key = dbRef.child("tasks").push().getKey();
        Task task = new Task(uid, name, date, className, priority);
        Map<String, Object> taskValues = task.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/user-tasks/" + user.getUid() + "/" + key, taskValues);

        dbRef.updateChildren(childUpdates);

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