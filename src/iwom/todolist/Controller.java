package iwom.todolist;
import iwom.todolist.datamodel.ToDoData;
import iwom.todolist.datamodel.ToDoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @FXML
    private ContextMenu listContextMenu;

    public void initialize() {
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });
        listContextMenu.getItems().addAll(deleteMenuItem);
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
        toDoListView.setItems(ToDoData.getInstance().getToDoItems());
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoListView.getSelectionModel().selectFirst();
        toDoListView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> param) {
                ListCell<ToDoItem> cell = new ListCell<ToDoItem>() {
                    @Override
                    protected void updateItem(ToDoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty) {
                            this.setText(null);
                        } else {
                            this.setText(item.getShortDescription());
                            if(item.getDeadline().isBefore(LocalDate.now().plusDays(1))) {
                                this.setTextFill(Color.BROWN);
                            }
                            if(item.getDeadline().isBefore(LocalDate.now())) {
                                this.setTextFill(Color.RED);
                            }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if(isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        }
                );
                return cell;
            }
        });
    }

    private void deleteItem(ToDoItem item) {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure");
        deleteAlert.setTitle("Delete Item");
        deleteAlert.setHeaderText("Deleting: " + item.getShortDescription());
        deleteAlert.setContentText("Are you sure that you want to delete this item? Press Ok or Cancel");
        Optional<ButtonType> result = deleteAlert.showAndWait();
        if(result.isPresent() && (result.get() == ButtonType.OK)) {
            ToDoData.getInstance().deleteToDoItem(item);
        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        ToDoItem selectedItem = toDoListView.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            if(event.getCode().equals(KeyCode.DELETE)) {
                deleteItem(selectedItem);
            }
        }
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
        dialog.setTitle("Create new Todo");
        dialog.setHeaderText("Use this dialog to create a new todo item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            TodoItemDialog controller = fxmlLoader.getController();
            ToDoItem newItem = controller.processResults();
            toDoListView.getSelectionModel().select(newItem);
            System.out.println("Ok pressed");
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            System.out.println("Cancel pressed");
        }
    }
 }
