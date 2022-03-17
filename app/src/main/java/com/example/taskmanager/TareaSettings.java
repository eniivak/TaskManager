package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TareaSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_settings);
        miDB gestorDB = new miDB(TareaSettings.this);
        Bundle extras= getIntent().getExtras();

        ImageButton botonborrar= findViewById(R.id.imageButton);
        TareaModel tm= new TareaModel(extras.getString("tarea"), "ena");

        botonborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gestorDB.borrarTarea(tm);

            }
        });
    }
}