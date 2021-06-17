package net.victorbetoni.tasker.model;

import java.sql.Timestamp;
import java.util.Date;

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
