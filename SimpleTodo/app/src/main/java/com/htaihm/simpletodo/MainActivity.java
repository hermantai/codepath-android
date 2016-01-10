package com.htaihm.simpletodo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.htaihm.simpletodo.repo.TodoItem;
import com.htaihm.simpletodo.repo.TodosDatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EditItemDialog.ItemAddedListener,
        EditItemDialog.ItemEditedListener {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_todo:
                FragmentManager fm = getSupportFragmentManager();
                EditItemDialog addItemDialog = EditItemDialog.newInstanceForNewItem();
                addItemDialog.show(fm, "fragment_save_item");
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
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
                                    Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        FragmentManager fm = getSupportFragmentManager();
                        EditItemDialog addItemDialog = EditItemDialog.newInstanceForEditItem(
                                items.get(pos), pos);
                        addItemDialog.show(fm, "fragment_save_item");
                    }
                }
        );
    }

    private void readItems() {
        try {
            items = new ArrayList<>(todosDatabaseHelper.getAllTodos());
        } catch (SQLException e) {
            Log.e(TAG_TODOS_REPO, "Error while trying to get todos from database", e);
            Toast.makeText(this, "Error getting todos: " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
            items = new ArrayList<>();
        }
    }

    @Override
    public void onItemAdded(TodoItem todoItem) {
        itemsAdapter.add(todoItem);
    }

    @Override
    public void onItemEdited(TodoItem todoItem, int position) {
        items.set(position, todoItem);
        itemsAdapter.notifyDataSetChanged();

        if (!lastQuery.isEmpty()) {
            // If there is a last query, the user is searching with query. Since the item's
            // text is changed, it may not be in the list any more, so we need to update
            // items again.
            updateItemsWithQuery(lastQuery);
        }
    }
}
