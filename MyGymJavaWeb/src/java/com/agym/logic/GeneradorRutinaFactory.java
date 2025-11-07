package com.agym.logic;

/**
 * Factory para crear la instancia correcta del generador de rutinas.
 * Desacopla al cliente (servlet) de las implementaciones concretas.
 *
 * --- Principios de Diseño Clave ---
 * Factory Pattern: Centraliza la lógica de creación de objetos.
 * OCP (Open/Closed Principle): Para añadir un nuevo generador, solo se
 * necesita modificar esta clase, sin afectar al servlet.
 * SRP (Single Responsibility Principle): Su única responsabilidad es crear
 * el generador adecuado.
 */
public class GeneradorRutinaFactory {

    /**
     * Obtiene la instancia del generador de rutinas apropiado.
     * @param diasDisponibles El número de días que el usuario tiene para entrenar.
     * @return Una subclase de {@link GeneradorRutinaBase}.
     */
    // Principio: SRP / SoC (Separation of Concerns)
    public static GeneradorRutinaBase getGenerador(int diasDisponibles) {
        switch (diasDisponibles) {
            case 2:
                return new GeneradorRutina2Dias();
            case 3:
                return new GeneradorRutina3Dias();
            case 4:
                return new GeneradorRutina4Dias();
            default:
                // Principio YAGNI (You Ain't Gonna Need It): No se implementan
                // generadores para otros números de días hasta que no sean necesarios.
                throw new IllegalArgumentException("Número de días no soportado: " + diasDisponibles);
        }
    }
}
