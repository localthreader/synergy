package net.victorbetoni.squadster.model;

import net.victorbetoni.squadster.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class User {
    private String username;
    private String name;
    private String email;

    public User(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public static Optional<User> by(String username) {
        try{
            PreparedStatement st = Database.getConnection().prepareStatement(
                    "SELECT Name, Email FROM Users WHERE Username=?");
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                return Optional.of(new User(username, rs.getString("Name"), rs.getString("Email")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();

    }

    public Set<Organization> getOrganizations() {
        Set<Organization> orgs = new HashSet<>();
        try{
            PreparedStatement st = Database.getConnection().prepareStatement(
                    "SELECT OrganizationID FROM OrganizationMembers WHERE Username=?"
            );
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                String uniqueid = rs.getString("OrganizationID");
                PreparedStatement st2 = Database.getConnection().prepareStatement(
                        "SELECT Name FROM Organizations WHERE UniqueID=?"
                );
                st2.setString(1, uniqueid);
                ResultSet rs2 = st2.executeQuery();
                rs2.next();
                String name = rs2.getString("Name");
                orgs.add(new Organization(name, UUID.fromString(uniqueid)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return orgs;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
