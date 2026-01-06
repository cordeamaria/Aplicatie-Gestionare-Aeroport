package util;

import OCSF.client.controller.UserAware;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

public class SceneSwitcher {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneSwitcher.class.getResource("/view/" + fxmlFile)
            );


            Scene scene = new Scene(loader.load(),1000,600);
            Object controller = loader.getController();

            if (controller instanceof UserAware userAware) {
                userAware.setLoggedUser(user);
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
