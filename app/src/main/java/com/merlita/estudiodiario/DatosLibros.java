package com.merlita.estudiodiario;

import java.sql.Date;

public class DatosLibros {
    private int _id;
    private String categoria;
    private String titulo;
    private String autor;
    private String idioma;
    private Long fecha_lectura_ini;
    private Long fecha_lectura_fin;
    private String prestado_a;
    private Float valoracion;
    private String formato;
    private String notas;
    String nombre;
    int edad, finalizado;

    /*
    public DatosLibros(int _id, String categoria, String titulo, String autor,
                       String idioma, Long fecha_lectura_ini, Long fecha_lectura_fin,
                       String prestado_a, Float valoracion, String formato, String notas) {
        set_id(_id);
        setAutor(autor);
        setCategoria(categoria);
        setFormato(formato);
        setIdioma(idioma);
        setFecha_lectura_fin(fecha_lectura_fin);
        setFecha_lectura_ini(fecha_lectura_ini);
        setPrestado_a(prestado_a);
        setValoracion(valoracion);
        setTitulo(titulo);
        setNotas(notas);
    }*/
    public DatosLibros(int _id, String categoria, String titulo, String autor,
                       String idioma, Long fecha_lectura_ini, Long fecha_lectura_fin,
                       String prestado_a, Float valoracion, String formato, String notas,
                       int finalizado) {
        set_id(_id);
        setAutor(autor);
        setCategoria(categoria);
        setFormato(formato);
        setIdioma(idioma);
        setFecha_lectura_fin(fecha_lectura_fin);
        setFecha_lectura_ini(fecha_lectura_ini);
        setPrestado_a(prestado_a);
        setValoracion(valoracion);
        setTitulo(titulo);
        setNotas(notas);
        setFinalizado(finalizado);
    }
    public DatosLibros() {

    }
    public DatosLibros(String titulo, String autor) {
        setTitulo(titulo);
        setAutor(autor);
    }



    //GETTERS Y SETTERS
    private void setFinalizado(int finalizado) { this.finalizado = finalizado; }
    public boolean getFinalizado() { return finalizado==1; }

    public int get_id() { return _id; }
    public void set_id(int _id) { this._id = _id; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public Long getFecha_lectura_ini() { return fecha_lectura_ini; }
    public void setFecha_lectura_ini(Long fecha_lectura_ini) { this.fecha_lectura_ini = fecha_lectura_ini; }

    public Long getFecha_lectura_fin() { return fecha_lectura_fin; }
    public void setFecha_lectura_fin(Long fecha_lectura_fin) { this.fecha_lectura_fin = fecha_lectura_fin; }

    public String getPrestado_a() { return prestado_a; }
    public void setPrestado_a(String prestado_a) { this.prestado_a = prestado_a; }

    public Float getValoracion() { return valoracion; }
    public void setValoracion(Float valoracion) { this.valoracion = valoracion; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    // Metodo auxiliar para convertir timestamp a Date
    public Date getFechaInicioDate() {
        return fecha_lectura_ini != null ? new Date(fecha_lectura_ini) : null;
    }

    public Date getFechaFinDate() {
        return fecha_lectura_fin != null ? new Date(fecha_lectura_fin) : null;
    }
}
