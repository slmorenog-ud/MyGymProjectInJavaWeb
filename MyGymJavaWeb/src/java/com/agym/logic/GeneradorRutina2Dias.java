package com.agym.logic;

import com.agym.modelo.Ejercicio;
import com.agym.modelo.Rutina;
import com.agym.modelo.Usuario;
import java.util.List;

public class GeneradorRutina2Dias extends GeneradorRutinaBase {

    @Override
    protected void construirRutina(Rutina rutina, Usuario usuario, List<Ejercicio> todosLosEjercicios, String seriesYRepeticiones) {
        rutina.agregarDia(crearDiaRutina("Día 1: Cuerpo Completo", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Espalda", "Piernas"));
        rutina.agregarDia(crearDiaRutina("Día 2: Cuerpo Completo", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Espalda", "Piernas"));
    }
}
