package com.example.adopta;


import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    EditText cedula,clave;
    Button ingresar;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        cedula = findViewById(R.id.cedula1);
        clave = findViewById(R.id.clave1);
        ingresar=findViewById(R.id.ingresar1);
        DB = new DBHelper(this, "recordv", null, 1);
        cargar();

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ced=cedula.getText().toString();
                String cl=clave.getText().toString();

                if (TextUtils.isEmpty(ced) || TextUtils.isEmpty(cl))
                    Toast.makeText(login.this, "LLENAR TODOS LOS CAMPOS!", Toast.LENGTH_SHORT).show();
                else {
                    Boolean validarcedcv= DB.validarCedulaClave(ced,cl);
                    if (validarcedcv==true){
                        Toast.makeText(login.this, "HAS INICIADO SESION!", Toast.LENGTH_SHORT).show();
                        Intent intencion = new Intent(getApplicationContext(),Consulta.class);
                        startActivity(intencion);

                    }else{
                        Toast.makeText(login.this, "ERROR AL INICIAR SESION", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void cargar(){
        Cursor cursor= DB.getReadableDatabase().rawQuery("select cedula, clave from usuarios",null);
        while (cursor.moveToNext()){
            String user=cursor.getString(0);
            String ced=cursor.getString(1);
            cedula.setText(user);
            clave.setText(ced);

        }
    }


}