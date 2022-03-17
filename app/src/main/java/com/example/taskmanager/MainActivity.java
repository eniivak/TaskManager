package com.example.taskmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Arrays;
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
        Bundle extras= getIntent().getExtras();


        toDoList= new ArrayList<>();
        arrayAdapter= new ArrayAdapter<>(this,R.layout.list_view_layout,toDoList);
        listView = findViewById(R.id.id_list_view);
        editText= findViewById(R.id.id_edit_text);
        listView.setAdapter(arrayAdapter);


        //DISPLAY ALL
        display(gestorDB,extras.getString("user"));

        //ABRIR TAREASETTINGS CUANDO SE TOCA UN ELEMENTO DEL LISTVIEW
       // final ArrayList<TareaModel>[] finalListaTareas = new ArrayList[]{listaTareas[0]};
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //toDoList.removeAll(Arrays.asList(finalListaTareas));
                TextView textView= (TextView) view;
                /*textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);*/
                Intent intent= new Intent(MainActivity.this, TareaSettings.class);
                intent.putExtra("tarea",textView.getText()); // para conseguir el nombre del usuario ingresado en el login al cargar el MainActivity
                intent.putExtra("usuario",extras.getString("user"));
                startActivity(intent);

                toDoList.clear();
                display(gestorDB,extras.getString("user"));// si haces esto se printean dos veces los elementos en el listview, habra que encontrar la manera de updatearlo

            }
        });



        //LISTENER DEL BOTON AÑADIR
        Button botonanadir= findViewById(R.id.id_boton_anadir);
        botonanadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToList(v,gestorDB,extras.getString("user"));

            }
        });



    }

    private void display(miDB gestorDB,String usuario){
        final ArrayList<TareaModel>[] listaTareas = new ArrayList[]{gestorDB.displayAll(usuario)};
        for (int i = 0; i< listaTareas[0].size(); i++){
            Log.i("tarea"," "+ listaTareas[0].size());
            TareaModel tm = new TareaModel(listaTareas[0].get(i).getNombre(),usuario);
            toDoList.add(tm.getNombre());
            arrayAdapter.notifyDataSetChanged();

        }
    }

    public void addItemToList(View view, miDB gestorDB,String usuario){
        Log.i("1", "ha entrado");
        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
        adb.setTitle("Añadir?");
        adb.setMessage("Seguro que quieres añadir la tarea?");
        adb.setNegativeButton("Cancelar", null);
        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                toDoList.add(editText.getText().toString());
                arrayAdapter.notifyDataSetChanged();
                TareaModel tm= new TareaModel(editText.getText().toString(), usuario);
                boolean ex=gestorDB.añadirTarea(tm);
                Log.i("2", String.valueOf(ex));
                editText.setText(" ");
            }});
        adb.show();

    }
   /* public void deleteItem(View view, miDB gestorDB){

        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
        adb.setTitle("Delete?");
        adb.setMessage("Are you sure you want to delete ?");
        adb.setNegativeButton("Cancel", null);
        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                toDoList.remove(1);
                arrayAdapter.notifyDataSetChanged();
                TareaModel tm= new TareaModel(editText.getText().toString());
                gestorDB.borrarTarea(tm);
                Log.i("1", String.valueOf(succes));
                //hay que añadir gestorDB remove
            }});
        adb.show();

    }*/

}