package com.example.adopta;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileWriter;

public class  Consulta extends AppCompatActivity {
    Button btn_agregar, btn_salvarbd, listar,visitaNueva,consultara,exportarvisita,genpdf,backup;
    DBHelper admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        btn_agregar = findViewById(R.id.btn_agregar);
        btn_salvarbd = findViewById(R.id.btn_salvarbd);
        listar=findViewById(R.id.consultar);
        visitaNueva=findViewById(R.id.visitaNueva);
        consultara=findViewById(R.id.consultara);
        exportarvisita=findViewById(R.id.exportarvisitas);
        genpdf=findViewById(R.id.genpdf);


        checkExternalStoragePermission();
        backupBD();

        admin = new DBHelper(this, "recordv", null, 1);

        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intencion);
            }
        });

        btn_salvarbd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportarCSV();

            }
        });

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenciona = new Intent(getApplicationContext(), Vista.class);
                startActivity(intenciona);
            }
        });

        visitaNueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenciona = new Intent(getApplicationContext(), insercion.class);
                startActivity(intenciona);
            }
        });

        consultara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenciona = new Intent(getApplicationContext(), VistaVisitas.class);
                startActivity(intenciona);
            }
        });

        exportarvisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportarDatosbCSV();
            }
        });

        genpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenciona = new Intent(getApplicationContext(), generarPDF.class);
                startActivity(intenciona);
            }
        });

    }

    //FUNCION PEDIR PERMISOS
    private void checkExternalStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para leer!");
        }
    }

    private  void backupBD(){
        admin = new DBHelper(this, "recordv", null, 1);
        admin.exportDB();

    }


    //FUNCION EXPORTAR EN CSV
    public void exportarCSV() {



        File carpeta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/ReporteEnExcel");
        String archivoAgenda = carpeta.toString() + "/" + "Registro.csv";

        //File carpeta = new File(getExternalFilesDir(null).getAbsolutePath()+"/ExportarSQLiteCSV");
        //String archivoAgenda = carpeta.toString() + "/" + "Usuarios.csv";

        boolean isCreate = false;
        if(!carpeta.exists()) {
            isCreate = carpeta.mkdir();
        }

        try {
            FileWriter fileWriter = new FileWriter(archivoAgenda);
            SQLiteDatabase db = admin.getWritableDatabase();
            Cursor fila = db.rawQuery("select * from datos", null);

            if(fila != null && fila.getCount() != 0) {
                fila.moveToFirst();
                do {

                    fileWriter.append(fila.getString(0));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(1));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(2));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(3));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(4));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(5));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(6));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(7));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(8));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(9));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(10));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(11));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(12));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(13));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(14));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(15));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(16));

                } while(fila.moveToNext());
            } else {
                Toast.makeText(Consulta.this, "No hay registros.", Toast.LENGTH_LONG).show();
            }

            db.close();
            fileWriter.close();
            Toast.makeText(Consulta.this, "SE CREO EL ARCHIVO CSV EXITOSAMENTE", Toast.LENGTH_LONG).show();

        } catch (Exception e) { }
    }


    public void exportarDatosbCSV() {

        File carpeta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/ReporteEnExcel");
        String archivoAgenda = carpeta.toString() + "/" + "Visitas.csv";
        //File carpeta = new File(getExternalFilesDir(null).getAbsolutePath()+"/ExportarSQLiteCSV");
        //String archivoAgenda = carpeta.toString() + "/" + "Visitas.csv";

        boolean isCreate = false;
        if(!carpeta.exists()) {
            isCreate = carpeta.mkdir();
        }

        try {
            FileWriter fileWriter = new FileWriter(archivoAgenda);
            SQLiteDatabase db = admin.getWritableDatabase();
            Cursor fila = db.rawQuery("select * from datosb", null);

            if(fila != null && fila.getCount() != 0) {
                fila.moveToFirst();
                do {

                    fileWriter.append(",");
                    fileWriter.append(fila.getString(0));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(1));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(2));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(3));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(4));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(5));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(6));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(7));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(8));

                } while(fila.moveToNext());
            } else {
                Toast.makeText(Consulta.this, "No hay registros", Toast.LENGTH_LONG).show();
            }

            db.close();
            fileWriter.close();
            Toast.makeText(Consulta.this, "SE CREO EL ARCHIVO CSV EXITOSAMENTE", Toast.LENGTH_LONG).show();

        } catch (Exception e) { }
    }



}

