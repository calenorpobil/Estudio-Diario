package com.merlita.estudiodiario.Modelos;

import java.util.Date;

public class Dato {
    private String fkTipoNombre;    // Clave foránea (referencia a DatoTipo)
    private String fkTipoEstudio;   // Clave foránea (referencia a DatoTipo)
    private Date fkOcurrencia;     // Clave foránea (referencia a Ocurrencia)
    private String valorText;      // Valor del dato

    // Constructor
    public Dato(String fkTipoNombre, String fkTipoEstudio, Date fkOcurrencia, String valorText) {
        this.fkTipoNombre = fkTipoNombre;
        this.fkTipoEstudio = fkTipoEstudio;
        this.fkOcurrencia = fkOcurrencia;
        this.valorText = valorText;
    }

    // Getters y Setters
    public String getFkTipoNombre() {
        return fkTipoNombre;
    }

    public void setFkTipoNombre(String fkTipoNombre) {
        this.fkTipoNombre = fkTipoNombre;
    }

    public String getFkTipoEstudio() {
        return fkTipoEstudio;
    }

    public void setFkTipoEstudio(String fkTipoEstudio) {
        this.fkTipoEstudio = fkTipoEstudio;
    }

    public Date getFkOcurrencia() {
        return fkOcurrencia;
    }

    public void setFkOcurrencia(Date fkOcurrencia) {
        this.fkOcurrencia = fkOcurrencia;
    }

    public String getValorText() {
        return valorText;
    }

    public void setValorText(String valorText) {
        this.valorText = valorText;
    }
}