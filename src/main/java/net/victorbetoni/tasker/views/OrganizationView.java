package net.victorbetoni.tasker.views;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.victorbetoni.tasker.TaskTracker;
import net.victorbetoni.tasker.enums.OrganizationRole;
import net.victorbetoni.tasker.model.Team;
import net.victorbetoni.tasker.model.Organization;
import net.victorbetoni.tasker.model.User;
import net.victorbetoni.tasker.utils.GraphUtils;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class OrganizationView implements View{

    private Organization organization;

    public OrganizationView(Organization organization) {
        this.organization = organization;
    }

    BorderPane layout = new BorderPane();

    @Override
    public Stage getStage() {
        Stage stage = new Stage();
        stage.setTitle(TaskTracker.SOFTWARE_NAME + " - " + organization.getName());
        stage.getIcons().add(TaskTracker.ICON);

        layout.setLeft(createUsersBox());
        layout.setTop(createHeader());
        layout.setCenter(createTeamWidgets());

        Scene scene = new Scene(layout);
        System.out.println(OrganizationView.class.getResource("/css/organizationview.css") == null);
        scene.getStylesheets().add(OrganizationView.class.getResource("/css/organizationview.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);

        return stage;
    }

    private HBox createHeader() {
        ImageView orgImg = new ImageView(new Image(UserPanelView.class.getResourceAsStream("/img/diagram.png")));
        HBox box = new HBox(orgImg);
        box.setPadding(new Insets(15, 0, 15, 25));
        GridPane gridPane = new GridPane();
        Text name = new Text(organization.getName());
        name.setFont(Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu-Medium.ttf"), 20));
        name.setFill(Color.WHITE);

        Text id = new Text(organization.getUuid().toString());
        id.setId("id-text");
        id.setOnMouseClicked((e) -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(organization.getUuid().toString());
            Clipboard.getSystemClipboard().setContent(content);
            id.setText("ID copiado para o clipboard!");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    id.setText(organization.getUuid().toString());
                }
            }, 1000);
        });
        id.setFont(Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu.ttf"), 14));
        id.setFill(Color.WHITE);

        gridPane.add(name, 0, 0);
        gridPane.add(id, 0, 1);
        gridPane.setPadding(new Insets(8, 0, 0, 25));
        gridPane.setVgap(10);

        box.setId("container");
        box.getChildren().add(gridPane);
        return box;
    }

    private HBox createUsersBox() {
        Map<User, OrganizationRole> members = organization.getMembers();
        System.out.println(members.size());
        GridPane usersContainer = new GridPane();

        Text membersText = new Text("Membros");
        membersText.setFont(Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu-Medium.ttf"), 20));

        GridPane membersTextPane = new GridPane();
        membersTextPane.setPadding(new Insets(15, 0, 15, 0));
        ColumnConstraints constraint = new ColumnConstraints(200);
        constraint.setHalignment(HPos.CENTER);
        constraint.setPercentWidth(100);
        membersTextPane.getColumnConstraints().add(constraint);
        membersTextPane.add(membersText, 0, 1);

        AtomicReference<Integer> index = new AtomicReference<>(0);

        members.forEach((user, role) -> {
            index.set(index.get() + 1);
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setPercentWidth(25);
            column1.setHalignment(HPos.CENTER);
            ColumnConstraints column2 = new ColumnConstraints(200);
            column2.setPercentWidth(90);
            column2.setHalignment(HPos.LEFT);

            GridPane userGrid = new GridPane();
            ImageView userIcon = new ImageView(new Image(UserPanelView.class.getResourceAsStream("/img/avatar.png")));
            Text userName = new Text(user.getName());
            userName.setFont(Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu.ttf"), 12));
            userGrid.getColumnConstraints().addAll(column1, column2);
            userGrid.add(userIcon, 0, 1);
            userGrid.add(userName, 1, 1);
            userGrid.setOnMouseEntered((e) -> GraphUtils.createFillTransition(userGrid, Color.WHITE, Color.valueOf("#b554ff")).play());
            userGrid.setOnMouseExited((e) -> GraphUtils.createFillTransition(userGrid, Color.valueOf("#ffffff"), Color.WHITE).play());
            userGrid.setId("user");
            usersContainer.add(userGrid, 0, index.get());

        });

        VBox vbox = new VBox(membersTextPane, usersContainer);

        HBox box = new HBox();
        box.setPrefWidth(1);
        box.setBackground(new Background(new BackgroundFill(Color.valueOf("#d1d1d1"), CornerRadii.EMPTY, Insets.EMPTY)));

        return new HBox(vbox, box);
    }

    private GridPane createTeamWidgets() {
        GridPane teamsPane = new GridPane();
        Set<Team> teams = organization.getTeams();
        AtomicInteger row = new AtomicInteger(1);
        AtomicInteger column = new AtomicInteger(1);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPrefWidth(150);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(130);

        teams.forEach(team -> {
            GridPane teamGrid = new GridPane();
            teamGrid.getColumnConstraints().add(columnConstraints);
            teamGrid.getRowConstraints().add(rowConstraints);
            teamGrid.setBackground(new Background(new BackgroundFill(Color.valueOf("#adadad"), CornerRadii.EMPTY, Insets.EMPTY)));
            Text teamName = new Text(team.getName());
            teamName.setFont(Font.loadFont(OrganizationView.class.getResourceAsStream("/font/Ubuntu-Medium.ttf"), 20));
            Text pendentTasks = new Text("Tarefas pendentes: " + team.getTasks().size());
            teamName.setFont(Font.loadFont(OrganizationView.class.getResourceAsStream("/font/Ubuntu.ttf"), 12));
            teamGrid.add(teamName, 0, 1);
            teamGrid.add(new Text(""), 0, 2);
            teamGrid.add(pendentTasks, 0, 2);
            teamGrid.setOnMouseClicked(event -> {
                layout.setCenter(createTeamView(team));
            });
            HBox box = new HBox(teamGrid);
            box.setMinHeight(120);
            box.setId("widget");
            teamsPane.add(box, row.get(), column.get());

            column.incrementAndGet();
            if(column.get() > 4) {
                column.set(1);
                row.incrementAndGet();
            }
        });
        ImageView addImg = new ImageView(new Image(UserPanelView.class.getResourceAsStream("/img/addTeam.png")));
        StackPane region = new StackPane(addImg);
        region.setId("image");
        HBox box = new HBox(region);
        box.setMinHeight(170);
        box.setId("widget");
        teamsPane.add(box, row.get(), column.get());
        region.setOnMouseClicked(e -> new CreateTeamView(organization).getStage().show());

        teamsPane.setHgap(20);
        teamsPane.setVgap(20);

        return teamsPane;
    }

    private HBox createTeamView(Team team) {
        return null;
    }
}
