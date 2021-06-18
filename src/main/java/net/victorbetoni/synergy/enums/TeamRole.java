package net.victorbetoni.synergy.enums;

import java.util.Arrays;

public enum TeamRole {
    LEADER(1), MEMBER(0);

    private int id;

    TeamRole(int id) {
        this.id = id;
    }

    public static TeamRole byId(int id) {
        return Arrays.stream(values()).filter(x -> x.getId() == id).findFirst().get();
    }

    public int getId() {return id;}
}
