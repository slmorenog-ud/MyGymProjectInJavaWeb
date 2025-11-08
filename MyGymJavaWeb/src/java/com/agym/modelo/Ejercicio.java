package com.agym.modelo;

/**
 * Representa un ejercicio de entrenamiento.
 * POJO con la información estática y dinámica (series/reps) de un ejercicio.
 */
public class Ejercicio {
    private int id;
    private String nombre;
    private String descripcion;
    private String grupoMuscular;
    private String dificultad;
    private String imagenUrl;
    private String seriesYRepeticiones; // Asignado dinámicamente al generar la rutina

    // Getters y Setters
    // Principio: KISS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getGrupoMuscular() { return grupoMuscular; }
    public void setGrupoMuscular(String grupoMuscular) { this.grupoMuscular = grupoMuscular; }
    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public String getSeriesYRepeticiones() { return seriesYRepeticiones; }
    public void setSeriesYRepeticiones(String seriesYRepeticiones) { this.seriesYRepeticiones = seriesYRepeticiones; }
}
