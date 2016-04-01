package controller;

import model.databaseManager.FileReader;
import model.databaseManager.LibrariesSaver;
import model.libraries.AvailableLibraries;
import model.libraries.BookFinder;
import model.libraries.FollowedLibraries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

class Controller{

    AvailableLibraries availableLibraries = new AvailableLibraries();
    FollowedLibraries followedLibraries = new FollowedLibraries();
    Logger logger = Logger.getLogger(Controller.class);

    @FXML public Button addButton;

    @FXML Button addLibrary;

    @FXML public Button removeButton;

    @FXML public TextField patternToAdd;

    @FXML public TextField URLadressToAdd;

    @FXML private Button deleteButton;

    @FXML private Text textURL;

    @FXML private Text textPattern;

    @FXML ListView<String> followedLibrariesVIEW;

    @FXML public ListView<String> availableLibrariesVIEW;

    @FXML  private TextArea textArea;



    @FXML
    private void clickONAvailAbledList() {
        addButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    @FXML
    private void clickOnFollowedList() {
        removeButton.setDisable(false);
    }

    @FXML
    private void showAddPanels(){

       if (!patternToAdd.isVisible()) {
           patternToAdd.setVisible(true);
           URLadressToAdd.setVisible(true);


           patternToAdd.setEditable(true);
           URLadressToAdd.setEditable(true);

           textURL.setVisible(true);
           textPattern.setVisible(true);
       }
        else {

            patternToAdd.setVisible(false);
            URLadressToAdd.setVisible(false);

           textURL.setVisible(false);
           textPattern.setVisible(false);

            patternToAdd.setEditable(false);
            URLadressToAdd.setEditable(false);
           if (!patternToAdd.getText().isEmpty()||!URLadressToAdd.getText().isEmpty()) {
               try {
                   if (!URLadressToAdd.getText().contains(".com") || !URLadressToAdd.getText().startsWith("www")) throw new Exception();
                   new LibrariesSaver(URLadressToAdd.getText(), patternToAdd.getText(), "src/main/resources/LIBS");
                   availableLibraries.stringListForGUI= new ArrayList<String>();
                   availableLibraries.listOfLibraries.add(new BookFinder(URLadressToAdd.getText(),patternToAdd.getText()));
                   availableLibraries.stringExtractor();
               } catch (Exception e) {
                   Component frame=new JFrame();
                   JOptionPane.showMessageDialog(frame, "Wrong adress");

                   logger.info("wrong adress inputed");
               }
               ObservableList<String> data = FXCollections.observableArrayList(availableLibraries.stringListForGUI);
               availableLibrariesVIEW.setItems(data);
               patternToAdd.setText("");
               URLadressToAdd.setText("");
           }
       }


    }

    @FXML
    private void initialize() {
        ObservableList<String> data = FXCollections.observableArrayList(availableLibraries.stringListForGUI);
        availableLibrariesVIEW.setItems(data);
        patternToAdd.setVisible(false);
        URLadressToAdd.setVisible(false);

    }

    @FXML
    private void addClicked()  {
        BookFinder selectedLibrary = availableLibraries.removeAction(availableLibrariesVIEW.getSelectionModel().getSelectedItem());

        followedLibraries.addAction(selectedLibrary);


        ObservableList<String> data = FXCollections.observableArrayList(followedLibraries.stringListForGUI);
        ObservableList<String> data2 = FXCollections.observableArrayList(availableLibraries.stringListForGUI);

        followedLibrariesVIEW.setItems(data);
        availableLibrariesVIEW.setItems(data2);


        try {
            textArea.setText(new FileReader("src/main/resources/DB", followedLibraries.stringListForGUI).getContentNeeded());
        } catch (IOException e) {
            logger.fatal("coudlnt find DB",e);
        }


        addButton.setDisable(true);
        deleteButton.setDisable(true);
    }
    @FXML
    private void deleteAction(){
        availableLibraries.deleteLibrary(availableLibrariesVIEW.getSelectionModel().getSelectedItem());

        ObservableList<String> data = FXCollections.observableArrayList(availableLibraries.stringListForGUI);
        availableLibrariesVIEW.setItems(data);
        deleteButton.setDisable(true);

    }

    @FXML
    private void removeClicked()  {

        BookFinder selectedLibrary = followedLibraries.removeAction(followedLibrariesVIEW.getSelectionModel().getSelectedItem());

        availableLibraries.addAction(selectedLibrary);


        ObservableList<String> data = FXCollections.observableArrayList(availableLibraries.stringListForGUI);
        ObservableList<String> data2 = FXCollections.observableArrayList(followedLibraries.stringListForGUI);

        availableLibrariesVIEW.setItems(data);
        followedLibrariesVIEW.setItems(data2);

        try {
            textArea.setText(new FileReader("src/main/resources/DB", followedLibraries.stringListForGUI).getContentNeeded());
        } catch (IOException e) {
            logger.error(e);
        }

        removeButton.setDisable(true);
    }


    @FXML
    private void URLadditionAreaOnClick(){
        textURL.setVisible(false);
    }

    @FXML
    private void patternAdditionAreaOnClick(){
        textPattern.setVisible(false);
    }

}





