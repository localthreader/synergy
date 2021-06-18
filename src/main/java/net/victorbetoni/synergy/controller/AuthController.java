package net.victorbetoni.synergy.controller;

import net.victorbetoni.synergy.database.Database;
import net.victorbetoni.synergy.exception.AuthenticationException;
import net.victorbetoni.synergy.model.User;
import net.victorbetoni.synergy.model.auth.Session;
import net.victorbetoni.synergy.utils.Pair;
import net.victorbetoni.synergy.utils.SQLUtils;

import java.sql.SQLException;

public class AuthController implements Controller {

    public Session openSession(String username, String name, String email) throws AuthenticationException {
        return new Session(new User(username, name, email), System.currentTimeMillis());
    }

    @SuppressWarnings("all")
    public void registerUser(String username, String name, String email, String password) throws AuthenticationException {
        SQLUtils.query("INSERT INTO Users VALUES (?,?,?,?)", Database.getConnection(), true,
                Pair.of(String.class, username),
                Pair.of(String.class, name),
                Pair.of(String.class, email),
                Pair.of(String.class, password));
    }

    @SuppressWarnings("all")
    public boolean isEmailRegistered(String email) {
        try {
            return SQLUtils.query("SELECT Email FROM Users WHERE Email LIKE ?", Database.getConnection(), false, Pair.of(String.class, email)).get().next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("all")
    public boolean isUsernameRegistered(String username) {
        try {
            return SQLUtils.query("SELECT Username FROM Users WHERE Username LIKE ?", Database.getConnection(), false, Pair.of(String.class, username)).get().next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 8
                && password.matches(".*\\d.*");
    }
}
