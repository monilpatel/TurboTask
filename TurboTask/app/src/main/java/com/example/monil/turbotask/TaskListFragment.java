package com.example.monil.turbotask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by monil on 4/19/2017.
 */

public class TaskListFragment extends Fragment {
    public static final String TAG_TASK_LIST = "task_list_fragment";
    private DatabaseReference mTasksReference;
    private FirebaseUser mUser;
    private RecyclerView mRecycler;
    private TaskAdapter mTaskAdapter;

    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser != null){
            mTasksReference = FirebaseDatabase.getInstance().getReference().child("data").child("user-tasks").child(mUser.getUid());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);
        mRecycler = (RecyclerView) v.findViewById(R.id.recycler_tasks);
        v.setOnClickListener(new TaskListListener());
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        if(mTasksReference != null){
            mTaskAdapter = new TaskAdapter(getContext(), mTasksReference);
            mRecycler.setAdapter(mTaskAdapter);
        }

        return v;
    }


    public class TaskListListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPosition = mRecycler.indexOfChild(v);
            Log.d("recycler", "Clicked and Position is : " + String.valueOf(itemPosition));
        }
    }
}