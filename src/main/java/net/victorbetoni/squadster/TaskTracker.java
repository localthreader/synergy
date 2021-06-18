package net.victorbetoni.squadster;

import javafx.scene.image.Image;
import net.victorbetoni.squadster.controller.AuthController;
import net.victorbetoni.squadster.controller.OrganizationController;
import net.victorbetoni.squadster.database.Database;
import net.victorbetoni.squadster.model.auth.OrganizationSession;
import net.victorbetoni.squadster.model.auth.Session;
import net.victorbetoni.squadster.utils.SQLUtils;

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
