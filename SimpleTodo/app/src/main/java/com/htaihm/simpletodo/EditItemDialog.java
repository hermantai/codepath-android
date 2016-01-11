package com.htaihm.simpletodo;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.htaihm.simpletodo.repo.TodoItem;
import com.htaihm.simpletodo.repo.TodosDatabaseHelper;

import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.UUID;


public class EditItemDialog extends DialogFragment {
    private static final String TAG_TODOS_REPO = "TodosDatabaseRepoError";

    private EditText etItemText;
    private Button btnSaveItem;
    private Spinner spinnerPriority;

    private boolean isNewItem;
    private int itemPos = -1;
    private TodoItem oldItem;

    public interface ItemAddedListener {
        void onItemAdded(TodoItem todoItem);
    }

    public interface ItemEditedListener {
        void onItemEdited(TodoItem todoItem, int position);
    }

    public EditItemDialog() {
        // Required empty public constructor
    }

    public static EditItemDialog newInstanceForEditItem(TodoItem oldItem, int position) {
        EditItemDialog frag = new EditItemDialog();
        Bundle args = new Bundle();
        args.putBoolean("isNewItem", false);
        args.putInt("itemPos", position);
        args.putSerializable("oldItem", oldItem);
        frag.setArguments(args);

        return frag;
    }

    public static EditItemDialog newInstanceForNewItem() {
        EditItemDialog frag = new EditItemDialog();
        Bundle args = new Bundle();
        args.putBoolean("isNewItem", true);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_save_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etItemText = (EditText) view.findViewById(R.id.etItemText);

        btnSaveItem = (Button) view.findViewById(R.id.btnSaveItem);
        btnSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveItem();
            }
        });

        spinnerPriority = (Spinner) view.findViewById(R.id.spinnerPriority);
        ArrayAdapter<TodoItem.Priority> prioritySpinnerAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                TodoItem.Priority.values());
        prioritySpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(prioritySpinnerAdapter);

        isNewItem = getArguments().getBoolean("isNewItem");

        if (isNewItem) {
            getDialog().setTitle("New Todo");
        } else {
            itemPos = getArguments().getInt("itemPos", -1);
            oldItem = (TodoItem) getArguments().getSerializable("oldItem");
            String oldText = oldItem.getText();
            etItemText.setText(oldText);
            etItemText.requestFocus();
            etItemText.setSelection(oldText.length());

            int posForSpinnerPriority = -1;
            TodoItem.Priority[] allPriorities = TodoItem.Priority.values();

            for (int i = 0; i < allPriorities.length; i++) {
                if (oldItem.getPriority() == allPriorities[i]) {
                    posForSpinnerPriority = i;
                    break;
                }
            }
            if (posForSpinnerPriority != -1) {
                spinnerPriority.setSelection(posForSpinnerPriority);
            }

            getDialog().setTitle("Edit Todo");
        }
    }

    public void onSaveItem() {
        String itemText = etItemText.getText().toString();
        TodoItem.Priority priority = (TodoItem.Priority) spinnerPriority.getSelectedItem();
        try {
            if (isNewItem) {
                GregorianCalendar now = new GregorianCalendar();
                TodoItem todoItem = new TodoItem(UUID.randomUUID().toString(), now, now, itemText,
                        priority);
                TodosDatabaseHelper.getInstance(getActivity()).addTodo(todoItem);

                ItemAddedListener itemAddedListener = (ItemAddedListener) getActivity();
                itemAddedListener.onItemAdded(todoItem);
            } else {
                GregorianCalendar updatedTime = new GregorianCalendar();
                updatedTime.setTimeInMillis(System.currentTimeMillis());


                TodoItem newTodoItem = new TodoItem(
                        oldItem.getId(),
                        oldItem.getCreatedTime(),
                        updatedTime,
                        itemText,
                        priority
                );
                TodosDatabaseHelper.getInstance(getActivity()).updateTodo(newTodoItem);

                ItemEditedListener itemEditedListener = (ItemEditedListener) getActivity();
                itemEditedListener.onItemEdited(newTodoItem, itemPos);
            }
            dismiss();
        } catch (SQLException e) {
            Log.e(TAG_TODOS_REPO, "Error saving a todo: " + itemText, e);
            Toast.makeText(getActivity(), "Error saving the todo: " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
