package com.example.monil.turbotask;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by monil on 4/26/2017.
 */

public class ClassViewHolder extends RecyclerView.ViewHolder {
    public TextView className;
    public ClassViewHolder(View itemView) {
        super(itemView);
        className = (TextView) itemView.findViewById(R.id.class_title);

    }
}
