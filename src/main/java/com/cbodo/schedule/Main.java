package com.cbodo.schedule;

import com.cbodo.schedule.model.User;
import com.cbodo.schedule.util.SceneHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.ResourceBundle;

public class Main extends Application {
    public static User CURRENT_USER;

    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle("com.cbodo.schedule.util.rb");
        if(SceneHelper.displayLogin(rb)) {
            Stage main = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cbodo/schedule/view/main-view.fxml"), rb);
            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            getClass().getResource("stylesheet.css")
                    ).toExternalForm()
            );

            main.setTitle(rb.getString("app.title"));
            main.setScene(scene);
            main.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}