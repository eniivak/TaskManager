package com.example.taskmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

        Log.i("ha entrado" , " en la clase MainActivity");
        miDB gestorDB= new miDB(MainActivity.this);
        Bundle extras= getIntent().getExtras();
        Log.i("ha pillado los extras" , extras.getString("user"));
        final ListView listViewWithCheckbox = (ListView)findViewById(R.id.listview);

        // Initiate listview data.

        final List<TareaModel> initItemList = this.display(gestorDB,extras.getString("user"));
        Log.i("ha hecho bien el display" , initItemList.toString());
        // Create a custom list view adapter with checkbox control.
        final AdapterListView listViewDataAdapter = new AdapterListView(getApplicationContext(), initItemList);

        //final ArrayList<Boolean> checkedarray=null;
        listViewDataAdapter.notifyDataSetChanged();

        // Set data adapter to list view.
        listViewWithCheckbox.setAdapter(listViewDataAdapter);

        listViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);

                // Translate the selected item to DTO object.
                TareaModel itemDto = (TareaModel) itemObject;

                // Get the checkbox.
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.checkbox);


                // Reverse the checkbox and clicked item check state.
                if(itemDto.isChecked())
                {
                    itemCheckbox.setChecked(false);
                    itemDto.setChecked(false);
                }else
                {
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                }

                //Toast.makeText(getApplicationContext(), "select item text : " + itemDto.getItemText(), Toast.LENGTH_SHORT).show();
            }
        });

        //BOTÓN DE MARCAR QUE LA TAREA ESTÁ HECHA
        Button marcarHecha= (Button) findViewById(R.id.list_marcar_hecha);
        marcarHecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    int size = initItemList.size();
                    for(int i=0;i<size;i++)
                    {
                        TareaModel dto = initItemList.get(i);

                        if(dto.isChecked())
                        {


                            TextView tv= (TextView) view;
                            tv.setPaintFlags( ~Paint.STRIKE_THRU_TEXT_FLAG);
                           // getItem(position).setChecked( ~Paint.STRIKE_THRU_TEXT_FLAG);
                            //row.setPaintFlags(row.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                            //dto.setChecked(false);
                        }else {
                            dto.setChecked(true);
                        }
                    }

                    listViewDataAdapter.notifyDataSetChanged();
                }

        });

        // LISTENER DE ABRIR EL SETTINGS
        Button selectAllButton = (Button)findViewById(R.id.list_abrir_config);

        selectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView= (TextView) view;
                //textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Intent intent= new Intent(MainActivity.this, TareaSettings.class);

                    int size = initItemList.size();
                    for(int i=0;i<size;i++)
                    {
                        TareaModel dto = initItemList.get(i);

                        if(dto.isChecked())
                        {
                            Log.i("la tarea de la que conseguimos los settings", initItemList.get(i).getNombre());
                            intent.putExtra("tarea",initItemList.get(i).getNombre()); // para conseguir el nombre del usuario ingresado en el login al cargar el MainActivity
                            intent.putExtra("usuario",extras.getString("user"));
                            startActivity(intent);

                            dto.setChecked(false);
                        }else {
                            dto.setChecked(true);
                        }
                    }

                    listViewDataAdapter.notifyDataSetChanged();



                //toDoList.clear();
                //display(gestorDB,extras.getString("user"));// si haces esto se printean dos veces los elementos en el listview, habra que encontrar la manera de updatearlo

               /* int size = initItemList.size();
                for(int i=0;i<size;i++)
                {
                    ListViewItemDTO dto = initItemList.get(i);
                    dto.setChecked(true);
                }

                listViewDataAdapter.notifyDataSetChanged();*/
            }
        });



/*
       toDoList= new ArrayList<>();
        arrayAdapter= new ArrayAdapter<>(this,R.layout.list_view_layout,toDoList);
        listView = findViewById(R.id.);
        editText= findViewById(R.id.id_edit_text);
        listView.setAdapter(arrayAdapter);


        //DISPLAY ALL
        display(gestorDB,extras.getString("user"));

        //ABRIR TAREASETTINGS CUANDO SE TOCA UN ELEMENTO DEL LISTVIEW
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //toDoList.removeAll(Arrays.asList(finalListaTareas));
                TextView textView= (TextView) view;
                //textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Intent intent= new Intent(MainActivity.this, TareaSettings.class);
                intent.putExtra("tarea",textView.getText()); // para conseguir el nombre del usuario ingresado en el login al cargar el MainActivity
                intent.putExtra("usuario",extras.getString("user"));
                startActivity(intent);

                toDoList.clear();
                display(gestorDB,extras.getString("user"));// si haces esto se printean dos veces los elementos en el listview, habra que encontrar la manera de updatearlo

            }
        });

*/

        //LISTENER DEL BOTON AÑADIR
        Button botonanadir= findViewById(R.id.id_boton_anadir);
        botonanadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToList(v,gestorDB,extras.getString("user"));
                Log.i("ha pulsado" , " boton añadir");
               listViewDataAdapter.notifyDataSetChanged();


            }
        });




    }

    private List<TareaModel> display(miDB gestorDB, String usuario){
        final ArrayList<TareaModel>[] listaTareas = new ArrayList[]{gestorDB.displayAll(usuario)};
        List<TareaModel> ret = new ArrayList<TareaModel>();
        for (int i = 0; i< listaTareas[0].size(); i++){
            Log.i("tarea"," "+ listaTareas[0].size());
            TareaModel tm = new TareaModel(listaTareas[0].get(i).getNombre(),usuario);
            tm.setChecked(false);
            tm.setItemText(tm.getNombre());
            ret.add(tm);
        }
        return ret;
    }

    public void addItemToList(View view, miDB gestorDB,String usuario){
        Log.i("1", "ha entrado");
        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
        adb.setTitle("Añadir?");
        adb.setMessage("Seguro que quieres añadir la tarea?");
        adb.setNegativeButton("Cancelar", null);
        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /*toDoList.add(editText.getText().toString());
                arrayAdapter.notifyDataSetChanged();*/
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