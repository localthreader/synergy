package net.victorbetoni.synergy;

import javafx.scene.image.Image;
import net.victorbetoni.synergy.controller.AuthController;
import net.victorbetoni.synergy.controller.OrganizationController;
import net.victorbetoni.synergy.database.Database;
import net.victorbetoni.synergy.model.auth.OrganizationSession;
import net.victorbetoni.synergy.model.auth.Session;
import net.victorbetoni.synergy.utils.SQLUtils;

public class Synergy {

    private static AuthController authController;
    private static OrganizationController organizationController;
    private static Session session;
    private static OrganizationSession organizationSession;
    private static Synergy instance;

    public static final String SOFTWARE_NAME = "Synergy";
    public static final Image ICON = new Image(Synergy.class.getResourceAsStream("/img/tasks.png"));


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

    public static Synergy getInstance() {
        return instance;
    }

    public static void setSession(Session givenSession) {
        session = givenSession;
    }

    public static void setOrganizationSession(OrganizationSession givenSession) {
        organizationSession = givenSession;
    }
}
