package com.agym.logic;

import com.agym.modelo.Ejercicio;
import com.agym.modelo.Rutina;
import com.agym.modelo.Usuario;
import java.util.List;

/**
 * Genera una rutina de 3 días ("Empuje-Tirón-Pierna").
 * Implementa el "hook" de la clase base para definir su estructura específica.
 */
public class GeneradorRutina3Dias extends GeneradorRutinaBase {

    @Override
    protected void construirRutina(Rutina rutina, Usuario usuario, List<Ejercicio> todosLosEjercicios, String seriesYRepeticiones) {
        rutina.agregarDia(crearDiaRutina("Día 1: Empuje", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Hombros", "Triceps"));
        rutina.agregarDia(crearDiaRutina("Día 2: Tirón", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Espalda", "Biceps"));
        rutina.agregarDia(crearDiaRutina("Día 3: Pierna", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 3, "Piernas"));
    }
}
