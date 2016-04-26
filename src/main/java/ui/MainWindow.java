package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import libraries.LibrariesList;

import java.io.IOException;
import java.util.Set;

public class MainWindow extends Application {

    private Set<LibrariesList.Categories> categories;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/mainWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }
}