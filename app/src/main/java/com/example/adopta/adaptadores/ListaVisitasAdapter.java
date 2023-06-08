package com.example.adopta.adaptadores;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adopta.R;
import com.example.adopta.entidades.Visitas;

import java.util.ArrayList;

public class ListaVisitasAdapter extends RecyclerView.Adapter<ListaVisitasAdapter.VisitasViewHolder> {

    ArrayList<Visitas> listaVisitas;

    public ListaVisitasAdapter(ArrayList <Visitas> listavisitas) {
        this.listaVisitas=listavisitas;
    }

    @NonNull
    @Override
    public ListaVisitasAdapter.VisitasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_visitas,null,false);
        return new VisitasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaVisitasAdapter.VisitasViewHolder visitasViewHolder, int i) {
        visitasViewHolder.ViewFecha.setText(listaVisitas.get(i).getFechaViewFecha());
        visitasViewHolder.ViewCedula.setText(listaVisitas.get(i).getCedulaViewCedula());
        visitasViewHolder.ViewVisita.setText(listaVisitas.get(i).getVisitaViewVisita());
        visitasViewHolder.ViewObjeto.setText(listaVisitas.get(i).getObjetoViewObjeto());
        visitasViewHolder.ViewObservaciones.setText(listaVisitas.get(i).getObservacionesViewObservaciones());

    }

    @Override
    public int getItemCount() {
        return listaVisitas.size();

    }

    public class VisitasViewHolder extends RecyclerView.ViewHolder {
        TextView ViewFecha, ViewCedula,ViewVisita,ViewObjeto,ViewObservaciones;

        public VisitasViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewFecha = itemView.findViewById(R.id.ViewVistaVisitasFecha);
            ViewCedula = itemView.findViewById(R.id.ViewVistaVisitasCedula);
            ViewVisita = itemView.findViewById(R.id.ViewVistaVisitasVisitas);
            ViewObjeto = itemView.findViewById(R.id.ViewVistaVisitasObjeto);
            ViewObservaciones = itemView.findViewById(R.id.ViewVistaVisitasObservaciones);

        }
    }
}
