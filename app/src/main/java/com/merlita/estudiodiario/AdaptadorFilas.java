package com.merlita.estudiodiario;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.merlita.estudiodiario.Modelos.Estudio;

import java.util.ArrayList;
import java.util.Collections;

public class AdaptadorFilas extends RecyclerView.Adapter<AdaptadorFilas.MiContenedor> {
    private Context context;
    private ArrayList<Estudio> lista;
    private boolean viendoDatosPrueba=true;
    private OnButtonClickListener listener;

    public interface OnButtonClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
        void onShareClick(int position);
    }


    @NonNull
    @Override
    public MiContenedor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflador =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflador.inflate(R.layout.text_row_item, parent, false);

        return new MiContenedor(v);
    }

    //PONER VALORES
    @Override
    public void onBindViewHolder(@NonNull MiContenedor holder, int position) {
        Estudio estudio = lista.get(position);
        holder.tvTitulo.setText(estudio.getNombre());
        holder.tvAutor.setText(estudio.getDescripcion());
        holder.tvEmoji.setText("🔵");

    }


    public AdaptadorFilas(Context context, ArrayList<Estudio> lista) {
        super();
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }



    public static class MiContenedor extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener
    {
        TextView tvTitulo, tvAutor, tvFecha, tvFormato, tvBandera;
        Button btBorrar;
        TextView tvEmoji;

        public MiContenedor(@NonNull View itemView) {
            super(itemView);

            tvTitulo = (TextView) itemView.findViewById(R.id.tvTitulo);
            tvAutor = (TextView) itemView.findViewById(R.id.tvDescripcion);
            tvFecha = (TextView) itemView.findViewById(R.id.tvFecha);
            tvFormato = (TextView) itemView.findViewById(R.id.tvFormato);
            tvEmoji = (TextView) itemView.findViewById(R.id.tvEmoji);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                        ContextMenu.ContextMenuInfo contextMenuInfo)
        {
            contextMenu.add(getAdapterPosition(), 121, 0, "EDITAR");
            contextMenu.add(getAdapterPosition(), 122, 1, "BORRAR");
        }
    }

    public void filtrarLista() {
        if(viendoDatosPrueba){
            lista.remove(0);
            lista.remove(0);
            lista.remove(0);
            viendoDatosPrueba=false;
        }
        notifyDataSetChanged();
    }


    public AdaptadorFilas(@NonNull Context context) {
        super();
    }



}
