package net.victorbetoni.tasker;

import javafx.application.Application;
import javafx.stage.Stage;
import net.victorbetoni.tasker.model.Organization;
import net.victorbetoni.tasker.views.CreateTeamView;
import net.victorbetoni.tasker.views.LoginFormView;
import net.victorbetoni.tasker.views.OrganizationView;

import java.util.UUID;

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