package com.merlita.estudiodiario.Ventanas;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
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
import com.merlita.estudiodiario.EstudiosSQLiteHelper;
import com.merlita.estudiodiario.Modelos.Estudio;
import com.merlita.estudiodiario.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        AdaptadorFilas.OnButtonClickListener{

    RecyclerView vistaRecycler;
    ArrayList<Estudio> listaEstudios = new ArrayList<Estudio>();
    TextView tv;
    AdaptadorFilas adaptadorFilas;
    Button btAlta, btCopia, btRevert;
    EditText et;
    int posicionEdicion;
    boolean ver=true;
    boolean isRecibiendo =false, isEnviando = false;
    int numServidor=1;

    ResultCallbackEnviar callbackEnviarServer;
    ResultCallbackRecibir callbackRecibirServer;


    File database = new File(
            Environment.getDataDirectory()+
                    "/data/com.merlita.estudiodiario/databases/"+"DBEstudios");
    File bk_database = new File(
            Environment.getDataDirectory()+
                    "/data/com.merlita.estudiodiario/files/"+"bk_DBEstudios");
    File database_server = new File(
            Environment.getDataDirectory()+
                    "/data/com.merlita.estudiodiario/databases/"+"server_DBEstudios");



    private static final String SERVIDOR_IP = "10.0.2.2";
    //10.0.2.2      LOCALHOST
    //172.17.0.1     LINUX
    private static final int PUERTO = 8888;



    private void toast(String e) {
        if(e!=null){
            Toast.makeText(this, e,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //EdgeToEdge.enable(this);





        tv = findViewById(R.id.tvTitulo);
        btAlta = findViewById(R.id.btAlta);
        btCopia = findViewById(R.id.btCopia);
        btRevert = findViewById(R.id.btRevert);
        vistaRecycler = findViewById(R.id.recyclerView);
        adaptadorFilas = new AdaptadorFilas(this, listaEstudios, this);


        vistaRecycler.setLayoutManager(new LinearLayoutManager(this));
        vistaRecycler.setAdapter(adaptadorFilas);

        //datosDePrueba();
        borrarTodo();
        actualizarDatos();




        btAlta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //ONCLICK

                Intent i = new Intent(MainActivity.this, AltaActivity.class);
                lanzadorAlta.launch(i);

                //sumarNumerosServer();

            }
        });

        btRevert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pone archivo en carpeta files.


                try {
                    recibirArchivo();
                }catch (SQLiteDatabaseCorruptException ex){
                    toast("El archivo está en uso. Inténtalo más tarde. ");
                }




            }
        });

        btCopia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isEnviando){
                    isEnviando = true;
                    enviarArchivoNube();
                    isEnviando= false;
                }


            }
        });
    }


    private void backupLocalCopiar() {
        try {
            copiarArchivo(database, bk_database);
            toast("Backup local hecha. ");
        } catch (IOException e) {
            toast("No se ha podido hacer la backup local. Vuelve a intentarlo. ");
        }
    }

    public void actualizarDatos() {
        try{
            try(EstudiosSQLiteHelper usdbh =
                        new EstudiosSQLiteHelper(this,
                                "DBEstudios", null, 1);){
                SQLiteDatabase db;
                db = usdbh.getWritableDatabase();

                listaEstudios.clear();

                rellenarLista(db);

                db.close();
            }
        } catch (SQLiteDatabaseCorruptException ex){
            toast("Intentalo en otro momento. ");
        }
        vistaRecycler.setLayoutManager(new LinearLayoutManager(this));
        vistaRecycler.setAdapter(adaptadorFilas);

    }

    private void copiarArchivo(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    private void recibirArchivo() {

        new Thread(recibirServer).start();

        // Comprobar Resultado
        callbackRecibirServer = new ResultCallbackRecibir() {
            @Override
            public void onSuccess() {
                // Actualizar UI o lógica post-éxito
                runOnUiThread(() -> {
                    toast("Se descargaron los datos de la nube. ");
                    try {
                        copiarArchivo(database_server, database);
                        toast("Backup de la nube revertida. ");
                        actualizarDatos();
                    } catch (IOException e) {
                        toast("No hay un archivo para revertir. ");
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                // Manejar error
                runOnUiThread(() -> {
                    try {
                        //REVERTIR LOCALMENTE
                        copiarArchivo(bk_database, database);
                        toast("Backup revertida localmente. ");
                        actualizarDatos();
                    } catch (IOException ex) {
                        toast("No se pudo revertir la copia. ");
                    }
                });
            }
        };

    }

    Runnable recibirServer = new Runnable() {
        public void run() {
            File archivoDestino;
            try (Socket socket = new Socket();) {
                socket.connect(new InetSocketAddress(SERVIDOR_IP, PUERTO), 1000);
                DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

                //DIGO AL SERVER QUE QUIERO RECIBIR ARCHIVO
                outStream.writeUTF("RECIBIR");

                System.out.println("Recibir dicho. ");

                // Solicitar archivo "DBEstudios"
                outStream.writeUTF("DBEstudios");


                try (DataInputStream inStream = new DataInputStream(socket.getInputStream())) {
                    // Recibir metadatos
                    String nombreArchivo = inStream.readUTF();
                    long tamanyoArchivo = inStream.readLong();

                    // Prepara el archivo de destino (cliente)
                    archivoDestino = database_server;

                    archivoDestino.delete();

                    if (!archivoDestino.exists()) {
                        try {
                            archivoDestino.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //GUARDAR ARCHIVO
                    try (FileOutputStream fos = new FileOutputStream(database_server);
                         BufferedOutputStream bos = new BufferedOutputStream(fos)) {


                        // Recibir y guardar el archivo por bloques de 4KB
                        byte[] buffer = new byte[4096];
                        int count;
                        long totalRecibido = 0;
                        while (totalRecibido < tamanyoArchivo && (count = inStream.read(buffer)) != -1) {
                            bos.write(buffer, 0, count);
                            totalRecibido += count;
                        }

                        System.out.println("Archivo recibido: " + nombreArchivo);
                        // Notificar éxito
                        if (callbackRecibirServer != null) {
                            new Handler(Looper.getMainLooper()).post(() -> callbackRecibirServer.onSuccess());
                        }
                    }
                }
            } catch (IOException e) {

                new Handler(Looper.getMainLooper()).post(() -> callbackRecibirServer.onFailure(e));
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    };
    private void enviarArchivoNube() {
        //ARCHIVO SQLITE:


        new Thread(enviarANube).start();

        // Comprobar Resultado
        callbackEnviarServer = new ResultCallbackEnviar() {
            @Override
            public void onSuccess() {
                // Actualizar UI o lógica post-éxito
                runOnUiThread(() -> {
                    toast("Se guardó el mensaje en la nube. ");
                });
            }

            @Override
            public void onFailure(Exception e) {
                // Manejar error
                runOnUiThread(() -> {
                    toast("La copia al servidor no funcionó. Se hará una backup local. ");
                    backupLocalCopiar();
                });
            }
        };



    }



    public interface ResultCallbackEnviar {
        void onSuccess();
        void onFailure(Exception e);
    }
    public interface ResultCallbackRecibir {
        void onSuccess();
        void onFailure(Exception e);
    }

    Runnable enviarANube = new Runnable() {
        @Override
        public void run() {
            try {
                final int BUFFER_SIZE = 4096; // 4 KB

                try (Socket socket = new Socket()) {

                    FileInputStream fis = new FileInputStream(database);
                    BufferedInputStream inStream = new BufferedInputStream(fis);
                    socket.connect(new InetSocketAddress(SERVIDOR_IP, PUERTO), 1000);
                    DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
                    //ENVIAR ARCHIVO
                    outStream.writeUTF("ENVIAR");


                    // Enviar metadatos: nombre y tamaño
                    outStream.writeUTF(database.getName()); // Nombre del archivo
                    outStream.writeLong(database.length()); // Tamaño en bytes

                    // Enviar archivo
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int count;
                    while ((count = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, count);
                    }

                    System.out.println("Archivo enviado: " + database.getName());
                    // Notificar éxito
                    if (callbackEnviarServer != null) {
                        new Handler(Looper.getMainLooper()).post(() -> callbackEnviarServer.onSuccess());
                    }


                } catch(java.net.SocketException ex) {
                    if (callbackEnviarServer != null) {
                        new Handler(Looper.getMainLooper()).post(() -> callbackEnviarServer.onFailure(ex));
                    }
                }catch (UnknownHostException e) {
                    System.err.println("Host desconocido: " + SERVIDOR_IP);
                    if (callbackEnviarServer != null) {
                        new Handler(Looper.getMainLooper()).post(() -> callbackEnviarServer.onFailure(e));
                    }
                } catch (IOException e) {
                    System.err.println("Error de E/S: " + e.getMessage());
                    if (callbackEnviarServer != null) {
                        new Handler(Looper.getMainLooper()).post(() -> callbackEnviarServer.onFailure(e));
                    }
                }
            } catch (Exception e) {
                //NOTIFICAR ERROR
                if (callbackEnviarServer != null) {
                    new Handler(Looper.getMainLooper()).post(() -> callbackEnviarServer.onFailure(e));
                }
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    };

    private void mostrarFormularioAlta()  {
        Intent i = new Intent(MainActivity.this, AltaActivity.class);
        lanzadorAlta.launch(i);
    }



    private void datosDePrueba() {
        Estudio a = new Estudio("Tomar Cafe", "Registro de consumo diario de café", 3);
        if(insertarSQL(a)!=-1)
            listaEstudios.add(a);
        a= new Estudio("Ir al gimnasio", "Seguimiento de sesiones de entrenamiento", 8);
        if(insertarSQL(a)!=-1)
            listaEstudios.add(a);
        a= new Estudio("Diario", "Registro personal diario", 32);
        if(insertarSQL(a)!=-1)
            listaEstudios.add(a);
    }

    private long insertarSQL(Estudio estudio){
        long  res=-1;
        try(EstudiosSQLiteHelper usdbh =
                    new EstudiosSQLiteHelper(this,
                            "DBEstudios", null, 1);) {

            res = usdbh.insertarSQL(estudio);
        }
        return res;
    }

    private void rellenarLista(SQLiteDatabase db) {
        Cursor c = db.rawQuery("select * from estudio", null);

        while (c.moveToNext()) {
            int index = c.getColumnIndex("NOMBRE");
            String nombre = c.getString(index);
            index = c.getColumnIndex("DESCRIPCION");
            String descripcion = c.getString(index);
            index = c.getColumnIndex("CUENTA");
            int cuenta = c.getInt(index);
            listaEstudios.add(new Estudio(nombre, descripcion, cuenta));
        }
        c.close();
    }





    //MENU CONTEXTUAL
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        Estudio libro;
        switch(item.getItemId())
        {
            case 121:
                //MENU --> EDITAR
                Intent i = new Intent(this, EditActivity.class);
                posicionEdicion = item.getGroupId();
                libro = listaEstudios.get(posicionEdicion);
                i.putExtra("NOMBRE", libro.getNombre());
                i.putExtra("DESCRIPCION", libro.getDescripcion());
                i.putExtra("CUENTA", libro.getCuenta());
                lanzadorEdit.launch(i);
                return true;
            case 122:
                //MENU --> BORRAR
                posicionEdicion = item.getGroupId();
                libro = listaEstudios.get(posicionEdicion);
                if(borrarSQL(libro)!=-1){
                    listaEstudios.remove(libro);
                }
                actualizarDatos();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private long borrarSQL(Estudio libro) {
        long res;
        try(EstudiosSQLiteHelper usdbh =
                    new EstudiosSQLiteHelper(this,
                            "DBEstudios", null, 1);) {
            res = usdbh.borrarSQL(libro);

        }
        return res;
    }
    private void borrarTodo() {
        long res;
        try(EstudiosSQLiteHelper usdbh =
                    new EstudiosSQLiteHelper(this,
                            "DBEstudios", null, 1);) {
            res = usdbh.borrarTodo();

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
                                data.getStringExtra("DESCRIPCION"),
                                data.getIntExtra("CUENTA", 0)
                        );

                        Estudio antig = listaEstudios.get(posicionEdicion);
                        // Editar el libro

                        int insertado = editarSQL(antig, editLibro);
                        if(insertado != -1){
                            listaEstudios.set(listaEstudios.indexOf(antig), editLibro);
                        }

                        actualizarDatos();

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
                        String nombre = data.getStringExtra("NOMBRE");
                        String desc = data.getStringExtra("DESCRIPCION");
                        int cuenta = data.getIntExtra("CUENTA", 0);
                        Estudio nuevoEstudio = new Estudio(nombre, desc, cuenta);

                        // Insertar en BD
                        long fila = insertarSQL(nuevoEstudio);
                        if(fila!=-1){
                            System.out.println(fila);
                            listaEstudios.add(nuevoEstudio);
                        }
                        actualizarDatos();
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




    private int editarSQL(Estudio antiguo, Estudio nuevo){
        int res=-1;
        try(EstudiosSQLiteHelper usdbh =
                    new EstudiosSQLiteHelper(this,
                            "DBEstudios", null, 1);){
            SQLiteDatabase db = usdbh.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("NOMBRE", nuevo.getNombre());
            values.put("DESCRIPCION", nuevo.getDescripcion());
            values.put("CUENTA", nuevo.getCuenta());

            // Actualizar usando el ID como condición
            String[] id = {antiguo.getNombre()};
            res= db.update("Estudio",
                    values,
                    "nombre = ?",
                    id);

            db.close();
        } catch (SQLiteConstraintException ex){
            toast(ex.getMessage());
        }
        return res;
    }



    @Override
    public void onButtonClick(int position) {
        // Lógica de actualización (ejemplo: modificar el elemento)
        actualizarDatos();
    }



}