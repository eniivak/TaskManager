package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        miDB gestorDB= new miDB(Login.this);
        Button botonlogin=  findViewById(R.id.button_login);
        EditText textousuario= findViewById(R.id.text_usuario);
        EditText textocontra= findViewById(R.id.text_contra);
        botonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Usuario usuario= new Usuario(textousuario.getText().toString(),textocontra.getText().toString());
                if(gestorDB.existeUsuario(usuario)){ //el usuario esta creado
                    //aqui dentro tengo que mirar que la contraseña este bien
                    Intent i= new Intent(Login.this, MainActivity.class);

                    i.putExtra("user", textousuario.getText().toString()    ); // para conseguir el nombre del usuario ingresado en el login al cargar el MainActivity
                    startActivity(i);
                }
                //gestorDB.añadirUsuario(usuario);
                Log.i("3","pues no esta creado");

            }
        });


    }
}