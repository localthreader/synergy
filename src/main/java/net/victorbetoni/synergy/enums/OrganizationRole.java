package net.victorbetoni.synergy.enums;

import java.util.Arrays;

public enum OrganizationRole {

    MEMBER(0), MANAGER(1), OWNER(2);

    private int id;

    OrganizationRole(int id) {
        this.id = id;
    }

    public static OrganizationRole byId(int id) {
        return Arrays.stream(values()).filter(x -> x.getId() == id).findFirst().get();
    }

    public int getId() {return id;}
}
