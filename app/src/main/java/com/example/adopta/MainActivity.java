package com.example.adopta;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText nombre,cedula, clave, clavea;
    Button ingresar, registrarse;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre=findViewById(R.id.nombreExtensionista);
        cedula = findViewById(R.id.cedula);
        clave = findViewById(R.id.clave);
        clavea = findViewById(R.id.clavea);
        registrarse = findViewById(R.id.registrarse);
        ingresar = findViewById(R.id.ingresar);
        DB = new DBHelper(this, "recordv", null, 1);



        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom=nombre.getText().toString();
                String ced=cedula.getText().toString();
                String cl=clave.getText().toString();
                String cla=clavea.getText().toString();

                if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(ced) || TextUtils.isEmpty(cl) || TextUtils.isEmpty(cla))
                    Toast.makeText(MainActivity.this, "LLENAR TODOS LOS CAMPOS!", Toast.LENGTH_SHORT).show();
                else{
                    if (cl.equals(cla)){
                        Boolean validacedu = DB.validarCedula(cl);
                        if (validacedu==false){
                            Boolean insertar = DB.insertarDatos(nom,ced,cl);
                            if (insertar==true){
                                Toast.makeText(MainActivity.this, "REGISTRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Consulta.class);
                                startActivity(intent);

                            }else{
                                Toast.makeText(MainActivity.this, "REGISTRO FALLIDO", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "USUARIO EXISTENTE", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "LA CONFIRMACION DE CONTRASEÑA ES ERRONEA", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencion = new Intent(getApplicationContext(),login.class);
                startActivity(intencion);
            }
        });


    }





}