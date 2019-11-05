package parsers;

import model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

// Represents Task parser
public class TaskParser {
    
    // EFFECTS: iterates over every JSONObject in the JSONArray represented by the input
    // string and parses it as a task; each parsed task is added to the list of tasks.
    // Any task that cannot be parsed due to malformed JSON data is not added to the
    // list of tasks.
    // Note: input is a string representation of a JSONArray
    public List<Task> parse(String input) {
        List<Task> tasks = new ArrayList<>();
        JSONArray tasksArray = new JSONArray(input);
        for (Object obj: tasksArray) {
            JSONObject taskJson = (JSONObject) obj;
            try {
                tasks.add(setTask(taskJson));
            } catch (JSONException e) {
                continue;
            }
        }
        return tasks;
    }

    public Task setTask(JSONObject j) {
        Task task = new Task(j.getString("description"));
        if (j.get("due-date") == JSONObject.NULL) {
            task.setDueDate(null);
        } else {
            task.setDueDate(getDueDate(j.getJSONObject("due-date")));
        }
        task.setStatus(getStatus(j.getString("status")));
        task.setPriority(getPriority(j.getJSONObject("priority")));
        for (Tag t: getTags(j.getJSONArray("tags"))) {
            task.addTag(t);
        }
        return task;
    }


    public Set<Tag> getTags(JSONArray j) {
        Set<Tag> tags = new HashSet<>();
        for (int i = 0; i < j.length(); i++) {
            Object obj = j.get(i);
            JSONObject objJson = (JSONObject) obj;
            String tagName = objJson.getString("name");
            tags.add(new Tag(tagName));
        }
        return tags;
    }

    public Priority getPriority(JSONObject j) {
        Priority priority = new Priority();
        priority.setImportant(j.getBoolean("important"));
        priority.setUrgent(j.getBoolean("urgent"));
        return priority;
    }

    public Status getStatus(String string) {
        if (string.equals("TODO")) {
            return Status.TODO;
        } else if (string.equals("UP_NEXT")) {
            return Status.UP_NEXT;
        } else if (string.equals("IN_PROGRESS")) {
            return Status.IN_PROGRESS;
        } else {
            return Status.DONE;
        }
    }

    public DueDate getDueDate(JSONObject j) {
        DueDate dueDate = new DueDate();
        int year = j.getInt("year");
        int month = j.getInt("month");
        int day = j.getInt("day");
        int hour = j.getInt("hour");
        int minute = j.getInt("minute");
        Calendar dueTime = new GregorianCalendar();
        dueTime.set(Calendar.YEAR, year);
        dueTime.set(Calendar.MONTH,month);
        dueTime.set(Calendar.DAY_OF_MONTH,day);
        dueTime.set(Calendar.HOUR_OF_DAY,hour);
        dueTime.set(Calendar.MINUTE,minute);
        dueDate.setDueDate(dueTime.getTime());
        return dueDate;
    }
}
