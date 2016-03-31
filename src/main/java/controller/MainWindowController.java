package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import model.libraries.LibrariesList;

import java.net.URL;
import java.util.*;

public class MainWindowController implements Initializable {
    private Collection<LibrariesList.Categories> categories;
    @FXML
    ListView<String> availableLibraries;
    private String selectedItem;
    @FXML
    ListView<String> chosenLibraries;
    @FXML
    ComboBox categoriesElement;
    @FXML
    Button toRightButton;
    @FXML
    Button toLeftButton;
    @FXML
    Button addButton;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        populateComboBox();
    }

    private void populateComboBox() {
        setCategories();
        ObservableList<LibrariesList.Categories> categoriesList = FXCollections.observableArrayList(categories);
        categoriesElement.getItems().clear();
        categoriesElement.getItems().addAll(categoriesList);
        categoriesElement.getSelectionModel().select(0);
    }

    private void setCategories() {
        categories = new ArrayList<>();
        categories.add(LibrariesList.Categories.NO_CATEGORY);
        categories.add(LibrariesList.Categories.ROMANCE);
        categories.add(LibrariesList.Categories.HISTORY);
        categories.add(LibrariesList.Categories.IT);
    }
    @FXML
    private void chooseFromList(){
        selectedItem = availableLibraries.getSelectionModel().getSelectedItem();
        if (selectedItem !=null){
            toRightButton.setDisable(false);
        }
        else toRightButton.setDisable(true);
    }
    @FXML
    private void moveToRightAction(){
        chosenLibraries.getItems().addAll(selectedItem);
        availableLibraries.getItems().remove(selectedItem);
    }
}
