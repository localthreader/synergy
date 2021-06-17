package net.victorbetoni.tasker.controller;

import net.victorbetoni.tasker.database.Database;
import net.victorbetoni.tasker.model.Team;
import net.victorbetoni.tasker.model.Organization;
import net.victorbetoni.tasker.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class TeamController {
    public Team createTeamFor(Organization organization, String name, List<User> members) {
        UUID id = UUID.randomUUID();
        try {
            PreparedStatement st = Database.getConnection().prepareStatement(
                "INSERT INTO Teams VALUES(?,?)"
            );
            st.setString(1, id.toString());
            st.setString(2, name);
            st.executeUpdate();

            PreparedStatement st2 = Database.getConnection().prepareStatement(
                    "INSERT INTO OrganizationTeams VALUES(?,?)"
            );
            st2.setString(1, id.toString());
            st2.setString(2, organization.getUuid().toString());
            st2.executeUpdate();
            return new Team(name, id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
