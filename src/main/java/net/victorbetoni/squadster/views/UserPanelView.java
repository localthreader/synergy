package net.victorbetoni.squadster.views;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.victorbetoni.squadster.TaskTracker;
import net.victorbetoni.squadster.database.Database;
import net.victorbetoni.squadster.model.Organization;
import net.victorbetoni.squadster.model.User;
import net.victorbetoni.squadster.utils.GraphUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class UserPanelView implements View {

    //private final Map VIEW_CONFIG = (Map) JSONUtils.parseJSONObject("config/ui_colors").get("user_panel");
    private User user;

    @Override
    public Stage getStage() {
        var stage = new Stage();
        stage.getIcons().add(TaskTracker.ICON);
        stage.setTitle(TaskTracker.SOFTWARE_NAME + " - User Panel");
        user = TaskTracker.getSession().getUser();

        BorderPane layout = new BorderPane();

        layout.setTop(this.createTopUserView());
        layout.setLeft(this.createOrganizationsGrid());
        layout.setCenter(createTasksView());

        var scene = new Scene(layout, 1280, 720);
        scene.getStylesheets().add(UserPanelView.class.getResource("/css/userpanel.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        return stage;
    }

    private HBox createTopUserView() {
        ImageView userImg = new ImageView(new Image(UserPanelView.class.getResourceAsStream("/img/user.png")));
        HBox box = new HBox(userImg);
        box.setPadding(new Insets(15, 0, 15, 25));
        GridPane gridPane = new GridPane();
        Text name = new Text(user.getName());
        name.setFont(Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu-Medium.ttf"), 20));
        name.setFill(Color.WHITE);

        Text email = new Text(user.getEmail());
        email.setFont(Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu.ttf"), 14));
        email.setFill(Color.WHITE);

        gridPane.add(name, 0, 0);
        gridPane.add(email, 0, 1);
        gridPane.setPadding(new Insets(8, 0, 0, 25));
        gridPane.setVgap(10);

        box.setId("user-background");
        box.getChildren().add(gridPane);
        return box;
    }

    private Pane createTasksView() {
        boolean bool = true;
        Text text = new Text();

        Pane pane = new GridPane();
        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setHalignment(HPos.CENTER);
        ((GridPane)pane).getColumnConstraints().add(constraints);

        text.setFill(Color.valueOf("#ababab"));
        text.setFont(Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu-Medium.ttf"), 30));
        if(bool) {
            pane = new StackPane();
            text.setText("Nenhuma tarefa atribuída.");
            pane.getChildren().add(text);
            StackPane.setAlignment(text, Pos.CENTER);
        }
        return pane;
    }

    private HBox createOrganizationsGrid() {

        Font UBUNTU = Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu.ttf"), 12);

        Text orgs = new Text("Organizações");
        orgs.setFont(Font.loadFont(UserPanelView.class.getResourceAsStream("/font/Ubuntu-Medium.ttf"), 20));

        GridPane orgTextPane = new GridPane();
        orgTextPane.setPadding(new Insets(15, 0, 15, 0));
        ColumnConstraints constraint = new ColumnConstraints();
        constraint.setHalignment(HPos.CENTER);
        constraint.setPercentWidth(100);
        orgTextPane.getColumnConstraints().add(constraint);
        orgTextPane.add(orgs, 0, 1);

        GridPane organizationsGrid = new GridPane();

        organizationsGrid.setAlignment(Pos.TOP_CENTER);
        organizationsGrid.setHgap(10);

        ColumnConstraints buttonColumn1 = new ColumnConstraints();
        buttonColumn1.setPercentWidth(25);
        buttonColumn1.setHalignment(HPos.CENTER);
        ColumnConstraints buttonColumn2 = new ColumnConstraints(250);
        buttonColumn2.setPercentWidth(90);
        buttonColumn2.setHalignment(HPos.LEFT);

        AtomicReference<Integer> index = new AtomicReference<>(0);

        List<Organization> organizations = new ArrayList<>();

        try(PreparedStatement st = Database.getConnection().prepareStatement("SELECT OrganizationID FROM OrganizationMembers WHERE Username=?")) {
            st.setString(1, user.getUsername());
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                PreparedStatement st2 = Database.getConnection().prepareStatement("SELECT Name FROM Organizations WHERE UniqueID=?");
                String uid = rs.getString("OrganizationID");
                st2.setString(1, uid);
                ResultSet rs2 = st2.executeQuery();
                rs2.next();
                String name = rs2.getString("Name");
                organizations.add(new Organization(name, UUID.fromString(uid)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String buttonsFadeIn = "#ff6186";
        String buttonsFadeOut = "#ffffff";

        organizations.forEach(org -> {
            index.set(index.get() + 1);
            ImageView orgIconB = new ImageView(new Image(UserPanelView.class.getResourceAsStream("/img/users.png")));
            GridPane orgButton = new GridPane();
            orgButton.setId("org-button");

            orgButton.getColumnConstraints().addAll(buttonColumn1, buttonColumn2);

            Text orgName = new Text(org.getName());
            orgName.setFont(UBUNTU);

            orgButton.add(orgIconB, 0, index.get());
            orgButton.add(orgName, 1, index.get());
            organizationsGrid.add(orgButton, 0, index.get());

            orgButton.setOnMouseEntered((e) -> GraphUtils.createFillTransition(orgButton, Color.WHITE, Color.valueOf(buttonsFadeIn)).play());
            orgButton.setOnMouseExited((e) -> GraphUtils.createFillTransition(orgButton, Color.valueOf(buttonsFadeOut), Color.WHITE).play());

            orgButton.setOnMouseClicked((e) -> {
                OrganizationView view = new OrganizationView(org);
                view.getStage().show();
            });
        });

        GridPane createOrgButton = new GridPane();
        createOrgButton.setId("org-button");
        ImageView plusIcon = new ImageView(new Image(UserPanelView.class.getResourceAsStream("/img/add.png")));
        Text createOrgText = new Text("Criar organização");
        createOrgText.setFont(UBUNTU);
        createOrgButton.getColumnConstraints().addAll(buttonColumn1, buttonColumn2);
        createOrgButton.add(plusIcon, 0, 1);
        createOrgButton.add(createOrgText, 1, 1);
        createOrgButton.setOnMouseEntered((e) -> GraphUtils.createFillTransition(createOrgButton, Color.WHITE, Color.valueOf(buttonsFadeIn)).play());
        createOrgButton.setOnMouseExited((e) -> GraphUtils.createFillTransition(createOrgButton, Color.valueOf(buttonsFadeOut), Color.WHITE).play());
        createOrgButton.setOnMouseClicked((e) -> {
            CreateOrganizationFormView view = new CreateOrganizationFormView();
            view.getStage().show();
        });
        organizationsGrid.add(createOrgButton, 0, index.get() + 1);

        GridPane joinOrgButton = new GridPane();
        joinOrgButton.setId("org-button");
        ImageView joinIcon = new ImageView(new Image(UserPanelView.class.getResourceAsStream("/img/enter.png")));
        Text joinOrgText = new Text("Entrar em uma organização");
        joinOrgText.setFont(UBUNTU);
        joinOrgButton.getColumnConstraints().addAll(buttonColumn1, buttonColumn2);
        joinOrgButton.add(joinIcon, 0, 1);
        joinOrgButton.add(joinOrgText, 1, 1);
        joinOrgButton.setOnMouseEntered((e) -> GraphUtils.createFillTransition(joinOrgButton, Color.WHITE, Color.valueOf(buttonsFadeIn)).play());
        joinOrgButton.setOnMouseExited((e) -> GraphUtils.createFillTransition(joinOrgButton, Color.valueOf(buttonsFadeOut), Color.WHITE).play());
        joinOrgButton.setOnMouseClicked((e) -> {
            JoinOrganizationView view = new JoinOrganizationView();
            view.getStage().show();
        });
        organizationsGrid.add(joinOrgButton, 0, index.get() + 2);

        VBox vbox = new VBox(orgTextPane, organizationsGrid);

        HBox box = new HBox();
        box.setPrefWidth(1);
        box.setBackground(new Background(new BackgroundFill(Color.valueOf("#d1d1d1"), CornerRadii.EMPTY, Insets.EMPTY)));

        return new HBox(vbox, box);
    }

}