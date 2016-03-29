package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by mmaczka on 18.03.16.
 */
public class UserGUI extends Application{
    @Override
    public void start(Stage primaryStage) {
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(UserGUI.class);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        } catch (Exception e) {
            logger.info("dupa");
        }

        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();

    }





    public static void main(String[] args)  {
        launch(args);
    }



}
