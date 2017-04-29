package com.example.monil.turbotask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monil on 4/19/2017.
 */

public class ClassesFragment extends Fragment implements View.OnClickListener {
    public static final String TAG_CLASSES = "classes_fragment";
    private EditText className;
    private Button saveClass;
    private DatabaseReference mTasksReference;
    private FirebaseUser mUser;
    private RecyclerView mRecycler;
    private TaskAdapter mTaskAdapter;

    public static ClassesFragment newInstance() {
        ClassesFragment fragment = new ClassesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser != null){
            mTasksReference = FirebaseDatabase.getInstance().getReference().child("data").child("classes").child(mUser.getUid());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.classes_fragment, container, false);
        mRecycler = (RecyclerView) v.findViewById(R.id.recycler_classes);
        className = (EditText) v.findViewById(R.id.inputClass);
        saveClass = (Button) v.findViewById(R.id.saveClass);
        //v.setOnClickListener(this);
        saveClass.setOnClickListener(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        if(mTasksReference != null){
            mTaskAdapter = new TaskAdapter(getContext(), mTasksReference);
            mRecycler.setAdapter(mTaskAdapter);
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.equals(saveClass))
        {
            Log.d("buttons", "task saved!" );
            if(!className.getText().toString().equals(""))
            {
                createNewClass(className.getText().toString());
                Toast.makeText(getContext(),"Class Saved!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createNewClass(String name){
        String key = mTasksReference.child("classes").push().getKey();
        ClassType newClass = new ClassType(name);
        Map<String, Object> classValues = newClass.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/classes/" + mUser.getUid() + "/" + key, classValues);

        mTasksReference.updateChildren(childUpdates);

    }
}