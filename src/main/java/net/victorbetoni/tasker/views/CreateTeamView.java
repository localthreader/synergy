package net.victorbetoni.tasker.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.victorbetoni.tasker.TaskTracker;
import net.victorbetoni.tasker.controller.OrganizationController;
import net.victorbetoni.tasker.enums.OrganizationRole;
import net.victorbetoni.tasker.model.Organization;
import net.victorbetoni.tasker.model.User;
import net.victorbetoni.tasker.model.auth.OrganizationSession;
import net.victorbetoni.tasker.utils.HashUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class CreateTeamView implements View {

    private Organization organization;
    private Set<User> currentUsers = new HashSet<>();

    public CreateTeamView(Organization organization) {
        this.organization = organization;
    }

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

        Label id = new Label("Nome do time:");
        id.setFont(UBUNTU);
        grid.add(id, 0, 1);
        TextField idField = new TextField();
        idField.setId("text-field");
        idField.setMinWidth(50);
        grid.add(idField, 1, 1);


        AtomicReference<ObservableList<String>> users = new AtomicReference<>(FXCollections.observableList(
                organization.getMembers().keySet().stream()
                        .filter(user -> !currentUsers.contains(user))
                        .map(User::getUsername)
                        .collect(Collectors.toList())));

        ComboBox<String> comboBox = new ComboBox<>(users.get());

        Button addMember = new Button("Adicionar");
        addMember.setOnMouseClicked((e) -> {
            if(!comboBox.getSelectionModel().isEmpty()) {
                User selectedUser = User.by(comboBox.getSelectionModel().getSelectedItem()).get();
                currentUsers.add(selectedUser);
                users.set(FXCollections.observableList(
                        organization.getMembers().keySet().stream()
                                .filter(user -> !currentUsers.contains(user))
                                .map(User::getUsername)
                                .collect(Collectors.toList())));
            }
        });

        Label selecione = new Label("Selecione um ou mais membros:");
        id.setFont(UBUNTU);
        grid.add(selecione, 0, 2);
        grid.add(comboBox, 1, 2);
        grid.add(addMember, 2, 2);

        Scene scene = new Scene(grid, 500, 250);
        stage.setScene(scene);
        return stage;
    }
}
