package techiewiz.simpletodo.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import techiewiz.simpletodo.model.Todo;

public class TodoDBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TodoDB";

    private static final String TABLE_TODO = "todo";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_DESCRIPTION};

    public TodoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE "+TABLE_TODO+
                " ( " + KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                      KEY_TITLE+ " TEXT, "+
                      KEY_DESCRIPTION+ " TEXT )";

        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        this.onCreate(db);
    }



    public void addTodo(Todo item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, item.getTitle());
        values.put(KEY_DESCRIPTION, item.getDescription());
        db.insert(TABLE_TODO, null, values);
        db.close();
    }

    public Todo getTodo(int id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODO,
                        COLUMNS,
                        " id = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null,
                        null);

        if (cursor != null)
            cursor.moveToFirst();

        Todo item = new Todo();
        item.setId(Integer.parseInt(cursor.getString(0)));
        item.setTitle(cursor.getString(1));
        item.setDescription(cursor.getString(2));
        return item;
    }

    public List<Todo> getAllTodo() {
        List<Todo> todos = new LinkedList<Todo>();

        String query = "SELECT  * FROM " + TABLE_TODO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Todo item = null;
        if (cursor.moveToFirst()) {
            do {
                item = new Todo();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setTitle(cursor.getString(1));
                item.setDescription(cursor.getString(2));

                todos.add(item);
            } while (cursor.moveToNext());
        }
        return todos;
    }

    public int updateTodo(Todo item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, item.getTitle());
        values.put(KEY_DESCRIPTION, item.getDescription());

        int i = db.update(TABLE_TODO,
                values,
                KEY_ID+" = ?",
                new String[] { String.valueOf(item.getId()) });

        db.close();
        return i;
    }

    public void deleteTodo(Todo item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO,
                KEY_ID+" = ?",
                new String[] { String.valueOf(item.getId()) });
        db.close();
    }
}
