package com.ffrah.to_do_list;

public class TaskModel {
    private int id;
    private String taskName;
    private boolean isDone;

    public int getId()
    {
        return this.id;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    public static int boolToInt(boolean b)
    {
        if(b)
            return 1;
        else
            return 0;
    }

    public static boolean intToBool(int i)
    {
        if(i == 0)
            return false;
        else
            return true;
    }

    TaskModel(int id, String task, int isDone)
    {
        this.id = id;
        this.taskName = task;
        this.isDone = intToBool(isDone);
    }

    TaskModel(int id, String task, boolean isDone)
    {
        this.id = id;
        this.taskName = task;
        this.isDone = isDone;
    }

    public String getAsString()
    {
        String string = "id: " + id + "; taskName: " + taskName + "; isDone: " + isDone;
        return string;
    }

    public String getIdAsString()
    {
        String string = id + ". ";
        return string;
    }
}
