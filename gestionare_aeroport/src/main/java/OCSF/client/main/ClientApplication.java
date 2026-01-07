package OCSF.client.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // We load the Login View first
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        // Make sure your FXML files are in 'src/resources/view' or adjust path accordingly

        Parent root = loader.load();
        primaryStage.setTitle("Autentificare Aeroport");
        primaryStage.setScene(new Scene(root,600,600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}