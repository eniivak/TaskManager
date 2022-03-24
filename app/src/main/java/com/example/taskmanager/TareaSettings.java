package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class TareaSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_settings);
        miDB gestorDB = new miDB(TareaSettings.this);
        Bundle extras= getIntent().getExtras();
        Log.i("ha entrado" , " en la clase TareaSettings");

        ImageButton botonborrar= findViewById(R.id.imageButton);

        //BORRAR TAREA
        botonborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TareaModel tm= new TareaModel(extras.getString("tarea"), extras.getString("usuario"));
                gestorDB.borrarTarea(tm);
                Intent intent= new Intent(TareaSettings.this, MainActivity.class); //para que cuando borres la tarea directamente te lleve al panel de las tareas
                startActivity(intent);
            }
        });


        //GUARDAR DESCRIPCIÓN DE LA TAREA
        Button guardardesc= findViewById(R.id.id_botonguardar_desc);
        guardardesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //coger el texto de la descripción y meterlo en la base de datos
                EditText descrip= findViewById(R.id.id_descrip);
                TareaModel tm= new TareaModel(extras.getString("tarea"),extras.getString("usuario"));
                tm.setDescrip(descrip.getText().toString());
                gestorDB.guardarDesc(tm);
            }
        });


        //VOLVER AL PANEL DE LAS TAREAS
        Button botonvolver= findViewById(R.id.id_boton_volver);
        botonvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(TareaSettings.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }
}