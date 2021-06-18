package net.victorbetoni.synergy.enums;

import java.util.Arrays;

public enum TaskType {
    COMMON(0), SUBTASKS(1), COMMENTED(2);

    private int id;

    TaskType(int id) {
        this.id = id;
    }

    public static TaskType byId(int id) {
        return Arrays.stream(values()).filter(x -> x.getId() == id).findFirst().get();
    }

    public int getId() {return id;}

}
