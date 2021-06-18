package net.victorbetoni.squadster.model;

import net.victorbetoni.squadster.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Team {
    private UUID uuid;
    private String name;

    public Team(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        try{
            PreparedStatement st = Database.getConnection().prepareStatement(
                "SELECT * FROM TeamTasks WHERE TeamID=? ");
            st.setString(1, uuid.toString());
            ResultSet tasksRs = st.executeQuery();
            while(tasksRs.next()) {
                PreparedStatement st2 = Database.getConnection().prepareStatement(
                        "SELECT * FROM Tasks WHERE UniqueID=? ");
                String taskId = tasksRs.getString("TaskID");
                st2.setString(1, taskId);
                ResultSet taskRs = st2.executeQuery();
                taskRs.next();
                String responsibleUserUsername = taskRs.getString("ResponsibleUser");
                Timestamp postedDate = taskRs.getTimestamp("PostedDate");
                int type = taskRs.getInt("Type");
                Timestamp deadLine = taskRs.getTimestamp("DeadLine");
                String title = taskRs.getString("Title");

                if(responsibleUserUsername == null) responsibleUserUsername = "";

                switch(type) {
                    case 1:
                        tasks.add(new Task.CommonTask(title, postedDate, Optional.ofNullable(deadLine), User.by(responsibleUserUsername)));
                        break;
                    case 2:
                        List<Task.CommonTask> subCommonTasks = new ArrayList<>();
                        try{
                            PreparedStatement commonSubtasksSt = Database.getConnection().prepareStatement(
                                    "SELECT FROM CommonSubtasks WHERE TaskID=? ORDER BY PostedDate");
                            commonSubtasksSt.setString(1, taskId);
                            ResultSet subtasks = commonSubtasksSt.executeQuery();
                            while(subtasks.next()) {
                                String subtasksTitle = subtasks.getString("Title");
                                Timestamp subtasksPostedDate = subtasks.getTimestamp("PostedDate");
                                subCommonTasks.add(new Task.CommonTask(subtasksTitle, subtasksPostedDate, Optional.empty(), Optional.empty()));
                            }
                            tasks.add(new Task.SubTasks(subCommonTasks, title, postedDate, Optional.ofNullable(deadLine), User.by(responsibleUserUsername)));
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case 3:
                        List<Comment> comments = new ArrayList<>();
                        PreparedStatement obsSt = Database.getConnection().prepareStatement(
                                "SELECT FROM TaskObservation WHERE TaskID=? ORDER BY PostedDate");
                        obsSt.setString(1, taskId);
                        ResultSet subtasks = obsSt.executeQuery();
                        while(subtasks.next()) {
                            Timestamp commentPostedDate = subtasks.getTimestamp("PostedDate");
                            String comment = subtasks.getString("Title");
                            comments.add(new Comment(commentPostedDate, comment));
                        }
                        tasks.add(new Task.CommentedTask(comments, title, postedDate, Optional.ofNullable(deadLine), User.by(responsibleUserUsername)));
                        break;
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return tasks;
    }
}
