package net.victorbetoni.synergy.controller;

import net.victorbetoni.synergy.database.Database;
import net.victorbetoni.synergy.model.Team;
import net.victorbetoni.synergy.model.Organization;
import net.victorbetoni.synergy.model.User;
import net.victorbetoni.synergy.utils.Pair;
import net.victorbetoni.synergy.utils.SQLUtils;

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
