package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class TareaSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_settings);
        miDB gestorDB = new miDB(TareaSettings.this);
        Bundle extras= getIntent().getExtras();

        ImageButton botonborrar= findViewById(R.id.imageButton);


        botonborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TareaModel tm= new TareaModel(extras.getString("tarea"), extras.getString("usuario"));
                gestorDB.borrarTarea(tm);

            }
        });

        Button botonvolver= findViewById(R.id.id_boton_volver);
        botonvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(TareaSettings.this, MainActivity.class);
                //intent.putExtra("tarea",textView.getText()); // para conseguir el nombre del usuario ingresado en el login al cargar el MainActivity
                //intent.putExtra("usuario",extras.getString("user"));
                startActivity(intent);
                //gestorDB.displayAll(extras.getString("usuario"));
            }
        });

    }
}