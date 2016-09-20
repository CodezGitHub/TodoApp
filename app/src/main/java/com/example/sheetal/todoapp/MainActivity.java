package com.example.sheetal.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAddItem;
    ListView lvItems;
    EditText etEditText;
    ArrayList<String> todoItems;
    ArrayAdapter<String> aTodoAdapter;
    private final int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        etEditText = (EditText) findViewById(R.id.etEditText);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);

        populateArrayItems();

        lvItems.setAdapter(aTodoAdapter);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTodoItem();
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                todoItems.remove(position);
                aTodoAdapter.notifyDataSetChanged();
                writeItems();

                return true;
            }
        });

        //Edit Item
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this,EditItemActivity.class);
                intent.putExtra("Text", lvItems.getItemAtPosition(position).toString());
                intent.putExtra("Position", position);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            String newText = data.getStringExtra("NewText");
            int position = data.getIntExtra("Position",0);

            todoItems.set(position,newText);
            aTodoAdapter.notifyDataSetChanged();
            writeItems();
        }

    }

    public void populateArrayItems()
    {
        readItems();
        aTodoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    public void addTodoItem()
    {
        todoItems.add(etEditText.getText().toString());
        aTodoAdapter.notifyDataSetChanged();
        etEditText.setText("");
        writeItems();
    }

    public void readItems()
    {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir,"todo.txt");

        try {

            todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
        }catch (IOException e)
        {
            todoItems = new ArrayList<String>();
            e.printStackTrace();
        }
    }

    public void writeItems()
    {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir,"todo.txt");

        try {
            FileUtils.writeLines(todoFile, todoItems);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
