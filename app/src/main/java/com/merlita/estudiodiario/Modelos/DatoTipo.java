package com.merlita.estudiodiario.Modelos;

public class DatoTipo {
    private String nombre;          // Clave primaria (compuesta)
    private String tipoDato;        // Tipo de dato (Numero, Texto, Fecha)
    private String descripcion;
    private String fkEstudio;       // Clave foránea (referencia a Estudio)

    // Constructor
    public DatoTipo(String nombre, String tipoDato, String descripcion, String fkEstudio) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.descripcion = descripcion;
        this.fkEstudio = fkEstudio;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFkEstudio() {
        return fkEstudio;
    }

    public void setFkEstudio(String fkEstudio) {
        this.fkEstudio = fkEstudio;
    }
}