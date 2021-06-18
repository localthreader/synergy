package net.victorbetoni.synergy.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.victorbetoni.synergy.Synergy;
import net.victorbetoni.synergy.controller.OrganizationController;
import net.victorbetoni.synergy.enums.OrganizationRole;
import net.victorbetoni.synergy.model.Organization;
import net.victorbetoni.synergy.model.auth.OrganizationSession;
import net.victorbetoni.synergy.utils.HashUtils;

import java.io.IOException;
import java.util.UUID;

public class CreateOrganizationFormView implements View {

    @Override
    public Stage getStage() {
        Stage stage = new Stage();

        stage.getIcons().add(Synergy.ICON);
        stage.setTitle(Synergy.SOFTWARE_NAME + " - User Panel");

        Font ubuntu = Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu.ttf"), 12);

        GridPane grid = new GridPane();

        Font UBUNTU = Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu-Medium.ttf"), 12);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);

        grid.getColumnConstraints().addAll(col1, col2);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text title = new Text("Criar organização:");
        title.setId("text");
        title.setFont(Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu-Medium.ttf"), 25));
        grid.add(title, 0, 0, 2, 1);

        Label name = new Label("Nome da organização:");
        name.setFont(UBUNTU);
        grid.add(name, 0, 1);
        TextField nameField = new TextField();
        nameField.setId("text-field");
        nameField.setMinWidth(50);
        grid.add(nameField, 1, 1);

        Label pw = new Label("Senha:");
        grid.add(pw, 0, 2);
        PasswordField passwordInput = new PasswordField();
        grid.add(passwordInput, 1, 2);

        Text errorText = new Text();
        grid.add(errorText, 1, 3);

        Button criar = new Button("Criar");
        criar.setOnMouseClicked((e) -> {
            try{
                String orgName = nameField.getText().trim();
                String password = passwordInput.getText().trim();
                if(orgName.length() > 20)
                    throw new IOException("O nome da organização deve ter no máximo 20 caracteres");
                if(password.length() < 5)
                    throw new IOException("A senha deve ter no mínimo 5 caracteres");

                OrganizationController controller = Synergy.getOrganizationController();

                Organization org = controller.createOrganization(orgName, HashUtils.sha256(password), UUID.randomUUID());
                controller.addMemberToOrganization(Synergy.getSession().getUser(), org, OrganizationRole.OWNER);
                Synergy.setOrganizationSession(new OrganizationSession(org, System.currentTimeMillis()));

                OrganizationView view = new OrganizationView(org);
                view.getStage().show();
                stage.close();

            }catch (IOException ex) {
                errorText.setText(ex.getMessage());
            }

        });
        criar.setMinWidth(40);
        grid.add(criar, 0, 4);

        Scene scene = new Scene(grid, 500, 250);
        stage.setScene(scene);

        return stage;
    }
}
