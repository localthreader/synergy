package net.victorbetoni.squadster.controller;

import net.victorbetoni.squadster.database.Database;
import net.victorbetoni.squadster.model.Team;
import net.victorbetoni.squadster.model.Organization;
import net.victorbetoni.squadster.model.User;
import net.victorbetoni.squadster.utils.Pair;
import net.victorbetoni.squadster.utils.SQLUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class TeamController {
    public Team createTeam(Organization organization, String name, List<User> members) {
        UUID id = UUID.randomUUID();
        SQLUtils.query("INSERT INTO Teams VALUES(?,?)", Database.getConnection(), true,
                Pair.of(String.class, id.toString()),
                Pair.of(String.class, name));

        SQLUtils.query("INSERT INTO Teams VALUES(?,?)", Database.getConnection(), true,
                Pair.of(String.class, id.toString()),
                Pair.of(String.class, organization.getUuid().toString()));

        members.forEach(member ->
            SQLUtils.query("INSERT INTO TeamUsers VALUES(?,?)", Database.getConnection(), true,
                    Pair.of(String.class, id.toString()),
                    Pair.of(String.class, member.getUsername())));
        return new Team(name, id);
    }
}
