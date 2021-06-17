package net.victorbetoni.tasker.model;

import net.victorbetoni.tasker.database.Database;
import net.victorbetoni.tasker.enums.OrganizationRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Organization {
    private String name;
    private UUID uuid;

    public Organization(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public Map<User, OrganizationRole> getMembers() {
        Map<User, OrganizationRole> members = new HashMap<>();
        try{
            PreparedStatement st = Database.getConnection().prepareStatement(
                    "SELECT Username,Role FROM OrganizationMembers WHERE OrganizationID=?");
            st.setString(1, uuid.toString());
            ResultSet usernamesRs = st.executeQuery();
            while(usernamesRs.next()) {
                int role = usernamesRs.getInt("Role");
                String username = usernamesRs.getString("Username");
                PreparedStatement userInfoStt = Database.getConnection().prepareStatement(
                        "SELECT Name,Email FROM Users WHERE Username=?");
                userInfoStt.setString(1, username);
                ResultSet userInfo = userInfoStt.executeQuery();
                userInfo.next();
                String name = userInfo.getString("Name");
                String email = userInfo.getString("Email");
                members.put(new User(username, name,email), OrganizationRole.byId(role));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return members;
    }

    public Set<Team> getTeams() {
        Set<Team> teams = new HashSet<>();
        try{
            PreparedStatement st = Database.getConnection().prepareStatement(
                    "SELECT TeamID FROM OrganizationTeam WHERE OrganizationID=?");
            st.setString(1, uuid.toString());
            ResultSet teamsID = st.executeQuery();
            while(teamsID.next()) {
                String teamId = teamsID.getString("TeamID");
                PreparedStatement namesStt = Database.getConnection().prepareStatement(
                        "SELECT Name FROM Team WHERE TeamID=?");
                namesStt.setString(1, teamId);
                ResultSet nameResult = namesStt.executeQuery();
                nameResult.next();
                String name = nameResult.getString("Name");
                teams.add(new Team(name, UUID.fromString(teamId)));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teams;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

}
