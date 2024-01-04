package com.ffrah.to_do_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    FloatingActionButton addNewTaskButton;

    View noTasksView;

    RecyclerView recyclerView;
    TaskListRecyclerAdapter taskAdapter;
    LinearLayoutManager layoutManager;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noTasksView = findViewById(R.id.noTasksView);
        noTasksView.setVisibility(View.GONE);

        addNewTaskButton = (FloatingActionButton) findViewById(R.id.addNewTaskFButton);
        recyclerView = (RecyclerView) findViewById(R.id.taskRecyclerView);

        db = new DatabaseHandler(this);
        loadTaskList();

        addNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newTaskDialog();
            }
        });
    }

    private void loadTaskList()
    {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        taskAdapter = new TaskListRecyclerAdapter(this);
        recyclerView.setAdapter(taskAdapter);
        checkIfEmpty();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.addANewTask){
            newTaskDialog();
        }
        if (id == R.id.deleteAllCompletedTasks){
            deleteCompletedTasksDialog();
        }
        if (id == R.id.deleteAllTasks){
            deleteAllTasksDialog();
        }
        /*if (id == R.id.changeOrder){  UNIMPLEMENTED
            //deleteAllTasksDialog();
            Toast.makeText(getApplicationContext(), "List order has changed", Toast.LENGTH_SHORT).show();
        }*/
        if (id == R.id.exit){
            finish();
        }
        return true;
    }

    public void newTaskDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Add a new task");
        final View alertDialogLayout = this.getLayoutInflater().inflate(R.layout.add_new_task_layout, null);
        builder.setView(alertDialogLayout);

        builder.setPositiveButton("OK", (dialog, which) ->
        {
            EditText editText = alertDialogLayout.findViewById(R.id.addNewTaskName);
            String taskName = editText.getText().toString();
            if (taskName.trim().length() > 0) {
                db.insertTask(taskName);
                taskAdapter.reloadData();
                checkIfEmpty();
            } else {
                Toast.makeText(getApplicationContext(), "Please add some text for a task",
                        Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void deleteCompletedTasksDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete all tasks checked as done ?");
        builder.setTitle("Delete completed tasks");
        builder.setCancelable(true);

        builder.setPositiveButton("YES", (DialogInterface.OnClickListener) (dialog, which) -> {
            db.deleteAllCompletedTasks();
            taskAdapter.reloadData();
            checkIfEmpty();
        });

        builder.setNegativeButton("CANCEL", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void deleteAllTasksDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete all tasks?");
        builder.setTitle("Delete all tasks");
        builder.setCancelable(true);

        builder.setPositiveButton("YES", (DialogInterface.OnClickListener) (dialog, which) -> {
            db.recreateDatabase();
            taskAdapter.reloadData();
            checkIfEmpty();
        });

        builder.setNegativeButton("CANCEL", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void checkIfEmpty()
    {
        if(db.isEmpty())
        {
            recyclerView.setVisibility(View.VISIBLE);
            noTasksView.setVisibility(View.GONE);
        }
        else
        {
            recyclerView.setVisibility(View.GONE);
            noTasksView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}