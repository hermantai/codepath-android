package com.htaihm.simpletodo.repo;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class TodoItem {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final String id;
    private final GregorianCalendar createdTime;
    private final GregorianCalendar updatedTime;
    private final String text;

    public TodoItem(String id, GregorianCalendar createdTime, GregorianCalendar updatedTime,
                    String text) {
        this.id = id;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.text = text;
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

    public String toString() {
        return String.format("%s - created at %s", text, dateFormat.format(createdTime.getTime()));
    }
}
