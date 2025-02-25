package com.merlita.estudiodiario.Modelos;

import java.util.Date;

public class Ocurrencia {
    private Date fecha;             // Clave primaria
    private String fkEstudioNombre; // Clave foránea (referencia a Estudio)

    // Constructor
    public Ocurrencia(Date fecha, String fkEstudioNombre) {
        this.fecha = fecha;
        this.fkEstudioNombre = fkEstudioNombre;
    }

    // Getters y Setters
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFkEstudioNombre() {
        return fkEstudioNombre;
    }

    public void setFkEstudioNombre(String fkEstudioNombre) {
        this.fkEstudioNombre = fkEstudioNombre;
    }
}