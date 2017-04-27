package com.example.monil.turbotask;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by monil on 4/26/2017.
 */

public class TaskViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TaskViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.task_name);
    }
}
