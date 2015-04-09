package techiewiz.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import techiewiz.simpletodo.db.TodoDBHelper;
import techiewiz.simpletodo.model.Todo;


public class EditTodo extends Activity {

    TodoDBHelper db;
    Todo selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittodo);
        db = new TodoDBHelper(this);
        Intent intent = getIntent();
        if (null != intent) {
            selectedItem = (Todo)getIntent().getSerializableExtra("todoObj");
            EditText text = (EditText) findViewById(R.id.etNewItem);
            EditText desc = (EditText) findViewById(R.id.etNewItemDesc);
            text.setText(selectedItem.getTitle());
            desc.setText(selectedItem.getDescription());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edittodo, menu);
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

    public void editTodo(View view) {
        EditText text = (EditText) findViewById(R.id.etNewItem);
        EditText desc = (EditText) findViewById(R.id.etNewItemDesc);
        selectedItem.setTitle(text.getText().toString());
        selectedItem.setDescription(desc.getText().toString());
        db.updateTodo(selectedItem);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cancelAction(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
