package controller;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.Task;
import pomodoro.CountDownTimer;
import ui.EditTask;
import ui.ListView;
import utility.JsonFileIO;
import utility.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static ui.PomoTodoApp.setScene;

// Controller class for Todobar UI
public class TodobarController implements Initializable {
    private static final String todoOptionsPopUpFXML = "resources/fxml/TodoOptionsPopUp.fxml";
    private static final String todoActionsPopUpFXML = "resources/fxml/TodoActionsPopUp.fxml";
    private File todooptionPopUpFile = new File(todoOptionsPopUpFXML);
    private File todoactionPopUpFile = new File(todoActionsPopUpFXML);


    @FXML
    private Label descriptionLabel;
    @FXML
    private JFXHamburger todoActionsPopUpBurger;
    @FXML
    private StackPane todoActionsPopUpContainer;
    @FXML
    private JFXRippler todoOptionsPopUpRippler;
    @FXML
    private StackPane todoOptionsPopUpBurger;

    private JFXPopup todoActionPopUp;
    private JFXPopup todoOptionPopUp;
    private Task task;
    
    // REQUIRES: task != null
    // MODIFIES: this
    // EFFECTS: sets the task in this Todobar
    //          updates the Todobar UI label to task's description
    public void setTask(Task task) {
        this.task = task;
        descriptionLabel.setText(task.getDescription());
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTodoActionPopUp();
        loadTodoActionPopUpActionListener();
        loadTodoOptionPopUp();
        loadTodoOptionPopUpActionListener();
    }

    private void loadTodoActionPopUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(todoactionPopUpFile.toURI().toURL());
            fxmlLoader.setController(new TodoActionPopUpController());
            todoActionPopUp = new JFXPopup(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTodoOptionPopUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(todooptionPopUpFile.toURI().toURL());
            fxmlLoader.setController(new TodoOptionPopUpController());
            todoOptionPopUp = new JFXPopup(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTodoOptionPopUpActionListener() {
        todoOptionsPopUpBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                todoOptionPopUp.show(todoOptionsPopUpBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.RIGHT,
                        -12,
                        15);
            }
        });
    }

    private void loadTodoActionPopUpActionListener() {
        todoActionsPopUpBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                todoActionPopUp.show(todoActionsPopUpBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.LEFT,
                        12,
                        15);
            }
        });
    }

    class TodoOptionPopUpController {
        @FXML
        private JFXListView<?> optionPopUpList;

        @FXML
        private void submit() {
            int selectedIndex = optionPopUpList.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    Logger.log("TodoOptionBarPopUpController", "Edit bar is not implemented");
                    startEditTaskDemo();
                    break;
                case 1:
                    Logger.log("TodoOptionBarPopUpController", "delete bar is not implemented");
                    List<Task> getListOfTasks = JsonFileIO.read();
                    getListOfTasks.remove(task);
                    JsonFileIO.write(getListOfTasks);
                    setScene(new ListView(getListOfTasks));
                    break;
                default:
                    Logger.log("ToolbarOptionsPopUpController", "No action is implemented for the selected option");
            }
            todoOptionPopUp.hide();
        }

        private void startEditTaskDemo() {
            setScene(new EditTask(task));
        }
    }

    class TodoActionPopUpController {
        @FXML
        private JFXListView<?> actionPopUpList;

        @FXML
        private void submit() {
            int selectedIndex = actionPopUpList.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    Logger.log("TodoActionPopUpController", "To do bar is not implemented");
                    break;
                case 1:
                    Logger.log("TodoActionPopUpController", "Up next bar is not implemented");
                    break;
                case 2:
                    Logger.log("TodoActionPopUpController", "In progress bar is not implemented");
                    break;
                case 3:
                    Logger.log("TodoActionPopUpController", "Done bar is not implemented");
                    break;
//                case 4:
//                    Logger.log("TodoActionPopUpController", "Pomodoro bar is not implemented");
//                    break;
                default:
                   // Logger.log("TodoActionPopUpController", "No action is implemented for the selected option");
//                    CountDownTimer c = new CountDownTimer();
                    CountDownTimer.main(new String[] {"123"});
            }
            todoActionPopUp.hide();
        }
    }
}
