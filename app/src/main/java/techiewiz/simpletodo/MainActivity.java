package techiewiz.simpletodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import techiewiz.simpletodo.db.TodoDBHelper;
import techiewiz.simpletodo.model.Todo;


public class MainActivity extends Activity {

    final Context context = this;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    int selectedPos;
    String selectedItem;
    TodoDBHelper db;
    List<Todo> todoList;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new TodoDBHelper(this);

        lvItems = (ListView) findViewById(R.id.lvItems);
        todoList = db.getAllTodo();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,todoList);
        lvItems.setAdapter(adapter);

        setupListViewListener();
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Todo itemTodo = todoList.get(position);

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Confirm deletion of " + itemTodo.getTitle()+"?");

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.deleteTodo(itemTodo);
                        adapter.remove(itemTodo);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Deleted Todo "+itemTodo.getTitle(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                final Todo itemTodo = todoList.get(position);

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Todo");
                //alert.setMessage(itemTodo.getTitle() + "\n" + itemTodo.getDescription());

                final EditText etTitle = new EditText(context);
                etTitle.setText(itemTodo.getTitle());
                layout.addView(etTitle);

                final EditText etDesc = new EditText(context);
                etDesc.setText(itemTodo.getDescription());
                layout.addView(etDesc);

                alert.setView(layout);

                alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        /*
                        Bundle b = new Bundle();
                        b.putSerializable("todoObj", itemTodo);
                        Intent todoList = new Intent(context, EditTodo.class);
                        Intent intent = todoList.putExtras(b);
                        startActivity(todoList);
                        */
                        itemTodo.setTitle(etTitle.getText().toString());
                        itemTodo.setDescription(etDesc.getText().toString());
                        db.updateTodo(itemTodo);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Updated Todo "+itemTodo.getTitle(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        Intent intent = new Intent(this, NewTodo.class);
        startActivity(intent);
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir,"todo.txt");
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch(IOException e){
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
