package com.example.taskmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> toDoList;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    EditText editText;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        miDB gestorDB= new miDB(MainActivity.this);
        Login log= new Login();
       /* ArrayList<TareaModel> lista=gestorDB.readCourses();
        Log.i("1",lista.toString());
        Log.i("2","algo");*/
        toDoList= new ArrayList<>();
        arrayAdapter= new ArrayAdapter<>(this,R.layout.list_view_layout,toDoList);
        listView = findViewById(R.id.id_list_view);
        editText= findViewById(R.id.id_edit_text);
        listView.setAdapter(arrayAdapter);

        //TACHAR EL TASK AL PULSARLO
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView= (TextView) view;
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });


        //LISTENER DEL BOTON AÑADIR
        Button botonanadir= findViewById(R.id.id_boton_anadir);
        botonanadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToList(v,gestorDB);

            }
        });


        //LISTENER DEL BOTON BORRAR
        Button botonborrar= findViewById(R.id.id_boton_borrar);
        botonborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteItem(v,gestorDB);
            }
        });



    }
    /*

     */
    public void addItemToList(View view, miDB gestorDB){
        Log.i("1", "ha entrado");
        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
        adb.setTitle("Añadir?");
        adb.setMessage("Seguro que quieres añadir la tarea?");
        adb.setNegativeButton("Cancelar", null);
        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                toDoList.add(editText.getText().toString());
                arrayAdapter.notifyDataSetChanged();
                TareaModel tm= new TareaModel(editText.getText().toString());
                boolean ex=gestorDB.añadirTarea(tm);
                Log.i("2", String.valueOf(ex));
                editText.setText(" ");
            }});
        adb.show();

    }
    public void deleteItem(View view, miDB gestorDB){

        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
        adb.setTitle("Delete?");
        adb.setMessage("Are you sure you want to delete ?");
        adb.setNegativeButton("Cancel", null);
        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                toDoList.remove(1);
                arrayAdapter.notifyDataSetChanged();
                TareaModel tm= new TareaModel(editText.getText().toString());
                boolean succes=gestorDB.borrar(tm);
                Log.i("1", String.valueOf(succes));
                //hay que añadir gestorDB remove
            }});
        adb.show();

    }

}