package com.agym.modelo;

import java.util.List;
import java.util.ArrayList;

/**
 * Representa una rutina de entrenamiento completa, compuesta por varios días.
 * <p>
 * Esta clase funciona como un contenedor para una lista de objetos {@link DiaRutina},
 * estructurando una semana o ciclo de entrenamiento.
 * </p>
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
     * <p>
     * Es un Value Object que agrupa una lista de ejercicios bajo un nombre
     * descriptivo (ej. "Día 1: Empuje").
     * </p>
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
