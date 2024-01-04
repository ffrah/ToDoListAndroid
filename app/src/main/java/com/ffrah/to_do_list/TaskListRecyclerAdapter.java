package com.ffrah.to_do_list;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskListRecyclerAdapter extends RecyclerView.Adapter<TaskListViewHolder> {
    private Context context;
    private ArrayList<TaskModel> taskArrayList;
    private DatabaseHandler db;

    private int checkedColor, uncheckedColor;

    public TaskListRecyclerAdapter(Context context)
    {
        this.context = context;
        getColors();
        db = new DatabaseHandler(context);
        reloadData();
    }

    private void getColors()
    {
        uncheckedColor = ResourcesCompat.getColor(context.getResources(), R.color.black, null);
        checkedColor = ResourcesCompat.getColor(context.getResources(), R.color.gray, null);
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_list_element, parent, false);
        return new TaskListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        holder.isDoneCB.setOnCheckedChangeListener(null);

        holder.taskNoTV.setText(taskArrayList.get(position).getIdAsString());
        holder.taskNameTV.setText(taskArrayList.get(position).getTaskName());
        holder.isDoneCB.setChecked(taskArrayList.get(position).getIsDone());
        holder.taskId = taskArrayList.get(position).getId();

        if(holder.isDoneCB.isChecked())
        {
            holder.taskNameTV.setTypeface(holder.taskNameTV.getTypeface(), Typeface.ITALIC);
            holder.taskNameTV.setTextColor(checkedColor);
            holder.taskNoTV.setTextColor(checkedColor);
            holder.taskNameTV.setPaintFlags(holder.taskNameTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            holder.taskNameTV.setTypeface(holder.taskNameTV.getTypeface(), Typeface.NORMAL);
            holder.taskNameTV.setTextColor(uncheckedColor);
            holder.taskNoTV.setTextColor(uncheckedColor);
            holder.taskNameTV.setPaintFlags(holder.taskNameTV.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.isDoneCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                db.updateTask(holder.taskId, b);
                reloadData();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    public void reloadData()
    {
        taskArrayList = db.getAllLabels();
        notifyDataSetChanged();
    }
}
