package com.htaihm.simpletodo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.htaihm.simpletodo.repo.TodoItem;

import java.util.List;

/**
 * An {@link ArrayAdapter} for the {@link ListView} to display todo items.
 */
public class TodoItemsAdapter extends ArrayAdapter<TodoItem> {
    public TodoItemsAdapter(Context context, List<TodoItem> data) {
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoItemView itemView = (TodoItemView) convertView;
        if (itemView == null) {
            itemView = TodoItemView.inflate(parent);
        }

        itemView.setItem(getItem(position));

        return itemView;
    }
}
