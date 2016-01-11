package com.htaihm.simpletodo.repo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class TodoItem implements Serializable {
    public enum Priority {
        LOW(0),
        MEDIUM(1),
        HIGH(2);

        private final int rank;

        Priority(int rank) {
            this.rank = rank;
        }

        int getRank() {
            return this.rank;
        }
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final String id;
    private final GregorianCalendar createdTime;
    private final GregorianCalendar updatedTime;
    private final String text;
    private final Priority priority;

    public TodoItem(String id, GregorianCalendar createdTime, GregorianCalendar updatedTime,
                    String text, Priority priority) {
        this.id = id;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.text = text;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public GregorianCalendar getCreatedTime() {
        return createdTime;
    }

    public GregorianCalendar getUpdatedTime() {
        return updatedTime;
    }

    public String getText() {
        return text;
    }

    public Priority getPriority() {
        return priority;
    }

    public String toString() {
        return String.format("%s - created at %s", text, dateFormat.format(createdTime.getTime()));
    }
}
