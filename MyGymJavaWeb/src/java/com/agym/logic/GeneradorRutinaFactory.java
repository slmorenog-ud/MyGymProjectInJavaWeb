package com.agym.logic;

public class GeneradorRutinaFactory {

    public static GeneradorRutinaBase getGenerador(int diasDisponibles) {
        switch (diasDisponibles) {
            case 2:
                return new GeneradorRutina2Dias();
            case 3:
                return new GeneradorRutina3Dias();
            case 4:
                return new GeneradorRutina4Dias();
            default:
                throw new IllegalArgumentException("Número de días no soportado: " + diasDisponibles);
        }
    }
}
