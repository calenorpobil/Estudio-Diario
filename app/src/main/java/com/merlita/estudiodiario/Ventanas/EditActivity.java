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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.merlita.estudiodiario.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {
    EditText etTitulo, etAutor, etFechaInicio,
            etFechaFin, etPrestado, etNotas, etValoracion;
    CheckBox cbFinalizado;
    Button bt;
    Intent upIntent;
    Spinner spCategoria, spIdioma, spFormato;
    int id_libro;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta);

        Bundle upIntent = this.getIntent().getExtras();
        assert upIntent != null;
        id_libro = upIntent.getInt("ID");

        String nombre = upIntent.getString("NOMBRE");
        String desc = upIntent.getString("DESCRIPCION");
        etTitulo = findViewById(R.id.etTitulo);
        etAutor = findViewById(R.id.etAutor);

// Poblar campos de texto
        etTitulo.setText(nombre);
        etAutor.setText(desc);

    }

    public void clickVolver(View v){
        Intent i = new Intent();

        // Obtengo referencias a todos los campos
        EditText etTitulo = findViewById(R.id.etTitulo);
        EditText etAutor = findViewById(R.id.etAutor);


        try {
            i.putExtra("NOMBRE",  etTitulo.getText());
            i.putExtra("DESCRIPCION", etAutor.getText());

            setResult(RESULT_OK, i);
        } finally {
            finish();
        }
    }
    private void setupDatePicker(EditText editText) {
        editText.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(this,
                    (view, year, month, day) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, day);
                        editText.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(selectedDate.getTime()));
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });
    }

    private void configurarSpinner(Spinner spinner, int arrayResId, String value) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (value != null) {
            int position = adapter.getPosition(value);
            if (position >= 0) {
                spinner.setSelection(position);
            }
        }
    }


}
