package com.agym.modelo;

import java.util.List;
import java.util.ArrayList;

/**
 * Representa una rutina de entrenamiento, compuesta por varios días.
 * Funciona como un contenedor para una lista de {@link DiaRutina}.
 */
public class Rutina {

    private List<DiaRutina> dias;

    public Rutina() {
        this.dias = new ArrayList<>();
    }

    public void agregarDia(DiaRutina dia) {
        this.dias.add(dia);
    }

    public List<DiaRutina> getDias() {
        return dias;
    }

    /**
     * Representa un día específico de entrenamiento dentro de una rutina.
     * Es un Value Object que agrupa ejercicios bajo un nombre.
     */
    public static class DiaRutina {
        private String nombre;
        private List<Ejercicio> ejercicios;

        public DiaRutina(String nombre) {
            this.nombre = nombre;
            this.ejercicios = new ArrayList<>();
        }

        public void agregarEjercicio(Ejercicio ejercicio) {
            this.ejercicios.add(ejercicio);
        }

        public String getNombre() {
            return nombre;
        }

        public List<Ejercicio> getEjercicios() {
            return ejercicios;
        }
    }
}
