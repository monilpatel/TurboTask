package com.example.monil.turbotask;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by monil on 4/23/2017.
 */

public class TaskFragment extends Fragment {

    public static final String TAG_TASK_FRAGMENT = "task_fragment_tag";
    private String mActiveFragmentTag;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public String getActiveTag(){
        return mActiveFragmentTag;
    }

    public void setActiveTag(String tag){
        mActiveFragmentTag = tag;
    }

}
