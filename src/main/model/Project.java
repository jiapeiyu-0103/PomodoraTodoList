package model;

import model.exceptions.NullArgumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Represents a Project, a collection of zero or more Tasks
// Class Invariant: no duplicated task; order of tasks is preserved
public class Project extends Todo {
    private String description;
    private List<Todo> tasks;
    
    // MODIFIES: this
    // EFFECTS: constructs a project with the given description
    //     the constructed project shall have no tasks.
    //  throws EmptyStringException if description is null or empty
    public Project(String description) {
        super(description);
        this.description = description;
        tasks = new ArrayList<>();
    }
    
    // MODIFIES: this
    // EFFECTS: task is added to this project (if it was not already part of it)
    //   throws NullArgumentException when task is null
    public void add(Todo task) {
        if (task == null) {
            throw new NullArgumentException();
        }
        if (!contains(task) && !task.equals(this)) {
            tasks.add(task);
        }
    }
    
    // MODIFIES: this
    // EFFECTS: removes task from this project
    //   throws NullArgumentException when task is null
    public void remove(Todo task) {
//        if (task == null) {
//            throw new NullArgumentException();
//        }
        if (contains(task)) {
            tasks.remove(task);
        }
    }
    
    // EFFECTS: returns the description of this project
    public String getDescription() {
        return description;
    }

    // EFFECTS: return a non-negative integer as the Estimated Time To Complete
    // Note: Estimated time to complete is a value that is expressed in
    //       hours of work required to complete a task or project.
    @Override
    public int getEstimatedTimeToComplete() {
        int numOfHoursNeeded = 0;
        for (Todo t: tasks) {
            numOfHoursNeeded += t.getEstimatedTimeToComplete();
        }
        return numOfHoursNeeded;
    }

    // EFFECTS: returns an unmodifiable list of tasks in this project.
    @Deprecated
    public List<Task> getTasks() {
        throw new UnsupportedOperationException();
    }


    // EFFECTS: returns an integer between 0 and 100 which represents
    //     the percentage of completion (rounded down to the nearest integer).
    //     the value returned is the average of the percentage of completion of
    //     all the tasks and sub-projects in this project.
    public int getProgress() {
        int percentageOfCompletion = 0;
        if (getNumberOfTasks() == 0) {
            return 0;
        }
        for (Todo t: tasks) {
            percentageOfCompletion += t.getProgress();
        }
        return (int) Math.floor(percentageOfCompletion / getNumberOfTasks());
    }

    // EFFECTS: returns the number of tasks (and sub-projects) in this project
    public int getNumberOfTasks() {
        int numOfTasks = 0;
        for (Todo t: tasks) {
            numOfTasks += 1;
        }
        return numOfTasks;
    }

    // EFFECTS: returns true if every task (and sub-project) in this project is completed, and false otherwise
    //     If this project has no tasks (or sub-projects), return false.
    public boolean isCompleted() {
        return getNumberOfTasks() != 0 && getProgress() == 100;
    }
    
    // EFFECTS: returns true if this project contains the task
    //   throws NullArgumentException when task is null
    public boolean contains(Todo task) {
//        if (task == null) {
//            throw new NullArgumentException("Illegal argument: task is null");
//        }
        return tasks.contains(task);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        Project project = (Project) o;
        return Objects.equals(description, project.description);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}