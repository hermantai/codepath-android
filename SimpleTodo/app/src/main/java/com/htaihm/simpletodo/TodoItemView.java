package com.htaihm.simpletodo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htaihm.simpletodo.repo.TodoItem;

import java.text.SimpleDateFormat;

/**
 * A {@link View} that displays a TodoItem inside a {@link ListView}
 */
public class TodoItemView extends RelativeLayout {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private TextView tvItemTextDisplay;
    private TextView tvItemPriorityDisplay;
    private TextView tvItemCreatedTimeDisplay;

    public TodoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflateChildren(context);
    }

    public TodoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void inflateChildren(Context context) {
        LayoutInflater.from(context).inflate(R.layout.list_item_todo_child, this, true);
        tvItemTextDisplay = (TextView) findViewById(R.id.tvItemTextDisplay);
        tvItemPriorityDisplay = (TextView) findViewById(R.id.tvItemPriorityDisplay);
        tvItemCreatedTimeDisplay = (TextView) findViewById(R.id.tvItemCreatedTimeDisplay);
    }

    public static TodoItemView inflate(ViewGroup parent) {
        return (TodoItemView) LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_item_todo, parent, false);
    }

    public void setItem(TodoItem todoItem) {
        tvItemTextDisplay.setText(todoItem.getText());
        tvItemPriorityDisplay.setText(todoItem.getPriority().toString());
        tvItemCreatedTimeDisplay.setText(
                "Created at " + dateFormat.format(todoItem.getCreatedTime().getTime()));

        switch (todoItem.getPriority()) {
            case LOW:
                tvItemPriorityDisplay.setTextColor(Color.GREEN);
                break;
            case MEDIUM:
                tvItemPriorityDisplay.setTextColor(Color.rgb(255, 99, 71));  // Orange
                break;
            case HIGH:
                tvItemPriorityDisplay.setTextColor(Color.RED);
                break;
        }
    }
}
