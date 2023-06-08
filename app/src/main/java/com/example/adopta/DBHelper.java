package com.example.adopta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Environment;


import androidx.annotation.Nullable;

import com.example.adopta.entidades.Usuarios;
import com.example.adopta.entidades.Visitas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper  {

    public static final String DBNAME="recordv.db";
    public static final String TABLA_DATOS="datos";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table usuarios (nombre TEXT,cedula TEXT primary key, clave TEXT)");

        db.execSQL("create table datos (fecha TEXT ,departamento TEXT, municipio TEXT," +
                "vereda TEXT, finca TEXT, nombre TEXT, cedula TEXT primary key, telefono TEXT," +
                "visita TEXT, actividad TEXT, objeto TEXT, observaciones TEXT, recomendaciones TEXT," +
                "dosis TEXT, fir TEXT, firma1 TEXT , firma2 TEXT ,foto3 TEXT)");

        db.execSQL("create table datosb (codigo integer primary key autoincrement,fecha TEXT, cedula TEXT,visita TEXT,objeto TEXT" +
                ", firma TEXT,observaciones TEXT, recomendaciones TEXT, dosis TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists usuarios");
        db.execSQL("drop table if exists datos");
        db.execSQL("drop table if exists datosb");

    }

    public Boolean insertarDatos (String nombre, String cedula, String clave){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("nombre",nombre);
        valores.put("cedula",cedula);
        valores.put("clave",clave);

        long resultado = db.insert("usuarios",null,valores);
        if (resultado==-1)
            return false;
        else
            return true;
    }

    //INSERTAR DATOS EN EL HOME
    public Boolean insertarDatosHome(String fecha,
                                     String departamento,
                                     String municipio
                                    , String vereda,
                                     String finca,
                                     String nombre,
                                     String cedula,
                                     String telefono,
                                     String visita,
                                     String actividad,
                                     String objeto,
                                     String observaciones,
                                     String recomendaciones,
                                     String dosis,
                                     String fir,
                                     Bitmap firma1 ,
                                     Bitmap firma2,
                                     Bitmap img_foto ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("fecha", fecha);
        valores.put("departamento", departamento);
        valores.put("municipio", municipio);
        valores.put("vereda", vereda);
        valores.put("finca", finca);
        valores.put("nombre", nombre);
        valores.put("cedula", cedula);
        valores.put("telefono", telefono);
        valores.put("visita", visita);
        valores.put("actividad",actividad);
        valores.put("objeto", objeto);
        valores.put("observaciones", observaciones);
        valores.put("recomendaciones", recomendaciones);
        valores.put("dosis", dosis);
        valores.put("fir",fir);
        valores.put("firma1", String.valueOf(firma1));
        valores.put("firma2", String.valueOf(firma2));



        long resultado = db.insert("datos",null,valores);
        if (resultado==-1)
            return false;
        else
            return true;
    }

    //INSERTAR DATOS EN LA TABLA DATOSB
    public Boolean insertarDatosB(   String fecha,
                                     String cedula,
                                     String visita,
                                     String objeto,
                                     String firma,
                                     String observaciones,
                                     String recomendaciones,
                                     String dosis
                                     ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("fecha", fecha);
        valores.put("cedula", cedula);
        valores.put("visita", visita);
        valores.put("objeto", objeto);
        valores.put("firma", firma);
        valores.put("observaciones", observaciones);
        valores.put("recomendaciones", recomendaciones);
        valores.put("dosis", dosis);

        long resultado = db.insert("datosb",null,valores);
        if (resultado==-1)
            return false;
        else
            return true;
    }

    public Boolean validarCedula (String cedula){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from usuarios where cedula=?",new String[] {cedula});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public Boolean validarCedulaClave (String cedula, String clave){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from usuarios where cedula=? and clave=?", new String[] {cedula,clave});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public ArrayList<Usuarios> mostrarUsuarios(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Usuarios> listaContactos = new ArrayList<>();
        Usuarios usuarios = null;
        Cursor cursorUsuarios= null;

        cursorUsuarios= db.rawQuery("select fecha, departamento, municipio, vereda, finca," +
                " nombre, cedula, telefono, visita, actividad,objeto,observaciones from datos order by fecha desc",null);

        if (cursorUsuarios.moveToFirst()){
            do{
                usuarios = new Usuarios();
                usuarios.setFechavista(cursorUsuarios.getString(0));
                usuarios.setDepartamentovista(cursorUsuarios.getString(1));
                usuarios.setMunicipiovista(cursorUsuarios.getString(2));
                usuarios.setVeredavista(cursorUsuarios.getString(3));
                usuarios.setFincavista(cursorUsuarios.getString(4));
                usuarios.setNombrevista(cursorUsuarios.getString(5));
                usuarios.setCedulavista(cursorUsuarios.getString(6));
                usuarios.setTelefonovista(cursorUsuarios.getString(7));
                usuarios.setVisitavista(cursorUsuarios.getString(8));
                usuarios.setActividadvista(cursorUsuarios.getString(9));
                usuarios.setObjetovista(cursorUsuarios.getString(10));
                usuarios.setObservacionesvista(cursorUsuarios.getString(11));
                listaContactos.add(usuarios);
            }while (cursorUsuarios.moveToNext());
        }
        cursorUsuarios.close();
        return listaContactos;

    }

    public void buscarUsuarios(Usuarios usuarios,String cedula){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor= db.rawQuery("select fecha,visita,objeto,firma,observaciones,recomendaciones,dosis from datosb where cedula='"+cedula+"'",null);

        if (cursor.moveToFirst()){
            do{
                usuarios.setFechavista(cursor.getString(0));
                usuarios.setVisitavista(cursor.getString(1));
                usuarios.setObjetovista(cursor.getString(2));
                usuarios.setFirma(cursor.getString(3));
                usuarios.setObservacionesvista(cursor.getString(4));
                usuarios.setRecomendaciones(cursor.getString(5));
                usuarios.setDosis(cursor.getString(6));

            }while (cursor.moveToNext());
        }

    }

    public void buscarUsuariosA(Usuarios usuarios,String cedula){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select departamento,municipio,vereda,finca,nombre,telefono,actividad from datos where cedula='"+cedula+"'",null);

        if (cursor.moveToFirst()){
            do{
                usuarios.setDepartamentovista(cursor.getString(0));
                usuarios.setMunicipiovista(cursor.getString(1));
                usuarios.setVeredavista(cursor.getString(2));
                usuarios.setFincavista(cursor.getString(3));
                usuarios.setNombrevista(cursor.getString(4));
                usuarios.setTelefonovista(cursor.getString(5));
                usuarios.setActividadvista(cursor.getString(6));

            }while (cursor.moveToNext());
        }

    }

    public ArrayList<Visitas> mostrarVisitas(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Visitas> listaVisitas = new ArrayList<>();
        Visitas visitas = null;
        Cursor cursorVisitas= null;
        cursorVisitas = db.rawQuery("select fecha, cedula, visita,objeto,observaciones from datosb",null);
        if (cursorVisitas.moveToNext()){
            do {
                visitas = new Visitas();
                visitas.setCedulaViewCedula(cursorVisitas.getString(0));
                visitas.setFechaViewFecha(cursorVisitas.getString(1));
                visitas.setVisitaViewVisita(cursorVisitas.getString(2));
                visitas.setObjetoViewObjeto(cursorVisitas.getString(3));
                visitas.setObservacionesViewObservaciones(cursorVisitas.getString(4));
                listaVisitas.add(visitas);

            }while (cursorVisitas.moveToNext());
        }
        cursorVisitas.close();
        return listaVisitas;
    }

    public void exportDB() {

        try {
            InputStream inputStream = new FileInputStream(
                    new File(Environment.getDataDirectory()
                            + "/data/com.example.appmvz/databases/recordv"));
            //OutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath()+ "/Download/copiadb.db"));
            OutputStream outputStream = new FileOutputStream(new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/copiadb.db/")));

            byte[] buffer = new byte[1024];
            int comprimiendo;
            while ((comprimiendo = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, comprimiendo);

            }
            inputStream.close();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();


        }
    }


}
