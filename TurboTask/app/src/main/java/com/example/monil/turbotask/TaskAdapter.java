package com.example.monil.turbotask;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monil on 4/26/2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder>{

    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private List<String> mTaskIds = new ArrayList<>();
    private List<Task> mTasks = new ArrayList<>();

    public TaskAdapter(final Context context, DatabaseReference ref){

        mContext = context;
        mDatabaseReference = ref;

        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("recycler", "onChildAdded:" + dataSnapshot.getKey());
                Task task = dataSnapshot.getValue(Task.class);
                mTaskIds.add(dataSnapshot.getKey());
                mTasks.add(task);
                notifyItemInserted(mTasks.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("recycler", "onChildChanged:" + dataSnapshot.getKey());
                Task newTask = dataSnapshot.getValue(Task.class);
                String taskKey = dataSnapshot.getKey();
                int taskIndex = mTaskIds.indexOf(taskKey);
                if (taskIndex > -1) {
                    mTasks.set(taskIndex, newTask);
                    notifyItemChanged(taskIndex);
                } else {
                    Log.w("recycler", "onChildChanged:unknown_child:" + taskKey);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("recycler", "onChildRemoved:" + dataSnapshot.getKey());
                String taskKey = dataSnapshot.getKey();
                int taskIndex = mTaskIds.indexOf(taskKey);
                if (taskIndex > -1) {
                    mTaskIds.remove(taskIndex);
                    mTasks.remove(taskIndex);
                    notifyItemRemoved(taskIndex);
                } else {
                    Log.w("recycler", "onChildRemoved:unknown_child:" + taskKey);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("recycler", "postTasks:onCancelled", databaseError.toException());
                Toast.makeText(mContext, "Failed to load tasks.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        ref.addChildEventListener(childEventListener);
        mChildEventListener = childEventListener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.name.setText(task.getName());
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public void cleanupListener(){
        if(mChildEventListener != null){
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }
}