package net.victorbetoni.synergy.controller;

import net.victorbetoni.synergy.database.Database;
import net.victorbetoni.synergy.enums.OrganizationRole;
import net.victorbetoni.synergy.model.Organization;
import net.victorbetoni.synergy.model.User;
import net.victorbetoni.synergy.utils.Pair;
import net.victorbetoni.synergy.utils.SQLUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrganizationController {
    public Organization createOrganization(String name, String password, UUID id) {
        SQLUtils.query("INSERT INTO Organizations VALUES (?,?,?)", Database.getConnection(), true,
                Pair.of(String.class, name),
                Pair.of(String.class, id.toString()),
                Pair.of(String.class, password));
        return new Organization(name, id);
    }

    public boolean organizationExists(UUID id) {
        try {
            return SQLUtils.query("SELECT * FROM Organizations WHERE UniqueID = ?", Database.getConnection(), false, Pair.of(String.class, id.toString())).get().next();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Map<String, String> getOrganizationInfos(UUID id) {
        try(ResultSet rs = SQLUtils.query("SELECT * FROM Organizations WHERE UniqueID = ?", Database.getConnection(), false,
                Pair.of(String.class, id.toString())).get()) {
            Map<String, String> infos = new HashMap<>();
            rs.next();
            infos.put("id", rs.getString("UniqueID"));
            infos.put("name", rs.getString("Name"));
            infos.put("password", rs.getString("Password"));
            return infos;
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void addMemberToOrganization(User user, Organization organization, OrganizationRole role) {
        SQLUtils.query("INSERT INTO OrganizationMembers VALUES(?,?,?)", Database.getConnection(), true,
                Pair.of(String.class, user.getUsername()),
                Pair.of(String.class, organization.getUuid().toString()),
                Pair.of(String.class, role.getId()));
    }
}
