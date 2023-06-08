package com.example.adopta;


import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adopta.adaptadores.ListaUsuariosAdapter;
import com.example.adopta.entidades.Usuarios;

import java.util.ArrayList;

public class Vista extends AppCompatActivity {

    RecyclerView listacontactos;
    DBHelper DB;
    ArrayList <Usuarios> listarArrayContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista);

        listacontactos=findViewById(R.id.listadoPersonas);

        listacontactos.setLayoutManager(new LinearLayoutManager(this));
        DB = new DBHelper(this, "recordv", null, 1);
        listarArrayContactos = new ArrayList<>();

        ListaUsuariosAdapter adapter = new ListaUsuariosAdapter(DB.mostrarUsuarios());
        listacontactos.setAdapter(adapter);

    }
}