package com.agym.logic;

import com.agym.modelo.Ejercicio;
import com.agym.modelo.Rutina;
import com.agym.modelo.Usuario;
import java.util.List;

public class GeneradorRutina4Dias extends GeneradorRutinaBase {

    @Override
    protected void construirRutina(Rutina rutina, Usuario usuario, List<Ejercicio> todosLosEjercicios, String seriesYRepeticiones) {
        rutina.agregarDia(crearDiaRutina("Día 1: Tren Superior", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Espalda", "Hombros"));
        rutina.agregarDia(crearDiaRutina("Día 2: Tren Inferior", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 3, "Piernas"));
        rutina.agregarDia(crearDiaRutina("Día 3: Tren Superior", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Espalda", "Biceps", "Triceps"));
        rutina.agregarDia(crearDiaRutina("Día 4: Tren Inferior", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 3, "Piernas"));
    }
}
