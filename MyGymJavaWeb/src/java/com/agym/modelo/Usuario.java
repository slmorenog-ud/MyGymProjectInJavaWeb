package com.agym.modelo;

/**
 * Representa a un usuario en el sistema.
 * Es un POJO (Plain Old Java Object) que contiene los datos del usuario.
 */
public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String password;
    private String fechaNacimiento;
    private String genero;
    private double altura;
    private double peso;
    private String experiencia;
    private String objetivo;
    private int diasDisponibles;
    private String prioridadMuscular;

    // Getters y Setters
    // Principio: KISS - Getters y Setters simples y directos.
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }
    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }
    public int getDiasDisponibles() { return diasDisponibles; }
    public void setDiasDisponibles(int diasDisponibles) { this.diasDisponibles = diasDisponibles; }
    public String getPrioridadMuscular() { return prioridadMuscular; }
    public void setPrioridadMuscular(String prioridadMuscular) { this.prioridadMuscular = prioridadMuscular; }
}
