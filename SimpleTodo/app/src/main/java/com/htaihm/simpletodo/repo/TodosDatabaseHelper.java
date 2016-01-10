package com.htaihm.simpletodo.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

/**
 * Keep the Todo items in a SQLite Datbase
 */
public class TodosDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todosDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TODO = "todo";

    // Todo table columns
    private static final String COL_TODO_ID = "id";
    // Timestamps are UNIX timestamps in milliseconds
    private static final String COL_TODO_CREATED_TIME = "created_time";
    private static final String COL_TODO_UPDATED_TIME = "updated_time";
    private static final String COL_TODO_TEXT = "text";


    private static TodosDatabaseHelper instance;

    public static synchronized TodosDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TodosDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public TodosDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = String.format(
                "CREATE TABLE %s(%s STRING PRIMARY KEY, %s INTEGER, %s INTEGER, %s STRING)",
                TABLE_TODO,
                COL_TODO_ID,
                COL_TODO_CREATED_TIME,
                COL_TODO_UPDATED_TIME,
                COL_TODO_TEXT);

        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        }
    }

    public TodoItem addTodo(String text) throws SQLException{
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            String id = UUID.randomUUID().toString();
            long now = System.currentTimeMillis();

            ContentValues cv = new ContentValues();
            cv.put(COL_TODO_ID, id);
            cv.put(COL_TODO_CREATED_TIME, now);
            cv.put(COL_TODO_UPDATED_TIME, now);
            cv.put(COL_TODO_TEXT, text);

            db.insertOrThrow(TABLE_TODO, null, cv);
            db.setTransactionSuccessful();

            GregorianCalendar createdTime = new GregorianCalendar();
            createdTime.setTimeInMillis(now);

            GregorianCalendar updatedTime = new GregorianCalendar();
            updatedTime.setTimeInMillis(now);
            TodoItem todoItem = new TodoItem(id, createdTime, updatedTime, text);
            return todoItem;
//            Log.e(LOG_TAG, "Error saving a todo: " + text, e);
//            Toast.makeText(context, "Error saving the todo: " + e.getLocalizedMessage(),
//                    Toast.LENGTH_LONG);
        } finally {
            db.endTransaction();
        }
    }

    public List<TodoItem> getAllTodos() throws SQLException {
        List<TodoItem> todos = new ArrayList<>();

        String TODO_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_TODO);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TODO_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(COL_TODO_ID));
                    String text = cursor.getString(cursor.getColumnIndex(COL_TODO_TEXT));

                    GregorianCalendar createdTime = new GregorianCalendar();
                    createdTime.setTimeInMillis(
                            cursor.getLong(cursor.getColumnIndex(COL_TODO_CREATED_TIME)));

                    GregorianCalendar updatedTime = new GregorianCalendar();
                    updatedTime.setTimeInMillis(
                            cursor.getLong(cursor.getColumnIndex(COL_TODO_UPDATED_TIME)));

                    TodoItem todoItem = new TodoItem(
                            id,
                            createdTime,
                            updatedTime,
                            text
                    );
                    todos.add(todoItem);
                } while(cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return todos;
    }

    public void updateTodo(TodoItem todoItem) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COL_TODO_UPDATED_TIME, todoItem.getUpdatedTime().getTimeInMillis());
            cv.put(COL_TODO_TEXT, todoItem.getText());

            db.update(TABLE_TODO, cv, "id = ?", new String[]{todoItem.getId()});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteTodo(String id) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_TODO, "id = ?", new String[]{id});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
