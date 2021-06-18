package net.victorbetoni.squadster.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import net.victorbetoni.squadster.TaskTracker;
import net.victorbetoni.squadster.controller.AuthController;
import net.victorbetoni.squadster.database.Database;
import net.victorbetoni.squadster.exception.AuthenticationException;
import net.victorbetoni.squadster.utils.HashUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormView implements View {

    @Override
    public Stage getStage() {
        var stage = new Stage();
        stage.setTitle(TaskTracker.SOFTWARE_NAME + " - Login");
        stage.getIcons().add(TaskTracker.ICON);
        var loginGrid = new GridPane();

        Font UBUNTU = Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu-Medium.ttf"), 12);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(10);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);

        loginGrid.getColumnConstraints().addAll(col1, col2);
        loginGrid.setAlignment(Pos.TOP_CENTER);
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        loginGrid.setPadding(new Insets(25, 25, 25, 25));

        Text title = new Text("Login:");
        title.setId("text");
        title.setFont(Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu-Medium.ttf"), 25));
        loginGrid.add(title, 0, 0, 2, 1);

        Label email = new Label("E-mail:");
        email.setFont(UBUNTU);
        loginGrid.add(email, 0, 1);
        TextField emailField = new TextField();
        emailField.setId("text-field");
        emailField.setMinWidth(50);
        loginGrid.add(emailField, 1, 1);

        Label pw = new Label("Senha:");
        loginGrid.add(pw, 0, 2);
        PasswordField passwordInput = new PasswordField();
        loginGrid.add(passwordInput, 1, 2);

        Text errorText = new Text();
        loginGrid.add(errorText, 1, 3);

        Button login = new Button("Entrar");
        login.setMinWidth(20);
        login.setAlignment(Pos.CENTER);
        login.setOnAction((event -> {
            try {
                AuthController controller = TaskTracker.getAuthController();
                if(!controller.isEmailRegistered(emailField.getText().trim()))
                    throw new AuthenticationException("Nenhuma conta com o email " + emailField.getText() + " foi encontrada.");

                String pwHash = HashUtils.sha256(passwordInput.getText().trim());

                String databasePw = null;
                String username = null;
                String nome = null;

                try{
                    PreparedStatement st = Database.getConnection().prepareStatement(
                    "SELECT * FROM Users WHERE Email LIKE ?"
                    );
                    st.setString(1, emailField.getText().trim());
                    ResultSet rs = st.executeQuery();
                    if(rs.next()) {
                        databasePw = rs.getString("Password");
                        username = rs.getString("Username");
                        nome = rs.getString("Name");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                if(!databasePw.equals(pwHash)) {
                    throw new AuthenticationException("Senha incorreta. Tente novamente.");
                }

                TaskTracker.setSession(controller.openSession(username, nome, emailField.getText().trim()));
                stage.close();
                UserPanelView userPanelView = new UserPanelView();
                userPanelView.getStage().show();

            } catch (AuthenticationException ex) {
                errorText.setText(ex.getMessage());
                errorText.setFill(Color.RED);
            }
        }));
        loginGrid.add(login, 0, 4);

        Hyperlink registerHyperlink = new Hyperlink("registre-se aqui");
        registerHyperlink.setOnAction((event) -> {
            stage.close();
            new RegisterFormView().getStage().show();
        });

        TextFlow registerText = new TextFlow(new Text("Caso não tenha uma conta,"), registerHyperlink);
        loginGrid.add(registerText, 1, 4);

        var scene = new Scene(loginGrid, 575, 225);
        scene.getStylesheets().add(LoginFormView.class.getResource("/css/login.css").toExternalForm());
        stage.setScene(scene);
        return stage;
    }
}
