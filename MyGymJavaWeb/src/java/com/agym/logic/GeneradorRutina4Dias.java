package com.agym.logic;

import com.agym.modelo.Ejercicio;
import com.agym.modelo.Rutina;
import com.agym.modelo.Usuario;
import java.util.List;

/**
 * Implementación concreta para generar una rutina de entrenamiento de 4 días.
 * Esta clase se especializa en crear una rutina dividida en "Tren Superior" y "Tren Inferior".
 */
public class GeneradorRutina4Dias extends GeneradorRutinaBase {

    /**
     * Construye la estructura de una rutina de 4 días.
     * @param rutina El objeto {@link Rutina} a poblar.
     * @param usuario El usuario para quien se genera la rutina.
     * @param todosLosEjercicios La lista de ejercicios disponibles.
     * @param seriesYRepeticiones Las series y repeticiones calculadas.
     */
    @Override
    protected void construirRutina(Rutina rutina, Usuario usuario, List<Ejercicio> todosLosEjercicios, String seriesYRepeticiones) {
        rutina.agregarDia(crearDiaRutina("Día 1: Tren Superior", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Espalda", "Hombros"));
        rutina.agregarDia(crearDiaRutina("Día 2: Tren Inferior", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 3, "Piernas"));
        rutina.agregarDia(crearDiaRutina("Día 3: Tren Superior", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Espalda", "Biceps", "Triceps"));
        rutina.agregarDia(crearDiaRutina("Día 4: Tren Inferior", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 3, "Piernas"));
    }
}
