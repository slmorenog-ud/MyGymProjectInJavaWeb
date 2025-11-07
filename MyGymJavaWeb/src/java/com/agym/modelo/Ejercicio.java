package com.agym.modelo;

/**
 * Representa un ejercicio de entrenamiento.
 * <p>
 * Esta clase POJO contiene toda la información estática de un ejercicio, como
 * su nombre, descripción, grupo muscular, etc. También incluye un campo
 * {@code seriesYRepeticiones} que se asigna dinámicamente cuando el ejercicio
 * se incluye en una rutina generada.
 * </p>
 */
public class Ejercicio {
    private int id;
    private String nombre;
    private String descripcion;
    private String grupoMuscular;
    private String dificultad;
    private String imagenUrl;
    private String seriesYRepeticiones; // Campo para almacenar las series y repeticiones personalizadas

    // Getters y Setters
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
