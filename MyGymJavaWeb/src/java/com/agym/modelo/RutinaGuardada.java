package com.agym.modelo;

import java.util.Date;

/**
 * Representa una {@link Rutina} guardada por un usuario.
 * Actúa como entidad de persistencia, añadiendo metadatos como fecha y estado.
 */
public class RutinaGuardada {
    private long id;
    private int usuarioId;
    private Date fechaGuardada;
    private String estado; // "Guardada" o "Completada"
    private Rutina rutina;

    public RutinaGuardada() {
        this.fechaGuardada = new Date();
        this.estado = "Guardada";
    }

    // Getters y Setters
    // Principio: KISS
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Date getFechaGuardada() {
        return fechaGuardada;
    }

    public void setFechaGuardada(Date fechaGuardada) {
        this.fechaGuardada = fechaGuardada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Rutina getRutina() {
        return rutina;
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }
}
