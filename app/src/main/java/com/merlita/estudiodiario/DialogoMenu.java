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

public class DialogoMenu extends DialogFragment {

    public interface CustomDialogListener {
        void onAltaLibroClick();
        void onAcercaDeClick();
        void onOrdenarClick();
        void onImportarClick();
        void onExportarClick();
        void onVerDatosPrueba();
    }

    private CustomDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CustomDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " debe implementar CustomDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialogo_personalizado, null);
        builder.setView(view);

        // Configurar botones
        Button btnAlta = view.findViewById(R.id.btnAltaLibro);
        Button btnAcerca = view.findViewById(R.id.btnAcercaDe);
        Button btnOrdenar = view.findViewById(R.id.btnOrdenarPor);
        Button btnImportar = view.findViewById(R.id.btnImportar);
        Button btnExportar = view.findViewById(R.id.btnExportar);
        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        //Button btnOcultarDatosDePrueba = view.findViewById(R.id.btnOcultarDatos);

        btnAlta.setOnClickListener(v -> {
            listener.onAltaLibroClick();
            //Dismiss cierra el dialogFragment anterior.
            dismiss();
        });

        btnAcerca.setOnClickListener(v -> {
            listener.onAcercaDeClick();
            dismiss();
        });

        btnOrdenar.setOnClickListener(v -> {
            listener.onOrdenarClick();
            dismiss();
        });

        btnImportar.setOnClickListener(v -> {
            listener.onImportarClick();
            dismiss();
        });

        btnExportar.setOnClickListener(v -> {
            listener.onExportarClick();
            dismiss();
        });

        /*btnOcultarDatosDePrueba.setOnClickListener(v -> {
            listener.onVerDatosPrueba();
            dismiss();
        });*/

        btnCancelar.setOnClickListener(v -> dismiss());

        return builder.create();
    }
}