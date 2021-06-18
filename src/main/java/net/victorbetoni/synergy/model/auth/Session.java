package net.victorbetoni.synergy.model.auth;

import net.victorbetoni.synergy.model.User;

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
