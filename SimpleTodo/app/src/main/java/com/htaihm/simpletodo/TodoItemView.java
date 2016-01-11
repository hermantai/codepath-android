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
 * A {@link View} that displays a TodoItem inside a {@link ListView}. The idea comes from
 * <a href="https://www.bignerdranch.com/blog/customizing-android-listview-rows-subclassing/">
 * Customizing Android ListView Rows by Subclassing</a>
 */
public class TodoItemView extends RelativeLayout {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private TextView tvItemTextDisplay;
    private TextView tvItemPriorityDisplay;
    private TextView tvItemCreatedTimeDisplay;

    public TodoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TodoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public static TodoItemView inflate(ViewGroup parent) {
        TodoItemView itemView = (TodoItemView) LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_item_todo, parent, false);
        itemView.tvItemTextDisplay = (TextView) itemView.findViewById(R.id.tvItemTextDisplay);
        itemView.tvItemPriorityDisplay = (TextView) itemView.findViewById(
                R.id.tvItemPriorityDisplay);
        itemView.tvItemCreatedTimeDisplay = (TextView) itemView.findViewById(
                R.id.tvItemCreatedTimeDisplay);

        return itemView;
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
