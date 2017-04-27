package com.example.monil.turbotask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monil on 4/19/2017.
 */

public class AddTaskFragment extends Fragment implements View.OnClickListener{

    public static final String TAG_ADD_TASK = "add_task_fragment";
    EditText inputTask;
    Button saveTask;


    private FirebaseUser user;
    private DatabaseReference dbRef;
//    private DatabaseReference userTasksRef;

    public static AddTaskFragment newInstance() {
        AddTaskFragment fragment = new AddTaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d("buttons", "add task fragment on create");
        dbRef = FirebaseDatabase.getInstance().getReference("data");
        user = FirebaseAuth.getInstance().getCurrentUser();
//        userTasksRef = FirebaseDatabase.getInstance().getReference("data").child("user-tasks").child(user.getUid());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("buttons", "on create view");

        View v = inflater.inflate(R.layout.add_task_fragment, container, false);
        inputTask = (EditText)v.findViewById(R.id.inputTask);
        saveTask = (Button) v.findViewById(R.id.saveTask);
        saveTask.setOnClickListener(this);
        return  v;
    }

    @Override
    public void onClick(View v) {
        Log.d("buttons", v.getId() + "");
        if(v.equals(saveTask)){
            Log.d("buttons", "task saved!" );
            createNewTask(user.getUid(), inputTask.getText().toString(), "", "", 0);
            //TODO: send back to task list
//            postTask();

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

    private void postTask(){
        final String uid = user.getUid();
        FirebaseDatabase.getInstance().getReference().child("user-task").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nameText = inputTask.getText().toString();
                        Task task = new Task(uid, nameText, "", "", 0);

                        // Push the comment, it will appear in the list
//                        userTasksRef.push().setValue(task);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
    }

}