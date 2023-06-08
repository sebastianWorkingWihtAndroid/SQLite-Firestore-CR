package com.example.adopta.adaptadores;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adopta.R;
import com.example.adopta.entidades.Usuarios;

import java.util.ArrayList;

public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.UsuariosViewHolder> {
    ArrayList<Usuarios> listaUsuarios;

    public ListaUsuariosAdapter(ArrayList<Usuarios> listausuarios){
        this.listaUsuarios = listausuarios;
    }
    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_personas,null,false);
        return new UsuariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {
        holder.viewFecha.setText(listaUsuarios.get(position).getFechavista());
        holder.viewdepartamento.setText(listaUsuarios.get(position).getDepartamentovista());
        holder.viewmunicipio.setText(listaUsuarios.get(position).getMunicipiovista());
        holder.viewvereda.setText(listaUsuarios.get(position).getVeredavista());
        holder.viewfinca.setText(listaUsuarios.get(position).getFincavista());
        holder.viewnombre.setText(listaUsuarios.get(position).getNombrevista());
        holder.viewcedula.setText(listaUsuarios.get(position).getCedulavista());
        holder.viewtelefono.setText(listaUsuarios.get(position).getTelefonovista());
        holder.viewnumerovisita.setText(listaUsuarios.get(position).getVisitavista());
        holder.viewactividad.setText(listaUsuarios.get(position).getActividadvista());
        holder.viewobjetovisita.setText(listaUsuarios.get(position).getObjetovista());
        holder.viewobservaciones.setText(listaUsuarios.get(position).getObservacionesvista());

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class UsuariosViewHolder extends RecyclerView.ViewHolder {
        TextView viewFecha,viewdepartamento,viewmunicipio,viewvereda, viewfinca,viewnombre,viewcedula,viewtelefono,viewnumerovisita,viewactividad,viewobjetovisita,viewobservaciones;

        public UsuariosViewHolder(@NonNull View itemView) {
            super(itemView);
            viewFecha=itemView.findViewById(R.id.fechaview);
            viewdepartamento=itemView.findViewById(R.id.departamentovista);
            viewmunicipio=itemView.findViewById(R.id.municipiovista);
            viewvereda=itemView.findViewById(R.id.veredavista);
            viewfinca=itemView.findViewById(R.id.fincavista);
            viewnombre=itemView.findViewById(R.id.nombrevista);
            viewcedula=itemView.findViewById(R.id.cedulavista);
            viewtelefono=itemView.findViewById(R.id.telefonovista);
            viewnumerovisita=itemView.findViewById(R.id.numerovisitavista);
            viewactividad=itemView.findViewById(R.id.actividadvisita);
            viewobjetovisita=itemView.findViewById(R.id.objetovisita);
            viewobservaciones=itemView.findViewById(R.id.observacionesvisita);


        }
    }
}
