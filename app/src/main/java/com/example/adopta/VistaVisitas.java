package com.example.adopta;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adopta.adaptadores.ListaVisitasAdapter;
import com.example.adopta.entidades.Visitas;

import java.util.ArrayList;

public class VistaVisitas extends AppCompatActivity {

    RecyclerView listaVisitas;
    ArrayList <Visitas> listaArrayVisitas;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_visitas);

        listaVisitas = findViewById(R.id.listaVisitas);

        listaVisitas.setLayoutManager(new LinearLayoutManager(this));
        DB = new DBHelper(this, "recordv", null, 1);
        listaArrayVisitas = new ArrayList<>();

        ListaVisitasAdapter adapter = new ListaVisitasAdapter(DB.mostrarVisitas());
        listaVisitas.setAdapter(adapter);

    }
}