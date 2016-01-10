package com.htaihm.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.htaihm.simpletodo.repo.TodoItem;
import com.htaihm.simpletodo.repo.TodosDatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EDIT_EXTRA_TEXT = "item_text";
    public static final String EDIT_EXTRA_POS = "item_pos";
    public static final int EDIT_REQUEST_CODE = 20;

    private static final String TAG_TODOS_REPO = "TodosDatabaseRepoError";

    private ArrayList<TodoItem> items;
    private ArrayAdapter<TodoItem> itemsAdapter;
    private ListView lvItems;
    private TodosDatabaseHelper todosDatabaseHelper;

    // Store the last query from Search
    private String lastQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todosDatabaseHelper = TodosDatabaseHelper.getInstance(this);

        lvItems = (ListView)findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todos_search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        lastQuery = newText;
                        updateItemsWithQuery(newText);
                        return true;
                    }
                }
        );
        return true;
    }

    private void updateItemsWithQuery(String query) {
        List<TodoItem> newItems = todosDatabaseHelper.searchTodos(query);
        items.clear();
        items.addAll(newItems);
        itemsAdapter.notifyDataSetChanged();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view, int pos, long id) {
                        try {
                            todosDatabaseHelper.deleteTodo(items.get(pos).getId());
                            items.remove(pos);
                            itemsAdapter.notifyDataSetChanged();
                        } catch (SQLException e) {
                            Log.e(TAG_TODOS_REPO, "Error deleting a todo: " + id, e);
                            Toast.makeText(MainActivity.this,
                                    "Error deleting the todo: " + e.getLocalizedMessage(),
                                    Toast.LENGTH_LONG);
                        }
                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        // start the EditItemActivity
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra(EDIT_EXTRA_TEXT, items.get(pos).getText());
                        i.putExtra(EDIT_EXTRA_POS, pos);

                        startActivityForResult(i, EDIT_REQUEST_CODE);
                    }
                }
        );
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        try {
            TodoItem todoItem = todosDatabaseHelper.addTodo(itemText);
            itemsAdapter.add(todoItem);
            etNewItem.setText("");
        } catch (SQLException e) {
            Log.e(TAG_TODOS_REPO, "Error saving a todo: " + itemText, e);
            Toast.makeText(this, "Error saving the todo: " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            String itemText = data.getStringExtra(MainActivity.EDIT_EXTRA_TEXT);
            int pos = data.getIntExtra(MainActivity.EDIT_EXTRA_POS, -1);

            if (pos != -1) {
                TodoItem oldTodoItem = items.get(pos);
                GregorianCalendar updatedTime = new GregorianCalendar();
                updatedTime.setTimeInMillis(System.currentTimeMillis());
                TodoItem newTodoItem = new TodoItem(
                        oldTodoItem.getId(),
                        oldTodoItem.getCreatedTime(),
                        updatedTime,
                        itemText
                );
                try {
                    todosDatabaseHelper.updateTodo(newTodoItem);
                    items.set(pos, newTodoItem);
                    itemsAdapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    Log.e(TAG_TODOS_REPO, "Error updating a todo: " + newTodoItem, e);
                    Toast.makeText(this, "Error updating the todo: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG);
                }
                if (!lastQuery.isEmpty()) {
                    // If there is a last query, the user is searching with query. Since the item's
                    // text is changed, it may not be in the list any more, so we need to update
                    // items again.
                    updateItemsWithQuery(lastQuery);
                }
            }
        }
    }

    private void readItems() {
        try {
            items = new ArrayList<>(todosDatabaseHelper.getAllTodos());
        } catch (SQLException e) {
            Log.e(TAG_TODOS_REPO, "Error while trying to get todos from database", e);
            Toast.makeText(this, "Error getting todos: " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG);
            items = new ArrayList<>();
        }
    }
}
