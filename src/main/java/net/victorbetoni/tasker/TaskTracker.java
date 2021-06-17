package net.victorbetoni.tasker;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.victorbetoni.tasker.controller.AuthController;
import net.victorbetoni.tasker.controller.OrganizationController;
import net.victorbetoni.tasker.database.Database;
import net.victorbetoni.tasker.model.Organization;
import net.victorbetoni.tasker.model.auth.OrganizationSession;
import net.victorbetoni.tasker.model.auth.Session;
import net.victorbetoni.tasker.utils.SQLUtils;

public class TaskTracker {

    private static AuthController authController;
    private static OrganizationController organizationController;
    private static Session session;
    private static OrganizationSession organizationSession;
    private static TaskTracker instance;

    public static final String SOFTWARE_NAME = "TaskTracker";
    public static final Image ICON = new Image(TaskTracker.class.getResourceAsStream("/img/tasks.png"));


    public void setup() {
        instance = this;
        Database.initializeConnection();
        SQLUtils.executeScript("sql/Setup.sql", Database.getConnection());
        authController = new AuthController();
        organizationController = new OrganizationController();
    }

    public static AuthController getAuthController() {
        return authController;
    }

    public static OrganizationController getOrganizationController() {
        return organizationController;
    }


    public static Session getSession() {
        return session;
    }

    public static TaskTracker getInstance() {
        return instance;
    }

    public static void setSession(Session givenSession) {
        session = givenSession;
    }

    public static void setOrganizationSession(OrganizationSession givenSession) {
        organizationSession = givenSession;
    }
}
