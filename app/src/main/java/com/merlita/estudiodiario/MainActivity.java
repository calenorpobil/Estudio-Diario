package com.merlita.estudiodiario;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.merlita.estudiodiario.Excepciones.miExcepcion;

import java.sql.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        DialogoMenu.CustomDialogListener, DialogoOrdenar.OrdenarDialogListener {

    RecyclerView vistaRecycler;
    ArrayList<DatosLibros> lista = new ArrayList<DatosLibros>();
    TextView tv;
    AdaptadorFilas adaptadorFilas;
    Button btAlta;
    EditText et;
    int posicionEdicion;
    DatosLibros auxiliar;
    private final ArrayList<DatosLibros> datosVacio =
            new ArrayList<>();
    boolean ver=true;

    SQLiteDatabase db;

    Intent resultado = null;

    private void toast(miExcepcion e) {
        Toast.makeText(this, e.getMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        try(UsuariosSQLiteHelper usdbh =
                new UsuariosSQLiteHelper(this,
                        "DBUsuarios", null, 1);){
            db = usdbh.getWritableDatabase();
            //db.execSQL("DROP TABLE IF EXISTS bdlibros");

            //Crear tabla si existe:
            usdbh.onCreate(db);

            datosDePrueba();
            rellenarLista();


            db.close();
        }

        /*
                "INSERT INTO libros (categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas) " +
                "VALUES ('Fantasía', 'El nombre del viento', 'Patrick Rothfuss', 'Español', NULL, NULL, NULL, 4.9, 'ePub', 'Narrativa envolvente y personajes complejos');" +
                "" +
                "INSERT INTO libros (categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas) " +
                "VALUES ('Ciencia', 'Breves respuestas a las grandes preguntas', 'Stephen Hawking', 'Español', 1680307200000, NULL, 'Carlos Mendoza', 4.5, 'Físico', 'Último libro del famoso físico teórico');" +
                "" +
                "INSERT INTO libros (categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas) " +
                "VALUES ('Filosofía', 'El mundo de Sofía', 'Jostein Gaarder', 'Español', 1682899200000, 1683936000000, NULL, 4.4, 'PDF', 'Novela sobre historia de la filosofía');" +
                "" +
                "INSERT INTO libros (categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas) " +
                "VALUES ('Autoayuda', 'Hábitos atómicos', 'James Clear', 'Español', 1685577600000, 1686700800000, 'María González', 4.3, 'Digital', 'Métodos prácticos para construir hábitos');" +
                "" +
                "INSERT INTO libros (categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas) " +
                "VALUES ('Historia', 'SPQR', 'Mary Beard', NULL, NULL, NULL, NULL, 4.2, 'ePub', 'Historia del Imperio Romano desde sus inicios');" +
                "" +
                "INSERT INTO libros (categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas) " +
                "VALUES ('Poesía', 'Veinte poemas de amor y una canción desesperada', 'Pablo Neruda', 'Español', 1688169600000, 1690761600000, NULL, 4.7, 'Físico', 'Clásico de la poesía amorosa en español');" +
                "\n" +
                "INSERT INTO libros (categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas) \n" +
                "VALUES ('Finanzas', 'Padre rico, padre pobre', 'Robert T. Kiyosaki', 'Español', 1690848000000, 1693440000000, 'David Torres', 4.0, 'Digital', 'Enfoque alternativo sobre educación financiera');");
*/
        if(lista.isEmpty()){
            auxiliar = new DatosLibros();
            datosVacio.add(auxiliar);
        }
        tv = findViewById(R.id.tvTitulo);
        btAlta = findViewById(R.id.btAlta);
        vistaRecycler = findViewById(R.id.recyclerView);
        adaptadorFilas = new AdaptadorFilas(this, this, lista);

        vistaRecycler.setLayoutManager(new LinearLayoutManager(this));
        vistaRecycler.setAdapter(adaptadorFilas);


        btAlta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                mostrarDialogo();
                //setResult(RESULT_OK, i);
                //finish();
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
                db.execSQL("INSERT INTO bdlibros (categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas, finalizado) \n" +
                        "VALUES ('Ficción', 'Cien años de soledad', 'Gabriel García Márquez', 'Español', 1672531200000, 1673740800000, NULL, 4.8, 'Físico', " +
                        "'Obra maestra del realismo mágico latinoamericano', 1);");
                db.execSQL("INSERT INTO bdlibros (categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas, finalizado) " +
                        "VALUES ('No ficción', 'Sapiens: De animales a dioses', 'Yuval Noah Harari', 'Inglés', 1677984000000, 1679289600000, NULL, 4.7, 'Digital', 'Fascinante recorrido por la historia humana', 0)");
                db.execSQL("INSERT INTO bdlibros (categoria, titulo, autor, idioma, fecha_lectura_ini, fecha_lectura_fin, prestado_a, valoracion, formato, notas, finalizado) " +
                        "VALUES ('Tecnología', 'Clean Code', 'Robert C. Martin', 'Inglés', 1675987200000, 1677283200000, 'Ana Sánchez', 4.6, 'Físico', 'Fundamental para mejores prácticas de programación', 1);");
                adaptadorFilas.setViendoDatosPrueba(true);
            } catch (SQLiteConstraintException e) {
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void rellenarLista() {
        Cursor c = db.rawQuery("select * from bdlibros;", null);

        while (c.moveToNext()) {
            int index = c.getColumnIndex("_id");
            int id = c.getInt(index);
            index = c.getColumnIndex("titulo");
            String titulo = c.getString(index);
            index = c.getColumnIndex("categoria");
            String categoria = c.getString(index);
            index = c.getColumnIndex("autor");
            String autor = c.getString(index);
            index = c.getColumnIndex("idioma");
            String idioma = c.getString(index);
            index = c.getColumnIndex("fecha_lectura_ini");
            Long fecha_lectura_ini = c.getLong(index);
            index = c.getColumnIndex("fecha_lectura_fin");
            Long fecha_lectura_fin = c.getLong(index);
            index = c.getColumnIndex("prestado_a");
            String prestado_a = c.getString(index);
            index = c.getColumnIndex("valoracion");
            Float valoracion = c.getFloat(index);
            index = c.getColumnIndex("formato");
            String formato = c.getString(index);
            index = c.getColumnIndex("notas");
            String notas = c.getString(index);
            index = c.getColumnIndex("finalizado");
            int finalizado = c.getInt(index);
            lista.add(new DatosLibros(id, categoria, titulo, autor, idioma,
                    fecha_lectura_fin, fecha_lectura_ini, prestado_a,
                    valoracion, formato, notas, finalizado));
        }
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
                DatosLibros libro = lista.get(posicionEdicion);
                i.putExtra("_ID", libro.get_id());
                i.putExtra("CATEGORIA", libro.getCategoria());
                i.putExtra("TITULO", libro.getTitulo());
                i.putExtra("AUTOR", libro.getAutor());
                i.putExtra("IDIOMA", libro.getIdioma());
                i.putExtra("FECHA_INICIO", libro.getFecha_lectura_ini());
                i.putExtra("FECHA_FIN", libro.getFecha_lectura_fin());
                i.putExtra("PRESTADO_A", libro.getPrestado_a());
                i.putExtra("VALORACION", libro.getValoracion());
                i.putExtra("FORMATO", libro.getFormato());
                i.putExtra("NOTAS", libro.getNotas());
                i.putExtra("FINALIZADO", libro.getFinalizado());
                lanzadorEdit.launch(i);
                return true;
            case 122:
                //MENU --> BORRAR
                lista.remove(item.getGroupId());
                adaptadorFilas.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }



    @Override
    public void onClick(View view) {
        tv.setText(lista.get(
                vistaRecycler.getChildAdapterPosition(view)).getTitulo());
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
                        DatosLibros editLibro = new DatosLibros(
                                data.getIntExtra("ID", -1),
                                data.getStringExtra("CATEGORIA"),
                                data.getStringExtra("TITULO"),
                                data.getStringExtra("AUTOR"),
                                data.getStringExtra("IDIOMA"),
                                data.getLongExtra("FECHA_INICIO", -1),
                                data.getLongExtra("FECHA_FIN", -1),
                                data.getStringExtra("PRESTADO_A"),
                                data.getFloatExtra("VALORACION", -1f),
                                data.getStringExtra("FORMATO"),
                                data.getStringExtra("NOTAS"),
                                data.getIntExtra("FINALIZADO", 0)
                        );

                        DatosLibros antig = lista.get(posicionEdicion);
                        lista.set(lista.indexOf(antig), editLibro);
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
                        DatosLibros nuevoLibro = new DatosLibros(
                                data.getIntExtra("ID", -1),
                                data.getStringExtra("CATEGORIA"),
                                data.getStringExtra("TITULO"),
                                data.getStringExtra("AUTOR"),
                                data.getStringExtra("IDIOMA"),
                                data.getLongExtra("FECHA_INICIO", -1),
                                data.getLongExtra("FECHA_FIN", -1),
                                data.getStringExtra("PRESTADO_A"),
                                data.getFloatExtra("VALORACION", -1f),
                                data.getStringExtra("FORMATO"),
                                data.getStringExtra("NOTAS"),
                                data.getIntExtra("FINALIZADO", 0)
                        );

                        // Insertar en BD
                        int id = insertarSQL(nuevoLibro);
                        nuevoLibro.set_id(id);
                        lista.add(nuevoLibro);
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
            try(UsuariosSQLiteHelper usdbh =
                        new UsuariosSQLiteHelper(this,
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

    private int insertarSQL(DatosLibros libro){
        int id=-1;
        try(UsuariosSQLiteHelper usdbh =
                    new UsuariosSQLiteHelper(this,
                            "DBUsuarios", null, 1);){
            db = usdbh.getWritableDatabase();

            Date c = new Date(System.currentTimeMillis());
            long fechaSQLPrueba = c.getTime();

            db.execSQL("INSERT INTO bdlibros (categoria, titulo, autor, idioma, fecha_lectura_ini" +
                    ", fecha_lectura_fin, prestado_a, valoracion, formato, notas, finalizado) " +
                    "VALUES ('"+libro.getCategoria()+"', '"+
                    libro.getTitulo()+"', '"+libro.getAutor()+"', '"+libro.getIdioma()+"', '" +
                    libro.getFecha_lectura_ini()+"', '"+libro.getFecha_lectura_fin()+"', '"+
                    libro.getPrestado_a()+"', '"+libro.getValoracion()+"', '" +
                    libro.getFormato()+"', '"+libro.getNotas()+"', "+libro.getFinalizado()+");");

            String sql = ("SELECT _ID FROM BDLIBROS WHERE TITULO = ?");
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
                id = cursor.getInt(0);
            }


            db.close();
        }
        return id;
    }
    private void editarSQL(DatosLibros libro){
        try(UsuariosSQLiteHelper usdbh =
                    new UsuariosSQLiteHelper(this,
                            "DBUsuarios", null, 1);){
            db = usdbh.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("categoria", libro.getCategoria());
            values.put("titulo", libro.getTitulo());
            values.put("autor", libro.getAutor());
            values.put("idioma", libro.getIdioma());
            values.put("fecha_lectura_ini", libro.getFecha_lectura_ini());
            values.put("fecha_lectura_fin", libro.getFecha_lectura_fin());
            values.put("prestado_a", libro.getPrestado_a());
            values.put("valoracion", libro.getValoracion());
            values.put("formato", libro.getFormato());
            values.put("notas", libro.getNotas());
            values.put("finalizado", libro.getFinalizado() ? 1 : 0);

            // Actualizar usando el ID como condición
            String[] id = {libro.get_id()+""};
            db.update("bdlibros",
                    values,
                    "_id = ?",
                    id);

            db.close();
        }
    }


    @Override
    public void onFinalizadoClick() {
        // Lógica para ordenar por finalizado
        adaptadorFilas.ordenarPorFinalizado();
    }

    @Override
    public void onFechaClick() {
        // Lógica para ordenar por fecha
        adaptadorFilas.ordenarPorFechaInicio();
    }

    @Override
    public void onTituloClick() {
        // Lógica para ordenar por título
        adaptadorFilas.ordenarPorTitulo();
    }

    @Override
    public void onAutorClick() {
        // Lógica para ordenar por autor
        adaptadorFilas.ordenarPorAutor();
    }

    private void mostrarDialogoOrdenar() {
        DialogoOrdenar dialog = new DialogoOrdenar();
        dialog.show(getSupportFragmentManager(), "DialogoOrdenar");
    }


}