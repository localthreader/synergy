package net.victorbetoni.squadster.model;

import java.sql.Timestamp;

public class Comment {
    private Timestamp postedDate;
    private String message;

    public Comment(Timestamp postedDate, String message) {
        this.postedDate = postedDate;
        this.message = message;
    }

    public Timestamp getPostedDate() {
        return postedDate;
    }

    public String getMessage() {
        return message;
    }
}
