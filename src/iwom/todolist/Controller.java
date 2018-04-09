package iwom.todolist;
import iwom.todolist.datamodel.ToDoData;
import iwom.todolist.datamodel.ToDoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller {
    private List<ToDoItem> toDoItemList;

    @FXML
    private ListView<ToDoItem> toDoListView;

    @FXML
    private TextArea toDoItemView;

    @FXML
    private Label deadlineLabel;

    @FXML
    private BorderPane mainBorderPane;

    public void initialize() {
        toDoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observable, ToDoItem oldValue, ToDoItem newValue) {
                if(newValue != null) {
                    ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
                    toDoItemView.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    deadlineLabel.setText("Due: " + df.format(item.getDeadline()));
                }
            }
        });
        toDoListView.getItems().setAll(ToDoData.getInstance().getToDoItems());
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoListView.getSelectionModel().selectFirst();
    }
    @FXML
    public void handleClickListView() {
        ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
        toDoItemView.setText(item.getDetails());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        deadlineLabel.setText("Due: " + df.format(item.getDeadline()));
    }

    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        try {
            Parent root = FXMLLoader.load(getClass().getResource("todoItemDialog.fxml"));
            dialog.getDialogPane().setContent(root);

        } catch (IOException e) {
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Ok pressed");
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            System.out.println("Cancel pressed");
        }
    }
 }
