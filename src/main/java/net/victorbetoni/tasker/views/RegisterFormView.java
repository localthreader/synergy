package net.victorbetoni.tasker.views;

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
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import net.victorbetoni.tasker.TaskTracker;
import net.victorbetoni.tasker.TaskerInitializer;
import net.victorbetoni.tasker.controller.AuthController;
import net.victorbetoni.tasker.exception.AuthenticationException;
import net.victorbetoni.tasker.utils.HashUtils;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterFormView implements View {

    @Override
    public Stage getStage() {
        var stage = new Stage();
        stage.getIcons().add(TaskTracker.ICON);
        stage.setTitle(TaskTracker.SOFTWARE_NAME + " - Registrar");
        var registerGrid = new GridPane();
        var col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        var col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        registerGrid.getColumnConstraints().addAll(col1, col2);
        registerGrid.setAlignment(Pos.TOP_CENTER);
        registerGrid.setHgap(10);
        registerGrid.setVgap(10);
        registerGrid.setPadding(new Insets(25, 25, 25, 25));

        Text title = new Text("Registrar");
        title.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: rgb(120,190,201)");
        registerGrid.add(title, 0, 0, 2, 1);

        Label username = new Label("Username: ");
        registerGrid.add(username, 0, 1);
        TextField usernameField = new TextField();
        registerGrid.add(usernameField, 1, 1);

        Label name = new Label("Nome: ");
        registerGrid.add(name, 0, 2);
        TextField nameField = new TextField();
        registerGrid.add(nameField, 1, 2);

        Label email = new Label("Email: ");
        registerGrid.add(email, 0, 3);
        TextField emailField = new TextField();
        registerGrid.add(emailField, 1, 3);

        Label password = new Label("Senha: ");
        registerGrid.add(password, 0, 4);
        PasswordField passwordField = new PasswordField();
        registerGrid.add(passwordField, 1, 4);

        Label passwordConfirm = new Label("Repita a senha: ");
        registerGrid.add(passwordConfirm, 0, 5);
        PasswordField passwordConfirmField = new PasswordField();
        registerGrid.add(passwordConfirmField, 1, 5);

        Text errorText = new Text();
        registerGrid.add(errorText, 1, 6);

        Hyperlink registerHyperlink = new Hyperlink("faça login aqui.");
        registerHyperlink.setOnAction((event) -> {
            stage.close();
            new LoginFormView().getStage().show();
        });

        TextFlow loginText = new TextFlow(new Text("Caso já tenha uma conta,"), registerHyperlink);
        registerGrid.add(loginText, 1, 7);

        Button registrar = new Button("Registrar");
        registrar.setMinWidth(40);
        registrar.setAlignment(Pos.CENTER);
        registrar.setOnAction((event -> {
            try {
                AuthController controller = TaskTracker.getAuthController();
                if(usernameField.getText().trim().length() < 8 || usernameField.getText().trim().length() > 16)
                    throw new AuthenticationException("O nome de usuário deve ter entre 8 e 16 caracteres.");
                if(nameField.getText().trim().length() > 30 || nameField.getText().split(" ").length < 1)
                    throw new AuthenticationException("O nome precisa ter no máximo 30 letras e no mínimo um sobrenome.");
                if(!passwordField.getText().trim().equals(passwordConfirmField.getText().trim()))
                    throw new AuthenticationException("As senhas não coincidem.");
                if(controller.isEmailRegistered(emailField.getText().trim()))
                    throw new AuthenticationException("Esse email já está registrado.");
                if(controller.isUsernameRegistered(usernameField.getText().trim()))
                    throw new AuthenticationException("Esse username já está registrado.");



                controller.registerUser(usernameField.getText().trim(), nameField.getText().trim(), emailField.getText().trim(), HashUtils.sha256(passwordField.getText()));
            } catch (AuthenticationException ex) {
                errorText.setText(ex.getMessage());
                errorText.setFill(Color.RED);
            }
        }));
        registerGrid.add(registrar, 0, 7);
        stage.setScene(new Scene(registerGrid, 575, 350));
        return stage;
    }

}
