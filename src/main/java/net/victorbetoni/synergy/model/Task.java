package net.victorbetoni.synergy.model;

import net.victorbetoni.synergy.enums.TaskType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Task {

    private String title;

    private Optional<User> responsibleUser;
    private Timestamp postedDate;
    private Optional<Timestamp> deadLine;
    private TaskType type;

     public Task(String title, TaskType type, Timestamp postedDate, Optional<Timestamp> deadLine, Optional<User> responsibleUser) {
        this.type = type;
        this.title = title;
        this.postedDate = postedDate;
        this.deadLine = deadLine;
        this.responsibleUser = responsibleUser;
     }

    public static class CommentedTask extends Task {
         private List<Comment> comments = new ArrayList<>();


        public CommentedTask(List<Comment> comments, String title, Timestamp postedDate, Optional<Timestamp> deadLine, Optional<User> responsibleUser) {
            super(title, TaskType.COMMENTED, postedDate, deadLine, responsibleUser);
            this.comments = comments;
        }

        public List<Comment> getComments() {
            return comments;
        }
    }

    public static class SubTasks extends Task{
        private List<CommonTask> tasks = new ArrayList<>();

        public SubTasks(List<CommonTask> tasks, String title, Timestamp postedDate, Optional<Timestamp> deadLine, Optional<User> responsibleUser) {
            super(title, TaskType.SUBTASKS, postedDate, deadLine, responsibleUser);
        }

        public List<CommonTask> getTasks() {
            return tasks;
        }
    }

    public static class CommonTask extends Task{
        public CommonTask(String title, Timestamp postedDate, Optional<Timestamp> deadLine, Optional<User> responsibleUser) {
            super(title, TaskType.COMMON, postedDate, deadLine, responsibleUser);
        }
    }
}
