package com.example.monil.turbotask;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by monil on 4/26/2017.
 */

public class TaskViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder  {
    public TextView name;
    public TextView className;
    public TextView date;
    public TextView priorityColor;

    public TaskViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.task_name);
        className = (TextView) itemView.findViewById(R.id.task_class_name);
        date = (TextView) itemView.findViewById(R.id.task_due_date);
        priorityColor = (TextView)itemView.findViewById(R.id.priorityColor);
        priorityColor.setBackgroundColor(Color.LTGRAY);

    }

    public void setPriorityColor(int val)
    {
        if(val == 1)
        {
            priorityColor.setBackgroundColor(Color.RED);
        }
        else if(val == 2)
        {
            priorityColor.setBackgroundColor(Color.YELLOW);
        }
        else if(val == 3)
        {
            priorityColor.setBackgroundColor(Color.GREEN);
        }
        else{
            priorityColor.setBackgroundColor(Color.LTGRAY);

        }
    }

    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY);

    }

    @Override
    public void onItemClear() {
       itemView.setBackgroundColor(Color.WHITE);

    }
}
