package com.merlita.estudiodiario.Ventanas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.merlita.estudiodiario.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AltaActivity extends AppCompatActivity {
    EditText etTitulo, etAutor;
    Button bt;
    Intent upIntent;
    Spinner spCategoria, spIdioma, spFormato;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta);

        etTitulo = findViewById(R.id.etTitulo);
        etTitulo.setHint("Título del libro");
        etAutor = findViewById(R.id.etAutor);
        etAutor.setHint("Nombre del autor");
        bt = findViewById(R.id.btnGuardar);


    }



    public void clickVolver(View v){
        Intent i = new Intent();

        // Obtengo referencias a todos los campos
        EditText etTitulo = findViewById(R.id.etTitulo);
        EditText etAutor = findViewById(R.id.etAutor);

        // Valido campos obligatorios (según esquema SQL)
        if (etTitulo.getText().toString().isEmpty() ||
                etAutor.getText().toString().isEmpty()) {

            Toast.makeText(this, "Complete los campos obligatorios (*)", Toast.LENGTH_SHORT).show();
            /*setResult(RESULT_CANCELED);
            finish();
            return;*/
        }

        try {
            // Convertir fechas a timestamp

            // Preparar todos los datos para enviar
            String nombre = etTitulo.getText().toString();
            String desc = etAutor.getText().toString();
            i.putExtra("DESCRIPCION", desc);
            i.putExtra("NOMBRE", nombre);

            setResult(RESULT_OK, i);
        } finally {
            finish();
        }
    }


}
