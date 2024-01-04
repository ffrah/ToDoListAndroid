package com.ffrah.to_do_list;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskListViewHolder extends RecyclerView.ViewHolder {
    TextView taskNoTV, taskNameTV;
    CheckBox isDoneCB;
    View bottomLine;

    int taskId;
    public TaskListViewHolder(@NonNull View itemView) {
        super(itemView);
        taskNoTV = itemView.findViewById(R.id.taskNoTextView);
        taskNameTV = itemView.findViewById(R.id.taskNameEditText);
        isDoneCB = itemView.findViewById(R.id.isDoneCheckbox);
        bottomLine = itemView.findViewById(R.id.bottomLine);
    }
}
