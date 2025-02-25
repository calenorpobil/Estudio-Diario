package com.merlita.estudiodiario;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class EstudiosSQLiteHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE IF NOT EXISTS ESTUDIO(" +
            "NOMBRE VARCHAR(50) PRIMARY KEY," +
            "DESCRIPCION VARCHAR(9)" +
            ");";

    public EstudiosSQLiteHelper(Context contexto, String nombre,
                                CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creaci�n de la tabla
        db.execSQL("DROP TABLE IF EXISTS estudio");

        db.execSQL(sqlCreate);
    }

    boolean addEstudio(String categoria, String titulo, String autor,
                     String idioma, Long fecha_lectura_ini, Long fecha_lectura_fin,
                     String prestado_a, Float valoracion, String formato, String notas){
        SQLiteDatabase sqldb = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("categoria", categoria);

        return sqldb.insert("libros", null, cv)!=-1;
    }





    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior,
                          int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aqu� utilizamos directamente
        //      la opci�n de eliminar la tabla anterior y crearla de nuevo
        //      vac�a con el nuevo formato.
        //      Sin embargo lo normal ser� que haya que migrar datos de la
        //      tabla antigua a la nueva, por lo que este m�todo deber�a
        //      ser m�s elaborado.

        //Se elimina la versi�n anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS libros");

        //Se crea la nueva versi�n de la tabla
        db.execSQL(sqlCreate);
    }
}
