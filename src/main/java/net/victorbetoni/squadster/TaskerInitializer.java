package net.victorbetoni.squadster;

import javafx.application.Application;
import javafx.stage.Stage;
import net.victorbetoni.squadster.views.LoginFormView;

/**
 * JavaFX App
 */
public class TaskerInitializer extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        TaskTracker taskTracker = new TaskTracker();
        taskTracker.setup();
        //OrganizationView(new Organization("Escritorio", UUID.randomUUID()));
        LoginFormView view = new LoginFormView();
        view.getStage().show();
    }
}