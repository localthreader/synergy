package net.victorbetoni.squadster.model.auth;

import net.victorbetoni.squadster.model.User;

public class Session {
    private User user;
    private long loginTime;

    public Session(User user, long loginTime) {
        this.user = user;
        this.loginTime = loginTime;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public User getUser() {
        return user;
    }
}
