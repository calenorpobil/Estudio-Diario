package com.merlita.estudiodiario;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class AdaptadorFilas extends RecyclerView.Adapter<AdaptadorFilas.MiContenedor>
    implements View.OnClickListener {
    private Context context;
    private ArrayList<DatosLibros> lista;
    private ArrayList<DatosLibros> listaSinDatosPrueba;
    private ArrayList<DatosLibros> librosOriginal;
    View.OnClickListener escuchador;
    private boolean viendoDatosPrueba=true;

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
        DatosLibros libro = lista.get(position);
        holder.tvTitulo.setText(libro.getTitulo());
        holder.tvAutor.setText(libro.getAutor());
        holder.tvEmoji.setText("☕");

    }

    public AdaptadorFilas(View.OnClickListener escuchador,
                          Context context, ArrayList<DatosLibros> lista) {
        super();
        this.escuchador = escuchador;
        this.context = context;
        this.lista = lista;
        this.librosOriginal = new ArrayList<>(lista);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onClick(View view) {
        if(escuchador!=null)
            escuchador.onClick(view);
    }

    public static class MiContenedor extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener
    {
        TextView tvTitulo, tvAutor, tvFecha, tvFormato, tvBandera;
        RatingBar rbEstrellas;
        CheckBox cbNotas, cbFinalizado, cbPrestado;
        TextView tvEmoji;

        public MiContenedor(@NonNull View itemView) {
            super(itemView);

            tvTitulo = (TextView) itemView.findViewById(R.id.tvTitulo);
            tvAutor = (TextView) itemView.findViewById(R.id.tvEdad);
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


    //METODOS PARA ORDENAR
    public void ordenarPorTitulo() {
        Collections.sort(lista, (l1, l2) -> l1.getTitulo().
                compareToIgnoreCase(l2.getTitulo()));
        notifyDataSetChanged();
    }

    public void ordenarPorAutor() {
        Collections.sort(lista, (l1, l2) -> l1.getAutor().
                compareToIgnoreCase(l2.getAutor()));
        notifyDataSetChanged();
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
    public void setViendoDatosPrueba(boolean ver){
        this.viendoDatosPrueba=ver;
    }

    public void ordenarPorFechaInicio() {
        Collections.sort(lista, (l1, l2) -> {
            if (l1.getFecha_lectura_ini() == null && l2.getFecha_lectura_ini() == null) return 0;
            if (l1.getFecha_lectura_ini() == null) return 1;
            if (l2.getFecha_lectura_ini() == null) return -1;
            // Más reciente primero:
            return l2.getFecha_lectura_ini().compareTo(l1.getFecha_lectura_ini());
        });
        notifyDataSetChanged();
    }
    public void ordenarPorFinalizado() {
        Collections.sort(lista, (l1, l2) -> {
            if (!l1.getFinalizado() && !l2.getFinalizado()) return 0;
            if (!l1.getFinalizado() && l2.getFinalizado()) return 1;
            return -1;
        });
        notifyDataSetChanged();
    }

    public void resetearOrden() {
        lista = new ArrayList<>(librosOriginal);
        notifyDataSetChanged();
    }



    public AdaptadorFilas(@NonNull Context context) {
        super();
    }



}
