package net.victorbetoni.synergy;

import javafx.application.Application;
import javafx.stage.Stage;
import net.victorbetoni.synergy.views.LoginFormView;

/**
 * JavaFX App
 */
public class SynergyInitializer extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Synergy synergy = new Synergy();
        synergy.setup();
        //OrganizationView(new Organization("Escritorio", UUID.randomUUID()));
        LoginFormView view = new LoginFormView();
        view.getStage().show();
    }
}