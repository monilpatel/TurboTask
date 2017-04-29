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

public class ClassAdapter extends RecyclerView.Adapter<ClassViewHolder>{

    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private List<String> mClassIds = new ArrayList<>();
    private List<ClassType> mClasses = new ArrayList<>();

    public ClassAdapter(final Context context, DatabaseReference ref){

        mContext = context;
        mDatabaseReference = ref;

        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("recycler", "onChildAdded:" + dataSnapshot.getKey());
                ClassType classTemp = dataSnapshot.getValue(ClassType.class);
                mClassIds.add(dataSnapshot.getKey());
                mClasses.add(classTemp);
                notifyItemInserted(mClasses.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("recycler", "onChildChanged:" + dataSnapshot.getKey());
                ClassType newClass = dataSnapshot.getValue(ClassType.class);
                String classKey = dataSnapshot.getKey();
                int classIndex = mClassIds.indexOf(classKey);
                if (classIndex > -1) {
                    mClasses.set(classIndex, newClass);
                    notifyItemChanged(classIndex);
                } else {
                    Log.w("recycler", "onChildChanged:unknown_child:" + classKey);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("recycler", "onChildRemoved:" + dataSnapshot.getKey());
                String classKey = dataSnapshot.getKey();
                int classIndex = mClassIds.indexOf(classKey);
                if (classIndex > -1) {
                    mClassIds.remove(classIndex);
                    mClasses.remove(classIndex);
                    notifyItemRemoved(classIndex);
                } else {
                    Log.w("recycler", "onChildRemoved:unknown_child:" + classKey);
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
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        ClassType classTemp = mClasses.get(position);
        holder.className.setText(classTemp.getClassName());
    }

    @Override
    public int getItemCount() {
        return mClasses.size();
    }

    public void cleanupListener(){
        if(mChildEventListener != null){
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }
}
