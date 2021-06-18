package net.victorbetoni.squadster.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.victorbetoni.squadster.TaskTracker;
import net.victorbetoni.squadster.controller.OrganizationController;
import net.victorbetoni.squadster.enums.OrganizationRole;
import net.victorbetoni.squadster.model.Organization;
import net.victorbetoni.squadster.model.auth.OrganizationSession;
import net.victorbetoni.squadster.utils.HashUtils;

import java.io.IOException;
import java.util.UUID;

public class JoinOrganizationView implements View {
    @Override
    public Stage getStage() {
        Stage stage = new Stage();

        stage.getIcons().add(TaskTracker.ICON);
        stage.setTitle(TaskTracker.SOFTWARE_NAME + " - User Panel");

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

        Text title = new Text("Entrar em uma organização:");
        title.setId("text");
        title.setFont(Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu-Medium.ttf"), 25));
        grid.add(title, 0, 0, 2, 1);

        Label id = new Label("ID da organização:");
        id.setFont(UBUNTU);
        grid.add(id, 0, 1);
        TextField idField = new TextField();
        idField.setId("text-field");
        idField.setMinWidth(50);
        grid.add(idField, 1, 1);

        Label pw = new Label("Senha:");
        grid.add(pw, 0, 2);
        PasswordField passwordInput = new PasswordField();
        grid.add(passwordInput, 1, 2);

        Text errorText = new Text();
        grid.add(errorText, 1, 3);

        Button entrar = new Button("Entrar");
        entrar.setOnMouseClicked((e) -> {
            try{
                String orgId = idField.getText().trim();
                String password = passwordInput.getText().trim();
                if(!TaskTracker.getOrganizationController().organizationExists(UUID.fromString(orgId)))
                    throw new IOException("Nenhuma organização encontrada");

                for(Organization org : TaskTracker.getSession().getUser().getOrganizations()) {
                    if(org.getUuid().toString().equalsIgnoreCase(orgId)) {
                        throw new IOException("Você já está nessa organização");
                    }
                }

                OrganizationController controller = TaskTracker.getOrganizationController();
                var infos = controller.getOrganizationInfos(UUID.fromString(orgId));

                if(HashUtils.sha256(password).equals(infos.get("password")))
                    throw new IOException("Senha incorreta");

                Organization org = new Organization(infos.get("name"), UUID.fromString(orgId));
                controller.addMemberToOrganization(TaskTracker.getSession().getUser(), org, OrganizationRole.MEMBER);
                TaskTracker.setOrganizationSession(new OrganizationSession(org, System.currentTimeMillis()));
                OrganizationView view = new OrganizationView(org);
                view.getStage().show();
                stage.close();

            }catch (IOException ex) {
                errorText.setText(ex.getMessage());
                errorText.setFill(Color.RED);
            }

        });

        grid.add(entrar, 1, 5);

        Scene scene = new Scene(grid, 500, 250);
        stage.setScene(scene);

        return stage;
    }
}
