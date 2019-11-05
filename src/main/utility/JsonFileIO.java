package utility;

import model.Task;
import org.json.JSONArray;
import org.json.JSONWriter;
import parsers.TaskParser;
import persistence.Jsonifier;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// File input/output operations
public class JsonFileIO {
    public static final File jsonDataFile = new File("./resources/json/tasks.json");
    
    // EFFECTS: attempts to read jsonDataFile and parse it
    //           returns a list of tasks from the content of jsonDataFile
    public static List<Task> read() {
        List<Task> tasks = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(jsonDataFile);
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                String stringLine = scanner.nextLine();
                stringBuilder.append(stringLine);
            }
            scanner.close();
            String finalString = stringBuilder.toString();
            TaskParser parser = new TaskParser();
            tasks = parser.parse(finalString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tasks;
    }
    
    // EFFECTS: saves the tasks to jsonDataFile
    public static void write(List<Task> tasks) {
        JSONArray tasksJson = Jsonifier.taskListToJson(tasks);
        try {
            FileWriter writer = new FileWriter(jsonDataFile);
            writer.write(tasksJson.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
