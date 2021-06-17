package net.victorbetoni.tasker.controller;

import net.victorbetoni.tasker.TaskTracker;
import net.victorbetoni.tasker.TaskerInitializer;
import net.victorbetoni.tasker.database.Database;
import net.victorbetoni.tasker.exception.AuthenticationException;
import net.victorbetoni.tasker.model.User;
import net.victorbetoni.tasker.model.auth.Session;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthController implements Controller {

    public Session openSession(String username, String name, String email) throws AuthenticationException {
        return new Session(new User(username, name, email), System.currentTimeMillis());
    }

    public void registerUser(String username, String name, String email, String password) throws AuthenticationException {
        try{
            PreparedStatement st = Database.getConnection().prepareStatement(
                    "INSERT INTO Users VALUES (?,?,?,?)"
            );
            st.setString(1, username);
            st.setString(2, name);
            st.setString(3, email);
            st.setString(4, password);
            st.executeUpdate();
            TaskTracker.setSession(openSession(username, name, email));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean isEmailRegistered(String email) {
        try{
            PreparedStatement st = Database.getConnection().prepareStatement(
                    "SELECT Email FROM Users WHERE Email LIKE ?"
            );
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean isUsernameRegistered(String username) {
        try{
            PreparedStatement st = Database.getConnection().prepareStatement(
                   "SELECT Username FROM Users WHERE Username LIKE ?"
            );
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 8
                && password.matches(".*\\d.*");
    }
}
