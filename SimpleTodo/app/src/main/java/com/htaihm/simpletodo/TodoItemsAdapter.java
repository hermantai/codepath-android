package com.htaihm.simpletodo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.htaihm.simpletodo.repo.TodoItem;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * An {@link ArrayAdapter} for the {@link ListView} to display todo items.
 */
public class TodoItemsAdapter extends ArrayAdapter<TodoItem> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final Context context;
    private final int layoutResourceId;
    private final List<TodoItem> todoItems;

    public TodoItemsAdapter(Context context, int resource, List<TodoItem> data) {
        super(context, resource, data);

        this.context = context;
        this.layoutResourceId = resource;
        this.todoItems = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tvItemTextDisplay;
        TextView tvItemPriorityDisplay;
        TextView tvItemCreatedTimeDisplay;

        if (convertView == null) {
            LayoutInflater inflator = ((Activity) context).getLayoutInflater();
            convertView = inflator.inflate(layoutResourceId, parent, false);
        }

        tvItemTextDisplay = (TextView) convertView.findViewById(R.id.tvItemTextDisplay);
        tvItemPriorityDisplay = (TextView) convertView.findViewById(R.id.tvItemPriorityDisplay);
        tvItemCreatedTimeDisplay = (TextView) convertView.findViewById(
                R.id.tvItemCreatedTimeDisplay);

        TodoItem item = todoItems.get(position);
        tvItemTextDisplay.setText(item.getText());
        tvItemPriorityDisplay.setText(item.getPriority().toString());
        tvItemCreatedTimeDisplay.setText(
                "Created at " + dateFormat.format(item.getCreatedTime().getTime()));

        switch (item.getPriority()) {
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

        return convertView;
    }
}
