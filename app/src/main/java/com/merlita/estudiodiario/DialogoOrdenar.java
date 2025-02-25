package com.merlita.estudiodiario;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogoOrdenar extends DialogFragment {

    public interface OrdenarDialogListener {
        void onFinalizadoClick();
        void onFechaClick();
        void onTituloClick();
        void onAutorClick();
    }

    private OrdenarDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OrdenarDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " debe implementar OrdenarDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialogo_ordenar, null);
        builder.setView(view);

        // Configurar botones
        Button btnFinalizado = view.findViewById(R.id.btnFinalizado);
        Button btnFecha = view.findViewById(R.id.btnPorFecha);
        Button btnTitulo = view.findViewById(R.id.btnPorTitulo);
        Button btnAutor = view.findViewById(R.id.btnPorAutor);
        Button btnCancelar = view.findViewById(R.id.btnCancelar);

        btnFinalizado.setOnClickListener(v -> {
            listener.onFinalizadoClick();
            dismiss();
        });

        btnFecha.setOnClickListener(v -> {
            listener.onFechaClick();
            dismiss();
        });

        btnTitulo.setOnClickListener(v -> {
            listener.onTituloClick();
            dismiss();
        });

        btnAutor.setOnClickListener(v -> {
            listener.onAutorClick();
            dismiss();
        });

        btnCancelar.setOnClickListener(v -> dismiss());

        return builder.create();
    }
}