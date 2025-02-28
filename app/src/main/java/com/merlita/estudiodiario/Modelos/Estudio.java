package com.merlita.estudiodiario.Modelos;

public class Estudio {
    private String nombre;          // Clave primaria
    private String descripcion;
    private int cuenta;

    // Constructor
    public Estudio(String nombre, String descripcion, int cuenta) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cuenta= cuenta;
    }

    // Getters y Setters
    public int getCuenta() {
        return cuenta;
    }

    public void setCuenta(int cuenta) {
        this.cuenta = cuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}