package com.example.monil.turbotask;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by monil on 4/26/2017.
 */

public class TaskViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView className;
    public TextView date;
    public TaskViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.task_name);
        className = (TextView) itemView.findViewById(R.id.task_class_name);
        date = (TextView) itemView.findViewById(R.id.task_due_date);

    }
}
