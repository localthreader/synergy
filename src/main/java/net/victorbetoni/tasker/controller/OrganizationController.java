package net.victorbetoni.tasker.controller;

import net.victorbetoni.tasker.database.Database;
import net.victorbetoni.tasker.enums.OrganizationRole;
import net.victorbetoni.tasker.model.Organization;
import net.victorbetoni.tasker.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrganizationController {
    public Organization createOrganization(String name, String password, UUID id) {
        try(PreparedStatement st = Database.getConnection().prepareStatement(
                "INSERT INTO Organizations VALUES (?,?,?)"
        )){
            st.setString(1, name);
            st.setString(2, id.toString());
            st.setString(3, password);
            st.executeUpdate();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Organization(name, id);
    }

    public boolean organizationExists(UUID id) {
        try(PreparedStatement st = Database.getConnection().prepareStatement(
                "SELECT * FROM Organizations WHERE UniqueID = ?"
        )){
            st.setString(1, id.toString());
            return st.executeQuery().next();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Map<String, String> getOrganizationInfos(UUID id) {
        try(PreparedStatement st = Database.getConnection().prepareStatement(
                "SELECT * FROM Organizations WHERE UniqueID = ?"
        )){
            st.setString(1, id.toString());
            Map<String, String> infos = new HashMap<>();
            ResultSet rs = st.executeQuery();
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
        try(PreparedStatement st = Database.getConnection().prepareStatement(
                "INSERT INTO OrganizationMembers VALUES(?,?,?)"
        )){
            st.setString(1, user.getUsername());
            st.setString(2, organization.getUuid().toString());
            st.setInt(3, role.getId());
            st.executeUpdate();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
