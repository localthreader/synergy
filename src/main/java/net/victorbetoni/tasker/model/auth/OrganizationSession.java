package net.victorbetoni.tasker.model.auth;

import net.victorbetoni.tasker.model.Organization;

public class OrganizationSession {
    private Organization organization;
    private long loginTime;

    public OrganizationSession(Organization organization, long loginTime){
        this.organization = organization;
        this.loginTime = loginTime;
    }

    public Organization getOrganization() {
        return organization;
    }

    public long getLoginTime() {
        return loginTime;
    }
}
