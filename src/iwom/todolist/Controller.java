package iwom.todolist;
import iwom.todolist.datamodel.ToDoItem;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<ToDoItem> toDoItemList;

    @FXML
    private ListView<ToDoItem> toDoListView;

    @FXML
    private TextArea toDoItemView;

    public void initialize() {
        ToDoItem item1 = new ToDoItem("Mail John", "Tell him to do so", LocalDate.of(2018, 4, 15));
        ToDoItem item2 = new ToDoItem("Doctors", "Pneumonia", LocalDate.of(2018, 4, 17));
        ToDoItem item3 = new ToDoItem("Paperwork", "Call him", LocalDate.of(2018, 4, 16));
        ToDoItem item4 = new ToDoItem("Johny", "Finished", LocalDate.of(2018, 4, 10));

        toDoItemList = new ArrayList<ToDoItem>();
        toDoItemList.add(item1);
        toDoItemList.add(item2);
        toDoItemList.add(item3);
        toDoItemList.add(item4);
        toDoListView.getItems().setAll(toDoItemList);
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    @FXML
    public void handleClickListView() {
        ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
        StringBuilder sb = new StringBuilder(item.getDetails());
        sb.append("\n\n\n\n");
        sb.append("Due: ");
        sb.append(item.getDeadline().toString());
        toDoItemView.setText(sb.toString());
    }
 }
