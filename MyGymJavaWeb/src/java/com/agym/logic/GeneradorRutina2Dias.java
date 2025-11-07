package com.agym.logic;

import com.agym.modelo.Ejercicio;
import com.agym.modelo.Rutina;
import com.agym.modelo.Usuario;
import java.util.List;

/**
 * Implementación concreta para generar una rutina de entrenamiento de 2 días.
 * Esta clase se especializa en crear una rutina de "Cuerpo Completo".
 */
public class GeneradorRutina2Dias extends GeneradorRutinaBase {

    /**
     * Construye la estructura de una rutina de 2 días.
     * @param rutina El objeto {@link Rutina} a poblar.
     * @param usuario El usuario para quien se genera la rutina.
     * @param todosLosEjercicios La lista de ejercicios disponibles.
     * @param seriesYRepeticiones Las series y repeticiones calculadas.
     */
    @Override
    protected void construirRutina(Rutina rutina, Usuario usuario, List<Ejercicio> todosLosEjercicios, String seriesYRepeticiones) {
        rutina.agregarDia(crearDiaRutina("Día 1: Cuerpo Completo", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Espalda", "Piernas"));
        rutina.agregarDia(crearDiaRutina("Día 2: Cuerpo Completo", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Espalda", "Piernas"));
    }
}
