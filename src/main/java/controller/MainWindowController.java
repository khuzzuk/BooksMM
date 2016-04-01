package controller;

import channels.Task;
import channels.TaskChannel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import messaging.subscribers.WriteToDBUISubscriber;
import model.libraries.LibrariesList;
import model.libraries.interpreters.InterpreterFactory;

import java.net.URL;
import java.util.*;

public class MainWindowController implements Initializable {
    private Collection<LibrariesList.Categories> categories;
    @FXML
    ListView<String> availableLibraries;
    private String selectedLeftItem;
    @FXML
    ListView<String> chosenLibraries;
    private String selectedRightItem;
    @FXML
    ComboBox categoriesElement;
    @FXML
    Button toRightButton;
    @FXML
    Button toLeftButton;
    @FXML
    Button addButton;
    @FXML
    TextArea queryResults;

    private LibrariesList.Categories category;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        new WriteToDBUISubscriber(this);
        populateComboBox();
        populateAvailableLibraries();
    }

    private void populateAvailableLibraries() {
        category = (LibrariesList.Categories) categoriesElement.getSelectionModel().getSelectedItem();
        ObservableList<String> rightList = FXCollections.observableArrayList(LibrariesList.getInstance().getLibrariesNames(category));
        availableLibraries.getItems().clear();
        availableLibraries.getItems().addAll(rightList);
    }

    private void populateComboBox() {
        setCategories();
        ObservableList<LibrariesList.Categories> categoriesList = FXCollections.observableArrayList(categories);
        categoriesElement.getItems().clear();
        categoriesElement.getItems().addAll(categoriesList);
        categoriesElement.getSelectionModel().select(0);
        categoriesElement.valueProperty().addListener(c -> populateAvailableLibraries());
    }

    private void setCategories() {
        categories = new ArrayList<>();
        categories.add(LibrariesList.Categories.NO_CATEGORY);
        categories.add(LibrariesList.Categories.ROMANCE);
        categories.add(LibrariesList.Categories.HISTORY);
        categories.add(LibrariesList.Categories.IT);
    }
    @FXML
    private void chooseFromLeftList(){
        selectedLeftItem = availableLibraries.getSelectionModel().getSelectedItem();
        if (selectedLeftItem !=null){
            toRightButton.setDisable(false);
        }
        else toRightButton.setDisable(true);
    }
    @FXML
    private void chooseFromRightList(){
        selectedRightItem = chosenLibraries.getSelectionModel().getSelectedItem();
        if (selectedRightItem !=null){
            toLeftButton.setDisable(false);
        }
        else toRightButton.setDisable(true);
    }
    @FXML
    private void moveToRightAction(){
        chosenLibraries.getItems().addAll(selectedLeftItem);
        availableLibraries.getItems().remove(selectedLeftItem);
        toRightButton.setDisable(true);
        populateTextArea("loading...");
        String address = LibrariesList.getInstance().getLibraryAddress(selectedLeftItem,
                (LibrariesList.Categories) categoriesElement.getSelectionModel().getSelectedItem());
        TaskChannel.putTask(new Task(InterpreterFactory.getInterpreter(address)));
    }
    @FXML
    private void moveToLeftAction(){
        availableLibraries.getItems().addAll(selectedRightItem);
        chosenLibraries.getItems().remove(selectedRightItem);
        toLeftButton.setDisable(true);
        populateTextArea("");
    }

    public synchronized void populateTextArea(String s) {
        /* TODO implementation of multiple threads accessing populateTextArea*/
        queryResults.setText(s);
    }
}
