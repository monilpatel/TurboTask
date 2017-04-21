package com.example.monil.turbotask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by monil on 4/19/2017.
 */

public class AddTaskFragment extends Fragment implements View.OnClickListener{

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

        //TODO: puyush said that all of the fragment layounts should be changed to this.
        //The tutorial I was following did it a little differently in other fragment classes
        View v = inflater.inflate(R.layout.add_task_fragment, container, false);

        // by doing it it like this we can now get elements by id on that view
        // button = (Button) v.findViewById(id)
        return  v;
    }

    @Override
    public void onClick(View v) {
    }
}