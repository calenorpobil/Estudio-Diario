package com.merlita.estudiodiario.Ventanas;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.merlita.estudiodiario.AdaptadorFilas;
import com.merlita.estudiodiario.HilosCliente.SumaNumero;
import com.merlita.estudiodiario.DialogoMenu;
import com.merlita.estudiodiario.DialogoOrdenar;
import com.merlita.estudiodiario.EstudiosSQLiteHelper;
import com.merlita.estudiodiario.FragmentoTexto;
import com.merlita.estudiodiario.Modelos.Estudio;
import com.merlita.estudiodiario.R;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        DialogoMenu.CustomDialogListener {

    RecyclerView vistaRecycler;
    ArrayList<Estudio> listaEstudios = new ArrayList<Estudio>();
    TextView tv;
    AdaptadorFilas adaptadorFilas;
    Button btAlta;
    EditText et;
    int posicionEdicion;
    boolean ver=true;
    int numServidor=1;

    SQLiteDatabase db;

    Intent resultado = null;

    private void toast(String e) {
        Toast.makeText(this, e,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //EdgeToEdge.enable(this);


        try(EstudiosSQLiteHelper usdbh =
                    new EstudiosSQLiteHelper(this,
                            "DBEstudios", null, 1);){
            db = usdbh.getWritableDatabase();
            //db.execSQL("DROP TABLE IF EXISTS bdlibros");

            //Crear tabla si existe:
            usdbh.onCreate(db);

            datosDePrueba();
            rellenarLista();



            db.close();
        }

        tv = findViewById(R.id.tvTitulo);
        btAlta = findViewById(R.id.btAlta);
        vistaRecycler = findViewById(R.id.recyclerView);
        adaptadorFilas = new AdaptadorFilas(this, listaEstudios);

        vistaRecycler.setLayoutManager(new LinearLayoutManager(this));
        vistaRecycler.setAdapter(adaptadorFilas);



        btAlta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //ONCLICK


                File h = getFilesDir().getParentFile();
                assert h != null;
                //ARCHIVO SQLITE:
                File database = new File(
                        Environment.getDataDirectory()+
                                "/data/com.merlita.estudiodiario/databases/"+"DBEstudios");
                //Tamaño:
                int size = (int) database.length();
                //Bytes:
                byte[] bytes = new byte[size];

                tv.setText(database.isFile()+"");



                toast("juan");

                SumaNumero c = new SumaNumero("Hilo", numServidor);

                Thread thread = new Thread(c);

                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    toast(e.getMessage());
                }
                numServidor = c.getValue();
                listaEstudios.get(1).setNombre(numServidor+"");

                adaptadorFilas.notifyDataSetChanged();

            }
        });
    }


    private void mostrarFormularioAlta()  {
        Intent i = new Intent(MainActivity.this, AltaActivity.class);
        lanzadorAlta.launch(i);
    }



    private void datosDePrueba() {
        if(db!=null){
            try{
                db.execSQL("INSERT INTO ESTUDIO (NOMBRE, DESCRIPCION)" +
                        "VALUES" +
                        " ('Tomar Café', 'Registro de consumo diario de café')," +
                        "('Ir al gimnasio', 'Seguimiento de sesiones de entrenamiento')," +
                        "('Diario', 'Registro personal diario');");
            } catch (SQLiteConstraintException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void rellenarLista() {
        Cursor c = db.rawQuery("select * from estudio;", null);

        while (c.moveToNext()) {
            int index = c.getColumnIndex("NOMBRE");
            String nombre = c.getString(index);
            index = c.getColumnIndex("DESCRIPCION");
            String descripcion = c.getString(index);
            listaEstudios.add(new Estudio(nombre, descripcion));
        }
        c.close();
    }




    //MENU CONTEXTUAL
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        switch(item.getItemId())
        {
            case 121:
                //MENU --> EDITAR
                Intent i = new Intent(this, EditActivity.class);
                posicionEdicion = item.getGroupId();
                Estudio libro = listaEstudios.get(posicionEdicion);
                i.putExtra("NOMBRE", libro.getNombre());
                i.putExtra("DESCRIPCION", libro.getDescripcion());
                lanzadorEdit.launch(i);
                return true;
            case 122:
                //MENU --> BORRAR
                listaEstudios.remove(item.getGroupId());
                adaptadorFilas.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }





    //RECOGER EDIT ACTIVITY
    ActivityResultLauncher<Intent> lanzadorEdit = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult resultado)
                {
                    if(resultado.getResultCode()==RESULT_OK) {
                        Intent data = resultado.getData();
                        assert data != null;
                        Estudio editLibro = new Estudio(
                                data.getStringExtra("NOMBRE"),
                                data.getStringExtra("DESCRIPCION")
                        );

                        Estudio antig = listaEstudios.get(posicionEdicion);
                        listaEstudios.set(listaEstudios.indexOf(antig), editLibro);
                        // Editar el libro

                        editarSQL(antig);
                        adaptadorFilas.notifyDataSetChanged();
                    }else{
                        //SIN DATOS
                    }
                }
            }
    );


    //RECOGER ALTA ACTIVITY
    ActivityResultLauncher<Intent>
            lanzadorAlta = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult resultado) {
                    if(resultado.getResultCode()==RESULT_OK) {

                        Intent data = resultado.getData();
                        assert data != null;
                        Estudio nuevoEstudio = new Estudio(
                                data.getStringExtra("NOMBRE"),
                                data.getStringExtra("DESCRIPCION")
                        );

                        // Insertar en BD
                        String nombre = insertarSQL(nuevoEstudio);
                        nuevoEstudio.setNombre(nombre);
                        listaEstudios.add(nuevoEstudio);
                        adaptadorFilas.notifyDataSetChanged();
                    }else{
                        //SIN DATOS
                    }
                }
            });

    //RECOGER ACERCA DE
    ActivityResultLauncher<Intent>
            lanzadorAcercaDe = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult resultado) {
                    if(resultado.getResultCode()==RESULT_OK) {

                        Intent data = resultado.getData();
                        assert data != null;

                    }else{
                        //SIN DATOS
                    }
                }
            });



    @Override
    public void onAltaLibroClick() {
        // Aquí va el código para el alta de libro
        mostrarFormularioAlta();
    }

    @Override
    public void onAcercaDeClick() {
        FragmentoTexto dialog = new FragmentoTexto();
        dialog.show(getSupportFragmentManager(), "AcercaDe");

    }

    @Override
    public void onOrdenarClick() {
        // Mostrar opciones de ordenación
        mostrarDialogoOrdenar();
    }

    @Override
    public void onImportarClick() {
        // Lógica de importación
    }

    @Override
    public void onExportarClick() {
        // Lógica de exportación
    }

    @Override
    public void onVerDatosPrueba() {
        if(ver){
            adaptadorFilas.filtrarLista();
            ver=false;
        }else{
            try(EstudiosSQLiteHelper usdbh =
                        new EstudiosSQLiteHelper(this,
                                "DBUsuarios", null, 1);) {
                db = usdbh.getWritableDatabase();

                datosDePrueba();
            }
            ver=true;
        }
        adaptadorFilas.notifyDataSetChanged();

    }

    // Método para mostrar el diálogo
    private void mostrarDialogo() {
        DialogoMenu dialog = new DialogoMenu();
        dialog.show(getSupportFragmentManager(), "CustomDialog");
    }

    private String insertarSQL(Estudio libro){
        String id="";
        try(EstudiosSQLiteHelper usdbh =
                    new EstudiosSQLiteHelper(this,
                            "DBUsuarios", null, 1);){
            db = usdbh.getWritableDatabase();

            db.execSQL("INSERT INTO estudio (nombre, descripcion) " +
                    "VALUES ('"+libro.getNombre()+"', '"+libro.getDescripcion()+"');");

            String sql = ("SELECT nombre FROM estudio WHERE nombre = ?");
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
                id = cursor.getString(0);
            }


            db.close();
        }
        return id;
    }
    private void editarSQL(Estudio libro){
        try(EstudiosSQLiteHelper usdbh =
                    new EstudiosSQLiteHelper(this,
                            "DBUsuarios", null, 1);){
            db = usdbh.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("NOMBRE", libro.getNombre());
            values.put("DESCRIPCION", libro.getDescripcion());

            // Actualizar usando el ID como condición
            String[] id = {libro.getNombre()};
            db.update("bdlibros",
                    values,
                    "_id = ?",
                    id);

            db.close();
        }
    }



    private void mostrarDialogoOrdenar() {
        DialogoOrdenar dialog = new DialogoOrdenar();
        dialog.show(getSupportFragmentManager(), "DialogoOrdenar");
    }


}