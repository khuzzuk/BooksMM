package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import model.libraries.LibrariesList;

import java.net.URL;
import java.util.*;

public class MainWindowController implements Initializable {
    private Collection<LibrariesList.Categories> categories;
    @FXML
    ComboBox categoriesElement;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        setCategories();
        ObservableList<LibrariesList.Categories> categoriesList = FXCollections.observableArrayList(categories);
        categoriesElement.getItems().clear();
        categoriesElement.getItems().addAll(categoriesList);
    }

    private void setCategories() {
        categories = new ArrayList<>();
        categories.add(LibrariesList.Categories.NO_CATEGORY);
        categories.add(LibrariesList.Categories.ROMANCE);
        categories.add(LibrariesList.Categories.HISTORY);
        categories.add(LibrariesList.Categories.IT);
    }
}
